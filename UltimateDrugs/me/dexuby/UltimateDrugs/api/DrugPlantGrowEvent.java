// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.api;

import me.dexuby.UltimateDrugs.drugs.growing.GrowingStage;
import me.dexuby.UltimateDrugs.drugs.growing.Plant;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class DrugPlantGrowEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private boolean cancel;
    private Plant plant;
    private GrowingStage nextGrowingStage;
    
    public DrugPlantGrowEvent(final Plant plant, final GrowingStage nextGrowingStage) {
        this.plant = plant;
        this.nextGrowingStage = nextGrowingStage;
    }
    
    public Plant getPlant() {
        return this.plant;
    }
    
    public GrowingStage getNextGrowingStage() {
        return this.nextGrowingStage;
    }
    
    public HandlerList getHandlers() {
        return DrugPlantGrowEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return DrugPlantGrowEvent.handlers;
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
