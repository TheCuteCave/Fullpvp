package dev.myclxss.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.myclxss.API;
import dev.myclxss.components.Color;

public class LeaveArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Verficamos si el que ejecuta el comando es un jugaodor o la consola
        if (!(sender instanceof Player)) {
            // Si el comando se ejecuta desde la consola, el codigo no continua
            return true;
        }
        Player player = (Player) sender;
        // Al ejecutar el comando realiza las siguientes acciones
        if (args.length == 0) {
            // Verifamos si te encuentras dentro de la lista (ArenaUser)
            if (API.getInstance().getArenaUser().contains(player.getUniqueId())) {
                // Si se encuentra dentro de la lista, lo removemos
                API.getInstance().getArenaUser().remove(player.getUniqueId());
                // Reproducimos un sonido al jugador para mejorar la experiencia
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 15, 15);
                // Añadimos un efecto para mejorar la experiencia
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 10));
                // Enviamos un mensaje para avisar que salio en forma de lista
                List<String> arenaLeaveMessage = API.getInstance().getLang().getStringList("ARENA.LEAVE-MESSAGE");
                for (int i = 0; i < arenaLeaveMessage.size(); i++) {
                    String joinMessage = arenaLeaveMessage.get(i);
                    player.sendMessage(Color.set(joinMessage));
                }
                // Agregamos al jugador a la lista del lobby
                // Verificamos primero si se encuntra ya dentro de la lista (LobbyUser)
                if (API.getInstance().getLobbyUser().contains(player.getUniqueId())) {
                    // Si se encuentra dentro lo removemos
                    API.getInstance().getLobbyUser().remove(player.getUniqueId());
                }
                // Lo agregamos a la lista
                API.getInstance().getLobbyUser().add(player.getUniqueId());
                // Enviamos al jugador al spawn principal
                World world = Bukkit.getServer().getWorld(API.getInstance().getLocations().getString("SPAWN.WORLD"));
                double x = API.getInstance().getLocations().getDouble("SPAWN.X");
                double y = API.getInstance().getLocations().getDouble("SPAWN.Y");
                double z = API.getInstance().getLocations().getDouble("SPAWN.Z");
                float yaw = (float) API.getInstance().getLocations().getDouble("SPAWN.YAW");
                float pitch = (float) API.getInstance().getLocations().getDouble("SPAWN.PITCH");
                Location location = new Location(world, x, y, z, yaw, pitch);
                player.teleport(location);
            }
            return true;
        }
        return false;
    }
}
