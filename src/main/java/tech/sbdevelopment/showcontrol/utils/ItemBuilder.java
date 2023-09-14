package tech.sbdevelopment.showcontrol.utils;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

@Getter
public class ItemBuilder {
    private final ItemStack itemStack;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(ItemStack stack) {
        this.itemStack = stack.clone();
    }

    public ItemBuilder(XMaterial material) {
        ItemStack item = material.parseItem();
        if (item == null) {
            throw new IllegalArgumentException("Received invalid / unsupported XMaterial: " + material.name());
        }
        this.itemStack = item;
    }

    private void applyToMeta(UnaryOperator<ItemMeta> callback) {
        this.itemStack.setItemMeta(callback.apply(this.itemStack.getItemMeta()));
    }

    public ItemBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder displayname(String name) {
        applyToMeta(meta -> {
            meta.setDisplayName(name);
            return meta;
        });
        return this;
    }

    public ItemBuilder lore(String... lore) {
        applyToMeta(meta -> {
            List<String> lores = meta.getLore();
            if (lores == null) lores = new ArrayList<>();
            for (String loreString : lore) {
                String[] loreParts = loreString.split("[\\r\\n]+");
                Collections.addAll(lores, loreParts);
            }
            meta.setLore(lores);
            return meta;
        });
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        lore(lore.toArray(String[]::new));
        return this;
    }

    public ItemBuilder flag(ItemFlag... flags) {
        applyToMeta(meta -> {
            meta.addItemFlags(flags);
            return meta;
        });
        return this;
    }

    public ItemBuilder hideAllFlags() {
        return flag(ItemFlag.values());
    }

    public ItemBuilder customModelData(int customModelData) {
        if (XMaterial.supports(13)) {
            applyToMeta(meta -> {
                meta.setCustomModelData(customModelData);
                return meta;
            });
        }
        return this;
    }

    public ItemBuilder enchant(Map<Enchantment, Integer> enchantments) {
        itemStack.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        itemStack.addEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder durability(int damage) {
        if (XMaterial.supports(13)) {
            applyToMeta(meta -> {
                if (!(meta instanceof org.bukkit.inventory.meta.Damageable)) return meta;
                ((org.bukkit.inventory.meta.Damageable) meta).setDamage(damage);
                return meta;
            });
        } else {
            itemStack.setDurability((short) damage);
        }
        return this;
    }

    public ItemBuilder unbreakable() {
        return unbreakable(true);
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        applyToMeta(meta -> {
            meta.setUnbreakable(unbreakable);
            return meta;
        });
        return this;
    }

    public ItemBuilder glow() {
        this.itemStack.addUnsafeEnchantment(Enchantment.LURE, 0);
        flag(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder skullTexture(String identifier) {
        applyToMeta(meta -> {
            SkullUtils.applySkin(meta, identifier);
            return meta;
        });
        return this;
    }

    public ItemBuilder armorColor(Color color) {
        applyToMeta(meta -> {
            if (!(meta instanceof LeatherArmorMeta)) return meta;
            ((LeatherArmorMeta) meta).setColor(color);
            return meta;
        });
        return this;
    }

    public ItemBuilder potionEffect(PotionType type) {
        if (!itemStack.getType().name().contains("POTION"))
            throw new UnsupportedOperationException("ItemStack is not a potion! (Type: " + itemStack.getType().name() + ")");
        applyToMeta(meta -> {
            if (!(meta instanceof org.bukkit.inventory.meta.PotionMeta)) return meta;
            ((org.bukkit.inventory.meta.PotionMeta) meta).setBasePotionData(new PotionData(type));
            return meta;
        });
        return this;
    }
}