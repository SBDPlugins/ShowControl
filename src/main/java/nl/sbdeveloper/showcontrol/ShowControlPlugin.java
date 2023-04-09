package nl.sbdeveloper.showcontrol;

import co.aikar.commands.PaperCommandManager;
import nl.sbdeveloper.showcontrol.api.ShowAPI;
import nl.sbdeveloper.showcontrol.commands.ShowCMD;
import nl.sbdeveloper.showcontrol.data.DataStorage;
import nl.sbdeveloper.showcontrol.data.Shows;
import nl.sbdeveloper.showcontrol.utils.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShowControlPlugin extends JavaPlugin {
    private static ShowControlPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("-------------------------------");
        getLogger().info("ShowControl v" + getDescription().getVersion());
        getLogger().info("Made by SBDeveloper");
        getLogger().info(" ");

        getLogger().info("Loading commands...");
        final PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.registerCommand(new ShowCMD());
        commandManager.getCommandCompletions().registerCompletion("showname", c -> Shows.getShowsMap().keySet());
        commandManager.getCommandCompletions().registerCompletion("showtype", c -> ShowAPI.getTriggers().keySet());

        getLogger().info("Loading GUI manageer...");
        Inventory.init();

        getLogger().info("Loading default triggers...");
        ShowAPI.index(ShowControlPlugin.class, "nl.sbdeveloper.showcontrol.api.triggers.impl");

        Bukkit.getScheduler().runTaskLater(this, DataStorage::load, 1L); //Load 1 tick later, because of multi world

        getLogger().info("Plugin enabled!");
        getLogger().info("-------------------------------");
    }

    @Override
    public void onDisable() {
        getLogger().info("Saving data...");
        DataStorage.save();
        Shows.getShowsMap().values().forEach(show -> show.forEach(showCue -> showCue.getTask().remove()));

        getLogger().info("Plugin disabled!");
        instance = null;
    }

    public static ShowControlPlugin getInstance() {
        return instance;
    }
}
