// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.growing.processors;

import org.bukkit.block.BlockFace;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.FixedMetadataValue;
import java.util.ArrayList;
import me.dexuby.UltimateDrugs.utils.Constants;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import org.bukkit.plugin.Plugin;
import me.dexuby.UltimateDrugs.block.BlockOption;
import me.dexuby.UltimateDrugs.drugs.growing.Weather;
import me.dexuby.UltimateDrugs.drugs.growing.WorldTime;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.Material;
import me.dexuby.UltimateDrugs.drugs.growing.GrowingStage;
import org.bukkit.block.Block;
import me.dexuby.UltimateDrugs.drugs.growing.PlantType;
import me.dexuby.UltimateDrugs.drugs.growing.Growing;
import me.dexuby.UltimateDrugs.drugs.Drug;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.event.Event;
import me.dexuby.UltimateDrugs.api.DrugPlantGrowEvent;
import me.dexuby.UltimateDrugs.utils.BlockUtils;
import me.dexuby.UltimateDrugs.drugs.growing.GrowingBlock;
import org.bukkit.Location;
import java.util.HashMap;
import me.dexuby.UltimateDrugs.block.SpaceReportType;
import me.dexuby.UltimateDrugs.block.SpaceReport;
import me.dexuby.UltimateDrugs.drugs.growing.Plant;
import java.security.SecureRandom;
import me.dexuby.UltimateDrugs.managers.PlantManager;
import me.dexuby.UltimateDrugs.Main;

public class SyncPlantProcessor extends PlantProcessor
{
    private final Main mainInstance;
    private final PlantManager plantManager;
    private final SecureRandom random;
    
    public SyncPlantProcessor(final Main mainInstance) {
        super(mainInstance);
        this.random = new SecureRandom();
        this.mainInstance = mainInstance;
        this.plantManager = mainInstance.getPlantManager();
    }
    
    @Override
    public void start() {
        for (final Plant plant : this.getPlants()) {
            if (this.plantManager.getToRemove().contains(plant.getUUID())) {
                continue;
            }
            final Drug drugById = this.mainInstance.getDrugManager().getDrugById(plant.getParentDrugId());
            if (drugById == null) {
                continue;
            }
            final Growing growing = drugById.getGrowing();
            final PlantType plantType = growing.getPlantType();
            final Block baseBlock = plant.getBaseBlock();
            if (!this.fulfillsGrowConditions(plant, plantType, baseBlock)) {
                continue;
            }
            if (plant.getTimeGrown() >= plant.getCurrentGrowingStage().getGrowDelay()) {
                final GrowingStage nextGrowingStage = this.plantManager.getNextGrowingStage(plant, plantType);
                final SpaceReport create = new SpaceReport.Creator().withGrowingStage(nextGrowingStage).withDirection(plant.getPlaceDirection()).withBaseBlock(baseBlock).doIgnorePlantBlocks(true).create(SpaceReportType.GROWING);
                if (!create.hasSpace()) {
                    continue;
                }
                final HashMap<Location, GrowingBlock> hashMap = new HashMap<Location, GrowingBlock>();
                for (final GrowingBlock growingBlock : nextGrowingStage.getGrowingBlocks()) {
                    final Block relativeBlock = BlockUtils.getRelativeBlock(plant.getPlaceDirection(), growingBlock.getBlockOffset(), baseBlock);
                    if (create.getFreeBlocks().contains(relativeBlock)) {
                        hashMap.put(relativeBlock.getLocation(), growingBlock);
                    }
                }
                final DrugPlantGrowEvent drugPlantGrowEvent = new DrugPlantGrowEvent(plant, nextGrowingStage);
                this.mainInstance.getServer().getPluginManager().callEvent((Event)drugPlantGrowEvent);
                if (drugPlantGrowEvent.isCancelled()) {
                    return;
                }
                this.removeCurrentPlantBlocks(plant);
                this.placeNewPlantBlocks(plant, hashMap);
                if (nextGrowingStage.isGrown()) {
                    this.plantManager.getToRemove().add(plant.getUUID());
                }
                plant.setCurrentGrowingStage(nextGrowingStage);
                plant.setTimeGrown(0);
                this.mainInstance.getConfigManager().savePlant(plant);
            }
            else {
                if (plant.getCurrentGrowingStage().isFertilizerRequired()) {
                    continue;
                }
                plant.addTimeGrown();
                if (growing.getRandomGrowChance() <= 0.0 || this.random.nextDouble() > growing.getRandomGrowChance() / 100.0) {
                    continue;
                }
                plant.addTimeGrown();
            }
        }
    }
    
    private boolean fulfillsGrowConditions(final Plant plant, final PlantType plantType, final Block block) {
        return this.hasRequiredLiquids(plantType, block) && this.isValidBiome(plantType, block.getBiome()) && this.isValidWorldTime(plantType, block.getWorld()) && this.isValidWeather(plantType, block.getWorld()) && this.hasProperGrowBlock(plant, plantType, block);
    }
    
    private boolean hasRequiredLiquids(final PlantType plantType, final Block block) {
        return (!plantType.isWaterRequired() || this.hasWater(block)) && (!plantType.isLavaRequired() || this.hasLava(block));
    }
    
    private boolean hasWater(final Block block) {
        for (int i = block.getX() - 2; i <= block.getX() + 2; ++i) {
            for (int j = block.getY() - 1; j <= block.getY() + 1; ++j) {
                for (int k = block.getZ() - 2; k <= block.getZ() + 2; ++k) {
                    if (block.getWorld().getBlockAt(i, j, k).getType() == Material.WATER) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean hasLava(final Block block) {
        for (int i = block.getX() - 2; i <= block.getX() + 2; ++i) {
            for (int j = block.getY() - 1; j <= block.getY() + 1; ++j) {
                for (int k = block.getZ() - 2; k <= block.getZ() + 2; ++k) {
                    if (block.getWorld().getBlockAt(i, j, k).getType() == Material.LAVA) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean isValidBiome(final PlantType plantType, final Biome biome) {
        return plantType.getBiomeWhitelist().isEmpty() || plantType.getBiomeWhitelist().contains(biome);
    }
    
    private boolean isValidWorldTime(final PlantType plantType, final World world) {
        if (plantType.getRequiredWorldTime() == null) {
            return true;
        }
        final long time = world.getTime();
        return plantType.getRequiredWorldTime() == ((time < 12300L || time > 23850L) ? WorldTime.DAY : WorldTime.NIGHT);
    }
    
    private boolean isValidWeather(final PlantType plantType, final World world) {
        if (plantType.getWeatherWhitelist().isEmpty()) {
            return true;
        }
        Weather weather;
        if (world.isThundering()) {
            weather = Weather.THUNDER;
        }
        else if (world.hasStorm()) {
            weather = Weather.RAIN;
        }
        else {
            weather = Weather.CLEAR;
        }
        return plantType.getWeatherWhitelist().contains(weather);
    }
    
    private boolean hasProperGrowBlock(final Plant plant, final PlantType plantType, final Block block) {
        if (plantType.getGrowBlockWhitelist().isEmpty()) {
            return true;
        }
        boolean b = false;
        for (final BlockOption blockOption : plantType.getGrowBlockWhitelist()) {
            if (b && !blockOption.isRequired()) {
                continue;
            }
            if (BlockUtils.getRelativeBlock(plant.getPlaceDirection(), blockOption.getBlockOffset(), block).getBlockData().matches(blockOption.getBlockData())) {
                b = true;
            }
            else {
                if (blockOption.isRequired()) {
                    return false;
                }
                continue;
            }
        }
        return b;
    }
    
    private void removeCurrentPlantBlocks(final Plant plant) {
        for (final Block block : plant.getCurrentBlocks()) {
            block.removeMetadata("ultimatedrugs-plant", (Plugin)this.mainInstance);
            block.setType(Material.AIR, false);
        }
        plant.getCurrentBlocks().clear();
    }
    
    private void placeNewPlantBlocks(final Plant plant, final Map<Location, GrowingBlock> map) {
        for (final Map.Entry<Location, GrowingBlock> entry : map.entrySet()) {
            final Location location = entry.getKey();
            final GrowingBlock growingBlock = entry.getValue();
            final Block block2 = location.getBlock();
            block2.setBlockData(growingBlock.getBlockData(), growingBlock.doApplyPhysics());
            if (growingBlock.getSkullTexture() != null) {
                ReflectionUtils.setSkullBlockTexture(block2, growingBlock.getSkullTexture());
            }
            if (growingBlock.useRandomRotation()) {
                if (block2.getBlockData() instanceof Rotatable) {
                    final Rotatable blockData = (Rotatable)block2.getBlockData();
                    blockData.setRotation(BlockUtils.getRandomRotatableBlockFace());
                    block2.setBlockData((BlockData)blockData);
                }
                else if (block2.getBlockData() instanceof Directional) {
                    final Directional blockData2 = (Directional)block2.getBlockData();
                    blockData2.setFacing(BlockUtils.getRandomDirectionalBlockFace());
                    block2.setBlockData((BlockData)blockData2);
                }
            }
            if (growingBlock.doAutoConnect() && Constants.AUTO_CONNECT_MATERIALS.contains(block2.getType()) && block2.getBlockData() instanceof Directional) {
                final Directional blockData3 = (Directional)block2.getBlockData();
                final ArrayList<Block> list = new ArrayList<Block>();
                for (int i = block2.getX() - 1; i <= block2.getX() + 1; ++i) {
                    if (i != block2.getX()) {
                        list.add(block2.getWorld().getBlockAt(i, block2.getY(), block2.getZ()));
                    }
                }
                for (int j = block2.getZ() - 1; j <= block2.getZ() + 1; ++j) {
                    if (j != block2.getZ()) {
                        list.add(block2.getWorld().getBlockAt(block2.getX(), block2.getY(), j));
                    }
                }
                for (final Block foundationBlock : list) {
                    if ((block2.getType() == Material.COCOA && foundationBlock.getType().name().endsWith("JUNGLE_LOG")) || (block2.getType() == Material.ATTACHED_PUMPKIN_STEM && foundationBlock.getType() == Material.PUMPKIN) || (block2.getType() == Material.ATTACHED_MELON_STEM && foundationBlock.getType() == Material.MELON)) {
                        final BlockFace face = block2.getFace(foundationBlock);
                        if (face != null) {
                            blockData3.setFacing(face);
                        }
                        block2.setBlockData((BlockData)blockData3);
                        plant.setFoundationBlock(foundationBlock);
                        break;
                    }
                }
            }
            plant.getCurrentBlocks().add(block2);
        }
        plant.getCurrentBlocks().forEach(block -> block.setMetadata("ultimatedrugs-plant", (MetadataValue)new FixedMetadataValue((Plugin)this.mainInstance, (Object)plant.getUUID().toString())));
    }
}
