package tech.sbdevelopment.showcontrol.api.triggers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
public abstract class Trigger {
    private final String[] dataString;

    /**
     * This method gets fired when the cue gets triggered
     */
    public abstract void trigger();

    /**
     * This method gets fired when a player wants to add a cue
     *
     * @param player The player that wants to add the cue, will be null if it's not a player
     * @param index  The current argument index
     * @param arg    The current argument
     * @return The tab complete value based on the index and argument
     */
    public abstract List<String> getArgumentTabComplete(@Nullable Player player, int index, String arg);

    /**
     * This method gets fired when the cue gets removed
     * It's not required, and does nothing if it's not needed.
     */
    public void remove() {
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

    /**
     * Get the ID of the trigger
     *
     * @return The ID
     */
    public String getTriggerId() {
        return getClass().getAnnotation(TriggerIdentifier.class).value();
    }
}