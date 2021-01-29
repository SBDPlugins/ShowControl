package nl.sbdeveloper.showapi;

import co.aikar.commands.PaperCommandManager;
import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import com.github.fierioziy.particlenativeapi.api.utils.ParticleException;
import com.github.fierioziy.particlenativeapi.core.ParticleNativeCore;
import nl.sbdeveloper.showapi.commands.ShowCMD;
import nl.sbdeveloper.showapi.data.DataSaving;
import nl.sbdeveloper.showapi.data.Shows;
import nl.sbdeveloper.showapi.utils.Inventory;
import nl.sbdeveloper.showapi.utils.YamlFile;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.apihelper.APIManager;

public final class ShowAPIPlugin extends JavaPlugin {
    private static ShowAPIPlugin instance;
    private final ShowAPI showAPI = new ShowAPI();
    
    private static PaperCommandManager commandManager;

    private static YamlFile data;
    private static ParticleNativeAPI particleAPI;

    @Override
    public void onLoad() {
        APIManager.registerAPI(showAPI, this);
    }

    @Override
    public void onEnable() {
        instance = this;

        data = new YamlFile("data");
        data.loadDefaults();

        APIManager.initAPI(ShowAPI.class);

        commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");

        commandManager.registerCommand(new ShowCMD());

        try {
            particleAPI = ParticleNativeCore.loadAPI(this);
        } catch (ParticleException ex) {
            ex.printStackTrace();
            getPluginLoader().disablePlugin(this);
            return;
        }

        Inventory.init();

        Bukkit.getScheduler().runTaskLater(this, DataSaving::load, 1L); //Load 1 tick later, because of multi world
    }

    @Override
    public void onDisable() {
        instance = null;

        DataSaving.save();

        Shows.getShowsMap().values().forEach(show -> show.forEach(showCue -> showCue.getTask().remove()));

        APIManager.disableAPI(ShowAPI.class);
    }

    public static ShowAPIPlugin getInstance() {
        return instance;
    }

    public static PaperCommandManager getCommandManager() {
        return commandManager;
    }

    public static YamlFile getData() {
        return data;
    }

    public static ParticleNativeAPI getParticleAPI() {
        return particleAPI;
    }
}
