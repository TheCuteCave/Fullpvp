package dev.myclxss.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import dev.myclxss.API;
import dev.myclxss.components.Color;
import dev.myclxss.components.Items;
import dev.myclxss.components.TitleAPI;

public class JoinArenaCommand implements CommandExecutor {

    private Map<Player, BukkitRunnable> countdownTasks = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {

            if (API.getInstance().getArenaUsers().contains(player.getUniqueId())) {
                player.sendMessage(API.getInstance().getLang().getString("ARENA.ERROR-JOIN-ARENA", true));
                return true;
            } else {
                startCountdown(player);
            }
            return true;
        }
        return false;
    }

    private void startCountdown(Player player) {
        if (countdownTasks.containsKey(player)) {
            player.sendMessage(API.getInstance().getLang().getString("ARENA.IN-THE-QUEUE", true));
            return;
        }
        BukkitRunnable countdownTask = new BukkitRunnable() {
            int countdown = 5;

            @Override
            public void run() {
                if (countdown > 0) {
                    if (!player.isSneaking()) {
                        TitleAPI.sendTitle(player, 30, 80, 30,
                                ChatColor.YELLOW + "Comenznado en" + " " + ChatColor.RED + countdown + " "
                                        + ChatColor.YELLOW + "segundos",
                                ChatColor.DARK_RED + "manten presionado shift para cancelar");
                        countdown--;
                    } else {
                        player.sendMessage(API.getInstance().getLang().getString("ARENA.CANCEL-QUEUE", true));
                        cancel();
                        countdownTasks.remove(player);
                    }
                } else {
                    if (API.getInstance().getLocations().getConfigurationSection("ARENA") == null) {
                        player.sendMessage(API.getInstance().getLang().getString("ARENA.ERROR-LOCATION", true));
                        cancel();
                        countdownTasks.remove(player);
                        return;
                    }
                    if (API.getInstance().getArenaUsers().contains(player.getUniqueId())) {
                        API.getInstance().getArenaUsers().remove(player.getUniqueId());
                    }
                    API.getInstance().getArenaUsers().add(player.getUniqueId());
                    // Cuando la cuenta regresiva llega a 0, teletransportar al jugador
                    World world = Bukkit.getServer()
                            .getWorld(API.getInstance().getLocations().getString("ARENA.WORLD"));
                    double x = API.getInstance().getLocations().getDouble("ARENA.X");
                    double y = API.getInstance().getLocations().getDouble("ARENA.Y");
                    double z = API.getInstance().getLocations().getDouble("ARENA.Z");
                    float yaw = (float) API.getInstance().getLocations().getDouble("ARENA.YAW");
                    float pitch = (float) API.getInstance().getLocations().getDouble("ARENA.PITCH");
                    Location location = new Location(world, x, y, z, yaw, pitch);

                    // Verifica si el jugador tiene algún item en su inventario
                    boolean inventarioVacio = true;
                    for (ItemStack item : player.getInventory().getContents()) {
                        if (item != null && item.getType() != Material.AIR) {
                            inventarioVacio = false;
                            break;
                        }
                    }

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

                        player.sendMessage(API.getInstance().getLang().getString("ARENA.VOID-IVENTORY", true));
                    } else {

                    }

                    List<String> arenaJoinMessage = API.getInstance().getLang().getStringList("ARENA.JOIN-MESSAGE");

                    for (int i = 0; i < arenaJoinMessage.size(); i++) {
                        String joinMessage = arenaJoinMessage.get(i);
                        player.sendMessage(Color.set(joinMessage));
                    }
                    player.sendMessage(API.getInstance().getLang().getString("ARENA.SEND-COMBAT", true));
                    player.teleport(location);
                    cancel();
                    countdownTasks.remove(player);
                }
            }
        };

        countdownTask.runTaskTimer(API.getInstance().getMain(), 0L, 20L); // Ejecutar cada segundo (20 ticks)
        countdownTasks.put(player, countdownTask);
    }
}