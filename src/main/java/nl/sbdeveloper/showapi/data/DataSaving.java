package nl.sbdeveloper.showapi.data;

import nl.sbdeveloper.showapi.ShowAPIPlugin;
import nl.sbdeveloper.showapi.api.ShowCue;
import nl.sbdeveloper.showapi.api.TriggerTask;
import nl.sbdeveloper.showapi.utils.MainUtil;
import nl.sbdeveloper.showapi.utils.YamlFile;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.*;

public class DataSaving {
    private static final Map<String, YamlFile> files = new HashMap<>();

    public static Map<String, YamlFile> getFiles() {
        return files;
    }

    public static void load() {
        File showsFolder = new File(ShowAPIPlugin.getInstance().getDataFolder(), "data");
        for (File showFile : showsFolder.listFiles()) {
            String showID = FilenameUtils.removeExtension(showFile.getName());
            YamlFile showConfig = new YamlFile("data/" + showID);
            files.put(showID, showConfig);

            List<ShowCue> cues = new ArrayList<>();
            for (String id : showConfig.getFile().getKeys(false)) {
                UUID cueID = UUID.fromString(id);
                TriggerTask data = MainUtil.parseData(showConfig.getFile().getString(id + ".Type") + " " + showConfig.getFile().getString(id + ".Data"));
                long time = showConfig.getFile().getLong(id + ".Time");

                cues.add(new ShowCue(cueID, time, data));
            }
            Shows.getShowsMap().put(showID, cues);
        }
    }

    public static void save() {
        for (Map.Entry<String, List<ShowCue>> entry : Shows.getShowsMap().entrySet()) {
            YamlFile file = files.get(entry.getKey());
            for (ShowCue cue : entry.getValue()) {
                file.getFile().set(cue.getCueID().toString() + ".Time", cue.getTime());
                file.getFile().set(cue.getCueID().toString() + ".Type", cue.getTask().getType().name());
                file.getFile().set(cue.getCueID().toString() + ".Data", cue.getTask().getDataString());
            }
            file.saveFile();
        }
    }
}
