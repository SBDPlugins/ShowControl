package nl.sbdeveloper.showapi.utils;

import com.samjakob.spigui.item.ItemBuilder;
import nl.sbdeveloper.showapi.api.ShowCue;
import nl.sbdeveloper.showapi.api.TriggerData;
import nl.sbdeveloper.showapi.api.TriggerType;
import org.apache.commons.lang.StringUtils;
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
    public static ItemStack pointToItem(ShowCue point) {
        ItemBuilder builder = new ItemBuilder(Material.NOTE_BLOCK);
        builder.name(ChatColor.ITALIC + "TimeCode: " + TimeUtil.showTime(point.getTicks()));

        List<String> lores = new ArrayList<>();
        lores.add(ChatColor.GREEN + "Type: " + ChatColor.AQUA + StringUtils.capitalize(point.getData().getType().name()));
        lores.add(ChatColor.GREEN + "Data:");
        for (String str : ChatPaginator.paginate(point.getData().getDataString(), 20).getLines()) {
            lores.add(ChatColor.AQUA + ChatColor.stripColor(str));
        }
        lores.add("");
        lores.add(ChatColor.RED + ChatColor.BOLD.toString() + "Click to remove!");

        builder.lore(lores);
        return builder.build();
    }

    public static TriggerData parseData(String data) {
        String[] dataSplitter = data.split(" ");
        String[] dataSplitterNew = Arrays.copyOfRange(dataSplitter, 1, dataSplitter.length);

        TriggerType type;
        try {
            type = TriggerType.valueOf(dataSplitter[0].toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }

        try {
            Constructor<? extends TriggerData> ctor = type.getTrigger().getConstructor(String[].class);
            if (dataSplitter.length < type.getMinArgs()) return null;
            return ctor.newInstance(new Object[] { dataSplitterNew });
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            return null;
        }
    }
}
