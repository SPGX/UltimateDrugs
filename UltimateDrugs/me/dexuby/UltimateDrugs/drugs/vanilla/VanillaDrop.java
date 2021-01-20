// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.vanilla;

import me.dexuby.UltimateDrugs.drugs.Drop;
import java.util.Collection;

public abstract class VanillaDrop
{
    private String permission;
    private int dropLimit;
    private Collection<Drop> drops;
    
    VanillaDrop(final String permission, final int dropLimit, final Collection<Drop> drops) {
        this.permission = permission;
        this.dropLimit = dropLimit;
        this.drops = drops;
    }
    
    public String getPermission() {
        return this.permission;
    }
    
    public int getDropLimit() {
        return this.dropLimit;
    }
    
    public Collection<Drop> getDrops() {
        return this.drops;
    }
}
