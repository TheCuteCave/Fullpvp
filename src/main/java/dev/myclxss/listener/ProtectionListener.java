package dev.myclxss.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import dev.myclxss.API;

public class ProtectionListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player target = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            if (!API.getInstance().getArenaUsers().contains(target.getUniqueId()) ||
                    !API.getInstance().getArenaUsers().contains(damager.getUniqueId())) {
                // Cancela el evento si ni el objetivo ni el agresor están en el conjunto
                // permitido
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        World world = Bukkit.getServer().getWorld(API.getInstance().getLocations().getString("SPAWN.WORLD"));
        double x = API.getInstance().getLocations().getDouble("SPAWN.X");
        double y = API.getInstance().getLocations().getDouble("SPAWN.Y");
        double z = API.getInstance().getLocations().getDouble("SPAWN.Z");
        float yaw = (float) API.getInstance().getLocations().getDouble("SPAWN.YAW");
        float pitch = (float) API.getInstance().getLocations().getDouble("SPAWN.PITCH");
        Location location = new Location(world, x, y, z, yaw, pitch);
        event.setRespawnLocation(location);

        event.getPlayer().sendMessage(ChatColor.GREEN + "Moriste y respawneaste en el spawn");
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Item droppedItem = event.getItemDrop();
        droppedItem.remove(); // Esto elimina instantáneamente el objeto que el jugador ha soltado
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
        return;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == DamageCause.FALL)
            event.setCancelled(true);
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        event.setCancelled(true);
        return;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            // Verifica si el jugador no está en la lista de arena.
            if (!API.getInstance().getArenaUsers().contains(player.getUniqueId())) {
                // Cancela el evento para que el nivel de comida no cambie.
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        event.setCancelled(true);
        return;
    }

    @EventHandler
    public void onblockPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
        return;
    }

    @EventHandler
    public void onblockBreake(BlockBreakEvent event) {
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
        return;
    }

    @EventHandler
    public void bucketFill(PlayerBucketEmptyEvent event) {
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
        return;
    }

    @EventHandler
    public void bucketEmpty(PlayerBucketFillEvent event) {
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
        return;
    }

    @EventHandler
    public void entityExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
        return;
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        event.setCancelled(true);
        return;
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }

}
