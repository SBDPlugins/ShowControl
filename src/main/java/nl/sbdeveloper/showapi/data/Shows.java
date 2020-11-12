package nl.sbdeveloper.showapi.data;

import nl.sbdeveloper.showapi.api.ShowCue;
import nl.sbdeveloper.showapi.api.TriggerData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Shows {
    private static final HashMap<String, List<ShowCue>> showsMap = new HashMap<>();

    public static void create(String name) {
        showsMap.put(name, new ArrayList<>());
    }

    public static void delete(String name) {
        showsMap.remove(name);
    }

    public static boolean exists(String name) {
        return showsMap.containsKey(name);
    }

    public static List<ShowCue> getPoints(String name) {
        if (!exists(name)) return new ArrayList<>();
        return showsMap.get(name);
    }

    public static void addPoint(String name, int sec, TriggerData data) {
        if (!exists(name)) return;
        getPoints(name).add(new ShowCue(sec, data));
    }

    public static void startShow(String name) {
        if (!exists(name)) return;
        getPoints(name).forEach(ShowCue::runAtTime);
    }

    public static void cancelShow(String name) {
        if (!exists(name)) return;
        getPoints(name).forEach(ShowCue::cancel);
    }
}
