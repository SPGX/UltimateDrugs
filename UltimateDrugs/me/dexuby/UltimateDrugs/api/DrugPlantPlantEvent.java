// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.api;

import me.dexuby.UltimateDrugs.block.SpaceReport;
import java.util.UUID;
import me.dexuby.UltimateDrugs.drugs.growing.Direction;
import me.dexuby.UltimateDrugs.drugs.growing.GrowingStage;
import org.bukkit.block.Block;
import me.dexuby.UltimateDrugs.drugs.Drug;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class DrugPlantPlantEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private boolean cancel;
    private Player player;
    private Drug drug;
    private Block baseBlock;
    private GrowingStage growingStage;
    private Direction direction;
    private UUID plantUUID;
    private SpaceReport spaceReport;
    
    public DrugPlantPlantEvent(final Player player, final Drug drug, final Block baseBlock, final GrowingStage growingStage, final Direction direction, final UUID plantUUID, final SpaceReport spaceReport) {
        this.player = player;
        this.drug = drug;
        this.baseBlock = baseBlock;
        this.growingStage = growingStage;
        this.direction = direction;
        this.plantUUID = plantUUID;
        this.spaceReport = spaceReport;
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
    
    public GrowingStage getGrowingStage() {
        return this.growingStage;
    }
    
    public Direction getDirection() {
        return this.direction;
    }
    
    public UUID getPlantUUID() {
        return this.plantUUID;
    }
    
    public SpaceReport getSpaceReport() {
        return this.spaceReport;
    }
    
    public HandlerList getHandlers() {
        return DrugPlantPlantEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return DrugPlantPlantEvent.handlers;
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
