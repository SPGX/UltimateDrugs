// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.vanilla;

import me.dexuby.UltimateDrugs.drugs.Drop;
import java.util.Collection;

public class BlockVanillaDrop extends VanillaDrop
{
    private boolean overrideDrops;
    private boolean overrideExp;
    private int experience;
    private boolean ignoreDrugPlants;
    private Collection<VanillaDropBlock> blocks;
    
    public BlockVanillaDrop(final String s, final int n, final Collection<Drop> collection, final boolean overrideDrops, final boolean overrideExp, final int experience, final boolean ignoreDrugPlants, final Collection<VanillaDropBlock> blocks) {
        super(s, n, collection);
        this.overrideDrops = overrideDrops;
        this.overrideExp = overrideExp;
        this.experience = experience;
        this.ignoreDrugPlants = ignoreDrugPlants;
        this.blocks = blocks;
    }
    
    public boolean doOverrideDrops() {
        return this.overrideDrops;
    }
    
    public boolean doOverrideExp() {
        return this.overrideExp;
    }
    
    public int getExperience() {
        return this.experience;
    }
    
    public boolean doIgnoreDrugPlants() {
        return this.ignoreDrugPlants;
    }
    
    public Collection<VanillaDropBlock> getBlocks() {
        return this.blocks;
    }
}
