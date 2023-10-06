package dev.myclxss;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import dev.myclxss.command.FirstCommand;
import dev.myclxss.command.JoinArenaCommand;
import dev.myclxss.command.LeaveArenaCommand;
import dev.myclxss.command.TutorialCommand;
import dev.myclxss.components.Files;
import dev.myclxss.components.Items;
import dev.myclxss.listener.JoinListener;
import dev.myclxss.listener.ProtectionListener;
import dev.myclxss.listener.TutorialListener;

public class API {

    private final List<UUID> arenaUsers = new ArrayList<>();
    private final List<UUID> tutorialUsers = new ArrayList<>();

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
        pluginManager.registerEvents(new ProtectionListener(), main);
        pluginManager.registerEvents(new TutorialListener(), main);

    }

    public void loadCommand() {

        main.getCommand("fullpvp").setExecutor(new FirstCommand());
        main.getCommand("join").setExecutor(new JoinArenaCommand());
        main.getCommand("leave").setExecutor(new LeaveArenaCommand());
        main.getCommand("tutorial").setExecutor(new TutorialCommand());

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

    public List<UUID> getTutorialUsers() {
        return tutorialUsers;
    }

    public Files getLang() {
        return lang;
    }

    public Files getLocations() {
        return locations;
    }
}
