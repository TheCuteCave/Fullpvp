package dev.myclxss.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.myclxss.API;
import dev.myclxss.components.Color;

public class FirstCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(Color.set("&r"));
            player.sendMessage(Color.set("&r"));
            player.sendMessage(Color.set("&7&m------------------------------"));
            player.sendMessage(Color.set("&e⁕ &8| &6&lFullpvp"));
            player.sendMessage(Color.set("&e⁕ &8| &fversion 1.0v"));
            player.sendMessage(Color.set("&e⁕ &8| &eby myclass"));
            player.sendMessage(Color.set("&7&m------------------------------"));
            player.sendMessage(Color.set("&r"));
            player.sendMessage(Color.set("&r"));
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
            if (!player.hasPermission("fullpvp.help") || !player.hasPermission("fullpvp.all")) {
                player.sendMessage(API.getInstance().getLang().getString("ERROR.NO-PERMISSION", true));
                return true;
            }
            List<String> helpList = Color.set(API.getInstance().getLang().getStringList("HELP-MESSAGE"));
            for (String list : helpList) {
                player.sendMessage(list);
            }
        }
        if (args[0].equalsIgnoreCase("set")) {
            if (args.length > 1 && args[1].equalsIgnoreCase("spawn")) {
                if (!player.hasPermission("fullpvp.setspawn") || !player.hasPermission("fullpvp.all")) {
                    player.sendMessage(API.getInstance().getLang().getString("ERROR.NO-PERMISSION", true));
                    return true;
                }
                API.getInstance().getLocations().set("SPAWN.WORLD", player.getLocation().getWorld().getName());
                API.getInstance().getLocations().set("SPAWN.X", Double.valueOf(player.getLocation().getX()));
                API.getInstance().getLocations().set("SPAWN.Y", Double.valueOf(player.getLocation().getY()));
                API.getInstance().getLocations().set("SPAWN.Z", Double.valueOf(player.getLocation().getZ()));
                API.getInstance().getLocations().set("SPAWN.YAW", Float.valueOf(player.getLocation().getYaw()));
                API.getInstance().getLocations().set("SPAWN.PITCH", Float.valueOf(player.getLocation().getPitch()));
                API.getInstance().getLocations().save();
                player.sendMessage(API.getInstance().getLang().getString("SPAWN.SET-LOCATION", true));
                return true;
            }
            if (args.length > 1 && args[1].equalsIgnoreCase("arena")) {
                if (!player.hasPermission("fullpvp.setarena") || !player.hasPermission("fullpvp.all")) {
                    player.sendMessage(API.getInstance().getLang().getString("ERROR.NO-PERMISSION", true));
                    return true;
                }
                API.getInstance().getLocations().set("ARENA.WORLD", player.getLocation().getWorld().getName());
                API.getInstance().getLocations().set("ARENA.X", Double.valueOf(player.getLocation().getX()));
                API.getInstance().getLocations().set("ARENA.Y", Double.valueOf(player.getLocation().getY()));
                API.getInstance().getLocations().set("ARENA.Z", Double.valueOf(player.getLocation().getZ()));
                API.getInstance().getLocations().set("ARENA.YAW", Float.valueOf(player.getLocation().getYaw()));
                API.getInstance().getLocations().set("ARENA.PITCH", Float.valueOf(player.getLocation().getPitch()));
                API.getInstance().getLocations().save();
                player.sendMessage(API.getInstance().getLang().getString("ARENA.SET-LOCATION", true));
                return true;
            }
        }
        return false;
    }
}
