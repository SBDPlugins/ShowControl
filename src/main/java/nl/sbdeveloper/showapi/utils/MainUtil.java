package nl.sbdeveloper.showapi.utils;

import nl.sbdeveloper.showapi.api.ShowCue;
import nl.sbdeveloper.showapi.api.TriggerTask;
import nl.sbdeveloper.showapi.api.TriggerType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainUtil {
    public static String __(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public static ItemStack pointToItem(ShowCue point) {
        ItemBuilder builder = new ItemBuilder(Material.NOTE_BLOCK);
        builder.setName(ChatColor.ITALIC + "TimeCode: " + TimeUtil.makeReadable(point.getTime()));

        List<String> lores = new ArrayList<>();
        lores.add(ChatColor.GREEN + "Type: " + ChatColor.AQUA + capitalize(point.getTask().getType().name()));
        lores.add(ChatColor.GREEN + "Data:");
        for (String str : ChatPaginator.paginate(point.getTask().getDataString(), 20).getLines()) {
            lores.add(ChatColor.AQUA + ChatColor.stripColor(str));
        }
        lores.add("");
        lores.add(ChatColor.RED + ChatColor.BOLD.toString() + "Click to remove!");

        builder.setLore(lores);
        return builder.toItemStack();
    }

    public static TriggerTask parseData(String data) {
        String[] dataSplitter = data.split(" ");
        String[] dataSplitterNew = Arrays.copyOfRange(dataSplitter, 1, dataSplitter.length);

        TriggerType type;
        try {
            type = TriggerType.valueOf(dataSplitter[0].toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }

        try {
            Constructor<? extends TriggerTask> ctor = type.getTrigger().getConstructor(String[].class);
            if (dataSplitter.length < type.getMinArgs()) return null;
            return ctor.newInstance(dataSplitterNew);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            return null;
        }
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
