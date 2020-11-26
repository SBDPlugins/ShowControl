package nl.sbdeveloper.showapi;

import com.samjakob.spigui.SpiGUI;
import nl.sbdeveloper.showapi.commands.ShowCMD;
import nl.sbdeveloper.showapi.data.DataSaving;
import nl.sbdeveloper.showapi.utils.YamlFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.apihelper.APIManager;

public final class ShowAPIPlugin extends JavaPlugin {

    private static ShowAPIPlugin instance;
    private final ShowAPI showAPI = new ShowAPI();
    private static SpiGUI spiGUI;
    private static YamlFile data;

    @Override
    public void onLoad() {
        APIManager.registerAPI(showAPI, this);
    }

    @Override
    public void onEnable() {
        instance = this;

        data = new YamlFile("data");
        data.loadDefaults();
        DataSaving.load();

        APIManager.initAPI(ShowAPI.class);

        spiGUI = new SpiGUI(this);
        spiGUI.setEnableAutomaticPagination(true);

        getCommand("mctpshow").setExecutor(new ShowCMD());
    }

    @Override
    public void onDisable() {
        instance = null;

        DataSaving.save();

        APIManager.disableAPI(ShowAPI.class);
    }

    public static ShowAPIPlugin getInstance() {
        return instance;
    }

    public static SpiGUI getSpiGUI() {
        return spiGUI;
    }

    public static YamlFile getData() {
        return data;
    }
}
