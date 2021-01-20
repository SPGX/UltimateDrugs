// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.events;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import me.dexuby.UltimateDrugs.drugs.growing.Plant;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Event;
import me.dexuby.UltimateDrugs.api.DrugPlantBreakEvent;
import java.util.UUID;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.event.block.BlockBreakEvent;
import me.dexuby.UltimateDrugs.Main;
import org.bukkit.event.Listener;

public class BlockBreak implements Listener
{
    private Main main;
    
    public BlockBreak(final Main main) {
        this.main = main;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(final BlockBreakEvent blockBreakEvent) {
        if (!blockBreakEvent.isCancelled() && blockBreakEvent.getBlock().hasMetadata("ultimatedrugs-plant")) {
            blockBreakEvent.setCancelled(true);
            final Plant plantByUUID = this.main.getPlantManager().getPlantByUUID(UUID.fromString(blockBreakEvent.getBlock().getMetadata("ultimatedrugs-plant").get(0).asString()));
            if (plantByUUID != null) {
                this.main.getServer().getPluginManager().callEvent((Event)new DrugPlantBreakEvent(blockBreakEvent.getPlayer(), plantByUUID, blockBreakEvent.getBlock()));
            }
            else {
                blockBreakEvent.getBlock().removeMetadata("ultimatedrugs-plant", (Plugin)this.main);
            }
        }
    }
}
