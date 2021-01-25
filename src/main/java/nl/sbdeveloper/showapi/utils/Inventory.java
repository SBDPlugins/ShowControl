package nl.sbdeveloper.showapi.utils;

import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import nl.sbdeveloper.showapi.ShowAPIPlugin;
import org.bukkit.entity.Player;

import static nl.sbdeveloper.showapi.utils.MainUtil.__;

public abstract class Inventory implements InventoryProvider {
    /*
     This file is part of FrogRacing.
     Copyright (c) 2018-2021 FrogNetwork - All Rights Reserved
     Unauthorized copying of this file, via any medium is strictly prohibited
     Proprietary and confidential
     Written by Stijn Bannink <stijnbannink23@gmail.com>, March 2020
    */

    private static InventoryManager manager;
    protected SmartInventory inventory;

    public Inventory(int rows, String title) {
        this(rows, title, true); //Standaard sluitbaar!
    }

    public Inventory(int rows, String title, boolean closeable) {
        this.inventory = SmartInventory.builder().id(title).provider(this).manager(manager).size(rows, 9).closeable(closeable).title(__("&8" + title)).build();
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        // Niet altijd nodig, daarom staat hij hier alvast.
    }

    protected void open(Player player) {
        this.inventory.open(player);
    }

    protected void open(Player player, int page) {
        this.inventory.open(player, page);
    }

    public void close(Player player) {
        this.inventory.close(player);
    }

    public static void init() {
        manager = new InventoryManager(ShowAPIPlugin.getInstance());
        manager.init();
    }
}
