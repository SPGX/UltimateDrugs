// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.api;

import org.bukkit.block.Block;
import me.dexuby.UltimateDrugs.drugs.growing.Plant;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class DrugPlantBreakEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private boolean cancel;
    private Player player;
    private Plant plant;
    private Block block;
    
    public DrugPlantBreakEvent(final Player player, final Plant plant, final Block block) {
        this.player = player;
        this.plant = plant;
        this.block = block;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Plant getPlant() {
        return this.plant;
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public HandlerList getHandlers() {
        return DrugPlantBreakEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return DrugPlantBreakEvent.handlers;
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
