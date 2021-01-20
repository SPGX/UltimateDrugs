// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.handlers;

import me.dexuby.UltimateDrugs.utils.ItemBuilder;
import org.bukkit.event.block.SpongeAbsorbEvent;
import org.bukkit.event.block.MoistureChangeEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import java.util.Iterator;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import me.dexuby.UltimateDrugs.structures.Structure;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventHandler;
import me.dexuby.UltimateDrugs.structures.StructureType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Entity;
import me.dexuby.UltimateDrugs.utils.EntityUtils;
import me.dexuby.UltimateDrugs.utils.MaterialUtils;
import java.util.Set;
import org.bukkit.Material;
import me.dexuby.UltimateDrugs.config.SettingsHolder;
import org.bukkit.event.player.PlayerItemHeldEvent;
import java.util.HashMap;
import me.dexuby.UltimateDrugs.structures.StructurePreviewRunnable;
import java.util.UUID;
import java.util.Map;
import me.dexuby.UltimateDrugs.Main;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Listener;

public class StructureHandler implements Listener
{
    private static final ItemStack TEST_ITEM;
    private Main main;
    private final Map<UUID, StructurePreviewRunnable> playersInPreview;
    
    public StructureHandler(final Main main) {
        this.playersInPreview = new HashMap<UUID, StructurePreviewRunnable>();
        this.main = main;
    }
    
    @EventHandler
    public void onPlayerItemHeld(final PlayerItemHeldEvent playerItemHeldEvent) {
        if (!this.main.getServer().getPluginManager().isPluginEnabled("ProtocolLib") || !(boolean)SettingsHolder.STRUCTURE_PREVIEW.getValue()) {
            return;
        }
        final Player player = playerItemHeldEvent.getPlayer();
        if (!this.isInPreview(player)) {
            final ItemStack item = player.getInventory().getItem(playerItemHeldEvent.getNewSlot());
            if (item != null && item.getType() != Material.AIR) {
                final StructureType structureTypeByItemStack = this.main.getStructureManager().getStructureTypeByItemStack(item);
                if (structureTypeByItemStack != null && !MaterialUtils.isAir(player.getTargetBlock((Set)null, 5).getType())) {
                    final StructurePreviewRunnable structurePreviewRunnable = new StructurePreviewRunnable(player, EntityUtils.getDirection((Entity)player), structureTypeByItemStack);
                    structurePreviewRunnable.runTaskTimer((Plugin)this.main, 0L, (long)SettingsHolder.STRUCTURE_PREVIEW_UPDATE_DELAY.getValue());
                    this.playersInPreview.put(player.getUniqueId(), structurePreviewRunnable);
                }
            }
        }
        else {
            this.playersInPreview.get(player.getUniqueId()).cancelSafely();
            this.playersInPreview.remove(player.getUniqueId());
        }
    }
    
    private boolean isInPreview(final Player player) {
        if (this.playersInPreview.containsKey(player.getUniqueId())) {
            if (!this.playersInPreview.get(player.getUniqueId()).isCancelled()) {
                return true;
            }
            this.playersInPreview.remove(player.getUniqueId());
        }
        return false;
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final Block clickedBlock = playerInteractEvent.getClickedBlock();
            if (clickedBlock == null || !clickedBlock.hasMetadata("ultimatedrugs.structure-block")) {
                return;
            }
            final Structure structureByBlock = this.main.getStructureManager().getStructureByBlock(clickedBlock);
            if (structureByBlock != null && structureByBlock.getInventory() != null) {
                playerInteractEvent.getPlayer().openInventory(structureByBlock.getInventory());
            }
        }
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent inventoryClickEvent) {
        if (this.main.getStructureManager().getStructureByInventory(inventoryClickEvent.getView().getTopInventory()) != null) {
            inventoryClickEvent.setCancelled(true);
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent blockBreakEvent) {
        final Block block = blockBreakEvent.getBlock();
        if (block.hasMetadata("ultimatedrugs.structure-block")) {
            final Structure structureByBlock = this.main.getStructureManager().getStructureByBlock(block);
            if (structureByBlock != null) {
                this.main.getStructureManager().removeStructure(structureByBlock);
            }
        }
    }
    
    @EventHandler
    public void onBlockFromTo(final BlockFromToEvent blockFromToEvent) {
        if (blockFromToEvent.getToBlock().hasMetadata("ultimatedrugs.structure-block")) {
            blockFromToEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockExplode(final BlockExplodeEvent blockExplodeEvent) {
        if (blockExplodeEvent.getBlock().hasMetadata("ultimatedrugs.structure-block")) {
            blockExplodeEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockPistonExtend(final BlockPistonExtendEvent blockPistonExtendEvent) {
        if (blockPistonExtendEvent.getBlock().hasMetadata("ultimatedrugs.structure-block")) {
            blockPistonExtendEvent.setCancelled(true);
        }
        final Iterator<Block> iterator = blockPistonExtendEvent.getBlocks().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().hasMetadata("ultimatedrugs.structure-block")) {
                blockPistonExtendEvent.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onBlockPistonRetract(final BlockPistonRetractEvent blockPistonRetractEvent) {
        if (blockPistonRetractEvent.getBlock().hasMetadata("ultimatedrugs.structure-block")) {
            blockPistonRetractEvent.setCancelled(true);
        }
        final Iterator<Block> iterator = blockPistonRetractEvent.getBlocks().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().hasMetadata("ultimatedrugs.structure-block")) {
                blockPistonRetractEvent.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onStructureGrow(final StructureGrowEvent structureGrowEvent) {
        if (structureGrowEvent.getLocation().getBlock().hasMetadata("ultimatedrugs.structure-block")) {
            structureGrowEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBurn(final BlockBurnEvent blockBurnEvent) {
        if (blockBurnEvent.getBlock().hasMetadata("ultimatedrugs.structure-block")) {
            blockBurnEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockFade(final BlockFadeEvent blockFadeEvent) {
        if (blockFadeEvent.getBlock().hasMetadata("ultimatedrugs.structure-block")) {
            blockFadeEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockFertilize(final BlockFertilizeEvent blockFertilizeEvent) {
        if (blockFertilizeEvent.getBlock().hasMetadata("ultimatedrugs.structure-block")) {
            blockFertilizeEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockGrow(final BlockGrowEvent blockGrowEvent) {
        if (blockGrowEvent.getBlock().hasMetadata("ultimatedrugs.structure-block")) {
            blockGrowEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockSpread(final BlockSpreadEvent blockSpreadEvent) {
        if (blockSpreadEvent.getBlock().hasMetadata("ultimatedrugs.structure-block")) {
            blockSpreadEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockIgnite(final BlockIgniteEvent blockIgniteEvent) {
        if (blockIgniteEvent.getBlock().hasMetadata("ultimatedrugs.structure-block")) {
            blockIgniteEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onCauldronLevelChange(final CauldronLevelChangeEvent cauldronLevelChangeEvent) {
        if (cauldronLevelChangeEvent.getBlock().hasMetadata("ultimatedrugs.structure-block")) {
            cauldronLevelChangeEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onLeavesDecay(final LeavesDecayEvent leavesDecayEvent) {
        if (leavesDecayEvent.getBlock().hasMetadata("ultimatedrugs.structure-block")) {
            leavesDecayEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onMoistureChange(final MoistureChangeEvent moistureChangeEvent) {
        if (moistureChangeEvent.getBlock().hasMetadata("ultimatedrugs.structure-block")) {
            moistureChangeEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onSpongeAbsorb(final SpongeAbsorbEvent spongeAbsorbEvent) {
        if (spongeAbsorbEvent.getBlock().hasMetadata("ultimatedrugs.structure-block")) {
            spongeAbsorbEvent.setCancelled(true);
        }
    }
    
    static {
        TEST_ITEM = new ItemBuilder(Material.STICK).build();
    }
}
