package nl.sbdeveloper.showapi.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import nl.sbdeveloper.showapi.api.ShowCue;
import nl.sbdeveloper.showapi.data.Shows;
import nl.sbdeveloper.showapi.utils.Inventory;
import nl.sbdeveloper.showapi.utils.ItemBuilder;
import nl.sbdeveloper.showapi.utils.MainUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static nl.sbdeveloper.showapi.utils.MainUtil.__;

public class ShowCueGUI extends Inventory {
    private final String showName;

    public ShowCueGUI(Player p, String name) {
        super(6, ChatColor.DARK_AQUA + "Show Cue Manager:");
        this.showName = name;
        open(p);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        List<ClickableItem> items = new ArrayList<>();
        Shows.getPoints(showName).stream().sorted(Comparator.comparing(ShowCue::getTime))
                .forEach(cue -> items.add(ClickableItem.of(MainUtil.pointToItem(cue), e -> {
                    Shows.removePoint(showName, cue);
                    open(player, pagination.getPage());
                })));

        ClickableItem[] itemsArray = new ClickableItem[items.size()];
        itemsArray = items.toArray(itemsArray);
        pagination.setItems(itemsArray);
        pagination.setItemsPerPage(45);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        contents.set(5, 0, ClickableItem.of(new ItemBuilder(Material.PLAYER_HEAD, 1)
                .setName(__("&7Vorige pagina"))
                .setLore(__("&eGa naar de vorige pagina."))
                .setSkullTexture("http://textures.minecraft.net/texture/6e8c3ce2aee6cf2faade7db37bbae73a36627ac1473fef75b410a0af97659f")
                .toItemStack(), e -> open(player, pagination.previous().getPage())));

        contents.set(5, 4, ClickableItem.of(new ItemBuilder(Material.BARRIER, 1)
                .setName(__("&7Sluiten"))
                .setLore(__("&eSluit dit menu."))
                .toItemStack(), e -> player.closeInventory()));

        contents.set(5, 8, ClickableItem.of(new ItemBuilder(Material.PLAYER_HEAD, 1)
                .setName(__("&7Volgende pagina"))
                .setLore(__("&eGa naar de volgende pagina."))
                .setSkullTexture("http://textures.minecraft.net/texture/6e8cd53664d9307b6869b9abbae2b7737ab762bb18bb34f31c5ca8f3edb63b6")
                .toItemStack(), e -> open(player, pagination.next().getPage())));
    }
}
