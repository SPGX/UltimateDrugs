// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.api;

import me.dexuby.UltimateDrugs.drugs.growing.Plant;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class DrugPlantInteractEvent extends Event
{
    private static final HandlerList handlers;
    private Player player;
    private Plant plant;
    
    public DrugPlantInteractEvent(final Player player, final Plant plant) {
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
        return DrugPlantInteractEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return DrugPlantInteractEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
