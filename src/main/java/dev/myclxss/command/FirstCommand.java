package dev.myclxss.command;

import java.util.List;

import org.bukkit.ChatColor; 
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.myclxss.API;
import dev.myclxss.components.Color;

public class FirstCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Verficamos si el que ejecuta el comando es un jugaodor o la consola
        if (!(sender instanceof Player)) {
            // Si el comando se ejecuta desde la consola, el codigo no continua
            return true;
        }
        Player player = (Player) sender;

        // Si el comando no va acompañado de otro argumento, enviamos un mensaje
        if (args.length == 0) {
            player.sendMessage(Color.set("&6code by myclass"));
            return true;
        }
        // Si el comando va acompañado del arugmento (help) continuamos con el codigo
        if (args[0].equalsIgnoreCase("help")) {
            // Verificamos si el jugador cuenta con los permisos suficientes
            if (!player.hasPermission("fullpvp.help") || !player.hasPermission("fullpvp.all")) {
                // Si no cuenta con los permisos suficientes se envia un mensaje de error y el
                // codigo no continua
                player.sendMessage(API.getInstance().getLang().getString("ERROR.NO-PERMISSION", true));
                return true; 
            }
            // Aqui enviamos el mensaje en forma de lista obtenido de una string
            List<String> helpString = Color.set(API.getInstance().getLang().getStringList("HELP-MESSAGE"));
            for (String helpMessage : helpString) {
                player.sendMessage(Color.set(helpMessage));
            }
        }
        // Si el comando va acompañado del argumento (checklist) continuamos con el
        // codigo
        if (args[0].equalsIgnoreCase("checklist")) {
            // Verificamos si el jugador cuenta con los permisos suficientes
            if (!player.hasPermission("fullpvp.checklist") || !player.hasPermission("fullpvp.all")) {
                // Si no cuenta con los permisos suficientes se envia un mensaje de error y el
                // codigo no continua
                player.sendMessage(API.getInstance().getLang().getString("ERROR.NO-PERMISSION", true));
                return true;
            }
            // Hacemos el chequeo de lista, dependiendo en que lista te encuentres
            // enviaremos un mensaje
            if (API.getInstance().getLobbyUser().contains(player.getUniqueId())) {
                player.sendMessage(
                        ChatColor.YELLOW + player.getName() + " " + Color.set("&fte encuentras en la lista de &eLobby"));
                return true;
            }
            if (API.getInstance().getArenaUser().contains(player.getUniqueId())) {
                player.sendMessage(
                        ChatColor.YELLOW + player.getName() + Color.set("&fte encuentras en la lista de &eArena"));
                return true;
            }
            if (API.getInstance().getTutorialUser().contains(player.getUniqueId())) {
                player.sendMessage(
                        ChatColor.YELLOW + player.getName() + Color.set("&fte encuentras en la lista de &eTutorial"));
                return true;
            }
        }
        // Si el comando va acompañado del arugmento (set) continuamos con el codigo
        if (args[0].equalsIgnoreCase("set")) {
            // Si solo se usa el argumento (set) no hacemos ninguna accion
            // Si el argumento va acompañado de un sub argumento en este caso (spawn),
            // continuamos con el codigo
            if (args.length > 1 && args[1].equalsIgnoreCase("spawn")) {
                // Verficamos si el jugador cuenta con los permisos necersario
                if (!player.hasPermission("fullpvp.setspawn") || !player.hasPermission("fullpvp.all")) {
                    // Si no cuenta con los permisos suficientes enviamos un mensaje de error y el
                    // codigo se detiene.
                    player.sendMessage(API.getInstance().getLang().getString("ERROR.NO-PERMISSION", true));
                    return true;
                }
                // Si cuenta con los permisos suficientes establecemos la ubicacion del spawn en
                // un archivo yml
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
            // Si el argumento va acompañado de un sub argumento en este caso (arena),
            // continuamos con el codigo
            if (args.length > 1 && args[1].equalsIgnoreCase("arena")) {
                // Verficamos si el jugador cuenta con los permisos necersario
                if (!player.hasPermission("fullpvp.setarena") || !player.hasPermission("fullpvp.all")) {
                    // Si no cuenta con los permisos suficientes enviamos un mensaje de error y el
                    // codigo se detiene.
                    player.sendMessage(API.getInstance().getLang().getString("ERROR.NO-PERMISSION", true));
                    return true;
                }
                // Si cuenta con los permisos suficientes establecemos la ubicacion de la arena
                // en un archivo yml
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
            // Si el argumento va acompañado de un sub argumento en este caso (tutorial),
            // continuamos con el codigo
            if (args.length > 1 && args[1].equalsIgnoreCase("tutorial")) {
                // Verficamos si el jugador cuenta con los permisos necersario
                if (!player.hasPermission("fullpvp.settutorial") || !player.hasPermission("fullpvp.all")) {
                    // Si no cuenta con los permisos suficientes enviamos un mensaje de error y el
                    // codigo se detiene.
                    player.sendMessage(API.getInstance().getLang().getString("ERROR.NO-PERMISSION"));
                    return true;
                }
                // Si cuenta con los permisos suficientes establecemos la ubicacion del tutorial
                // en un archivo yml
                API.getInstance().getLocations().set("TUTORIAL.WORLD", player.getLocation().getWorld().getName());
                API.getInstance().getLocations().set("TUTORIAL.X", Double.valueOf(player.getLocation().getX()));
                API.getInstance().getLocations().set("TUTORIAL.Y", Double.valueOf(player.getLocation().getY()));
                API.getInstance().getLocations().set("TUTORIAL.Z", Double.valueOf(player.getLocation().getZ()));
                API.getInstance().getLocations().set("TUTORIAL.YAW", Float.valueOf(player.getLocation().getYaw()));
                API.getInstance().getLocations().set("TUTORIAL.PITCH", Float.valueOf(player.getLocation().getPitch()));
                API.getInstance().getLocations().save();
                player.sendMessage(API.getInstance().getLang().getString("TUTORIAL.SET-LOCATION", true));
                return true;
            }
        }
        return false;
    }
}
