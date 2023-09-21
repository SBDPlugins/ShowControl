package tech.sbdevelopment.showcontrol;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import tech.sbdevelopment.showcontrol.api.SCAPI;
import tech.sbdevelopment.showcontrol.commands.ShowCMD;
import tech.sbdevelopment.showcontrol.data.DataStorage;
import tech.sbdevelopment.showcontrol.utils.inventories.Inventory;

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
        commandManager.getCommandCompletions().registerCompletion("showname", c -> SCAPI.getShowsMap().keySet());
        commandManager.getCommandCompletions().registerCompletion("showtype", c -> SCAPI.getTriggers().keySet());
        commandManager.getCommandCompletions().registerCompletion("cuearg", c -> {
            String arguments = c.getContextValue(String.class, 3);
            String[] args = arguments.split(" ", -1);
            if (args.length < 1) {
                return null;
            }
            int lastArgIndex = args.length - 2;
            return SCAPI.getTabComplete(args[0], c.getSender() instanceof Player ? c.getPlayer() : null, lastArgIndex, args[lastArgIndex + 1]);
        });

        getLogger().info("Loading GUI manageer...");
        Inventory.init(this);

        getLogger().info("Loading default triggers...");
        SCAPI.index(ShowControlPlugin.class, "tech.sbdevelopment.showcontrol.api.triggers.impl");

        Bukkit.getScheduler().runTaskLater(this, DataStorage::load, 1L); //Load 1 tick later, because of multi world

        getLogger().info("Plugin enabled!");
        getLogger().info("-------------------------------");
    }

    @Override
    public void onDisable() {
        getLogger().info("Saving data...");
        DataStorage.save();
        SCAPI.getShowsMap().values().forEach(show -> show.forEach(showCue -> showCue.getData().remove()));

        getLogger().info("Plugin disabled!");
        instance = null;
    }

    public static ShowControlPlugin getInstance() {
        return instance;
    }
}
