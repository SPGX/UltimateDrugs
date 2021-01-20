// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs;

import org.bukkit.inventory.ItemStack;

public class DrugRelatedGood
{
    private String id;
    private ItemStack itemStack;
    private double sellPrice;
    private double buyPrice;
    
    public DrugRelatedGood(final String id, final ItemStack itemStack, final double sellPrice, final double buyPrice) {
        this.id = id;
        this.itemStack = itemStack;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
    }
    
    public String getId() {
        return this.id;
    }
    
    public ItemStack getItemStack() {
        return this.itemStack;
    }
    
    public double getSellPrice() {
        return this.sellPrice;
    }
    
    public double getBuyPrice() {
        return this.buyPrice;
    }
}
