package tech.sbdevelopment.showcontrol;

import co.aikar.commands.PaperCommandManager;
import tech.sbdevelopment.showcontrol.api.ShowAPI;
import tech.sbdevelopment.showcontrol.commands.ShowCMD;
import tech.sbdevelopment.showcontrol.data.DataStorage;
import tech.sbdevelopment.showcontrol.data.Shows;
import tech.sbdevelopment.showcontrol.utils.inventories.Inventory;
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
        Inventory.init(this);

        getLogger().info("Loading default triggers...");
        ShowAPI.index(ShowControlPlugin.class, "tech.sbdevelopment.showcontrol.api.triggers.impl");

        Bukkit.getScheduler().runTaskLater(this, DataStorage::load, 1L); //Load 1 tick later, because of multi world

        getLogger().info("Plugin enabled!");
        getLogger().info("-------------------------------");
    }

    @Override
    public void onDisable() {
        getLogger().info("Saving data...");
        DataStorage.save();
        Shows.getShowsMap().values().forEach(show -> show.forEach(showCue -> showCue.getData().remove()));

        getLogger().info("Plugin disabled!");
        instance = null;
    }

    public static ShowControlPlugin getInstance() {
        return instance;
    }
}
