// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.block;

import org.bukkit.block.data.BlockData;

public class BlockOption
{
    private BlockData blockData;
    private BlockOffset blockOffset;
    private boolean required;
    
    public BlockOption(final BlockData blockData, final BlockOffset blockOffset, final boolean required) {
        this.blockData = blockData;
        this.blockOffset = blockOffset;
        this.required = required;
    }
    
    public BlockData getBlockData() {
        return this.blockData;
    }
    
    public BlockOffset getBlockOffset() {
        return this.blockOffset;
    }
    
    public boolean isRequired() {
        return this.required;
    }
}
