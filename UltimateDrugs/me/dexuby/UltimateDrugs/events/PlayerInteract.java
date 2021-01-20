// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.events;

import org.bukkit.event.EventHandler;
import org.bukkit.block.BlockFace;
import me.dexuby.UltimateDrugs.drugs.Drug;
import java.util.Iterator;
import me.dexuby.UltimateDrugs.drugs.growing.Fertilizer;
import me.dexuby.UltimateDrugs.drugs.growing.Plant;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import me.dexuby.UltimateDrugs.api.DrugConsumeEvent;
import me.dexuby.UltimateDrugs.drugs.ConsumeOption;
import me.dexuby.UltimateDrugs.api.DrugPlantPrePlantEvent;
import me.dexuby.UltimateDrugs.utils.BlockUtils;
import me.dexuby.UltimateDrugs.api.DrugPlantFertilizerUseEvent;
import me.dexuby.UltimateDrugs.api.DrugPlantSendStatusEvent;
import org.bukkit.event.Event;
import me.dexuby.UltimateDrugs.api.DrugPlantInteractEvent;
import java.util.UUID;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.event.player.PlayerInteractEvent;
import me.dexuby.UltimateDrugs.managers.DrugManager;
import me.dexuby.UltimateDrugs.Main;
import org.bukkit.event.Listener;

public class PlayerInteract implements Listener
{
    private Main main;
    private DrugManager drugManager;
    
    public PlayerInteract(final Main main) {
        this.main = main;
        this.drugManager = main.getDrugManager();
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent playerInteractEvent) {
        final boolean cancelled = playerInteractEvent.isCancelled();
        final Player player = playerInteractEvent.getPlayer();
        if (playerInteractEvent.getHand() == EquipmentSlot.OFF_HAND) {
            if (this.drugManager.isUseableDrugItem(player.getInventory().getItemInOffHand())) {
                playerInteractEvent.setCancelled(true);
            }
            return;
        }
        final Block clickedBlock = playerInteractEvent.getClickedBlock();
        final ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK && clickedBlock != null && clickedBlock.hasMetadata("ultimatedrugs-plant")) {
            playerInteractEvent.setCancelled(true);
            final Plant plantByUUID = this.main.getPlantManager().getPlantByUUID(UUID.fromString(clickedBlock.getMetadata("ultimatedrugs-plant").get(0).asString()));
            if (plantByUUID != null) {
                this.main.getServer().getPluginManager().callEvent((Event)new DrugPlantInteractEvent(player, plantByUUID));
                if (player.isSneaking()) {
                    this.main.getServer().getPluginManager().callEvent((Event)new DrugPlantSendStatusEvent(player, plantByUUID));
                }
                final Fertilizer fertilizerByItemStack;
                if ((fertilizerByItemStack = this.drugManager.getFertilizerByItemStack(itemInMainHand)) != null) {
                    boolean b = false;
                    for (final String anotherString : fertilizerByItemStack.getDrugIds()) {
                        if (anotherString.contains(":")) {
                            final String[] split = anotherString.split(":");
                            if (split.length != 2) {
                                continue;
                            }
                            final String anotherString2 = split[0];
                            final int int1 = Integer.parseInt(split[1]);
                            if (plantByUUID.getParentDrugId().equalsIgnoreCase(anotherString2) && plantByUUID.getCurrentGrowingStage().getOrderId() == int1) {
                                b = true;
                                break;
                            }
                            continue;
                        }
                        else {
                            if (plantByUUID.getParentDrugId().equalsIgnoreCase(anotherString)) {
                                b = true;
                                break;
                            }
                            continue;
                        }
                    }
                    if (b) {
                        this.main.getServer().getPluginManager().callEvent((Event)new DrugPlantFertilizerUseEvent(player, plantByUUID, fertilizerByItemStack));
                    }
                }
            }
        }
        final Drug drugBySeed;
        if ((drugBySeed = this.drugManager.getDrugBySeed(itemInMainHand)) != null) {
            if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK && clickedBlock != null) {
                playerInteractEvent.setCancelled(true);
                final BlockFace targetBlockFace = BlockUtils.getTargetBlockFace(playerInteractEvent.getPlayer(), false);
                if (targetBlockFace != null) {
                    this.main.getServer().getPluginManager().callEvent((Event)new DrugPlantPrePlantEvent(player, drugBySeed, clickedBlock.getRelative(targetBlockFace)));
                }
            }
        }
        else {
            final Drug drugByItemStack;
            if ((drugByItemStack = this.drugManager.getDrugByItemStack(itemInMainHand)) != null) {
                if (drugByItemStack.getConsumeOption() == ConsumeOption.RIGHT_CLICK) {
                    if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK || playerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR) {
                        playerInteractEvent.setCancelled(true);
                        this.main.getServer().getPluginManager().callEvent((Event)new DrugConsumeEvent(player, drugByItemStack));
                    }
                }
                else if (!cancelled) {
                    playerInteractEvent.setCancelled(false);
                }
            }
        }
    }
}
