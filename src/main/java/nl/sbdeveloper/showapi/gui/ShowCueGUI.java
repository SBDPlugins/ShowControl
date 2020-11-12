package nl.sbdeveloper.showapi.gui;

import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import nl.sbdeveloper.showapi.ShowAPIPlugin;
import nl.sbdeveloper.showapi.api.ShowCue;
import nl.sbdeveloper.showapi.data.Shows;
import nl.sbdeveloper.showapi.utils.MainUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class ShowCueGUI {
    public static void openGUI(String name, Player p) {
        List<ShowCue> points = Shows.getPoints(name);

        SGMenu menu = ShowAPIPlugin.getSpiGUI().create(ChatColor.DARK_AQUA + "Show Cue Manager:", MainUtil.pointsToRow(points.size()));
        menu.setAutomaticPaginationEnabled(true);

        for (ShowCue cue : points) {
            SGButton button = new SGButton(MainUtil.pointToItem(cue))
                    .withListener((InventoryClickEvent e) -> {
                 points.remove(cue);

                 openGUI(name, p); //Refresh
            });

            menu.addButton(button);
        }

        p.openInventory(menu.getInventory());
    }
}
