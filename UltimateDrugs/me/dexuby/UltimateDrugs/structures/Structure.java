// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.structures;

import java.util.Iterator;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import me.dexuby.UltimateDrugs.Main;
import org.bukkit.inventory.Inventory;
import org.bukkit.block.Block;
import java.util.Set;

public class Structure
{
    private final StructureType structureType;
    private final Set<Block> blockList;
    private final Inventory inventory;
    
    public Structure(final StructureType structureType, final Set<Block> blockList, final Inventory inventory) {
        this.structureType = structureType;
        this.blockList = blockList;
        this.inventory = inventory;
    }
    
    public StructureType getStructureType() {
        return this.structureType;
    }
    
    public Set<Block> getBlockList() {
        return this.blockList;
    }
    
    public Inventory getInventory() {
        return this.inventory;
    }
    
    public void removeBlocks(final Main main) {
        for (final Block block : this.blockList) {
            block.removeMetadata("ultimatedrugs.structure-block", (Plugin)main);
            block.setType(Material.AIR);
        }
        this.blockList.clear();
    }
}
