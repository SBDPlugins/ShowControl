package nl.sbdeveloper.showapi.utils;

import com.samjakob.spigui.item.ItemBuilder;
import nl.sbdeveloper.showapi.ShowAPI;
import nl.sbdeveloper.showapi.api.ShowCue;
import nl.sbdeveloper.showapi.api.TriggerData;
import nl.sbdeveloper.showapi.api.TriggerType;
import nl.sbdeveloper.showapi.api.triggers.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.ArrayUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainUtil {
    public static int pointsToRow(int points) {
       return (int) Math.ceil((double) points / 9);
    }

    public static ItemStack pointToItem(ShowCue point) {
        ItemBuilder builder = new ItemBuilder(Material.NOTE_BLOCK);
        builder.name(ChatColor.ITALIC + "TimeCode: " + TimeUtil.showTime(point.getTimeSeconds()));

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

        if (type == TriggerType.COMMAND && dataSplitter.length >= 2) {
            return new CommandTrigger(dataSplitterNew);
        } else if (type == TriggerType.FIREWORK && dataSplitter.length >= 6) {
            return new FireworkTrigger(dataSplitterNew);
        } else if (type == TriggerType.LASER && dataSplitter.length == 6) {
            return new LaserTrigger(dataSplitterNew);
        } else if (type == TriggerType.SPOT && dataSplitter.length == 6) {
            return new SpotTrigger(dataSplitterNew);
        } else if (type == TriggerType.ANIMA && dataSplitter.length == 2) {
            return new AnimaTrigger(dataSplitterNew);
        }

        Bukkit.getLogger().info("Aan het einde. Incorrecte type of te weinig data!");
        return null;
    }
}
