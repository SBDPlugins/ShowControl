package tech.sbdevelopment.showcontrol.gui;

import fr.minuskube.inv.ClickableItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import tech.sbdevelopment.showcontrol.api.SCAPI;
import tech.sbdevelopment.showcontrol.api.points.ShowCuePoint;
import tech.sbdevelopment.showcontrol.api.shows.Show;
import tech.sbdevelopment.showcontrol.utils.MainUtil;
import tech.sbdevelopment.showcontrol.utils.inventories.PaginationInventory;

import java.util.Comparator;
import java.util.Optional;

public class ShowCueGUI extends PaginationInventory {
    public ShowCueGUI(Player p, String name) {
        super(5, ChatColor.DARK_AQUA + "Show Cue Manager:");

        Optional<Show> showOpt = SCAPI.getShow(name);
        if (showOpt.isEmpty()) {
            p.sendMessage(MainUtil.__("&cShow not found!"));
            return;
        }
        Show show = showOpt.get();

        show.getCuePoints().stream().sorted(Comparator.comparing(ShowCuePoint::getTime))
                .forEach(cue -> addItem(ClickableItem.of(cue.getGUIItem(), e -> {
                    show.removePoint(cue);
                    new ShowCueGUI(p, name).open(p);
                })));

        open(p);
    }
}
