package nl.sbdeveloper.showapi.api;

import java.util.UUID;

/**
 * A cue point of a show
 */
public class ShowCue {
    private final UUID cueID;
    private final Long time;
    private final TriggerTask data;

    /**
     * Create a new cue point
     *
     * @param time The starttime (milli)
     * @param data The data
     */
    public ShowCue(Long time, TriggerTask data) {
        this(UUID.randomUUID(), time, data);
    }

    /**
     * Load an exisiting cue point
     *
     * @param uuid The UUID
     * @param time The starttime (milli)
     * @param data The data
     */
    public ShowCue(UUID uuid, Long time, TriggerTask data) {
        this.cueID = uuid;
        this.time = time;
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
     * Get the time (milli)
     *
     * @return The time (milli)
     */
    public Long getTime() {
        return time;
    }

    /**
     * Get the data of this cue
     *
     * @return The data
     */
    public TriggerTask getTask() {
        return data;
    }
}