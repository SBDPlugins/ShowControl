package nl.sbdeveloper.showcontrol.utils.inventories;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import nl.sbdeveloper.showcontrol.utils.ItemBuilder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static nl.sbdeveloper.showcontrol.utils.MainUtil.__;

public abstract class PaginationInventory extends Inventory {
    private final int paginationRows; //Amount of rows for pagination (excluding button row)
    private final int paginationRow; //The start row of the pagination (starts from 0)
    private final List<ClickableItem> items = new ArrayList<>();
    private Pagination pagination; //Available after init() is called, can only be used in the event of a ClickableItem!!!

    private final int staticRows; //Amount of rows that are static
    private final boolean filler;

    protected PaginationInventory(int paginationRows, String title) {
        //Default full-inventory pagination
        this(paginationRows, 0, 0, title, false);
    }

    protected PaginationInventory(int paginationRows, int staticRows, String title, boolean filler) {
        this(paginationRows, 0, staticRows, title, filler);
    }

    protected PaginationInventory(int paginationRows, int paginationRow, int staticRows, String title, boolean filler) {
        //Custom pagination with static rows
        super(paginationRows + 1 + staticRows, title, false);
        this.paginationRows = paginationRows;
        this.paginationRow = paginationRow;
        this.staticRows = staticRows;
        this.filler = filler;
    }

    public void addItem(ClickableItem item) {
        this.items.add(item);
    }

    public void refresh(Player player) {
        open(player, pagination.getPage());
    }

    @Override
    public void addItems(Player player, InventoryContents contents) {
        //Set up static items
        if (filler) {
            //Fill all static rows
            for (int i = paginationRows + 1; i <= paginationRows + staticRows; i++) {
                contents.fillRow(i, ClickableItem.empty(new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).displayname(__("&r")).getItemStack()));
            }
        }
        addStaticItems(player, contents);

        //Set up pagination
        pagination = contents.pagination();
        pagination.setItems(items.toArray(ClickableItem[]::new));
        pagination.setItemsPerPage(9 * paginationRows);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, paginationRow, 0));

        contents.set(paginationRows, 0, ClickableItem.of(new ItemBuilder(XMaterial.ARROW.parseItem()).displayname(__("&6First page"))
                        .lore(__("&fGo to the first page (" + pagination.first().getPage() + ")")).getItemStack(),
                e -> open(player, pagination.first().getPage())));
        contents.set(paginationRows, 3, ClickableItem.of(new ItemBuilder(XMaterial.OAK_SIGN.parseItem()).displayname(__("&6Previous"))
                        .lore(__("&fGo to the previous page (" + pagination.previous().getPage() + ")")).getItemStack(),
                e -> open(player, pagination.previous().getPage())));
        contents.set(paginationRows, 5, ClickableItem.of(new ItemBuilder(XMaterial.OAK_SIGN.parseItem()).displayname(__("&6Next"))
                        .lore(__("&fGo to the next page (" + pagination.next().getPage() + ")")).getItemStack(),
                e -> open(player, pagination.next().getPage())));
        contents.set(paginationRows, 8, ClickableItem.of(new ItemBuilder(XMaterial.ARROW.parseItem()).displayname(__("&6Last page"))
                        .lore(__("&fGo to the last page (" + pagination.last().getPage() + ")")).getItemStack(),
                e -> open(player, pagination.last().getPage())));
    }

    public void addStaticItems(Player player, InventoryContents contents) {
        //Override this method to add static items
    }
}