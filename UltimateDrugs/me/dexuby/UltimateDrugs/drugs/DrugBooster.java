// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs;

import java.util.Collection;
import java.util.UUID;

public class DrugBooster
{
    private String id;
    private UUID ownerUUID;
    private boolean ownerOnly;
    private boolean allDrugs;
    private Collection<String> affectedDrugs;
    private double multiplier;
    private boolean stackable;
    private boolean active;
    private long duration;
    private long expirationTimestamp;
     
    public DrugBooster(String id, UUID ownerUUID, boolean ownerOnly, boolean allDrugs, Collection<String> affectedDrugs, double multiplier, boolean stackable, boolean active, long duration, long expirationTimestamp) {
        this.id = id;
        this.ownerUUID = ownerUUID;
        this.ownerOnly = ownerOnly;
        this.allDrugs = allDrugs;
        this.affectedDrugs = affectedDrugs;
        this.multiplier = multiplier;
        this.stackable = stackable;
        this.active = active;
        this.duration = duration;
        this.expirationTimestamp = expirationTimestamp;
    }
    
    public String getId() {
        return this.id;
    }
    
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }
    
    public boolean isOwnerOnly() {
        return this.ownerOnly;
    }
    
    public boolean isAllDrugs() {
        return this.allDrugs;
    }
    
    public Collection<String> getAffectedDrugs() {
        return this.affectedDrugs;
    }
    
    public double getMultiplier() {
        return this.multiplier;
    }
    
    public boolean isStackable() {
        return this.stackable;
    }
    
    public boolean isActive() {
        return this.active;
    }
    
    public void setActive(final boolean active) {
        this.active = active;
    }
    
    public long getDuration() {
        return this.duration;
    }
    
    public long getExpirationTimestamp() {
        return this.expirationTimestamp;
    }
    
    public void setExpirationTimestamp(final long expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }
}
