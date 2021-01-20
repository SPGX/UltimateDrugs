// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.api;

import org.bukkit.entity.Player;
import me.dexuby.UltimateDrugs.drugs.growing.Plant;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class DrugPlantReplantEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private boolean cancel;
    private Plant plant;
    private Player player;
    
    public DrugPlantReplantEvent(final Plant plant, final Player player) {
        this.plant = plant;
        this.player = player;
    }
    
    public Plant getPlant() {
        return this.plant;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public HandlerList getHandlers() {
        return DrugPlantReplantEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return DrugPlantReplantEvent.handlers;
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
