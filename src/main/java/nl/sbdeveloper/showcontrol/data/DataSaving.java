package nl.sbdeveloper.showcontrol.data;

import nl.sbdeveloper.showcontrol.ShowControlPlugin;
import nl.sbdeveloper.showcontrol.api.InvalidTriggerException;
import nl.sbdeveloper.showcontrol.api.ShowAPI;
import nl.sbdeveloper.showcontrol.api.ShowCuePoint;
import nl.sbdeveloper.showcontrol.api.TooFewArgumentsException;
import nl.sbdeveloper.showcontrol.api.triggers.Trigger;
import nl.sbdeveloper.showcontrol.utils.YamlFile;

import java.io.File;
import java.util.*;

public class DataSaving {
    private static final Map<String, YamlFile> files = new HashMap<>();

    public static Map<String, YamlFile> getFiles() {
        return files;
    }

    public static void load() {
        File showsFolder = new File(ShowControlPlugin.getInstance().getDataFolder(), "data");
        for (File showFile : showsFolder.listFiles()) {
            String showID = removeExtension(showFile.getName());
            YamlFile showConfig = new YamlFile("data/" + showID);
            files.put(showID, showConfig);

            List<ShowCuePoint> cues = new ArrayList<>();
            for (String id : showConfig.getFile().getKeys(false)) {
                UUID cueID = UUID.fromString(id);
                Trigger data;
                try {
                    data = ShowAPI.getTrigger(showConfig.getFile().getString(id + ".Type") + " " + showConfig.getFile().getString(id + ".Data"));
                } catch (ReflectiveOperationException | InvalidTriggerException | TooFewArgumentsException e) {
                    e.printStackTrace();
                    return;
                }
                long time = showConfig.getFile().getLong(id + ".Time");

                cues.add(new ShowCuePoint(cueID, time, data));
            }
            Shows.getShowsMap().put(showID, cues);
        }
    }

    public static void save() {
        for (Map.Entry<String, List<ShowCuePoint>> entry : Shows.getShowsMap().entrySet()) {
            YamlFile file = files.containsKey(entry.getKey()) ? files.get(entry.getKey()) : new YamlFile("data/" + entry.getKey());
            for (ShowCuePoint cue : entry.getValue()) {
                file.getFile().set(cue.getCueID().toString() + ".Time", cue.getTime());
                file.getFile().set(cue.getCueID().toString() + ".Type", cue.getTask().getTriggerId());
                file.getFile().set(cue.getCueID().toString() + ".Data", cue.getTask().getDataString());
            }
            file.saveFile();

            if (!files.containsKey(entry.getKey())) files.put(entry.getKey(), file);
        }
    }

    private static String removeExtension(String fileName) {
        int lastDot = fileName.lastIndexOf(".");
        return (lastDot == -1 ? fileName : fileName.substring(0, lastDot));
    }
}
