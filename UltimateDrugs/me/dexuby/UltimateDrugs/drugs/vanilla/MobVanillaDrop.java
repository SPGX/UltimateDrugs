// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.vanilla;

import me.dexuby.UltimateDrugs.drugs.Drop;
import org.bukkit.entity.EntityType;
import java.util.Collection;

public class MobVanillaDrop extends VanillaDrop
{
    private boolean overrideDrops;
    private boolean overrideExp;
    private int experience;
    private Collection<EntityType> entityTypes;
    
    public MobVanillaDrop(final String s, final int n, final Collection<Drop> collection, final boolean overrideDrops, final boolean overrideExp, final int experience, final Collection<EntityType> entityTypes) {
        super(s, n, collection);
        this.overrideDrops = overrideDrops;
        this.overrideExp = overrideExp;
        this.experience = experience;
        this.entityTypes = entityTypes;
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
    
    public Collection<EntityType> getEntityTypes() {
        return this.entityTypes;
    }
}
