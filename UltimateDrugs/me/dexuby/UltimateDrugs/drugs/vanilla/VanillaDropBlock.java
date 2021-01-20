// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.vanilla;

import org.bukkit.block.data.BlockData;

public class VanillaDropBlock
{
    private BlockData blockData;
    private boolean ignoreData;
    
    public VanillaDropBlock(final BlockData blockData, final boolean ignoreData) {
        this.blockData = blockData;
        this.ignoreData = ignoreData;
    }
    
    public BlockData getBlockData() {
        return this.blockData;
    }
    
    public boolean doIgnoreData() {
        return this.ignoreData;
    }
}
