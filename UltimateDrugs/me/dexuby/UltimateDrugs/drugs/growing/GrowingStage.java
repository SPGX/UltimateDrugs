// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.growing;

import me.dexuby.UltimateDrugs.drugs.Drop;
import java.util.Collection;
import java.util.List;

public class GrowingStage
{
    private int orderId;
    private boolean grown;
    private int growDelay;
    private float mcMmoBonusXp;
    private boolean fertilizerRequired;
    private int requiredBlockCount;
    private List<GrowingBlock> growingBlocks;
    private int dropLimit;
    private Collection<Drop> drops;
    
    public GrowingStage(final int orderId, final boolean grown, final int growDelay, final float mcMmoBonusXp, final boolean fertilizerRequired, final int requiredBlockCount, final List<GrowingBlock> growingBlocks, final int dropLimit, final Collection<Drop> drops) {
        this.orderId = orderId;
        this.grown = grown;
        this.growDelay = growDelay;
        this.mcMmoBonusXp = mcMmoBonusXp;
        this.fertilizerRequired = fertilizerRequired;
        this.requiredBlockCount = requiredBlockCount;
        this.growingBlocks = growingBlocks;
        this.dropLimit = dropLimit;
        this.drops = drops;
    }
    
    public int getOrderId() {
        return this.orderId;
    }
    
    public boolean isGrown() {
        return this.grown;
    }
    
    public int getGrowDelay() {
        return this.growDelay;
    }
    
    public float getMcMmoBonusXp() {
        return this.mcMmoBonusXp;
    }
    
    public boolean isFertilizerRequired() {
        return this.fertilizerRequired;
    }
    
    public int getRequiredBlockCount() {
        return this.requiredBlockCount;
    }
    
    public List<GrowingBlock> getGrowingBlocks() {
        return this.growingBlocks;
    }
    
    public int getDropLimit() {
        return this.dropLimit;
    }
    
    public Collection<Drop> getDrops() {
        return this.drops;
    }
}
