package nl.sbdeveloper.showcontrol.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import nl.sbdeveloper.showcontrol.api.ShowCuePoint;
import nl.sbdeveloper.showcontrol.data.Shows;
import nl.sbdeveloper.showcontrol.utils.MainUtil;
import nl.sbdeveloper.showcontrol.utils.inventories.PaginationInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static nl.sbdeveloper.showcontrol.utils.MainUtil.__;

public class ShowCueGUI extends PaginationInventory {
    public ShowCueGUI(Player p, String name) {
        super(6, ChatColor.DARK_AQUA + "Show Cue Manager:");

        Shows.getPoints(name).stream().sorted(Comparator.comparing(ShowCuePoint::getTime))
                .forEach(cue -> addItem(ClickableItem.of(MainUtil.pointToItem(cue), e -> {
                    Shows.removePoint(name, cue);
                    refresh(p);
                })));

        open(p);
    }
}
