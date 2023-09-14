package tech.sbdevelopment.showcontrol.utils;

import tech.sbdevelopment.showcontrol.api.points.ShowCuePoint;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

import java.util.ArrayList;
import java.util.List;

public class MainUtil {
    public static String __(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public static ItemStack pointToItem(ShowCuePoint point) {
        TriggerIdentifier identifier = point.getData().getClass().getAnnotation(TriggerIdentifier.class);

        List<String> lores = new ArrayList<>();
        lores.add(ChatColor.GREEN + "Type: " + ChatColor.AQUA + capitalize(point.getData().getTriggerId()));
        lores.add(ChatColor.GREEN + "Data:");
        for (String str : ChatPaginator.paginate(point.getData().getDataString(), 20).getLines()) {
            lores.add(ChatColor.AQUA + ChatColor.stripColor(str));
        }
        lores.add("");
        lores.add(ChatColor.RED + ChatColor.BOLD.toString() + "Click to remove!");

        return new ItemBuilder(identifier.item())
                .displayname(ChatColor.LIGHT_PURPLE + ChatColor.ITALIC.toString() + "TimeCode: " + TimeUtil.makeReadable(point.getTime()))
                .lore(lores).getItemStack();
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
