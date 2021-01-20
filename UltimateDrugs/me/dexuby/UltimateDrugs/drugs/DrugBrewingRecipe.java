// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs;

import java.util.List;
import org.bukkit.inventory.ItemStack;

public class DrugBrewingRecipe extends DrugRecipe
{
    private ItemStack input;
    private ItemStack fuel;
    private int speed;
    private List<ItemStack> results;
    
    public DrugBrewingRecipe(final String s, final ItemStack input, final ItemStack fuel, final int speed, final List<ItemStack> results) {
        super(s);
        this.input = input;
        this.fuel = fuel;
        this.speed = speed;
        this.results = results;
    }
    
    public ItemStack getInput() {
        return this.input;
    }
    
    public ItemStack getFuel() {
        return this.fuel;
    }
    
    public int getSpeed() {
        return this.speed;
    }
    
    public List<ItemStack> getResults() {
        return this.results;
    }
}
