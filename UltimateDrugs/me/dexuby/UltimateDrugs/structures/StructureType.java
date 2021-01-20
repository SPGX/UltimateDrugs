// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.structures;

import me.dexuby.UltimateDrugs.block.BlockOffset;
import me.dexuby.UltimateDrugs.block.OffsetBlock;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public class StructureType
{
    private final ItemStack spawnItem;
    private final List<OffsetBlock> blockList;
    private final BlockOffset foundationBlockOffset;
    
    public StructureType(final ItemStack spawnItem, final List<OffsetBlock> blockList) {
        this.spawnItem = spawnItem;
        this.blockList = blockList;
        this.foundationBlockOffset = null;
    }
    
    public StructureType(final ItemStack spawnItem, final List<OffsetBlock> blockList, final BlockOffset foundationBlockOffset) {
        this.spawnItem = spawnItem;
        this.blockList = blockList;
        this.foundationBlockOffset = foundationBlockOffset;
    }
    
    public ItemStack getSpawnItem() {
        return this.spawnItem;
    }
    
    public List<OffsetBlock> getBlockList() {
        return this.blockList;
    }
    
    public BlockOffset getFoundationBlockOffset() {
        return this.foundationBlockOffset;
    }
}
