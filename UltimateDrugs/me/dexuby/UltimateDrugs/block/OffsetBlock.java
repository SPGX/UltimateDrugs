// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.block;

import org.bukkit.block.data.BlockData;

public abstract class OffsetBlock
{
    private BlockData blockData;
    private BlockOffset blockOffset;
    
    public OffsetBlock(final BlockData blockData, final BlockOffset blockOffset) {
        this.blockData = blockData;
        this.blockOffset = blockOffset;
    }
    
    public BlockData getBlockData() {
        return this.blockData;
    }
    
    public BlockOffset getBlockOffset() {
        return this.blockOffset;
    }
}
