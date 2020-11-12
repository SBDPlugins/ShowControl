package nl.sbdeveloper.showapi.api;

public abstract class TriggerData {
    private final TriggerType type;
    private final String[] dataString;

    /**
     * Create a new trigger
     */
    public TriggerData(TriggerType type, String[] dataString) {
        this.type = type;
        this.dataString = dataString;
    }

    /**
     * This method gets fired when the cue gets triggered
     */
    public void trigger() {}

    /**
     * Get the trigger type
     *
     * @return The trigger type
     */
    public TriggerType getType() {
        return type;
    }

    public String getDataString() {
        StringBuilder builder = new StringBuilder();
        for (String s : dataString) {
            builder.append(s).append(" ");
        }
        return builder.toString().trim();
    }
}