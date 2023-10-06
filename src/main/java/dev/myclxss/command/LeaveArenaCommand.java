package dev.myclxss.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.myclxss.API;
import dev.myclxss.components.Color;

public class LeaveArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            if (API.getInstance().getArenaUsers().contains(player.getUniqueId())) {
                API.getInstance().getArenaUsers().remove(player.getUniqueId());

                World world = Bukkit.getServer().getWorld(API.getInstance().getLocations().getString("SPAWN.WORLD"));
                double x = API.getInstance().getLocations().getDouble("SPAWN.X");
                double y = API.getInstance().getLocations().getDouble("SPAWN.Y");
                double z = API.getInstance().getLocations().getDouble("SPAWN.Z");
                float yaw = (float) API.getInstance().getLocations().getDouble("SPAWN.YAW");
                float pitch = (float) API.getInstance().getLocations().getDouble("SPAWN.PITCH");
                Location location = new Location(world, x, y, z, yaw, pitch);

                List<String> arenaLeaveMessage = API.getInstance().getLang().getStringList("ARENA.LEAVE-MESSAGE");

                for (int i = 0; i < arenaLeaveMessage.size(); i++) {
                    String joinMessage = arenaLeaveMessage.get(i);
                    player.sendMessage(Color.set(joinMessage));
                }

                player.teleport(location);
            }
        }
        return false;
    }
}
