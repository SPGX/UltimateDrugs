// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.managers;

import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import me.dexuby.UltimateDrugs.structures.Structure;
import me.dexuby.UltimateDrugs.structures.StructureType;
import java.util.Collection;
import me.dexuby.UltimateDrugs.Main;

public class StructureManager
{
    private final Main main;
    private final Collection<StructureType> structureTypes;
    private final Collection<Structure> structures;
    
    public StructureManager(final Main main) {
        this.structureTypes = new ArrayList<StructureType>();
        this.structures = new ArrayList<Structure>();
        this.main = main;
    }
    
    public StructureType getStructureTypeByItemStack(final ItemStack itemStack) {
        return this.structureTypes.stream().filter(structureType -> structureType.getSpawnItem().isSimilar(itemStack)).findFirst().orElse(null);
    }
    
    public Structure getStructureByInventory(final Inventory obj) {
        return this.structures.stream().filter(structure -> structure.getInventory() != null && structure.getInventory().equals(obj)).findFirst().orElse(null);
    }
    
    public Structure getStructureByBlock(final Block block) {
        return this.structures.stream().filter(structure -> structure.getBlockList().contains(block)).findFirst().orElse(null);
    }
    
    public void addStructureType(final StructureType structureType) {
        this.structureTypes.add(structureType);
    }
    
    public void removeStructureType(final StructureType structureType) {
        this.structureTypes.remove(structureType);
    }
    
    public void addStructure(final Structure structure) {
        this.structures.add(structure);
    }
    
    public void removeStructure(final Structure structure) {
        structure.removeBlocks(this.main);
        this.structures.remove(structure);
    }
}
