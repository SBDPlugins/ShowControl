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

    /**
     * Create a new cue point
     *
     * @param timeSeconds The starttime in seconds
     * @param data The data
     */
    public ShowCue(int timeSeconds, TriggerData data) {
        this(UUID.randomUUID(), timeSeconds, data);
    }

    /**
     * Load an exisiting cue point
     *
     * @param uuid The UUID
     * @param timeSeconds The starttime in seconds
     * @param data The data
     */
    public ShowCue(UUID uuid, int timeSeconds, TriggerData data) {
        this.cueID = uuid;
        this.timeSeconds = timeSeconds;
        this.data = data;
    }

    /**
     * Get the ID of the cue point
     *
     * @return The UUID
     */
    public UUID getCueID() {
        return cueID;
    }

    /**
     * Get the time in seconds
     *
     * @return The time in seconds
     */
    public int getTimeSeconds() {
        return timeSeconds;
    }

    /**
     * Get the data of this cue
     *
     * @return The data
     */
    public TriggerData getData() {
        return data;
    }

    /**
     * Start this cue point
     */
    public void runAtTime() {
        this.taskID = Bukkit.getScheduler().runTaskLater(ShowAPIPlugin.getInstance(), data::trigger, 20 * timeSeconds).getTaskId();
    }

    /**
     * Cancel this cue point
     */
    public void cancel() {
        Bukkit.getScheduler().cancelTask(taskID);
    }
}