package nl.sbdeveloper.showcontrol.utils;

import nl.sbdeveloper.showcontrol.api.ShowCuePoint;
import nl.sbdeveloper.showcontrol.api.triggers.TriggerIdentifier;
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
        TriggerIdentifier identifier = point.getTask().getClass().getAnnotation(TriggerIdentifier.class);

        ItemBuilder builder = new ItemBuilder(identifier.item());
        builder.setName(ChatColor.ITALIC + "TimeCode: " + TimeUtil.makeReadable(point.getTime()));

        List<String> lores = new ArrayList<>();
        lores.add(ChatColor.GREEN + "Type: " + ChatColor.AQUA + capitalize(point.getTask().getTriggerId()));
        lores.add(ChatColor.GREEN + "Data:");
        for (String str : ChatPaginator.paginate(point.getTask().getDataString(), 20).getLines()) {
            lores.add(ChatColor.AQUA + ChatColor.stripColor(str));
        }
        lores.add("");
        lores.add(ChatColor.RED + ChatColor.BOLD.toString() + "Click to remove!");

        builder.setLore(lores);
        return builder.toItemStack();
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
