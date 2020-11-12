package nl.sbdeveloper.showapi;

import com.samjakob.spigui.SpiGUI;
import nl.sbdeveloper.showapi.commands.ShowCMD;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.apihelper.APIManager;

public final class ShowAPIPlugin extends JavaPlugin {

    private static ShowAPIPlugin instance;
    private final ShowAPI showAPI = new ShowAPI();
    private static SpiGUI spiGUI;

    @Override
    public void onLoad() {
        APIManager.registerAPI(showAPI, this);
    }

    @Override
    public void onEnable() {
        instance = this;

        APIManager.initAPI(ShowAPI.class);

        spiGUI = new SpiGUI(this);

        getCommand("mctpshow").setExecutor(new ShowCMD());
    }

    @Override
    public void onDisable() {
        instance = null;
        APIManager.disableAPI(ShowAPI.class);
    }

    public static ShowAPIPlugin getInstance() {
        return instance;
    }

    public static SpiGUI getSpiGUI() {
        return spiGUI;
    }
}
