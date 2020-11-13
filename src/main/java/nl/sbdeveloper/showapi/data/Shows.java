package nl.sbdeveloper.showapi.data;

import nl.sbdeveloper.showapi.ShowAPIPlugin;
import nl.sbdeveloper.showapi.api.ShowCue;
import nl.sbdeveloper.showapi.api.TriggerData;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Shows {
    private static final HashMap<String, List<ShowCue>> showsMap = new HashMap<>();

    public static void create(String name) {
        showsMap.put(name, new ArrayList<>());
        Bukkit.getScheduler().runTaskAsynchronously(ShowAPIPlugin.getInstance(), DataSaving::save);
    }

    public static void delete(String name) {
        showsMap.remove(name);

        ShowAPIPlugin.getData().getFile().set("Shows." + name, null);
        ShowAPIPlugin.getData().saveFile();
    }

    public static boolean exists(String name) {
        return showsMap.containsKey(name);
    }

    public static List<ShowCue> getPoints(String name) {
        if (!exists(name)) return new ArrayList<>();
        return showsMap.get(name);
    }

    public static void removePoint(String name, ShowCue point) {
        if (!exists(name)) return;

        showsMap.get(name).remove(point);

        ShowAPIPlugin.getData().getFile().set("Shows." + name + "." + point.getCueID(), null);
        ShowAPIPlugin.getData().saveFile();
    }

    public static void addPoint(String name, int sec, TriggerData data) {
        if (!exists(name)) return;
        getPoints(name).add(new ShowCue(sec, data));
        Bukkit.getScheduler().runTaskAsynchronously(ShowAPIPlugin.getInstance(), DataSaving::save);
    }

    public static void startShow(String name) {
        if (!exists(name)) return;
        getPoints(name).forEach(ShowCue::runAtTime);
    }

    public static void cancelShow(String name) {
        if (!exists(name)) return;
        getPoints(name).forEach(ShowCue::cancel);
    }

    public static HashMap<String, List<ShowCue>> getShowsMap() {
        return showsMap;
    }
}
