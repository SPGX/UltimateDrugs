// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs;

import org.bukkit.inventory.ItemStack;

public class ConsumeRequirementItem
{
    private ItemStack itemStack;
    private boolean damageOnConsume;
    private boolean removeOnConsume;
    
    public ConsumeRequirementItem(final ItemStack itemStack, final boolean damageOnConsume, final boolean removeOnConsume) {
        this.itemStack = itemStack;
        this.damageOnConsume = damageOnConsume;
        this.removeOnConsume = removeOnConsume;
    }
    
    public ItemStack getItemStack() {
        return this.itemStack;
    }
    
    public boolean doDamageOnConsume() {
        return this.damageOnConsume;
    }
    
    public boolean doRemoveOnConsume() {
        return this.removeOnConsume;
    }
}
