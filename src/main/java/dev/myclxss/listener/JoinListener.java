package dev.myclxss.listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import dev.myclxss.API;
import dev.myclxss.components.Color;
import dev.myclxss.components.Items;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {

            if (API.getInstance().getLocations().getConfigurationSection("TUTORIAL") == null) {
                player.sendMessage(API.getInstance().getLang().getString("TUTORIAL.ERROR-LOCATION", true));
                return;
            }

            if (API.getInstance().getTutorialUsers().contains(player.getUniqueId())) {
                API.getInstance().getTutorialUsers().remove(player.getUniqueId());
            }
            API.getInstance().getTutorialUsers().add(player.getUniqueId());

            player.getInventory().clear();
            player.getInventory().setItem(8, Items.tutorialItem);

            List<String> joinMessageString = API.getInstance().getLang().getStringList("TUTORIAL.JOIN-MESSAGE");

            for (int i = 0; i < joinMessageString.size(); i++) {
                String joinMessage = joinMessageString.get(i);
                player.sendMessage(Color.set(joinMessage));
            }

            (new BukkitRunnable() {
                public void run() {
                    World w = Bukkit.getServer().getWorld(API.getInstance().getLocations().getString("TUTORIAL.WORLD"));
                    double x = API.getInstance().getLocations().getDouble("TUTORIAL.X");
                    double y = API.getInstance().getLocations().getDouble("TUTORIAL.Y");
                    double z = API.getInstance().getLocations().getDouble("TUTORIAL.Z");
                    float yaw = (float) API.getInstance().getLocations().getDouble("TUTORIAL.YAW");
                    float pitch = (float) API.getInstance().getLocations().getDouble("TUTORIAL.PITCH");
                    Location loc = new Location(w, x, y, z, yaw, pitch);
                    player.teleport(loc);
                }
            }).runTaskLater(API.getInstance().getMain(), 3L);

        }

        if (API.getInstance().getLocations().getConfigurationSection("SPAWN") == null) {
            player.sendMessage(API.getInstance().getLang().getString("ERROR.SPAWN-LOCATION", true));
            return;
        }

        // Si el spawn existe envia al jugador a la ubicacion establecida
        (new BukkitRunnable() {
            public void run() {
                World w = Bukkit.getServer().getWorld(API.getInstance().getLocations().getString("SPAWN.WORLD"));
                double x = API.getInstance().getLocations().getDouble("SPAWN.X");
                double y = API.getInstance().getLocations().getDouble("SPAWN.Y");
                double z = API.getInstance().getLocations().getDouble("SPAWN.Z");
                float yaw = (float) API.getInstance().getLocations().getDouble("SPAWN.YAW");
                float pitch = (float) API.getInstance().getLocations().getDouble("SPAWN.PITCH");
                Location loc = new Location(w, x, y, z, yaw, pitch);
                player.teleport(loc);
            }
        }).runTaskLater(API.getInstance().getMain(), 3L);
    }
}
