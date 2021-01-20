// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.growing;

import java.util.List;
import org.bukkit.block.Block;
import java.util.UUID;

public class Plant
{
    private UUID uuid;
    private UUID owner;
    private String parentDrugId;
    private Block baseBlock;
    private Direction placeDirection;
    private Block foundationBlock;
    private List<Block> currentBlocks;
    private GrowingStage currentGrowingStage;
    private int timeGrown;
    
    public Plant(final UUID uuid, final UUID owner, final String parentDrugId, final Block baseBlock, final Direction placeDirection, final Block foundationBlock, final List<Block> currentBlocks, final GrowingStage currentGrowingStage, final int timeGrown) {
        this.uuid = uuid;
        this.owner = owner;
        this.parentDrugId = parentDrugId;
        this.baseBlock = baseBlock;
        this.placeDirection = placeDirection;
        this.foundationBlock = foundationBlock;
        this.currentBlocks = currentBlocks;
        this.currentGrowingStage = currentGrowingStage;
        this.timeGrown = timeGrown;
    }
    
    public UUID getUUID() {
        return this.uuid;
    }
    
    public UUID getOwner() {
        return this.owner;
    }
    
    public String getParentDrugId() {
        return this.parentDrugId;
    }
    
    public Block getBaseBlock() {
        return this.baseBlock;
    }
    
    public Direction getPlaceDirection() {
        return this.placeDirection;
    }
    
    public Block getFoundationBlock() {
        return this.foundationBlock;
    }
    
    public void setFoundationBlock(final Block foundationBlock) {
        this.foundationBlock = foundationBlock;
    }
    
    public List<Block> getCurrentBlocks() {
        return this.currentBlocks;
    }
    
    public GrowingStage getCurrentGrowingStage() {
        return this.currentGrowingStage;
    }
    
    public void setCurrentGrowingStage(final GrowingStage currentGrowingStage) {
        this.currentGrowingStage = currentGrowingStage;
    }
    
    public int getTimeGrown() {
        return this.timeGrown;
    }
    
    public void addTimeGrown() {
        ++this.timeGrown;
    }
    
    public void setTimeGrown(final int timeGrown) {
        this.timeGrown = timeGrown;
    }
}
