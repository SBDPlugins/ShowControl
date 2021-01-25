package nl.sbdeveloper.showapi.api;

public abstract class TriggerTask {
    private final TriggerType type;
    private final String[] dataString;

    /**
     * Create a new trigger
     */
    public TriggerTask(TriggerType type, String[] dataString) {
        this.type = type;
        this.dataString = dataString;
    }

    /**
     * This method gets fired when the cue gets triggered
     */
    public abstract void trigger();

    /**
     * This method gets fired when the cue gets removed
     */
    public abstract void remove();

    /**
     * Get the trigger type
     *
     * @return The trigger type
     */
    public TriggerType getType() {
        return type;
    }

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
}