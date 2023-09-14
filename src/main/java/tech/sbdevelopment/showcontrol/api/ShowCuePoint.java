package tech.sbdevelopment.showcontrol.api;

import tech.sbdevelopment.showcontrol.api.triggers.Trigger;

import java.util.UUID;

/**
 * A cue point of a show
 */
public class ShowCuePoint {
    private final UUID cueID;
    private final Long time;
    private final Trigger data;

    /**
     * Create a new cue point
     *
     * @param time The starttime (milli)
     * @param data The data
     */
    public ShowCuePoint(Long time, Trigger data) {
        this(UUID.randomUUID(), time, data);
    }

    /**
     * Load an exisiting cue point
     *
     * @param uuid The UUID
     * @param time The starttime (milli)
     * @param data The data
     */
    public ShowCuePoint(UUID uuid, Long time, Trigger data) {
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
    public Trigger getTask() {
        return data;
    }
}