package dev.myclxss.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import dev.myclxss.API;
import dev.myclxss.components.Color;
import dev.myclxss.components.Items;
import dev.myclxss.components.TitleAPI;

public class JoinArenaCommand implements CommandExecutor {

    private Map<Player, BukkitRunnable> countdownTasks = new HashMap<>();

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
            // verficamos si la ubicacion de la arena exista, si no existe enviamos un
            // mensaje de error y no continua el codigo.
            if (API.getInstance().getLocations().getConfigurationSection("ARENA") == null) {
                player.sendMessage(API.getInstance().getLang().getString("ERROR.ARENA-LOCATION", true));
                return true;
            }
            // Ahora verificamos si el jugador ya se encuentra dentro de la lista, si es asi
            // le mandamos un mensaje de error
            if (API.getInstance().getArenaUser().contains(player.getUniqueId())) {
                // Aqui enviamos el mensaje de error y el codigo se detiene
                player.sendMessage(API.getInstance().getLang().getString("ARENA.ERROR-JOIN-ARENA", true));
                return true;
            } else {
                // Si el jugador no esta dentro de la lista, iniciamos la cuenta regresiva
                startCountdown(player);
            }

            return true;
        }
        return false;
    }

    private void startCountdown(Player player) {
        // Aqui verificamos si el jugador ya se encuentra dentro de la cuenta regresiva
        if (countdownTasks.containsKey(player)) {
            // Si ya se encuentra dentro, enviamos un mensaje mencionandolo y el codigo se
            // detiene
            player.sendMessage(API.getInstance().getLang().getString("ARENA.IN-THE-QUEUE", true));
            return;
        }
        // Si no se encuentra dentro de la cuenta regresiva, esta comienza
        BukkitRunnable countdownTask = new BukkitRunnable() {
            int countdown = 5;

            // Comienza una tarea para realizar un cooldown de 5 segundos
            @Override
            public void run() {
                if (countdown > 0) {
                    // Verificamos si el jugador mantiene presionado la tecla (shift)
                    if (!player.isSneaking()) {
                        // Enviamos un titulo en la cara del jugador con el tiempo restante
                        TitleAPI.sendTitle(player, 30, 50, 30,
                                Color.set("&5&lEntrando en") + " " + ChatColor.YELLOW + countdown,
                                Color.set("&cmanten shift para cancelar"));
                        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 15, 15);

                        countdown--;
                    } else {
                        // Si el jugador uso (shift) cancelamos la cuenta regresia y enviamos un mensaje
                        // de cancelacion

                        player.sendMessage(API.getInstance().getLang().getString("ARENA.CANCEL-QUEUE", true));
                        // Reproducimos un sonido al jugador para mejorar la experiencia
                        player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 15, 15);
                        cancel();
                        // removemos al jugador de la lsita de cuenta regresiva
                        countdownTasks.remove(player);
                    }
                } else {
                    if (API.getInstance().getLobbyUser().contains(player.getUniqueId())) {
                        API.getInstance().getLobbyUser().remove(player.getUniqueId());
                    }
                    // Al entrar a la arena, añadimos al jugador a la lista (ArenaUser)
                    API.getInstance().getArenaUser().add(player.getUniqueId());
                    // Reproducimos un sonido al jugador para mejorar la experiencia
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 15, 15);
                    // Añadimos un efecto para mejorar la experiencia
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 10));
                    // Enviamos un mensaje de entrada a la arena en forma de lista
                    List<String> arenaJoinMessage = API.getInstance().getLang().getStringList("ARENA.JOIN-MESSAGE");
                    for (int i = 0; i < arenaJoinMessage.size(); i++) {
                        String joinMessage = arenaJoinMessage.get(i);
                        player.sendMessage(Color.set(joinMessage));
                    }
                    // Verificamos si el jugador tiene items en su inventario
                    boolean inventarioVacio = true;
                    for (ItemStack item : player.getInventory().getContents()) {
                        if (item != null && item.getType() != Material.AIR) {
                            inventarioVacio = false;
                            break;
                        }
                    }
                    // Si el inventario esta vacio, hacemos una limpieza para evitar bugs y
                    // agregamos items
                    if (inventarioVacio) {

                        player.getInventory().clear();
                        // Si el inventario está vacío, dale una armadura de diamante
                        player.getInventory().setItem(0, Items.swordKit);
                        player.getInventory().setItem(1, Items.rodKit);
                        player.getInventory().setItem(2, Items.goldenappleKit);

                        player.getInventory().setHelmet(Items.helmetKit);
                        player.getInventory().setChestplate(Items.chestplateKit);
                        player.getInventory().setLeggings(Items.legginsKit);
                        player.getInventory().setBoots(Items.bootsKit);
                        // Enviamos un mneje para que el jugador se entere de la accion
                        player.sendMessage(API.getInstance().getLang().getString("ARENA.VOID-IVENTORY", true));
                    } else {
                        // Si el inventario no esta vacio no hacemos nada
                    }
                    // Ahora enviamos al jugador a la ubiacion de la arena
                    World world = Bukkit.getServer()
                            .getWorld(API.getInstance().getLocations().getString("ARENA.WORLD"));
                    double x = API.getInstance().getLocations().getDouble("ARENA.X");
                    double y = API.getInstance().getLocations().getDouble("ARENA.Y");
                    double z = API.getInstance().getLocations().getDouble("ARENA.Z");
                    float yaw = (float) API.getInstance().getLocations().getDouble("ARENA.YAW");
                    float pitch = (float) API.getInstance().getLocations().getDouble("ARENA.PITCH");
                    Location location = new Location(world, x, y, z, yaw, pitch);
                    player.teleport(location);
                    // Cancelamos la cuenta regresiva y removemos al jugador de la lista
                    cancel();
                    countdownTasks.remove(player);
                }

            }
        };
        countdownTask.runTaskTimer(API.getInstance().getMain(), 0L, 20L); // Ejecutar cada segundo (20 ticks)
        countdownTasks.put(player, countdownTask);
    }
}
