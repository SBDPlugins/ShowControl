package tech.sbdevelopment.showcontrol.gui;

import fr.minuskube.inv.ClickableItem;
import tech.sbdevelopment.showcontrol.api.ShowCuePoint;
import tech.sbdevelopment.showcontrol.data.Shows;
import tech.sbdevelopment.showcontrol.utils.MainUtil;
import tech.sbdevelopment.showcontrol.utils.inventories.PaginationInventory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Comparator;

public class ShowCueGUI extends PaginationInventory {
    public ShowCueGUI(Player p, String name) {
        super(5, ChatColor.DARK_AQUA + "Show Cue Manager:");

        Shows.getPoints(name).stream().sorted(Comparator.comparing(ShowCuePoint::getTime))
                .forEach(cue -> addItem(ClickableItem.of(MainUtil.pointToItem(cue), e -> {
                    Shows.removePoint(name, cue);
                    new ShowCueGUI(p, name).open(p);
                })));

        open(p);
    }
}
