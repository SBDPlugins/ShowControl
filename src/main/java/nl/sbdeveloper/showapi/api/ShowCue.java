package nl.sbdeveloper.showapi.api;

import nl.sbdeveloper.showapi.ShowAPIPlugin;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * A cue point of a show
 */
public class ShowCue {
    private final UUID cueID;
    private final int timeSeconds;
    private final TriggerData data;
    private int taskID;

    public ShowCue(int timeSeconds, TriggerData data) {
        this.cueID = UUID.randomUUID();
        this.timeSeconds = timeSeconds;
        this.data = data;
    }

    public ShowCue(UUID uuid, int timeSeconds, TriggerData data) {
        this.cueID = uuid;
        this.timeSeconds = timeSeconds;
        this.data = data;
    }

    public UUID getCueID() {
        return cueID;
    }

    public int getTimeSeconds() {
        return timeSeconds;
    }

    public TriggerData getData() {
        return data;
    }

    public void runAtTime() {
        this.taskID = Bukkit.getScheduler().runTaskLater(ShowAPIPlugin.getInstance(), data::trigger, 20 * timeSeconds).getTaskId();
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(taskID);
    }
}