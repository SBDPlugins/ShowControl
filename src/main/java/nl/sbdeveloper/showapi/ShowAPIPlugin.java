package nl.sbdeveloper.showapi;

import co.aikar.commands.PaperCommandManager;
import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import com.github.fierioziy.particlenativeapi.api.utils.ParticleException;
import com.github.fierioziy.particlenativeapi.core.ParticleNativeCore;
import nl.sbdeveloper.showapi.api.TriggerType;
import nl.sbdeveloper.showapi.commands.ShowCMD;
import nl.sbdeveloper.showapi.data.DataConversion;
import nl.sbdeveloper.showapi.data.DataSaving;
import nl.sbdeveloper.showapi.data.Shows;
import nl.sbdeveloper.showapi.utils.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class ShowAPIPlugin extends JavaPlugin {
    private static ShowAPIPlugin instance;

    private static PaperCommandManager commandManager;

    private static ParticleNativeAPI particleAPI;

    @Override
    public void onEnable() {
        instance = this;

        DataConversion.handle();

        commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");

        commandManager.registerCommand(new ShowCMD());

        commandManager.getCommandCompletions().registerCompletion("showname", c -> Shows.getShowsMap().keySet());
        commandManager.getCommandCompletions().registerStaticCompletion("showtype", Arrays.stream(TriggerType.values()).map(Enum::name).collect(Collectors.toList()));

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
    }

    public static ShowAPIPlugin getInstance() {
        return instance;
    }

    public static PaperCommandManager getCommandManager() {
        return commandManager;
    }

    public static ParticleNativeAPI getParticleAPI() {
        return particleAPI;
    }
}
