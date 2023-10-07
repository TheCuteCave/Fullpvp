package dev.myclxss.event;

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
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        // Verificamos si el jugador esta interactuando con el item que queremos
        if (event.getItem() != null && event.getItem().getItemMeta().getDisplayName() != null
                && event.getItem().getItemMeta().getDisplayName().equals(Color.set("&cSalir del Tutorial"))) {
            // Si es el mismo, realizamos las acciones
            Action action = event.getAction();
            // Con el click derecho accionaremos las acciones
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                // Verificamos si el jugador se encuentra dentro de la lista (TutorialUser)
                if (API.getInstance().getTutorialUser().contains(player.getUniqueId())) {
                    // Si se encuentra dentro lo removemos
                    API.getInstance().getTutorialUser().remove(player.getUniqueId());
                }
                // Añadimos al jugador a la lista del lobby (LobbyUser)
                API.getInstance().getLobbyUser().add(player.getUniqueId());
                // Reproducimos un sonido al jugador para mejorar la experiencia
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 15, 15);
                // Añadimos un efecto para mejorar la experiencia
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 10));
                // Eliminamos el inventario del jugador para evitar bugs
                player.getInventory().clear();
                // Enviamos un mensaje en forma de lista, mencionando la salida del tutorial
                List<String> leaveTutorialMSG = API.getInstance().getLang().getStringList("TUTORIAL.LEAVE-MESSAGE");
                for (int i = 0; i < leaveTutorialMSG.size(); i++) {
                    String leaveMessage = leaveTutorialMSG.get(i);
                    player.sendMessage(Color.set(leaveMessage));
                }
                // Enviamos al jugador a la ubicacion del spawn principal
                World world = Bukkit.getServer().getWorld(API.getInstance().getLocations().getString("SPAWN.WORLD"));
                double x = API.getInstance().getLocations().getDouble("SPAWN.X");
                double y = API.getInstance().getLocations().getDouble("SPAWN.Y");
                double z = API.getInstance().getLocations().getDouble("SPAWN.Z");
                float yaw = (float) API.getInstance().getLocations().getDouble("SPAWN.YAW");
                float pitch = (float) API.getInstance().getLocations().getDouble("SPAWN.PITCH");
                Location location = new Location(world, x, y, z, yaw, pitch);
                player.teleport(location);
            }
        }
    }

    @EventHandler
    public void onBlockCMD(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        // Verificamos si el jugador forma parte de la lista (TutorialUser)
        if (API.getInstance().getTutorialUser().contains(player.getUniqueId())) {
            // Si es asi reproducimos una lista, para bloquearle comandos al jugador
            List<String> blockList = API.getInstance().getLang().getStringList("TUTORIAL.DENY-COMMANDS.COMMANDS");
            for (String string : blockList) {
                if (event.getMessage().startsWith(string)) {
                    event.setCancelled(true);
                    event.getPlayer()
                            .sendMessage(API.getInstance().getLang().getString("TUTORIAL.DENY-COMMANDS.MESSAGE", true));
                    return;
                }
            }
        }
    }
}
