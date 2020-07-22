package nl.sbdeveloper.showapi;

import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.apihelper.APIManager;

public final class ShowAPIPlugin extends JavaPlugin {

    private static ShowAPIPlugin instance;
    private ShowAPI showAPI = new ShowAPI();

    @Override
    public void onLoad() {
        APIManager.registerAPI(showAPI, this);
    }

    @Override
    public void onEnable() {
        instance = this;
        APIManager.initAPI(ShowAPI.class);
    }

    @Override
    public void onDisable() {
        instance = null;
        APIManager.disableAPI(ShowAPI.class);
    }

    public static ShowAPIPlugin getInstance() {
        return instance;
    }
}
