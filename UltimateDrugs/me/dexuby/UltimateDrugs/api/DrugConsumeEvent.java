// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.api;

import me.dexuby.UltimateDrugs.drugs.Drug;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class DrugConsumeEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private boolean cancel;
    private Player player;
    private Drug drug;
    
    public DrugConsumeEvent(final Player player, final Drug drug) {
        this.player = player;
        this.drug = drug;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Drug getDrug() {
        return this.drug;
    }
    
    public HandlerList getHandlers() {
        return DrugConsumeEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return DrugConsumeEvent.handlers;
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
