package dev.myclxss.components;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Items {

    // Items del lobby
    public static ItemStack joinArenaItem;
    public static ItemStack serverSelectorItem;

    // Items del lobby
    public static ItemStack swordKit;
    public static ItemStack rodKit;
    public static ItemStack helmetKit;
    public static ItemStack chestplateKit;
    public static ItemStack legginsKit;
    public static ItemStack bootsKit;

    public static void init() {

        // Items del lobby
        createJoinArenaItem();
        createServerSelectorItem();

        // Items del lobby
        createSwordKit();
        createRodKit();
        createHelmetKit();
        createChestplateKit();
        createLeggingsKit();
        createBootsKit();
    }

    // Items del lobby
    private static void createJoinArenaItem() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Color.set("&cArena Item"));
        List<String> lore = new ArrayList<>();
        lore.add(Color.set("&alore xd"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        joinArenaItem = item;
    }

    private static void createServerSelectorItem() {
        ItemStack item = new ItemStack(Material.COMPASS, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Color.set("&eServer Selector"));
        List<String> lore = new ArrayList<>();
        lore.add(Color.set("&alore xd"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        serverSelectorItem = item;
    }

    // Items del lobby
    private static void createSwordKit() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Color.set("&CEspada del kit"));
        List<String> lore = new ArrayList<>();
        lore.add(Color.set("&alore xd"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        swordKit = item;
    }

    private static void createRodKit() {
        ItemStack item = new ItemStack(Material.FISHING_ROD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Color.set("&cCaña del kit"));
        List<String> lore = new ArrayList<>();
        lore.add(Color.set("&alore xd"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        rodKit = item;
    }

    private static void createHelmetKit() {
        ItemStack item = new ItemStack(Material.DIAMOND_HELMET, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Color.set("&eCasco del kit"));
        List<String> lore = new ArrayList<>();
        lore.add(Color.set("&alore xd"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        helmetKit = item;
    }

    private static void createChestplateKit() {
        ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Color.set("&ePechera del kit"));
        List<String> lore = new ArrayList<>();
        lore.add(Color.set("&alore xd"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        chestplateKit = item;
    }

    private static void createLeggingsKit() {
        ItemStack item = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Color.set("&ePantalon del kit"));
        List<String> lore = new ArrayList<>();
        lore.add(Color.set("&alore xd"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        legginsKit = item;
    }

    private static void createBootsKit() {
        ItemStack item = new ItemStack(Material.DIAMOND_BOOTS, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Color.set("&EBotas del kit"));
        List<String> lore = new ArrayList<>();
        lore.add(Color.set("&alore xd"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        bootsKit = item;
    }

}
