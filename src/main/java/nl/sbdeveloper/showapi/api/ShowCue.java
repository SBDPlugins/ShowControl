package nl.sbdeveloper.showapi.api;

import nl.sbdeveloper.showapi.ShowAPIPlugin;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * A cue point of a show
 */
public class ShowCue {
    private final UUID cueID;
    private final int ticks;
    private final TriggerData data;
    private int taskID;

    /**
     * Create a new cue point
     *
     * @param ticks The starttime in ticks
     * @param data The data
     */
    public ShowCue(int ticks, TriggerData data) {
        this(UUID.randomUUID(), ticks, data);
    }

    /**
     * Load an exisiting cue point
     *
     * @param uuid The UUID
     * @param ticks The starttime in ticks
     * @param data The data
     */
    public ShowCue(UUID uuid, int ticks, TriggerData data) {
        this.cueID = uuid;
        this.ticks = ticks;
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
    public int getTicks() {
        return ticks;
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
        this.taskID = Bukkit.getScheduler().runTaskLater(ShowAPIPlugin.getInstance(), data::trigger, ticks).getTaskId();
    }

    /**
     * Cancel this cue point
     */
    public void cancel() {
        Bukkit.getScheduler().cancelTask(taskID);
    }
}