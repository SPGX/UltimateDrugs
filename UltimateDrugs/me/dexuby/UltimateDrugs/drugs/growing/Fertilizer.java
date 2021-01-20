// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.growing;

import java.util.Collection;
import org.bukkit.inventory.ItemStack;

public class Fertilizer
{
    private String id;
    private ItemStack itemStack;
    private int growAmount;
    private Collection<String> drugIds;
    
    public Fertilizer(final String id, final ItemStack itemStack, final int growAmount, final Collection<String> drugIds) {
        this.id = id;
        this.itemStack = itemStack;
        this.growAmount = growAmount;
        this.drugIds = drugIds;
    }
    
    public String getId() {
        return this.id;
    }
    
    public ItemStack getItemStack() {
        return this.itemStack;
    }
    
    public int getGrowAmount() {
        return this.growAmount;
    }
    
    public Collection<String> getDrugIds() {
        return this.drugIds;
    }
}
