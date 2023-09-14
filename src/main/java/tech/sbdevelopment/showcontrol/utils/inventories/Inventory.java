package tech.sbdevelopment.showcontrol.utils.inventories;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import tech.sbdevelopment.showcontrol.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static tech.sbdevelopment.showcontrol.utils.MainUtil.__;

public abstract class Inventory implements InventoryProvider {
    private static InventoryManager manager;
    protected SmartInventory inventory;
    private final boolean filler;

    public Inventory(int rows, String title, boolean filler) {
        this(rows, title, filler, true);
    }

    public Inventory(int rows, String title, boolean filler, boolean closeable) {
        if (rows < 1 || rows > 6) {
            throw new IllegalArgumentException("Amount of rows must be between 1 and 6");
        }

        this.inventory = SmartInventory.builder().id(title).provider(this).manager(manager).size(rows, 9).closeable(closeable).title(__("&8" + title)).build();
        this.filler = filler;
    }

    protected void open(Player player) {
        this.inventory.open(player);
    }

    protected void open(Player player, int page) {
        Bukkit.getLogger().info("Opening page " + page + " for " + player.getName() + "...");
        this.inventory.open(player, page);
    }

    public void close(Player player) {
        this.inventory.close(player);
    }

    public static void init(JavaPlugin plugin) {
        manager = new InventoryManager(plugin);
        manager.init();
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        if (filler) {
            inventoryContents.fill(ClickableItem.empty(new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).displayname(__("&r")).getItemStack()));
        }
        addItems(player, inventoryContents);
    }

    public abstract void addItems(Player player, InventoryContents contents);
}
