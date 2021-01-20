// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.api;

import me.dexuby.UltimateDrugs.drugs.growing.Fertilizer;
import me.dexuby.UltimateDrugs.drugs.growing.Plant;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class DrugPlantFertilizerUseEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private boolean cancel;
    private Player player;
    private Plant plant;
    private Fertilizer fertilizer;
    
    public DrugPlantFertilizerUseEvent(final Player player, final Plant plant, final Fertilizer fertilizer) {
        this.player = player;
        this.plant = plant;
        this.fertilizer = fertilizer;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Plant getPlant() {
        return this.plant;
    }
    
    public Fertilizer getFertilizer() {
        return this.fertilizer;
    }
    
    public HandlerList getHandlers() {
        return DrugPlantFertilizerUseEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return DrugPlantFertilizerUseEvent.handlers;
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
