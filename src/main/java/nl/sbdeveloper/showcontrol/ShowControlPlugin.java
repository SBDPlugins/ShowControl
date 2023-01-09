package nl.sbdeveloper.showcontrol;

import co.aikar.commands.PaperCommandManager;
import nl.sbdeveloper.showcontrol.api.ShowAPI;
import nl.sbdeveloper.showcontrol.commands.ShowCMD;
import nl.sbdeveloper.showcontrol.data.DataSaving;
import nl.sbdeveloper.showcontrol.data.Shows;
import nl.sbdeveloper.showcontrol.utils.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShowControlPlugin extends JavaPlugin {
    private static ShowControlPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        final PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");

        commandManager.registerCommand(new ShowCMD());

        commandManager.getCommandCompletions().registerCompletion("showname", c -> Shows.getShowsMap().keySet());
        commandManager.getCommandCompletions().registerCompletion("showtype", c -> ShowAPI.getTriggers().keySet());

        Inventory.init();

        Bukkit.getScheduler().runTaskLater(this, DataSaving::load, 1L); //Load 1 tick later, because of multi world
    }

    @Override
    public void onDisable() {
        instance = null;

        DataSaving.save();

        Shows.getShowsMap().values().forEach(show -> show.forEach(showCue -> showCue.getTask().remove()));
    }

    public static ShowControlPlugin getInstance() {
        return instance;
    }
}
