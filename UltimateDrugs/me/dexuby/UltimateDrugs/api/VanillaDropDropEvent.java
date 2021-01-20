// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.api;

import me.dexuby.UltimateDrugs.drugs.Drop;
import java.util.Collection;
import org.bukkit.Location;
import me.dexuby.UltimateDrugs.drugs.vanilla.VanillaDrop;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class VanillaDropDropEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private boolean cancel;
    private VanillaDrop vanillaDrop;
    private Location location;
    private Collection<Drop> drops;
    private Object[] extraData;
    
    public VanillaDropDropEvent(final VanillaDrop vanillaDrop, final Location location, final Collection<Drop> drops, final Object... extraData) {
        this.vanillaDrop = vanillaDrop;
        this.location = location;
        this.drops = drops;
        this.extraData = extraData;
    }
    
    public VanillaDrop getVanillaDrop() {
        return this.vanillaDrop;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public Collection<Drop> getDrops() {
        return this.drops;
    }
    
    public Object[] getExtraData() {
        return this.extraData;
    }
    
    public HandlerList getHandlers() {
        return VanillaDropDropEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return VanillaDropDropEvent.handlers;
    }
    
    public boolean isCancelled() {
        return this.cancel;
    }
    
    public void setCancelled(final boolean cancel) {
        this.cancel = cancel;
    }
    
    static {
        handlers = new HandlerList();
    }
}
