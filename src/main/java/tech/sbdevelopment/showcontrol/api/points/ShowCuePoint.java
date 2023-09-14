package tech.sbdevelopment.showcontrol.api.points;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;

import java.util.UUID;

/**
 * A cue point of a show
 */
@Getter
@AllArgsConstructor
public class ShowCuePoint {
    private final UUID cueID;
    private final Long time;
    private final Trigger data;

    /**
     * Create a new cue point
     *
     * @param time The start-time (milliseconds)
     * @param data The data
     */
    public ShowCuePoint(Long time, Trigger data) {
        this(UUID.randomUUID(), time, data);
    }
}