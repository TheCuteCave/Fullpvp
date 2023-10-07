package dev.myclxss.event;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import dev.myclxss.API;
import dev.myclxss.components.Color;
import dev.myclxss.components.Items;
import dev.myclxss.components.TitleAPI;

public class JoinListener implements Listener {

    @EventHandler
    public void userJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Si es la primera vez que el jugador entra se ejecuta lo siguiente
        if (!player.hasPlayedBefore()) {
            // Verificamos si la ubicacion del tutorial existe, si no existe enviamos un
            // mensaje de error y no continua el codigo.
            if (API.getInstance().getLocations().getConfigurationSection("TUTORIAL") == null) {
                player.sendMessage(API.getInstance().getLang().getString("TUTORIAL.ERROR-LOCATION", true));
                return;
            }
            // Si la ubicacion del tutorial existe, continuamos con el codigo y añadimos al
            // jugador a una lista llamada (TutorialUser)
            API.getInstance().getTutorialUser().add(player.getUniqueId());

            // Borramos el inventario del jugador en su totalidad
            player.getInventory().clear();
            // Añadimos un item, en este caso con el que el jugador podra salir del modo
            // tutorial
            player.getInventory().setItem(4, Items.tutorialItem);
            // Reproducimos un sonido al jugador para mejorar la experiencia
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 15, 15);
            // Añadimos un efecto para mejorar la experiencia
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 10));
            // Enviamos un titulo al jugador
            TitleAPI.sendTitle(player, 40, 60, 40, API.getInstance().getLang().getString("TUTORIAL.TITLE"),
                    API.getInstance().getLang().getString("TUTORIAL.SUBTITLE"));
            // Enviamos un mensaje de bienvenida al jugador, en formato lista.
            List<String> joinTutorialString = API.getInstance().getLang().getStringList("TUTORIAL.JOIN-MESSAGE");
            for (int i = 0; i < joinTutorialString.size(); i++) {
                String joinTutorialMessage = joinTutorialString.get(i);
                player.sendMessage(Color.set(joinTutorialMessage));
            }
            // Enviamos al jugador a la ubicacion del tutorial
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
            return;
        }
        // Si el jugador ya entro antes, verficamos si la ubicacion del spawn principal
        // existe si no existe enviamos un mensaje de error y no continua el codigo.
        if (API.getInstance().getLocations().getConfigurationSection("SPAWN") == null) {
            player.sendMessage(API.getInstance().getLang().getString("ERROR.SPAWN-LOCATION", true));
            return;
        }
        // Si la ubicacion del spawn principal existe continuamos con el codigo y
        // añadimos al jugador a una lista
        // llamada (LobbyUser)
        // Primero verificamos si el jugador se encuentra ya dentro de la lista, si es
        // asi lo removemos para evitar bugs
        if (API.getInstance().getLobbyUser().contains(player.getUniqueId())) {
            // Aqui removemos al jugador de la lista
            API.getInstance().getLobbyUser().remove(player.getUniqueId());
        }
        // Aqui agregamos al jugador a la lista
        API.getInstance().getLobbyUser().add(player.getUniqueId());
        // Reproducimos un sonido al jugador para mejorar la experiencia
        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 15, 15);
        // Añadimos un efecto para mejorar la experiencia
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 10));
        // Enviamos al jugador a la ubicacion del spawn principal
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
