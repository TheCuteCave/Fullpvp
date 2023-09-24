package dev.myclxss;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import dev.myclxss.command.FirstCommand;
import dev.myclxss.command.JoinArenaCommand;
import dev.myclxss.components.Files;
import dev.myclxss.components.Items;
import dev.myclxss.listener.CombatListener;
import dev.myclxss.listener.JoinListener;

public class API {

    private final List<UUID> arenaUsers = new ArrayList<>();

    private static API instance;
    private final Fullpvp main;

    private Files lang;
    private Files locations;

    public API(final Fullpvp plugin) {

        instance = this;
        main = plugin;

        Items.init();

        lang = new Files(plugin, "lang");
        locations = new Files(plugin, "locations");

        loadListener();
        loadCommand();

    }

    public void loadListener() {

        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new JoinListener(), main);
        pluginManager.registerEvents(new CombatListener(), main);

    }

    public void loadCommand() {

        main.getCommand("fullpvp").setExecutor(new FirstCommand());
        main.getCommand("join").setExecutor(new JoinArenaCommand());

    }

    public Fullpvp getMain() {
        return main;
    }

    public static API getInstance() {
        return instance;
    }

    public List<UUID> getArenaUsers() {
        return arenaUsers;
    }

    public Files getLang() {
        return lang;
    }

    public Files getLocations() {
        return locations;
    }
}
