package tech.sbdevelopment.showcontrol.utils;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;
import tech.sbdevelopment.showcontrol.api.points.ShowCuePoint;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;

import java.util.ArrayList;
import java.util.List;

public class MainUtil {
    public static String __(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
