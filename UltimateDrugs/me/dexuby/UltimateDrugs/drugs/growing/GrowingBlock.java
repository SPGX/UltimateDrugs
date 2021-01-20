// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.growing;

import me.dexuby.UltimateDrugs.block.BlockOffset;
import org.bukkit.block.data.BlockData;
import me.dexuby.UltimateDrugs.block.OffsetBlock;

public class GrowingBlock extends OffsetBlock
{
    private String skullTexture;
    private boolean randomRotation;
    private boolean applyPhysics;
    private boolean autoConnect;
    private boolean ignoreSpace;
    
    public GrowingBlock(final BlockData blockData, final BlockOffset blockOffset, final String skullTexture, final boolean randomRotation, final boolean applyPhysics, final boolean autoConnect, final boolean ignoreSpace) {
        super(blockData, blockOffset);
        this.skullTexture = skullTexture;
        this.randomRotation = randomRotation;
        this.applyPhysics = applyPhysics;
        this.autoConnect = autoConnect;
        this.ignoreSpace = ignoreSpace;
    }
    
    public String getSkullTexture() {
        return this.skullTexture;
    }
    
    public boolean useRandomRotation() {
        return this.randomRotation;
    }
    
    public boolean doApplyPhysics() {
        return this.applyPhysics;
    }
    
    public boolean doAutoConnect() {
        return this.autoConnect;
    }
    
    public boolean doIgnoreSpace() {
        return this.ignoreSpace;
    }
}
