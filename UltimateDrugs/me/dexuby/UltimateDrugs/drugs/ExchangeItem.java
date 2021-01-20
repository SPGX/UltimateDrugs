// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs;

import org.bukkit.inventory.ItemStack;

public class ExchangeItem
{
    private ItemStack item;
    private double chance;
    private boolean forceDrop;
    private boolean breakSuccessChain;
    private int preferredSlot;
    
    public ExchangeItem(final ItemStack item, final double chance, final boolean forceDrop, final boolean breakSuccessChain, final int preferredSlot) {
        this.item = item;
        this.chance = chance;
        this.forceDrop = forceDrop;
        this.breakSuccessChain = breakSuccessChain;
        this.preferredSlot = preferredSlot;
    }
    
    public ItemStack getItem() {
        return this.item;
    }
    
    public double getChance() {
        return this.chance;
    }
    
    public boolean doForceDrop() {
        return this.forceDrop;
    }
    
    public boolean doBreakSuccessChain() {
        return this.breakSuccessChain;
    }
    
    public int getPreferredSlot() {
        return this.preferredSlot;
    }
}
