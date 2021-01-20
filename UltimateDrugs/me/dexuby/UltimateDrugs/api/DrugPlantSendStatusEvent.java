// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.api;

import me.dexuby.UltimateDrugs.drugs.growing.Plant;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class DrugPlantSendStatusEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private boolean cancel;
    private Player player;
    private Plant plant;
    
    public DrugPlantSendStatusEvent(final Player player, final Plant plant) {
        this.player = player;
        this.plant = plant;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Plant getPlant() {
        return this.plant;
    }
    
    public HandlerList getHandlers() {
        return DrugPlantSendStatusEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return DrugPlantSendStatusEvent.handlers;
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
