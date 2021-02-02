package nl.sbdeveloper.showapi.data;

import nl.sbdeveloper.showapi.ShowAPIPlugin;
import nl.sbdeveloper.showapi.api.ShowCue;
import nl.sbdeveloper.showapi.api.TriggerTask;
import nl.sbdeveloper.showapi.utils.YamlFile;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Shows {
    private static final HashMap<String, List<ShowCue>> showsMap = new HashMap<>();
    private static final HashMap<String, ScheduledExecutorService> showTimers = new HashMap<>();

    public static void create(String name) {
        showsMap.put(name, new ArrayList<>());
        Bukkit.getScheduler().runTaskAsynchronously(ShowAPIPlugin.getInstance(), DataSaving::save);
    }

    public static void delete(String name) {
        showsMap.remove(name);

        File data = new File(ShowAPIPlugin.getInstance().getDataFolder(), "data/" + name + ".yml");
        data.delete();
    }

    public static boolean exists(String name) {
        return showsMap.containsKey(name);
    }

    public static List<ShowCue> getPoints(String name) {
        if (!exists(name)) return new ArrayList<>();
        return showsMap.get(name);
    }

    public static void addPoint(String name, Long time, TriggerTask data) {
        if (!exists(name)) return;
        getPoints(name).add(new ShowCue(time, data));
        Bukkit.getScheduler().runTaskAsynchronously(ShowAPIPlugin.getInstance(), DataSaving::save);
    }

    public static void removePoint(String name, ShowCue point) {
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
        Bukkit.getLogger().info("Scheduled show " + name);
        for (ShowCue point : getPoints(name)) {
            Bukkit.getLogger().info("Point " + point.getTask().getDataString() + " on " + point.getTime());
            showTimer.schedule(() -> Bukkit.getScheduler().runTask(ShowAPIPlugin.getInstance(), () -> point.getTask().trigger()), point.getTime(), TimeUnit.MILLISECONDS);
        }
        showTimers.put(name, showTimer);
    }

    public static void cancelShow(String name) {
        if (!exists(name)) return;
        if (!showTimers.containsKey(name)) return;
        ScheduledExecutorService showTimer = showTimers.get(name);
        showTimer.shutdownNow();
    }

    public static HashMap<String, List<ShowCue>> getShowsMap() {
        return showsMap;
    }
}
