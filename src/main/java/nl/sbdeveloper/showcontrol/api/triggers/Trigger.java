package nl.sbdeveloper.showcontrol.api.triggers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class Trigger {
    private final String[] dataString;

    /**
     * This method gets fired when the cue gets triggered
     */
    public abstract void trigger();

    /**
     * This method gets fired when the cue gets removed
     * It's not required, and does nothing if it's not needed.
     */
    public void remove() {}

    /**
     * Get the datastring from this cue
     *
     * @return The datastring
     */
    public String getDataString() {
        StringBuilder builder = new StringBuilder();
        for (String s : dataString) {
            builder.append(s).append(" ");
        }
        return builder.toString().trim();
    }

    public String getTriggerId() {
        return getClass().getAnnotation(TriggerIdentifier.class).value();
    }
}