package tech.sbdevelopment.showcontrol.api.points;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;
import tech.sbdevelopment.showcontrol.utils.ItemBuilder;
import tech.sbdevelopment.showcontrol.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static tech.sbdevelopment.showcontrol.utils.MainUtil.capitalize;

/**
 * A cue point of a show
 */
@Getter
@AllArgsConstructor
public class ShowCuePoint {
    /**
     * The ID of the cue point
     */
    private final UUID cueID;

    /**
     * The start-time (milliseconds)
     */
    private final Long time;

    /**
     * The data
     */
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

    public ItemStack getGUIItem() {
        TriggerIdentifier identifier = data.getClass().getAnnotation(TriggerIdentifier.class);

        List<String> lores = new ArrayList<>();
        lores.add(ChatColor.GREEN + "Type: " + ChatColor.AQUA + capitalize(data.getTriggerId()));
        lores.add(ChatColor.GREEN + "Data:");
        for (String str : ChatPaginator.paginate(data.getDataString(), 20).getLines()) {
            lores.add(ChatColor.AQUA + ChatColor.stripColor(str));
        }
        lores.add("");
        lores.add(ChatColor.RED + ChatColor.BOLD.toString() + "Click to remove!");

        return new ItemBuilder(identifier.item())
                .displayname(ChatColor.LIGHT_PURPLE + ChatColor.ITALIC.toString() + "TimeCode: " + TimeUtil.makeReadable(getTime()))
                .lore(lores).getItemStack();
    }
}