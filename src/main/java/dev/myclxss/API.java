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
import dev.myclxss.event.JoinListener;
import dev.myclxss.event.TutorialListener;
import dev.myclxss.event.UserListener;
import dev.myclxss.event.WorldListener;

public class API {

    private final List<UUID> lobbyUser = new ArrayList<>();
    private final List<UUID> arenaUser = new ArrayList<>();
    private final List<UUID> tutorialUser = new ArrayList<>();

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
        pluginManager.registerEvents(new TutorialListener(), main);
        pluginManager.registerEvents(new UserListener(), main);
        pluginManager.registerEvents(new WorldListener(), main);

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

    public List<UUID> getLobbyUser() {
        return lobbyUser;
    }

    public List<UUID> getArenaUser() {
        return arenaUser;
    }

    public List<UUID> getTutorialUser() {
        return tutorialUser;

    }

    public Files getLang() {
        return lang;
    }

    public Files getLocations() {
        return locations;
    }
}
