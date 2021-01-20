// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.utils;

import org.bukkit.Color;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.ItemFlag;
import java.util.Collection;
import java.util.List;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemBuilder
{
    private ItemStack itemStack;
    
    public ItemBuilder(final Material material) {
        this.itemStack = new ItemStack(material);
    }
    
    public ItemBuilder(final ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }
    
    public ItemBuilder itemMeta(final ItemMeta itemMeta) {
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }
    
    public ItemBuilder type(final Material type) {
        this.itemStack.setType(type);
        return this;
    }
    
    public ItemBuilder amount(final int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }
    
    public ItemBuilder damage(final int damage) {
        if (this.itemStack.getItemMeta() instanceof Damageable) {
            final Damageable damageable = (Damageable)this.itemStack.getItemMeta();
            damageable.setDamage(damage);
            this.itemStack.setItemMeta((ItemMeta)damageable);
        }
        return this;
    }
    
    public ItemBuilder unbreakable(final boolean unbreakable) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setUnbreakable(unbreakable);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }
    
    public ItemBuilder displayName(final String displayName) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(displayName);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }
    
    public ItemBuilder lore(final List<String> lore) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setLore((List)lore);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }
    
    public ItemBuilder appendLore(final List<String> lore) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            if (itemMeta.hasLore()) {
                final List lore2 = itemMeta.getLore();
                lore2.addAll(lore);
                itemMeta.setLore(lore2);
                this.itemStack.setItemMeta(itemMeta);
            }
            else {
                itemMeta.setLore((List)lore);
                this.itemStack.setItemMeta(itemMeta);
            }
        }
        return this;
    }
    
    public ItemBuilder addItemFlags(final ItemFlag... array) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addItemFlags(array);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }
    
    public ItemBuilder skullTexture(final String s) {
        if (this.itemStack.getItemMeta() instanceof SkullMeta) {
            ReflectionUtils.setSkullTexture(this.itemStack, s);
        }
        return this;
    }
    
    public ItemBuilder skullOwner(final OfflinePlayer owningPlayer) {
        if (this.itemStack.getItemMeta() instanceof SkullMeta) {
            final SkullMeta itemMeta = (SkullMeta)this.itemStack.getItemMeta();
            itemMeta.setOwningPlayer(owningPlayer);
            this.itemStack.setItemMeta((ItemMeta)itemMeta);
        }
        return this;
    }
    
    public ItemBuilder addEnchantment(final Enchantment enchantment, final int n) {
        this.itemStack.addUnsafeEnchantment(enchantment, n);
        return this;
    }
    
    public ItemBuilder addPotionEffect(final PotionEffect potionEffect) {
        if (this.itemStack.getItemMeta() instanceof PotionMeta) {
            final PotionMeta itemMeta = (PotionMeta)this.itemStack.getItemMeta();
            itemMeta.addCustomEffect(potionEffect, false);
            this.itemStack.setItemMeta((ItemMeta)itemMeta);
        }
        return this;
    }
    
    public ItemBuilder potionType(final PotionType potionType) {
        if (this.itemStack.getItemMeta() instanceof PotionMeta) {
            final PotionMeta itemMeta = (PotionMeta)this.itemStack.getItemMeta();
            itemMeta.setBasePotionData(new PotionData(potionType));
            this.itemStack.setItemMeta((ItemMeta)itemMeta);
        }
        return this;
    }
    
    public ItemBuilder potionColor(final Color color) {
        if (this.itemStack.getItemMeta() instanceof PotionMeta) {
            final PotionMeta itemMeta = (PotionMeta)this.itemStack.getItemMeta();
            itemMeta.setColor(color);
            this.itemStack.setItemMeta((ItemMeta)itemMeta);
        }
        return this;
    }
    
    public ItemBuilder setStringTag(final String s, final String s2) {
        this.itemStack = ReflectionUtils.setItemStackStringTag(this.itemStack, s, s2);
        return this;
    }
    
    public ItemBuilder customModelData(final int i) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null && ReflectionUtils.getVersionId() >= 240) {
            itemMeta.setCustomModelData(Integer.valueOf(i));
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }
    
    public ItemStack build() {
        return this.itemStack;
    }
}
