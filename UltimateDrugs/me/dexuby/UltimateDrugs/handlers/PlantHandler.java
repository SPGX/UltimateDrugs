// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.handlers;

import me.dexuby.UltimateDrugs.drugs.Drop;
import java.security.SecureRandom;
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
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import me.dexuby.UltimateDrugs.utils.MaterialUtils;
import org.bukkit.event.block.BlockPhysicsEvent;
import me.dexuby.UltimateDrugs.api.DrugPlantFertilizerUseEvent;
import java.util.Map;
import me.dexuby.UltimateDrugs.utils.TextUtils;
import me.dexuby.UltimateDrugs.api.DrugPlantSendStatusEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.Material;
import me.dexuby.UltimateDrugs.utils.Constants;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import me.dexuby.UltimateDrugs.drugs.growing.GrowingBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.GameMode;
import me.dexuby.UltimateDrugs.drugs.growing.Direction;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import me.dexuby.UltimateDrugs.api.DrugPlantPlantEvent;
import java.util.UUID;
import me.dexuby.UltimateDrugs.block.SpaceReportType;
import me.dexuby.UltimateDrugs.block.SpaceReport;
import me.dexuby.UltimateDrugs.drugs.growing.GrowingStage;
import me.dexuby.UltimateDrugs.utils.NumberUtils;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import org.bukkit.permissions.PermissionAttachmentInfo;
import java.util.List;
import me.dexuby.UltimateDrugs.utils.BlockUtils;
import me.dexuby.UltimateDrugs.block.BlockOption;
import org.bukkit.entity.Entity;
import me.dexuby.UltimateDrugs.utils.EntityUtils;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.block.BlockFace;
import me.dexuby.UltimateDrugs.api.DrugPlantPrePlantEvent;
import org.bukkit.event.EventHandler;
import me.dexuby.UltimateDrugs.drugs.Drug;
import com.gmail.nossr50.api.ExperienceAPI;
import org.bukkit.event.Event;
import me.dexuby.UltimateDrugs.api.DrugPlantReplantEvent;
import me.dexuby.UltimateDrugs.api.DrugPlantBreakEvent;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import me.dexuby.UltimateDrugs.drugs.growing.Growing;
import java.util.HashMap;
import org.bukkit.plugin.Plugin;
import me.dexuby.UltimateDrugs.drugs.growing.Plant;
import java.util.LinkedList;
import me.dexuby.UltimateDrugs.drugs.growing.processors.PlantProcessor;
import java.util.Queue;
import org.bukkit.scheduler.BukkitRunnable;

import me.dexuby.UltimateDrugs.drugs.growing.processors.SyncPlantProcessor;
import me.dexuby.UltimateDrugs.drugs.growing.processors.DefaultPlantProcessor;
import me.dexuby.UltimateDrugs.config.SettingsHolder;
import me.dexuby.UltimateDrugs.drugs.growing.processors.ProcessorType;
import me.dexuby.UltimateDrugs.managers.DrugManager;
import me.dexuby.UltimateDrugs.managers.PlantManager;
import me.dexuby.UltimateDrugs.Main;
import org.bukkit.event.Listener;

public class PlantHandler implements Listener
{
    private Main main;
    private PlantManager plantManager;
    private DrugManager drugManager;
    
    public PlantHandler(final Main main) {
    	this.main = main;
        this.plantManager = main.getPlantManager();
        this.drugManager = main.getDrugManager();
        ProcessorType processortype = (ProcessorType)SettingsHolder.PLANT_PROCESSOR_TYPE.getValue();
        Object object = processortype == ProcessorType.DEFAULT ? new DefaultPlantProcessor(main) : new SyncPlantProcessor(main);
        main.getServer().getScheduler().runTaskTimerAsynchronously((Plugin)main, () -> {
            this.plantManager.updateGrowingPlants();
            final Queue queue = this.getLoadedPlants();
            if (!queue.isEmpty()) {
                final int l1 = (int)Math.ceil((double)queue.size() / 20.0D);
                final int[] aint = new int[]{0};
                (new BukkitRunnable() {
                   public void run() {
                      int i2 = 0;

                      LinkedList linkedlist;
                      for(linkedlist = new LinkedList(); i2 < l1 && !queue.isEmpty(); ++i2) {
                         linkedlist.add(queue.poll());
                      }

                      ((PlantProcessor) object).setPlants(linkedlist).start();
                      ++aint[0];
                      if (aint[0] >= 20 || queue.isEmpty()) {
                         this.cancel();
                      }

                   }
                }).runTaskTimer(main, 0L, 1L);
             }
        }, 20L, 20L);
    }
    
    private Queue<Plant> getLoadedPlants() {
        final LinkedList<Plant> list = new LinkedList<Plant>();
        final HashMap<Object, Growing> hashMap = new HashMap<Object, Growing>();
        for (final Plant plant : new ArrayList<Plant>(this.plantManager.getGrowingPlants())) {
            Growing growing;
            if (hashMap.containsKey(plant.getParentDrugId())) {
                growing = hashMap.get(plant.getParentDrugId());
            }
            else {
                hashMap.put(plant.getParentDrugId(), this.main.getDrugManager().getDrugById(plant.getParentDrugId()).getGrowing());
                growing = hashMap.get(plant.getParentDrugId());
            }
            if (plant.getBaseBlock().getWorld().isChunkLoaded(plant.getBaseBlock().getX() >> 4, plant.getBaseBlock().getZ() >> 4) || growing.doIgnoreChunkState()) {
                list.add(plant);
            }
        }
        return list;
    }
    
    @EventHandler
    public void onDrugPlantBreak(final DrugPlantBreakEvent drugPlantBreakEvent) {
        if (drugPlantBreakEvent.isCancelled()) {
            return;
        }
        final Plant plant = drugPlantBreakEvent.getPlant();
        this.destroyPlant(drugPlantBreakEvent.getPlant(), drugPlantBreakEvent.getBlock());
        final Drug drugById = this.drugManager.getDrugById(plant.getParentDrugId());
        if (this.main.getPlantManager().getHighestGrowingStage(drugById.getGrowing().getPlantType()).getOrderId() == plant.getCurrentGrowingStage().getOrderId() && drugById.getGrowing().doAutoReplant() && drugPlantBreakEvent.getPlayer().hasPermission("ultimatedrugs.auto-replant")) {
            this.main.getServer().getPluginManager().callEvent((Event)new DrugPlantReplantEvent(plant, drugPlantBreakEvent.getPlayer()));
        }
        if (plant.getCurrentGrowingStage().getMcMmoBonusXp() != 0.0f && this.main.getServer().getPluginManager().isPluginEnabled("mcMMO")) {
            ExperienceAPI.addRawXP(drugPlantBreakEvent.getPlayer(), "herbalism", plant.getCurrentGrowingStage().getMcMmoBonusXp(), "UNKNOWN");
        }
    }
    
    @EventHandler
    public void onDrugPlantPrePlant(final DrugPlantPrePlantEvent drugPlantPrePlantEvent) {
        final Drug drug = drugPlantPrePlantEvent.getDrug();
        final Player player = drugPlantPrePlantEvent.getPlayer();
        final Block baseBlock = drugPlantPrePlantEvent.getBaseBlock();
        final BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(baseBlock, baseBlock.getState(), baseBlock.getRelative(BlockFace.DOWN), drugPlantPrePlantEvent.getPlayer().getInventory().getItemInMainHand(), player, true, EquipmentSlot.HAND);
        this.main.getServer().getPluginManager().callEvent((Event)blockPlaceEvent);
        if (blockPlaceEvent.isCancelled()) {
            return;
        }
        boolean b = true;
        final Direction direction = EntityUtils.getDirection((Entity)player);
        if (!drug.getGrowing().getPlantType().getPlantBlockWhitelist().isEmpty()) {
            int n = 0;
            for (final BlockOption blockOption : drug.getGrowing().getPlantType().getPlantBlockWhitelist()) {
                if (n != 0 && !blockOption.isRequired()) {
                    continue;
                }
                if (BlockUtils.getRelativeBlock(direction, blockOption.getBlockOffset(), baseBlock).getBlockData().matches(blockOption.getBlockData())) {
                    n = 1;
                }
                else {
                    if (blockOption.isRequired()) {
                        n = 0;
                        break;
                    }
                    continue;
                }
            }
            if (n == 0) {
                b = false;
            }
        }
        if (!drug.getGrowing().getPlantType().getBiomeWhitelist().isEmpty() && !drug.getGrowing().getPlantType().getBiomeWhitelist().contains(baseBlock.getBiome())) {
            b = false;
        }
        final int intValue = (int)SettingsHolder.PLAYER_PLANT_LIMIT.getValue();
        if (intValue >= 0 && !player.hasPermission("ultimatedrugs.plant-limit.bypass") && this.main.getPlantManager().getPlantsByOwner(player.getUniqueId()).size() >= intValue) {
            b = false;
        }
        String s = null;
        List<String> list = player.getEffectivePermissions().stream().filter(permissionAttachmentInfo -> permissionAttachmentInfo.getPermission().startsWith(String.format("ultimatedrugs.plant-limit.%s.", drug.getId()))).map(PermissionAttachmentInfo::getPermission).collect(Collectors.toList());
        list.remove(String.format("ultimatedrugs.plant-limit.%s.bypass", drug.getId()));
        int n2 = 0;
        for (String s2 : list) {
            String s3 = s2.split("\\.")[3];
            if (NumberUtils.isValidInteger(s3)) {
                final int int1 = Integer.parseInt(s3);
                if (int1 <= n2) {
                    continue;
                }
                n2 = int1;
                s = s2;
            }
        }
        if (s != null && !player.hasPermission(String.format("ultimatedrugs.plant-limit.%s.bypass", drug.getId())) && this.main.getPlantManager().getPlantsByOwner(player.getUniqueId()).stream().filter(plant -> plant.getParentDrugId().equals(drug.getId())).count() >= n2) {
            b = false;
        }
        if (b) {
            final GrowingStage growingStage2 = drugPlantPrePlantEvent.getDrug().getGrowing().getPlantType().getGrowingStages().stream().filter(growingStage -> growingStage.getOrderId() == 0).findFirst().orElse(null);
            if (growingStage2 != null) {
                final SpaceReport create = new SpaceReport.Creator().withGrowingStage(growingStage2).withDirection(direction).withBaseBlock(baseBlock).doIgnorePlantBlocks(false).create(SpaceReportType.GROWING);
                if (create.hasSpace()) {
                    this.main.getServer().getPluginManager().callEvent((Event)new DrugPlantPlantEvent(drugPlantPrePlantEvent.getPlayer(), drugPlantPrePlantEvent.getDrug(), drugPlantPrePlantEvent.getBaseBlock(), growingStage2, direction, UUID.randomUUID(), create));
                }
            }
        }
    }
    
    @EventHandler
    public void onDrugPlantPlant(final DrugPlantPlantEvent drugPlantPlantEvent) {
        if (drugPlantPlantEvent.isCancelled()) {
            return;
        }
        final GrowingStage growingStage = drugPlantPlantEvent.getGrowingStage();
        final Direction direction = drugPlantPlantEvent.getDirection();
        final UUID plantUUID = drugPlantPlantEvent.getPlantUUID();
        final int heldItemSlot = drugPlantPlantEvent.getPlayer().getInventory().getHeldItemSlot();
        final ItemStack item = drugPlantPlantEvent.getPlayer().getInventory().getItem(heldItemSlot);
        if (item == null) {
            return;
        }
        if (drugPlantPlantEvent.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            }
            else {
                drugPlantPlantEvent.getPlayer().getInventory().clear(heldItemSlot);
            }
        }
        this.createPlant(plantUUID, drugPlantPlantEvent.getPlayer().getUniqueId(), drugPlantPlantEvent.getDrug().getId(), growingStage, direction, drugPlantPlantEvent.getBaseBlock(), drugPlantPlantEvent.getBaseBlock().getRelative(BlockFace.DOWN), drugPlantPlantEvent.getSpaceReport());
    }
    
    @EventHandler
    public void onDrugPlantReplant(final DrugPlantReplantEvent drugPlantReplantEvent) {
        if (drugPlantReplantEvent.isCancelled()) {
            return;
        }
        final Plant plant = drugPlantReplantEvent.getPlant();
        final Drug drugById = this.main.getDrugManager().getDrugById(plant.getParentDrugId());
        if (drugById.getGrowing().isReplantSeedRequired()) {
            boolean b = false;
            for (int i = 0; i <= 40; ++i) {
                final ItemStack item = drugPlantReplantEvent.getPlayer().getInventory().getItem(i);
                if (item != null && item.isSimilar(drugById.getGrowing().getSeedItemStack())) {
                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                    }
                    else {
                        drugPlantReplantEvent.getPlayer().getInventory().clear(i);
                    }
                    b = true;
                    break;
                }
            }
            if (!b) {
                drugPlantReplantEvent.setCancelled(true);
                return;
            }
        }
        final GrowingStage growingStage2 = drugById.getGrowing().getPlantType().getGrowingStages().stream().filter(growingStage -> growingStage.getOrderId() == drugById.getGrowing().getReplantStage()).findFirst().orElse(null);
        if (growingStage2 != null) {
            final SpaceReport create = new SpaceReport.Creator().withGrowingStage(growingStage2).withDirection(plant.getPlaceDirection()).withBaseBlock(plant.getBaseBlock()).doIgnorePlantBlocks(false).create(SpaceReportType.GROWING);
            if (create.hasSpace()) {
                this.createPlant(UUID.randomUUID(), plant.getOwner(), plant.getParentDrugId(), growingStage2, plant.getPlaceDirection(), plant.getBaseBlock(), plant.getFoundationBlock(), create);
            }
        }
    }
    
    private void createPlant(final UUID uuid, final UUID uuid2, final String s, final GrowingStage growingStage, final Direction direction, final Block block2, Block block3, final SpaceReport spaceReport) {
        if (!spaceReport.hasSpace()) {
            return;
        }
        final ArrayList<Block> list = new ArrayList<Block>();
        for (final GrowingBlock growingBlock : growingStage.getGrowingBlocks()) {
            final Block relativeBlock = BlockUtils.getRelativeBlock(direction, growingBlock.getBlockOffset(), block2);
            if (spaceReport.getFreeBlocks().contains(relativeBlock)) {
                relativeBlock.setBlockData(growingBlock.getBlockData(), growingBlock.doApplyPhysics());
                if (growingBlock.getSkullTexture() != null) {
                    ReflectionUtils.setSkullBlockTexture(relativeBlock, growingBlock.getSkullTexture());
                }
                if (growingBlock.useRandomRotation()) {
                    if (relativeBlock.getBlockData() instanceof Rotatable) {
                        final Rotatable blockData = (Rotatable)relativeBlock.getBlockData();
                        blockData.setRotation(BlockUtils.getRandomRotatableBlockFace());
                        relativeBlock.setBlockData((BlockData)blockData);
                    }
                    else if (relativeBlock.getBlockData() instanceof Directional) {
                        final Directional blockData2 = (Directional)relativeBlock.getBlockData();
                        blockData2.setFacing(BlockUtils.getRandomDirectionalBlockFace());
                        relativeBlock.setBlockData((BlockData)blockData2);
                    }
                }
                if (growingBlock.doAutoConnect() && Constants.AUTO_CONNECT_MATERIALS.contains(relativeBlock.getType())) {
                    final ArrayList<Block> list2 = new ArrayList<Block>();
                    for (int i = relativeBlock.getX() - 1; i <= relativeBlock.getX() + 1; ++i) {
                        if (i != relativeBlock.getX()) {
                            list2.add(relativeBlock.getWorld().getBlockAt(i, relativeBlock.getY(), relativeBlock.getZ()));
                        }
                    }
                    for (int j = relativeBlock.getZ() - 1; j <= relativeBlock.getZ() + 1; ++j) {
                        if (j != relativeBlock.getZ()) {
                            list2.add(relativeBlock.getWorld().getBlockAt(relativeBlock.getX(), relativeBlock.getY(), j));
                        }
                    }
                    for (final Block block4 : list2) {
                        if ((relativeBlock.getType() == Material.COCOA && block4.getType().name().endsWith("JUNGLE_LOG")) || (relativeBlock.getType() == Material.ATTACHED_PUMPKIN_STEM && block4.getType() == Material.PUMPKIN) || (relativeBlock.getType() == Material.ATTACHED_MELON_STEM && block4.getType() == Material.MELON)) {
                            final Directional blockData3 = (Directional)relativeBlock.getBlockData();
                            final BlockFace face = relativeBlock.getFace(block4);
                            if (face != null) {
                                blockData3.setFacing(face);
                            }
                            relativeBlock.setBlockData((BlockData)blockData3);
                            block3 = block4;
                            break;
                        }
                    }
                }
                list.add(relativeBlock);
            }
        }
        list.forEach(block -> block.setMetadata("ultimatedrugs-plant", (MetadataValue)new FixedMetadataValue((Plugin)this.main, (Object)uuid.toString())));
        final Plant plant = new Plant(uuid, uuid2, s, block2, direction, block3, list, growingStage, 0);
        this.main.getPlantManager().registerPlant(plant);
        this.main.getPlantManager().registerGrowingPlant(plant);
    }
    
    @EventHandler
    public void onDrugPlantSendStatus(final DrugPlantSendStatusEvent drugPlantSendStatusEvent) {
        if (drugPlantSendStatusEvent.isCancelled()) {
            return;
        }
        final Plant plant = drugPlantSendStatusEvent.getPlant();
        final Drug drugById = this.drugManager.getDrugById(plant.getParentDrugId());
        if (!plant.getCurrentGrowingStage().isGrown()) {
            final int n = (plant.getCurrentGrowingStage().getGrowDelay() > 0) ? plant.getCurrentGrowingStage().getGrowDelay() : plant.getTimeGrown();
            final int n2 = (int)Math.round(plant.getTimeGrown() / (double)n * 10.0);
            final StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= 10; ++i) {
                sb.append((i <= n2) ? this.main.getConfigManager().getLangString("filled-bar-part") : this.main.getConfigManager().getLangString("empty-bar-part"));
            }
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("%plant_stage%", Integer.toString(plant.getCurrentGrowingStage().getOrderId() + 1));
            if (drugById != null) {
                hashMap.put("%max_plant_stage%", Integer.toString(drugById.getGrowing().getPlantType().getGrowingStages().size()));
            }
            hashMap.put("%progress_percentage%", Integer.toString((int)Math.round(plant.getTimeGrown() / (double)n * 100.0)));
            hashMap.put("%progress_bar%", sb.toString());
            hashMap.put("%plant_name%", drugById.getDisplayName());
            drugPlantSendStatusEvent.getPlayer().sendMessage(TextUtils.replaceVariables(this.main.getConfigManager().getLangString("plant-progress-status"), hashMap));
        }
        else {
            drugPlantSendStatusEvent.getPlayer().sendMessage(this.main.getConfigManager().getLangString("plant-progress-status-grown"));
        }
    }
    
    @EventHandler
    public void onDrugPlantFertilizerUse(final DrugPlantFertilizerUseEvent drugPlantFertilizerUseEvent) {
        if (drugPlantFertilizerUseEvent.isCancelled()) {
            return;
        }
        final Plant plant = drugPlantFertilizerUseEvent.getPlant();
        if (!plant.getCurrentGrowingStage().isGrown() && plant.getTimeGrown() < plant.getCurrentGrowingStage().getGrowDelay()) {
            final int heldItemSlot = drugPlantFertilizerUseEvent.getPlayer().getInventory().getHeldItemSlot();
            final ItemStack item = drugPlantFertilizerUseEvent.getPlayer().getInventory().getItem(heldItemSlot);
            if (item == null) {
                return;
            }
            if (drugPlantFertilizerUseEvent.getPlayer().getGameMode() != GameMode.CREATIVE) {
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                }
                else {
                    drugPlantFertilizerUseEvent.getPlayer().getInventory().clear(heldItemSlot);
                }
            }
            plant.setTimeGrown(plant.getTimeGrown() + drugPlantFertilizerUseEvent.getFertilizer().getGrowAmount());
        }
    }
    
    @EventHandler
    public void onBlockPhysics(final BlockPhysicsEvent blockPhysicsEvent) {
        final Block block = blockPhysicsEvent.getBlock();
        if (block.hasMetadata("ultimatedrugs-plant")) {
            blockPhysicsEvent.setCancelled(true);
            final Block sourceBlock = blockPhysicsEvent.getSourceBlock();
            final Plant plantByUUID = this.main.getPlantManager().getPlantByUUID(UUID.fromString(block.getMetadata("ultimatedrugs-plant").get(0).asString()));
            if (plantByUUID != null && MaterialUtils.isAir(sourceBlock.getType()) && sourceBlock.getLocation().equals((Object)plantByUUID.getFoundationBlock().getLocation())) {
                boolean doRemoveIfFlying = false;
                final Drug drugById = this.drugManager.getDrugById(plantByUUID.getParentDrugId());
                if (drugById != null && drugById.getGrowing() != null) {
                    doRemoveIfFlying = drugById.getGrowing().getPlantType().doRemoveIfFlying();
                }
                if (!block.getType().isOccluding() || !block.getType().isSolid() || doRemoveIfFlying) {
                    this.destroyPlant(plantByUUID, block);
                }
            }
        }
    }
    
    @EventHandler
    public void onBlockFromTo(final BlockFromToEvent blockFromToEvent) {
        if (blockFromToEvent.getToBlock().hasMetadata("ultimatedrugs-plant") && !blockFromToEvent.getToBlock().getType().isSolid()) {
            blockFromToEvent.setCancelled(true);
            final Block toBlock = blockFromToEvent.getToBlock();
            this.destroyPlant(this.main.getPlantManager().getPlantByUUID(UUID.fromString(((MetadataValue)toBlock.getMetadata("ultimatedrugs-plant").get(0)).asString())), toBlock);
        }
    }
    
    @EventHandler
    public void onBlockExplode(final BlockExplodeEvent blockExplodeEvent) {
        if (blockExplodeEvent.getBlock().hasMetadata("ultimatedrugs-plant")) {
            blockExplodeEvent.setCancelled(true);
        }
        else {
            for (final Block block : blockExplodeEvent.blockList()) {
                if (block.hasMetadata("ultimatedrugs-plant")) {
                    this.destroyPlant(this.main.getPlantManager().getPlantByUUID(UUID.fromString(((MetadataValue)block.getMetadata("ultimatedrugs-plant").get(0)).asString())), block);
                }
            }
        }
    }
    
    @EventHandler
    public void onBlockPistonExtend(final BlockPistonExtendEvent blockPistonExtendEvent) {
        if (blockPistonExtendEvent.getBlock().hasMetadata("ultimatedrugs-plant")) {
            blockPistonExtendEvent.setCancelled(true);
        }
        else if (!blockPistonExtendEvent.isCancelled()) {
            for (final Block block : blockPistonExtendEvent.getBlocks()) {
                if (block.hasMetadata("ultimatedrugs-plant")) {
                    this.destroyPlant(this.main.getPlantManager().getPlantByUUID(UUID.fromString(((MetadataValue)block.getMetadata("ultimatedrugs-plant").get(0)).asString())), block);
                    blockPistonExtendEvent.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onBlockPistonRetract(final BlockPistonRetractEvent blockPistonRetractEvent) {
        if (blockPistonRetractEvent.getBlock().hasMetadata("ultimatedrugs-plant")) {
            blockPistonRetractEvent.setCancelled(true);
        }
        else if (!blockPistonRetractEvent.isCancelled() && blockPistonRetractEvent.isSticky()) {
            for (final Block block : blockPistonRetractEvent.getBlocks()) {
                if (block.hasMetadata("ultimatedrugs-plant") && block.getType().isSolid()) {
                    this.destroyPlant(this.main.getPlantManager().getPlantByUUID(UUID.fromString(((MetadataValue)block.getMetadata("ultimatedrugs-plant").get(0)).asString())), block);
                    blockPistonRetractEvent.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onStructureGrow(final StructureGrowEvent structureGrowEvent) {
        if (structureGrowEvent.getLocation().getBlock().hasMetadata("ultimatedrugs-plant")) {
            structureGrowEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBurn(final BlockBurnEvent blockBurnEvent) {
        if (blockBurnEvent.getBlock().hasMetadata("ultimatedrugs-plant")) {
            blockBurnEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockFade(final BlockFadeEvent blockFadeEvent) {
        if (blockFadeEvent.getBlock().hasMetadata("ultimatedrugs-plant")) {
            blockFadeEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockFertilizeEvent(final BlockFertilizeEvent blockFertilizeEvent) {
        if (blockFertilizeEvent.getBlock().hasMetadata("ultimatedrugs-plant")) {
            blockFertilizeEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockGrow(final BlockGrowEvent blockGrowEvent) {
        if (blockGrowEvent.getBlock().hasMetadata("ultimatedrugs-plant")) {
            blockGrowEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockSpread(final BlockSpreadEvent blockSpreadEvent) {
        if (blockSpreadEvent.getSource().hasMetadata("ultimatedrugs-plant")) {
            blockSpreadEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockIgnite(final BlockIgniteEvent blockIgniteEvent) {
        if (blockIgniteEvent.getBlock().hasMetadata("ultimatedrugs-plant")) {
            blockIgniteEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onCauldronLevelChange(final CauldronLevelChangeEvent cauldronLevelChangeEvent) {
        if (cauldronLevelChangeEvent.getBlock().hasMetadata("ultimatedrugs-plant")) {
            cauldronLevelChangeEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onLeavesDecay(final LeavesDecayEvent leavesDecayEvent) {
        if (leavesDecayEvent.getBlock().hasMetadata("ultimatedrugs-plant")) {
            leavesDecayEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onMoistureChange(final MoistureChangeEvent moistureChangeEvent) {
        if (moistureChangeEvent.getBlock().hasMetadata("ultimatedrugs-plant")) {
            moistureChangeEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onSpongeAbsorb(final SpongeAbsorbEvent spongeAbsorbEvent) {
        if (spongeAbsorbEvent.getBlock().hasMetadata("ultimatedrugs-plant")) {
            spongeAbsorbEvent.setCancelled(true);
        }
    }
    
    private void destroyPlant(final Plant plant, final Block block) {
        this.plantManager.unregisterPlant(plant.getUUID());
        for (final Block block2 : plant.getCurrentBlocks()) {
            block2.removeMetadata("ultimatedrugs-plant", (Plugin)this.main);
            block2.setType(Material.AIR, false);
        }
        this.main.getConfigManager().removePlant(plant);
        if (plant.getCurrentGrowingStage().getDrops().size() > 0) {
            final SecureRandom secureRandom = new SecureRandom();
            int n = 0;
            final int dropLimit = plant.getCurrentGrowingStage().getDropLimit();
            int n2 = 0;
            for (final Drop drop : plant.getCurrentGrowingStage().getDrops()) {
                if (dropLimit > 0 && n >= dropLimit) {
                    break;
                }
                final ItemStack clone = drop.getItemStack().clone();
                clone.setAmount((drop.getMinAmount() != drop.getMaxAmount()) ? (secureRandom.nextInt(drop.getMaxAmount() - drop.getMinAmount() + 1) + drop.getMinAmount()) : drop.getMaxAmount());
                if (n2 != 0 && drop.doBreakSuccessChain()) {
                    n2 = 0;
                }
                else if (secureRandom.nextDouble() <= drop.getDropChance() / 100.0) {
                    if (!MaterialUtils.isAir(clone.getType())) {
                        block.getWorld().dropItemNaturally(block.getLocation(), clone);
                    }
                    ++n;
                    n2 = 1;
                }
                else {
                    n2 = 0;
                }
            }
        }
    }
}
