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
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.myclxss.API;
import dev.myclxss.components.Color;
import dev.myclxss.components.Items;
import dev.myclxss.components.TitleAPI;

public class TutorialCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Verficamos si el que ejecuta el comando es un jugaodor o la consola
        if (!(sender instanceof Player)) {
            // Verficamos si el que ejecuta el comando es un jugaodor o la consola
            return true;
        }
        Player player = (Player) sender;
        // Al ejecutar el comando realiza las siguientes acciones
        if (args.length == 0) {
            // Verificamos si el jugador cuenta con los permisos suficientes
            if (!player.hasPermission("fullpvp.tutorial")) {
                // Si no cuenta con los permisos suficientes se envia un mensaje de error y el
                // codigo no continua
                player.sendMessage(API.getInstance().getLang().getString("ERROR.NO-PERMISSION", true));
                return true;
            }
            // Verificar si el inventario del jugador está vacío
            if (isInventoryEmpty(player)) {
                // Verificamos si el jugador se encuentra dentro de la lista (LobbyUser)
                if (API.getInstance().getLobbyUser().contains(player.getUniqueId())) {
                    // Si se encuentra dentro lo removemos
                    API.getInstance().getLobbyUser().remove(player.getUniqueId());
                }
                // Lo agregamos a la lista del tutorial
                API.getInstance().getTutorialUser().add(player.getUniqueId());
                // Reproducimos un sonido al jugador para mejorar la experiencia
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 15, 15);
                // Añadimos un efecto para mejorar la experiencia
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 10));
                // Eliminamos todos los items del jugador para evitar bugs
                player.getInventory().clear();
                // Agregamos el item del tutorial
                player.getInventory().setItem(4, Items.tutorialItem);
                // Enviamos un titulo al jugador
                TitleAPI.sendTitle(player, 30, 50, 30, API.getInstance().getLang().getString("TUTORIAL.TITLE"),
                        API.getInstance().getLang().getString("TUTORIAL.SUBTITLE"));
                // Enviamos un mensaje en forma de lista
                List<String> joinMessageString = API.getInstance().getLang().getStringList("TUTORIAL.JOIN-MESSAGE");
                for (int i = 0; i < joinMessageString.size(); i++) {
                    String joinMessage = joinMessageString.get(i);
                    player.sendMessage(Color.set(joinMessage));
                }
                // Enviamos al jugador a la ubicacion del tutorial
                World world = Bukkit.getServer().getWorld(API.getInstance().getLocations().getString("TUTORIAL.WORLD"));
                double x = API.getInstance().getLocations().getDouble("TUTORIAL.X");
                double y = API.getInstance().getLocations().getDouble("TUTORIAL.Y");
                double z = API.getInstance().getLocations().getDouble("TUTORIAL.Z");
                float yaw = (float) API.getInstance().getLocations().getDouble("TUTORIAL.YAW");
                float pitch = (float) API.getInstance().getLocations().getDouble("TUTORIAL.PITCH");
                Location location = new Location(world, x, y, z, yaw, pitch);
                player.teleport(location);
                return true;
            } else {
                // El jugador tiene elementos en su inventario, cancelamos el codigo
                player.sendMessage(Color.set("&CNo puedes entrar al tutorial, guarda tus items e inventate de nuevo"));
            }
        }
        return false;
    }
    // Método para verificar si el inventario del jugador está vacío
    private boolean isInventoryEmpty(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                return false; // El inventario contiene al menos un elemento
            }
        }
        return true; // El inventario está vacío
    }
}
