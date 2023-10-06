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
import dev.myclxss.components.Items;

public class TutorialCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            if (player.hasPermission("fullpvp.jointutorial")) {

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
                World world = Bukkit.getServer().getWorld(API.getInstance().getLocations().getString("TUTORIAL.WORLD"));
                double x = API.getInstance().getLocations().getDouble("TUTORIAL.X");
                double y = API.getInstance().getLocations().getDouble("TUTORIAL.Y");
                double z = API.getInstance().getLocations().getDouble("TUTORIAL.Z");
                float yaw = (float) API.getInstance().getLocations().getDouble("TUTORIAL.YAW");
                float pitch = (float) API.getInstance().getLocations().getDouble("TUTORIAL.PITCH");
                Location location = new Location(world, x, y, z, yaw, pitch);
                player.teleport(location);
            } else {
                player.sendMessage(API.getInstance().getLang().getString("ERROR.NO-PERMISSION"));
                return true;
            }
        }
        return false;
    }
}
