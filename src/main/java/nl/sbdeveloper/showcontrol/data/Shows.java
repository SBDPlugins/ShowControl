package nl.sbdeveloper.showcontrol.data;

import lombok.Getter;
import nl.sbdeveloper.showcontrol.ShowControlPlugin;
import nl.sbdeveloper.showcontrol.api.ShowCuePoint;
import nl.sbdeveloper.showcontrol.api.triggers.Trigger;
import nl.sbdeveloper.showcontrol.utils.YamlFile;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Shows {
    @Getter
    private static final HashMap<String, List<ShowCuePoint>> showsMap = new HashMap<>();
    private static final HashMap<String, ScheduledExecutorService> showTimers = new HashMap<>();

    public static void create(String name) {
        showsMap.put(name, new ArrayList<>());
        DataSaving.save();
    }

    public static void delete(String name) {
        showsMap.remove(name);

        File data = new File(ShowControlPlugin.getInstance().getDataFolder(), "data/" + name + ".yml");
        data.delete();
    }

    public static boolean exists(String name) {
        return showsMap.containsKey(name);
    }

    public static List<ShowCuePoint> getPoints(String name) {
        if (!exists(name)) return new ArrayList<>();
        return showsMap.get(name);
    }

    public static void addPoint(String name, Long time, Trigger data) {
        if (!exists(name)) return;
        getPoints(name).add(new ShowCuePoint(time, data));
        DataSaving.save();
    }

    public static void removePoint(String name, ShowCuePoint point) {
        if (!exists(name)) return;

        point.getTask().remove();
        showsMap.get(name).remove(point);

        YamlFile data = DataSaving.getFiles().get(name);

        data.getFile().set(point.getCueID().toString(), null);
        data.saveFile();
    }

    public static void startShow(String name) {
        if (!exists(name)) return;
        ScheduledExecutorService showTimer = Executors.newSingleThreadScheduledExecutor();
        for (ShowCuePoint point : getPoints(name)) {
            showTimer.schedule(() -> Bukkit.getScheduler().runTask(ShowControlPlugin.getInstance(), () -> point.getTask().trigger()), point.getTime(), TimeUnit.MILLISECONDS);
        }
        showTimers.put(name, showTimer);
    }

    public static void cancelShow(String name) {
        if (!exists(name)) return;
        if (!showTimers.containsKey(name)) return;
        ScheduledExecutorService showTimer = showTimers.get(name);
        showTimer.shutdownNow();
    }
}
