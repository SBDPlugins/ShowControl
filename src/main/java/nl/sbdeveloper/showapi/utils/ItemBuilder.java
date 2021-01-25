package nl.sbdeveloper.showapi.utils;

import com.cryptomorin.xseries.SkullUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionType;

import java.util.*;
import java.util.function.Function;

/**
 * @author Blutkrone
 */
public class ItemBuilder {
    /*
     This file is part of FrogRacing.
     Copyright (c) 2018-2021 FrogNetwork - All Rights Reserved
     Unauthorized copying of this file, via any medium is strictly prohibited
     Proprietary and confidential
     Written by Stijn Bannink <stijnbannink23@gmail.com>, March 2020
    */

    private final ItemStack is;
    private static boolean loaded = false;
    private static boolean usePotionSetColor = false;

    /** */
    private static void init() {
        try {
            Class.forName("org.bukkit.Color");
            usePotionSetColor = true;
        } catch (Exception e) {
            usePotionSetColor = false;
        }

        loaded = true;
    }

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param m The material to create the ItemBuilder with.
     */
    public ItemBuilder(Material m) {
        this(m, 1);
    }

    /**
     * Create a new ItemBuilder over an existing itemstack.
     *
     * @param is The itemstack to create the ItemBuilder over.
     */
    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param m      The material of the item.
     * @param amount The evaluate of the item.
     */
    public ItemBuilder(Material m, int amount) {
        this(m, amount, (short) 0);
    }

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param m          The material of the item.
     * @param amount     The evaluate of the item.
     * @param durability The durability of the item.
     */
    public ItemBuilder(Material m, int amount, short durability) {
        this(m, amount, durability, null);
    }

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param m          The material of the item.
     * @param amount     The evaluate of the item.
     * @param durability The durability of the item.
     */
    public ItemBuilder(Material m, int amount, short durability, Byte data) {
        if (!loaded) init();

        if (data != null && data > 0 && data <= 15) {
            is = new ItemStack(m, amount);
            setDurability(durability);

            // Ensure that we only allow applying data on valid objects.
            if (is.getType().name().contains("STAINED_GLASS")) {
                setData(GlassColor.values()[data]);
            } else if (is.getType().name().contains("WOOL")) {
                setData(WoolColor.values()[data]);
            } else if (is.getType().name().contains("DYE") || DyeColor.is(is.getType())) {
                setData(DyeColor.values()[data]);
            } else
                Bukkit.getLogger().warning("Unable to assign " + m + " a data value!");
        } else {
            is = new ItemStack(m, amount);
            setDurability(durability);
        }
    }

    public static ItemBuilder create(ItemStack from) {
        return new ItemBuilder(from);
    }

    public ItemBuilder setData(GlassColor color) {
        if (is.getType().name().contains("STAINED_GLASS")) {
            color.apply(this);
        } else
            throw new IllegalArgumentException("Cannot apply " + color.getClass().getSimpleName() + " on " + is.getType());
        return this;
    }

    public ItemBuilder setData(WoolColor color) {
        if (is.getType().name().contains("WOOL")) {
            color.apply(this);
        } else
            throw new IllegalArgumentException("Cannot apply " + color.getClass().getSimpleName() + " on " + is.getType());
        return this;
    }

    public ItemBuilder setData(DyeColor color) {
        if (is.getType().name().contains("DYE") || DyeColor.is(is.getType())) {
            color.apply(this);
        } else
            throw new IllegalArgumentException("Cannot apply " + color.getClass().getSimpleName() + " on " + is.getType());
        return this;
    }

    public ItemBuilder setData(PotionColor color) {
        if (is.getType().name().contains("POTION")) {
            color.apply(this);
        } else
            throw new IllegalArgumentException("Cannot apply " + color.getClass().getSimpleName() + " on " + is.getType());
        return this;
    }

    /**
     * Clone the ItemBuilder into a new one.
     *
     * @return The cloned instance.
     */
    public ItemBuilder clone() {
        return new ItemBuilder(is.clone());
    }

    /**
     * Change the durability of the item.
     *
     * @param dur The durability to set it to.
     */
    public ItemBuilder setDurability(short dur) {
        if (is.getType() == Material.AIR) return this;
        ItemMeta meta = is.getItemMeta();
        if (meta instanceof org.bukkit.inventory.meta.Damageable) {
            ((org.bukkit.inventory.meta.Damageable) meta).setDamage(dur);
            is.setItemMeta(meta);
        }
        return this;
    }

    /**
     * Change the durability of the item.
     *
     * @param data The durability to set it to.
     */
    @Deprecated
    public ItemBuilder setData(byte data) {
        if (is.getType() == Material.AIR) return this;
        is.setData(new MaterialData(is.getType(), data));
        return this;
    }

    /**
     * Change the unbreakable of the item.
     *
     * @param unbreakable The unbreakable to set it to.
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        if (is.getType() == Material.AIR) return this;
        ItemMeta meta = is.getItemMeta();
        meta.setUnbreakable(unbreakable);
        is.setItemMeta(meta);
        return this;
    }

    /**
     * Change the evaluate of the item.
     *
     * @param amount The evaluate to set it to.
     */
    public ItemBuilder setAmount(int amount) {
        if (is.getType() == Material.AIR) return this;
        is.setAmount(amount);
        return this;
    }

    /**
     * Add an item flag
     *
     * @param flags item flagS
     */
    public ItemBuilder addFlags(ItemFlag... flags) {
        if (is.getType() == Material.AIR) return this;
        ItemMeta im = is.getItemMeta();
        im.addItemFlags(flags);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Remove an item flag
     *
     * @param flags item flagS
     */
    public ItemBuilder removeFlags(ItemFlag... flags) {
        if (is.getType() == Material.AIR) return this;
        ItemMeta im = is.getItemMeta();
        im.removeItemFlags(flags);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Set the displayname of the item.
     *
     * @param name The name to change it to.
     */
    public ItemBuilder setName(String name) {
        if (is.getType() == Material.AIR || name == null || name.isEmpty()) return this;
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Add an unsafe enchantment.
     *
     * @param ench  The enchantment to add.
     * @param level The level to put the enchant on.
     */
    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        if (is.getType() == Material.AIR) return this;
        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    /**
     * Remove a certain enchant from the item.
     *
     * @param ench The enchantment to remove
     */
    public ItemBuilder removeEnchantment(Enchantment ench) {
        if (is.getType() == Material.AIR) return this;
        is.removeEnchantment(ench);
        return this;
    }

    public ItemBuilder glow() {
        if (is.getType() == Material.AIR) return this;
        is.addUnsafeEnchantment(Enchantment.LURE, 0);
        ItemMeta im = is.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Set the skull getOwner for the item. Works on skulls only.
     *
     * @param owner The name of the skull's getOwner.
     */
    public ItemBuilder setSkullOwner(String owner) {
        if (is.getType() == Material.AIR) return this;
        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            im.setOwner(owner);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    public ItemBuilder setSkullTexture(String url) {
        if (is.getType() == Material.AIR) return this;
        is.setItemMeta(SkullUtils.applySkin(is.getItemMeta(), url));
        return this;
    }

    /**
     * Add an enchant to the item.
     *
     * @param ench  The enchant to add
     * @param level The level
     */
    public ItemBuilder addEnchant(Enchantment ench, int level) {
        if (is.getType() == Material.AIR) return this;
        ItemMeta im = is.getItemMeta();
        im.addEnchant(ench, level, true);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Add multiple enchants at once.
     *
     * @param enchantments The enchants to add.
     */
    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        if (is.getType() == Material.AIR) return this;
        is.addEnchantments(enchantments);
        return this;
    }

    /**
     * Sets infinity durability on the item by setting the durability to Short.MAX_VALUE.
     */
    public ItemBuilder setInfinityDurability() {
        if (is.getType() == Material.AIR) return this;
        is.setDurability(Short.MAX_VALUE);
        return this;
    }

    /**
     * Re-sets the lore.
     *
     * @param lore The lore to set it to.
     */
    public ItemBuilder setLore(String... lore) {
        if (is.getType() == Material.AIR || lore == null) return this;
        ItemMeta im = is.getItemMeta();
        im.setLore(Arrays.asList(lore));
        is.setItemMeta(im);
        return this;
    }

    /**
     * Re-sets the lore.
     *
     * @param lore The lore to set it to.
     */
    public ItemBuilder setLore(List<String> lore) {
        if (is.getType() == Material.AIR) return this;
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Remove a lore line.
     *
     * @param line The lore to remove.
     */
    public ItemBuilder removeLoreLine(String line) {
        if (is.getType() == Material.AIR) return this;
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (!lore.contains(line)) return this;
        lore.remove(line);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Remove a lore line.
     *
     * @param index The index of the lore line to remove.
     */
    public ItemBuilder removeLoreLine(int index) {
        if (is.getType() == Material.AIR) return this;
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (index < 0 || index > lore.size()) return this;
        lore.remove(index);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Add a lore line.
     *
     * @param line The lore line to add.
     */
    public ItemBuilder addLoreLine(String line) {
        if (is.getType() == Material.AIR) return this;
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore()) lore = new ArrayList<>(im.getLore());
        lore.add(line);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLine(String... lines) {
        if (is.getType() == Material.AIR) return this;
        for (String line : lines) {
            addLoreLine(line);
        }
        return this;
    }

    public ItemBuilder addLoreLine(Collection<String> lines) {
        if (is.getType() == Material.AIR) return this;
        for (String line : lines) {
            addLoreLine(line);
        }
        return this;
    }

    /**
     * Add a lore line.
     *
     * @param line The lore line to add.
     * @param pos  The index of where to put it.
     */
    public ItemBuilder addLoreLine(String line, int pos) {
        if (is.getType() == Material.AIR) return this;
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.set(pos, line);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Direct access to the item meta
     *
     * @param modifier function to modify the meta.
     */
    public ItemBuilder writeMeta(
            Function<ItemMeta, ItemMeta> modifier
    ) {
        if (is.getType() == Material.AIR) return this;
        is.setItemMeta(modifier.apply(is.getItemMeta()));
        return this;
    }

    /**
     * Retrieves the itemstack from the ItemBuilder.
     *
     * @return The itemstack created/modified by the ItemBuilder instance.
     */
    public ItemStack toItemStack() {
        return is;
    }

    public enum GlassColor {
        WHITE,
        ORANGE,
        MAGENTA,
        LIGHT_BLUE,
        YELLOW,
        LIME,
        PINK,
        GRAY,
        LIGHT_GRAY,
        CYAN,
        PURPLE,
        BLUE,
        BROWN,
        GREEN,
        RED,
        BLACK;

        private void apply(ItemBuilder builder) {
            boolean panel = builder.is.getType().name().contains("STAINED_GLASS_PANE");
            builder.is.setType(Material.valueOf(name() + (panel ? "_STAINED_GLASS_PANE" : "STAINED_GLASS")));
        }
    }

    public enum WoolColor {
        WHITE,
        ORANGE,
        MAGENTA,
        LIGHT_BLUE,
        YELLOW,
        LIME,
        PINK,
        GRAY,
        LIGHT_GRAY,
        CYAN,
        PURPLE,
        BLUE,
        BROWN,
        GREEN,
        RED,
        BLACK;

        private void apply(ItemBuilder builder) {
            builder.is.setType(Material.valueOf(name() + "_WOOL"));
        }
    }

    public enum DyeColor {
        BLACK(0, "INK_SACK"),
        RED(1, "ROSE_RED"),
        GREEN(2, "CACTUS_GREEN"),
        BROWN(3, "COCOA_BEANS"),
        BLUE(4, "LAPIS_LAZULI"),
        PURPLE(5, "PURPLE_DYE"),
        CYAN(6, "CYAN_DYE"),
        LIGHT_GRAY(7, "LIGHT_GRAY_DYE"),
        GRAY(8, "GRAY_DYE"),
        PINK(9, "PINK_DYE"),
        LIME(10, "LIME_DYE"),
        YELLOW(11, "DANDELION_YELLOW"),
        LIGHT_BLUE(12, "LIGHT_BLUE_DYE"),
        MAGENTA(13, "MAGENTA_DYE"),
        ORANGE(14, "ORANGE_DYE"),
        WHITE(15, "BONE_MEAL");

        private final String coded;

        DyeColor(int data, String coded) {
            this.coded = coded;
        }

        static boolean is(Material mat) {
            if (mat.name().contains("DYE")) return true;
            if (mat.name().contains("INK_SAC")) return true;

            for (DyeColor dyeColor : values()) {
                if (dyeColor.name().equalsIgnoreCase(mat.name()))
                    return true;
            }

            return false;
        }

        private void apply(ItemBuilder builder) {
            builder.is.setType(Material.valueOf(coded));
        }
    }

    public enum PotionColor {
        PINK("REGEN", 0xF442E2),
        CYAN("SPEED", 0x42f1f4),
        GOLD("FIRE_RESISTANCE", 0xf4b942),
        DARK_GREEN("POISON", 0x365b0e),
        RED("INSTANT_HEAL", 0xe5251b),
        DARK_BLUE("NIGHT_VISION", 0x092366),
        DARK_GRAY("WEAKNESS", 0x2e2f33),
        DARK_RED("STRENGTH", 0x4f0a01),
        GRAY("SLOWNESS", 0x939393),
        LIGHT_GREEN("JUMP", 0x42f4b0),
        BROWN("INSTANT_DAMAGE", 0xa54126),
        BLUE("WATER_BREATHING", 0x0e59ef), // TEAL
        LIGHT_GRAY("INVISIBILITY", 0xc4c4c4),
        GREEN("LUCK", 0x1f890f),
        BLACK(null, 0x161616),
        LIGHT_BROWN("TURTLE_MASTER", 0xad581b),
        SILVER("SLOW_FALLING", 0xd8ccc3);

        private final String potionType;
        private final int rgb;

        PotionColor(String potionType, int rgb) {
            this.potionType = potionType;
            this.rgb = rgb;
        }

        private void apply(ItemBuilder builder) {
            PotionMeta meta = (PotionMeta) builder.is.getItemMeta();
            if (usePotionSetColor) {
                meta.setColor(Color.fromRGB(rgb));
            } else if (potionType != null) {
                try {
                    meta.setBasePotionData(new org.bukkit.potion.PotionData(PotionType.valueOf(potionType)));
                } catch (Exception e) {
                    Bukkit.getLogger().severe("Unknown Potion Color: " + this + ", " +
                            "the underlying " + potionType + " handle isn't available in this version!");
                }
            }
            builder.is.setItemMeta(meta);
        }
    }

    public enum FireworkColor {

    }
}

