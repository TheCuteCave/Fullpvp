package dev.myclxss.listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.myclxss.API;
import dev.myclxss.components.Color;

public class TutorialListener implements Listener {

    @EventHandler
    public void itemInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (event.getItem() != null && event.getItem().getItemMeta().getDisplayName() != null
                && event.getItem().getItemMeta().getDisplayName().equals(Color.set("&cSalir del Tutorial"))) {

            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_AIR || action == Action.LEFT_CLICK_AIR) {
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 15, 15);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 5));

                if (API.getInstance().getTutorialUsers().contains(player.getUniqueId())) {
                    API.getInstance().getTutorialUsers().remove(player.getUniqueId());
                }
                player.getInventory().clear();

                World world = Bukkit.getServer().getWorld(API.getInstance().getLocations().getString("SPAWN.WORLD"));
                double x = API.getInstance().getLocations().getDouble("SPAWN.X");
                double y = API.getInstance().getLocations().getDouble("SPAWN.Y");
                double z = API.getInstance().getLocations().getDouble("SPAWN.Z");
                float yaw = (float) API.getInstance().getLocations().getDouble("SPAWN.YAW");
                float pitch = (float) API.getInstance().getLocations().getDouble("SPAWN.PITCH");
                Location location = new Location(world, x, y, z, yaw, pitch);
                player.teleport(location);

                List<String> leaveTutorialMSG = API.getInstance().getLang().getStringList("TUTORIAL.LEAVE-MESSAGE");

                for (int i = 0; i < leaveTutorialMSG.size(); i++) {
                    String leaveMessage = leaveTutorialMSG.get(i);
                    player.sendMessage(Color.set(leaveMessage));
                }

                return;
            }
        }
        return;
    }

    @EventHandler
    public void onBlockCommands(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();

        if (API.getInstance().getTutorialUsers().contains(player.getUniqueId())) {
            List<String> blockList = API.getInstance().getLang().getStringList("TUTORIAL.DENY-COMMANDS.COMMANDS");
            for (String string : blockList) {
                if (event.getMessage().startsWith(string)) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(API.getInstance().getLang().getString("TUTORIAL.DENY-COMMANDS.MESSAGE", true));
                    return;
                }
            }
        }
    }
}
