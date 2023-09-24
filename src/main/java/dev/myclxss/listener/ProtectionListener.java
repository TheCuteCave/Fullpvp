package dev.myclxss.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import dev.myclxss.API;

public class ProtectionListener implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

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
}
