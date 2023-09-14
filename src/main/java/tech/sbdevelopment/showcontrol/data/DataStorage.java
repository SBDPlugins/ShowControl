package tech.sbdevelopment.showcontrol.data;

import tech.sbdevelopment.showcontrol.ShowControlPlugin;
import tech.sbdevelopment.showcontrol.api.exceptions.InvalidTriggerException;
import tech.sbdevelopment.showcontrol.api.ShowAPI;
import tech.sbdevelopment.showcontrol.api.points.ShowCuePoint;
import tech.sbdevelopment.showcontrol.api.exceptions.TooFewArgumentsException;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.utils.YamlFile;

import java.io.File;
import java.util.*;

public class DataStorage {
    private static final Map<String, YamlFile> files = new HashMap<>();

    public static Map<String, YamlFile> getFiles() {
        return files;
    }

    public static void load() {
        // Create data folder if not exists
        if (!ShowControlPlugin.getInstance().getDataFolder().exists())
            ShowControlPlugin.getInstance().getDataFolder().mkdir();
        if (!new File(ShowControlPlugin.getInstance().getDataFolder(), "data").exists())
            new File(ShowControlPlugin.getInstance().getDataFolder(), "data").mkdir();

        File showsFolder = new File(ShowControlPlugin.getInstance().getDataFolder(), "data");
        for (File showFile : showsFolder.listFiles()) {
            String showID = removeExtension(showFile.getName());
            YamlFile showConfig = new YamlFile(ShowControlPlugin.getInstance(), "data/" + showID);
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
            YamlFile file = files.containsKey(entry.getKey()) ? files.get(entry.getKey()) : new YamlFile(ShowControlPlugin.getInstance(), "data/" + entry.getKey());
            for (ShowCuePoint cue : entry.getValue()) {
                file.getFile().set(cue.getCueID().toString() + ".Time", cue.getTime());
                file.getFile().set(cue.getCueID().toString() + ".Type", cue.getData().getTriggerId());
                file.getFile().set(cue.getCueID().toString() + ".Data", cue.getData().getDataString());
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
