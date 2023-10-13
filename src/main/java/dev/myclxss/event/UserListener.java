package dev.myclxss.event;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import dev.myclxss.API;
import dev.myclxss.components.Color;
import dev.myclxss.components.TitleAPI;

public class UserListener implements Listener {

    private Map<Player, BukkitRunnable> spectatorModeMap = new HashMap<>();

    // Este evento realiza acciones cuando el jugador muere
    @EventHandler
    public void deathPlayer(PlayerDeathEvent event) {
        Player player = event.getEntity();
        // Verificamos si el jugador que murio esta dentro de la lista (arenaUser)
        if (API.getInstance().getArenaUser().contains(player.getUniqueId())) {
            // Si se enuentra dentro de la lista, lo removemos de ella
            API.getInstance().getArenaUser().remove(player.getUniqueId());
            player.sendMessage("moriste y fuiste removido de la lista arena");
            return;
        }
    }

    // Este evento realiza acciones despues de que el jugador revive
    @EventHandler
    public void respawnPlayer(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        // Establecer el modo de juego a espectador
        player.setGameMode(GameMode.SPECTATOR);
        // Enviamos un mensaje mencionando que murio
        player.sendMessage(Color.set("&4Ha muerto"));
        // Enviamos al jugador a la zona del espectador
        World world = Bukkit.getServer().getWorld(API.getInstance().getLocations().getString("SPECTATOR.WORLD"));
        double x = API.getInstance().getLocations().getDouble("SPECTATOR.X");
        double y = API.getInstance().getLocations().getDouble("SPECTATOR.Y");
        double z = API.getInstance().getLocations().getDouble("SPECTATOR.Z");
        float yaw = (float) API.getInstance().getLocations().getDouble("SPECTATOR.YAW");
        float pitch = (float) API.getInstance().getLocations().getDouble("SPECTATOR.PITCH");
        Location location = new Location(world, x, y, z, yaw, pitch);
        event.setRespawnLocation(location);
        // Iniciamos la tarea de modo espectador
        spectatorMode(player);

    }

    // Creamos la accion de modo espectador
    private void spectatorMode(Player player) {
        // Verificamos si el jugador ya se encuentra dentro del modo espectador
        if (spectatorModeMap.containsKey(player)) {
            // Enviamos un mensaje para mencionar que se cancelo la cuenta regresiva
            player.sendMessage(Color.set("&cYa te encuentras en modo espectador"));
            return;
        }
        BukkitRunnable spectatorModeTask = new BukkitRunnable() {
            int time = 7;

            // Comenzamos una tarea
            @Override
            public void run() {
                if (time > 0) {
                    // Mandamos un titulo, mencionando la reaparicion del jugador
                    TitleAPI.sendTitle(player, 30, 50, 30,
                            Color.set("&a&lREANIMANDO"),
                            Color.set("&A&LEN" + " " + ChatColor.YELLOW + ChatColor.BOLD + time));
                    // Reproducimos un audio para mejorar la experiencia
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 15, 15);
                    time--;
                } else {
                    // Cuando la cuenta llega a 0, enviamos un mensaje mencionando que revivio
                    player.sendMessage(Color.set("&aReviviste"));
                    // Establecemos el modo de juego del jugador a Survival
                    player.setGameMode(GameMode.SURVIVAL);
                    // Añadimos al jugador a la lista de (LobbyUser)
                    if (API.getInstance().getLobbyUser().contains(player.getUniqueId())) {
                        API.getInstance().getLobbyUser().remove(player.getUniqueId());
                    }
                    API.getInstance().getLobbyUser().add(player.getUniqueId());
                    player.sendMessage("lo añadimos a la lista de lobby");
                    // Enviamos al jugar a la ubicacion del spawn principal
                    World w = Bukkit.getServer().getWorld(API.getInstance().getLocations().getString("SPAWN.WORLD"));
                    double x = API.getInstance().getLocations().getDouble("SPAWN.X");
                    double y = API.getInstance().getLocations().getDouble("SPAWN.Y");
                    double z = API.getInstance().getLocations().getDouble("SPAWN.Z");
                    float yaw = (float) API.getInstance().getLocations().getDouble("SPAWN.YAW");
                    float pitch = (float) API.getInstance().getLocations().getDouble("SPAWN.PITCH");
                    Location loc = new Location(w, x, y, z, yaw, pitch);
                    player.teleport(loc);
                    // Cancelamos la tarea para evitar que continue
                    this.cancel();
                    spectatorModeMap.remove(player);
                }
            }
        };
        spectatorModeTask.runTaskTimer(API.getInstance().getMain(), 0L, 20L); // Ejecutar cada segundo (20 ticks)
        spectatorModeMap.put(player, spectatorModeTask);
    }

    // Este evento realiza acciones cuando 2 entidades estan combatiendo
    @EventHandler
    public void damageEntitys(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            // Instanceamos al agresor y al agredido
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            // Verificamos si el agresor y agredido se encuentran dentro de la lista
            // (arenUser)
            if (!API.getInstance().getArenaUser().contains(player.getUniqueId()) ||
                    !API.getInstance().getArenaUser().contains(damager.getUniqueId())) {
                // Si se encuentran dentro el evento no se cancela, pero si no estan dentro este
                // se canacela
                event.setCancelled(true);
            }
        }
    }

    // Este evento realiza acciones cuando un item es tirado por un jugador
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        // Obtenemos los items que se tiran al suelo
        Item droppedItem = event.getItemDrop();
        // Los borramos
        droppedItem.remove();
        // Si el jugador se encuentra en creativo el evento no se cancela
        // Si no estas en creativo el evento se cancela
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
        return;
    }

    // Con este evento verificamos si se colocan bloques
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        // Verificamos si el jugador se encuentra en modo creativo, si es asi no
        // cancelamos el evento
        // Si el jugador no esta en modo creativo cancelamos el evento
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
        return;
    }

    // Con este evento verificamos si se rompen bloques
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        // Verificamos si el jugador se encuentra en modo creativo, si es asi no
        // cancelamos el evento
        // Si el jugador no esta en modo creativo cancelamos el evento
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
        return;
    }

    // Con este evento verificamos si el jugador intenta recoger un item del suelo
    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        // Verificamos si el jugador esta en modo creativo, si es asi si puede recoger
        // el item
        // Si el jugador no esta en creativo, no puede recoger el item
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
        return;
    }
}
