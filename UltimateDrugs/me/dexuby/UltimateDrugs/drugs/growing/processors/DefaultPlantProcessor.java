package me.dexuby.UltimateDrugs.drugs.growing.processors;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import me.dexuby.UltimateDrugs.Main;
import me.dexuby.UltimateDrugs.api.DrugPlantGrowEvent;
import me.dexuby.UltimateDrugs.block.BlockOption;
import me.dexuby.UltimateDrugs.block.SpaceReport;
import me.dexuby.UltimateDrugs.block.SpaceReportType;
import me.dexuby.UltimateDrugs.drugs.Drug;
import me.dexuby.UltimateDrugs.drugs.growing.ChunkLocation;
import me.dexuby.UltimateDrugs.drugs.growing.GrowingBlock;
import me.dexuby.UltimateDrugs.drugs.growing.GrowingStage;
import me.dexuby.UltimateDrugs.drugs.growing.Plant;
import me.dexuby.UltimateDrugs.drugs.growing.PlantType;
import me.dexuby.UltimateDrugs.drugs.growing.Weather;
import me.dexuby.UltimateDrugs.drugs.growing.WorldTime;
import me.dexuby.UltimateDrugs.managers.DrugManager;
import me.dexuby.UltimateDrugs.managers.PlantManager;
import me.dexuby.UltimateDrugs.utils.BlockUtils;
import me.dexuby.UltimateDrugs.utils.Constants;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.event.Event;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class DefaultPlantProcessor extends PlantProcessor {
  private final Main main;
  
  private final PlantManager plantManager;
  
  private final DrugManager drugManager;
  
  public DefaultPlantProcessor(Main paramMain) {
    super(paramMain);
    this.main = paramMain;
    this.plantManager = paramMain.getPlantManager();
    this.drugManager = paramMain.getDrugManager();
  }
  
  public void start() {
    HashMap<Object, Object> hashMap = new HashMap<>();
    Queue<Plant> queue = getPlants();
    SecureRandom secureRandom = new SecureRandom();
    
    queue.forEach(paramPlant -> collectChunkSnapshots(paramPlant, (Map<ChunkLocation, ChunkSnapshot>) queue));
    this.main.getServer().getScheduler().runTaskAsynchronously((Plugin)this.main, () -> {
          for (Plant plant : queue) {
            if (this.plantManager.getToRemove().contains(plant.getUUID()))
              continue; 
            boolean bool = true;
            Block block = plant.getBaseBlock();
            Drug drug = this.drugManager.getDrugById(plant.getParentDrugId());
            if (drug == null)
              continue; 
            PlantType plantType = drug.getGrowing().getPlantType();
            if (plantType.isWaterRequired()) {
              boolean bool1 = false;
              for (int i = block.getX() - 2; i <= block.getX() + 2; i++) {
                for (int j = block.getY() - 1; j <= block.getY() + 1; j++) {
                  for (int k = block.getZ() - 2; k <= block.getZ() + 2; k++) {
                    ChunkSnapshot chunkSnapshot = getChunkSnapshotByChunkLocation(new ChunkLocation(block.getWorld(), i >> 4, k >> 4), (Map<ChunkLocation, ChunkSnapshot>) queue);
                    Material material = chunkSnapshot.getBlockType(Math.floorMod(i, 16), j, Math.floorMod(k, 16));
                    if (material == Material.WATER)
                      bool1 = true; 
                  } 
                } 
              } 
              if (!bool1)
                bool = false; 
            } 
            if (bool && plantType.isLavaRequired()) {
              boolean bool1 = false;
              for (int i = block.getX() - 2; i <= block.getX() + 2; i++) {
                for (int j = block.getY() - 1; j <= block.getY() + 1; j++) {
                  for (int k = block.getZ() - 2; k <= block.getZ() + 2; k++) {
                    ChunkSnapshot chunkSnapshot = getChunkSnapshotByChunkLocation(new ChunkLocation(block.getWorld(), i >> 4, k >> 4), (Map<ChunkLocation, ChunkSnapshot>) queue);
                    Material material = chunkSnapshot.getBlockType(Math.floorMod(i, 16), j, Math.floorMod(k, 16));
                    if (material == Material.LAVA)
                      bool1 = true; 
                  } 
                } 
              } 
              if (!bool1)
                bool = false; 
            } 
            if (bool) {
              if (plant.getTimeGrown() >= plant.getCurrentGrowingStage().getGrowDelay()) {
            	  GrowingStage growingstage = this.plantManager.getNextGrowingStage(plant, plantType);
            	  HashMap<Location, GrowingBlock> hashmap1 = new HashMap<Location, GrowingBlock>();
                  SpaceReport spacereport = (new SpaceReport.Creator()).withGrowingStage(growingstage).withDirection(plant.getPlaceDirection()).withBaseBlock(block).doIgnorePlantBlocks(true).create(SpaceReportType.GROWING);

                  for(GrowingBlock growingblock : growingstage.getGrowingBlocks()) {
                     Block block1 = BlockUtils.getRelativeBlock(plant.getPlaceDirection(), growingblock.getBlockOffset(), block);
                     if (spacereport.getFreeBlocks().contains(block1)) {
                        hashmap1.put(block1.getLocation(), growingblock);
                     }
                  }
                
                  if (spacereport.hasSpace()) {
                	  this.main.getServer().getScheduler().runTask(this.main, () -> {
                          if (this.fulfillsGrowConditions(plant, plantType)) {
                             DrugPlantGrowEvent drugplantgrowevent = new DrugPlantGrowEvent(plant, growingstage);
                             this.main.getServer().getPluginManager().callEvent(drugplantgrowevent);
                             if (drugplantgrowevent.isCancelled() || this.plantManager.getToRemove().contains(plant.getUUID())) {
                                return;
                             }

                             for(Block block2 : plant.getCurrentBlocks()) {
                                block2.removeMetadata("ultimatedrugs-plant", this.main);
                                block2.setType(Material.AIR, false);
                             }

                             plant.getCurrentBlocks().clear();
                             
                             for(Entry entry : hashmap1.entrySet()) {
                            	 
                                Location location = (Location)entry.getKey();
                                Block block3 = location.getBlock();
                                block3.setBlockData(((GrowingBlock)entry.getValue()).getBlockData(), ((GrowingBlock)entry.getValue()).doApplyPhysics());
                                if (((GrowingBlock)entry.getValue()).getSkullTexture() != null) {
                                   ReflectionUtils.setSkullBlockTexture(block3, ((GrowingBlock)entry.getValue()).getSkullTexture());
                                }

                                if (((GrowingBlock)entry.getValue()).useRandomRotation()) {
                                   if (block3.getBlockData() instanceof Rotatable) {
                                      Rotatable rotatable = (Rotatable)block3.getBlockData();
                                      rotatable.setRotation(BlockUtils.getRandomRotatableBlockFace());
                                      block3.setBlockData(rotatable);
                                   } else if (block3.getBlockData() instanceof Directional) {
                                      Directional directional = (Directional)block3.getBlockData();
                                      directional.setFacing(BlockUtils.getRandomDirectionalBlockFace());
                                      block3.setBlockData(directional);
                                   }
                                }

                                if (((GrowingBlock)entry.getValue()).doAutoConnect() && Constants.AUTO_CONNECT_MATERIALS.contains(block3.getType()) && block3.getBlockData() instanceof Directional) {
                                   Directional directional1 = (Directional)block3.getBlockData();
                                   ArrayList<Block> arraylist = new ArrayList<Block>();

                                   for(int i2 = block3.getX() - 1; i2 <= block3.getX() + 1; ++i2) {
                                      if (i2 != block3.getX()) {
                                         arraylist.add(block3.getWorld().getBlockAt(i2, block3.getY(), block3.getZ()));
                                      }
                                   }

                                   for(int j2 = block3.getZ() - 1; j2 <= block3.getZ() + 1; ++j2) {
                                      if (j2 != block3.getZ()) {
                                         arraylist.add(block3.getWorld().getBlockAt(block3.getX(), block3.getY(), j2));
                                      }
                                   }

                                   for(Block block4 : arraylist) {
                                      if (block3.getType() == Material.COCOA && block4.getType().name().endsWith("JUNGLE_LOG") || block3.getType() == Material.ATTACHED_PUMPKIN_STEM && block4.getType() == Material.PUMPKIN || block3.getType() == Material.ATTACHED_MELON_STEM && block4.getType() == Material.MELON) {
                                         BlockFace blockface = block3.getFace(block4);
                                         if (blockface != null) {
                                            directional1.setFacing(blockface);
                                         }

                                         block3.setBlockData(directional1);
                                         plant.setFoundationBlock(block4);
                                         break;
                                      }
                                   }
                                }

                                plant.getCurrentBlocks().add(block3);
                             }

                             plant.getCurrentBlocks().forEach((var2) -> {
                                var2.setMetadata("ultimatedrugs-plant", new FixedMetadataValue(this.main, plant.getUUID().toString()));
                             });
                             if (growingstage.isGrown()) {
                                this.plantManager.getToRemove().add(plant.getUUID());
                             }

                             plant.setCurrentGrowingStage(growingstage);
                             plant.setTimeGrown(0);
                             this.main.getConfigManager().savePlant(plant);
                          }

                       });

                  }
              } else {
                  this.main.getServer().getScheduler().runTask(this.main, () -> {
                      if (!plant.getCurrentGrowingStage().isFertilizerRequired()) {
                         if (this.fulfillsGrowConditions(plant, plantType)) {
                            plant.addTimeGrown();
                            if (drug.getGrowing().getRandomGrowChance() > 0.0D && secureRandom.nextDouble() <= drug.getGrowing().getRandomGrowChance() / 100.0D) {
                               plant.addTimeGrown();
                            }
                         }

                      }
                   });
                }
            
            } 
          } 
        });
  }
  
  private ChunkSnapshot getChunkSnapshotByChunkLocation(ChunkLocation paramChunkLocation, Map<ChunkLocation, ChunkSnapshot> paramMap) {
    return paramMap.entrySet().stream()
      .filter(paramEntry -> ((ChunkLocation)paramEntry.getKey()).toString().equals(paramChunkLocation.toString()))
      .findFirst().map(Map.Entry::getValue).orElse(null);
  }
  
  private void collectChunkSnapshots(Plant paramPlant, Map<ChunkLocation, ChunkSnapshot> paramMap) {
    Chunk chunk = paramPlant.getBaseBlock().getChunk();
    for (int i = chunk.getX() - 1; i <= chunk.getX() + 1; i++) {
      for (int j = chunk.getZ() - 1; j <= chunk.getZ() + 1; j++) {
        Chunk chunk1 = chunk.getWorld().getChunkAt(i, j);
        boolean bool = chunk1.isLoaded();
        if (!bool)
          chunk1.load(); 
        ChunkLocation chunkLocation = new ChunkLocation(paramPlant.getBaseBlock().getWorld(), chunk1.getX(), chunk1.getZ());
        if (paramMap.keySet().stream().noneMatch(paramChunkLocation2 -> paramChunkLocation2.toString().equals(chunkLocation.toString())))
          paramMap.put(chunkLocation, chunk1.getChunkSnapshot()); 
        if (!bool)
          chunk1.unload(); 
      } 
    } 
  }
  
  private boolean fulfillsGrowConditions(Plant paramPlant, PlantType paramPlantType) {
    Block block = paramPlant.getBaseBlock();
    boolean bool = true;
    if (!paramPlantType.getBiomeWhitelist().isEmpty() && 
      !paramPlantType.getBiomeWhitelist().contains(block.getBiome()))
      bool = false; 
    if (paramPlantType.getRequiredWorldTime() != null) {
      long l = block.getWorld().getTime();
      WorldTime worldTime = (l < 12300L || l > 23850L) ? WorldTime.DAY : WorldTime.NIGHT;
      if (!worldTime.equals(paramPlantType.getRequiredWorldTime()))
        bool = false; 
    } 
    if (!paramPlantType.getWeatherWhitelist().isEmpty()) {
      Weather weather;
      if (block.getWorld().isThundering()) {
        weather = Weather.THUNDER;
      } else if (block.getWorld().hasStorm()) {
        weather = Weather.RAIN;
      } else {
        weather = Weather.CLEAR;
      } 
      if (!paramPlantType.getWeatherWhitelist().contains(weather))
        bool = false; 
    } 
    if (!paramPlantType.getGrowBlockWhitelist().isEmpty()) {
      boolean bool1 = false;
      for (BlockOption blockOption : paramPlantType.getGrowBlockWhitelist()) {
        if (bool1 && !blockOption.isRequired())
          continue; 
        Block block1 = BlockUtils.getRelativeBlock(paramPlant.getPlaceDirection(), blockOption.getBlockOffset(), block);
        if (block1.getBlockData().matches(blockOption.getBlockData())) {
          bool1 = true;
          continue;
        } 
        if (blockOption.isRequired()) {
          bool1 = false;
          break;
        } 
      } 
      if (!bool1)
        bool = false; 
    } 
    return bool;
  }
}
