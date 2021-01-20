// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.growing;

import org.bukkit.inventory.ItemStack;

public class Growing
{
    private boolean autoReplant;
    private boolean replantSeedRequired;
    private int replantStage;
    private double randomGrowChance;
    private boolean ignoreChunkState;
    private PlantType plantType;
    private ItemStack seedItemStack;
    
    public Growing(final boolean autoReplant, final boolean replantSeedRequired, final int replantStage, final double randomGrowChance, final boolean ignoreChunkState, final PlantType plantType, final ItemStack seedItemStack) {
        this.autoReplant = autoReplant;
        this.replantSeedRequired = replantSeedRequired;
        this.replantStage = replantStage;
        this.randomGrowChance = randomGrowChance;
        this.ignoreChunkState = ignoreChunkState;
        this.plantType = plantType;
        this.seedItemStack = seedItemStack;
    }
    
    public boolean doAutoReplant() {
        return this.autoReplant;
    }
    
    public boolean isReplantSeedRequired() {
        return this.replantSeedRequired;
    }
    
    public int getReplantStage() {
        return this.replantStage;
    }
    
    public double getRandomGrowChance() {
        return this.randomGrowChance;
    }
    
    public boolean doIgnoreChunkState() {
        return this.ignoreChunkState;
    }
    
    public PlantType getPlantType() {
        return this.plantType;
    }
    
    public ItemStack getSeedItemStack() {
        return this.seedItemStack;
    }
}
