// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs;

import org.bukkit.inventory.FurnaceRecipe;

public class DrugFurnaceRecipe extends DrugRecipe
{
    private FurnaceRecipe furnaceRecipe;
    
    public DrugFurnaceRecipe(final String s, final FurnaceRecipe furnaceRecipe) {
        super(s);
        this.furnaceRecipe = furnaceRecipe;
    }
    
    public FurnaceRecipe getFurnaceRecipe() {
        return this.furnaceRecipe;
    }
}
