package nl.sbdeveloper.showcontrol.data;

import nl.sbdeveloper.showcontrol.ShowControlPlugin;
import nl.sbdeveloper.showcontrol.api.TriggerTask;
import nl.sbdeveloper.showcontrol.utils.MainUtil;
import nl.sbdeveloper.showcontrol.utils.YamlFile;

import java.io.File;

public class DataConversion {
    public static void handle() {
        if (isOldSystem()) convert();
    }

    private static boolean isOldSystem() {
        File dataFolder = ShowControlPlugin.getInstance().getDataFolder();
        File dataFile = new File(dataFolder, "data.yml");
        return dataFile.exists();
    }

    private static void convert() {
        File dataFolder = new File(ShowControlPlugin.getInstance().getDataFolder(), "data");
        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        YamlFile dataFile = new YamlFile("data");
        for (String name : dataFile.getFile().getConfigurationSection("Shows").getKeys(false)) {
            //STEP 1: Convert to new system.
            YamlFile showFile = new YamlFile("data/" + name);
            for (String id : dataFile.getFile().getConfigurationSection("Shows." + name).getKeys(false)) {
                TriggerTask data = MainUtil.parseData(dataFile.getFile().getString("Shows." + name + "." + id + ".Type") + " " + dataFile.getFile().getString("Shows." + name + "." + id + ".Data"));
                long time = dataFile.getFile().getLong("Shows." + name + "." + id + ".Time");

                showFile.getFile().set(id + ".Time", time);
                showFile.getFile().set(id + ".Type", data.getType().name());
                showFile.getFile().set(id + ".Data", data.getDataString());
            }
            showFile.saveFile();
        }

        //STEP 2: Remove old storage.
        File data = new File(ShowControlPlugin.getInstance().getDataFolder(), "data.yml");
        data.delete();
    }
}
