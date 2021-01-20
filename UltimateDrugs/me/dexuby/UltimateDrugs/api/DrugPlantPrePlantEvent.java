// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.api;

import org.bukkit.block.Block;
import me.dexuby.UltimateDrugs.drugs.Drug;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class DrugPlantPrePlantEvent extends Event
{
    private static final HandlerList handlers;
    private Player player;
    private Drug drug;
    private Block baseBlock;
    
    public DrugPlantPrePlantEvent(final Player player, final Drug drug, final Block baseBlock) {
        this.player = player;
        this.drug = drug;
        this.baseBlock = baseBlock;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Drug getDrug() {
        return this.drug;
    }
    
    public Block getBaseBlock() {
        return this.baseBlock;
    }
    
    public HandlerList getHandlers() {
        return DrugPlantPrePlantEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return DrugPlantPrePlantEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
