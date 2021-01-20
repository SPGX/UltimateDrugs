package me.dexuby.UltimateDrugs.handlers;

import java.util.Collection;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.dexuby.UltimateDrugs.Main;
import me.dexuby.UltimateDrugs.api.VanillaDropDropEvent;
import me.dexuby.UltimateDrugs.config.SettingsHolder;
import me.dexuby.UltimateDrugs.drugs.Drop;
import me.dexuby.UltimateDrugs.drugs.vanilla.BlockVanillaDrop;
import me.dexuby.UltimateDrugs.drugs.vanilla.FishingVanillaDrop;
import me.dexuby.UltimateDrugs.drugs.vanilla.MobVanillaDrop;
import me.dexuby.UltimateDrugs.drugs.vanilla.VanillaDrop;
import me.dexuby.UltimateDrugs.managers.VanillaDropManager;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class VanillaHandler implements Listener {
   private Main main;
   private VanillaDropManager vanillaDropManager;

   public VanillaHandler(Main var1) {
      this.main = var1;
      this.vanillaDropManager = var1.getVanillaDropManager();
   }

   @EventHandler
   public void onVanillaDropDrop(VanillaDropDropEvent var1) {
      if (!var1.isCancelled() && var1.getLocation().getWorld() != null) {
         int i = 0;
         int j = var1.getVanillaDrop().getDropLimit();
         boolean flag = false;
         Random random = new Random();

         for(Drop drop : var1.getDrops()) {
            if (j > 0 && i >= j) {
               break;
            }

            ItemStack itemstack = drop.getItemStack().clone();
            int k = drop.getMinAmount() != drop.getMaxAmount() ? random.nextInt(drop.getMaxAmount() - drop.getMinAmount() + 1) + drop.getMinAmount() : drop.getMaxAmount();
            itemstack.setAmount(k);
            if (flag && drop.doBreakSuccessChain()) {
               flag = false;
            } else if (random.nextDouble() <= drop.getDropChance() / 100.0D) {
               if (var1.getVanillaDrop() instanceof FishingVanillaDrop) {
                  if (var1.getExtraData().length == 1) {
                     Vector vector = (Vector)var1.getExtraData()[0];
                     Item item = var1.getLocation().getWorld().dropItem(var1.getLocation(), itemstack);
                     item.setVelocity(vector);
                  }
               } else {
                  var1.getLocation().getWorld().dropItemNaturally(var1.getLocation(), itemstack);
               }

               ++i;
               flag = true;
            } else {
               flag = false;
            }
         }

      }
   }

   @EventHandler
   public void onBlockBreak(BlockBreakEvent var1) {
      if (!var1.isCancelled()) {
         if (((Boolean)SettingsHolder.VANILLA_STORE_PLAYER_BLOCKS.getValue()).booleanValue() && this.vanillaDropManager.isPlayerPlacedBlock(var1.getBlock())) {
            this.vanillaDropManager.removePlayerPlacedBlockLocation(var1.getBlock().getLocation());
         }

         if (var1.getPlayer().getGameMode() != GameMode.CREATIVE) {
            for(VanillaDrop vanilladrop : this.vanillaDropManager.getVanillaDrops().stream().filter((var1x) -> {
               return var1x instanceof BlockVanillaDrop && (!((BlockVanillaDrop)var1x).doIgnoreDrugPlants() || !var1.getBlock().hasMetadata("ultimatedrugs-plant")) && ((BlockVanillaDrop)var1x).getBlocks().stream().anyMatch((var1xx) -> {
                  return var1xx.doIgnoreData() && var1.getBlock().getType() == var1xx.getBlockData().getMaterial() || var1xx.getBlockData().matches(var1.getBlock().getBlockData());
               });
            }).collect(Collectors.toList())) {
               BlockVanillaDrop blockvanilladrop = (BlockVanillaDrop)vanilladrop;
               if (blockvanilladrop.getPermission() == null || var1.getPlayer().hasPermission(blockvanilladrop.getPermission())) {
                  if (blockvanilladrop.doOverrideDrops()) {
                     var1.setDropItems(false);
                  }

                  if (blockvanilladrop.doOverrideExp()) {
                     var1.setExpToDrop(0);
                  }

                  if (blockvanilladrop.getExperience() != 0) {
                     var1.setExpToDrop(var1.getExpToDrop() + blockvanilladrop.getExperience());
                  }

                  this.main.getServer().getPluginManager().callEvent(new VanillaDropDropEvent(blockvanilladrop, var1.getBlock().getLocation(), blockvanilladrop.getDrops(), new Object[0]));
               }
            }

            if (((Boolean)SettingsHolder.VANILLA_AUTO_REPLANT.getValue()).booleanValue() && var1.getPlayer().hasPermission("ultimatedrugs.vanilla.replant") && var1.getBlock().getBlockData() instanceof Ageable) {
               if (var1.getBlock().hasMetadata("ultimatedrugs-plant") && ((Boolean)SettingsHolder.VANILLA_AUTO_IGNORE_DRUG_PLANTS.getValue()).booleanValue()) {
                  return;
               }

               Collection collection = (Collection)SettingsHolder.VANILLA_AUTO_REPLANT_BLACKLIST.getValue();
               if (!collection.contains(var1.getBlock().getType())) {
                  Ageable ageable = (Ageable)var1.getBlock().getBlockData();
                  if (ageable.getAge() == ageable.getMaximumAge()) {
                     ageable.setAge(0);
                     this.main.getServer().getScheduler().runTaskLater(this.main, () -> {
                        var1.getBlock().setBlockData(ageable);
                     }, 1L);
                  }
               }
            }

         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onBlockPlace(BlockPlaceEvent var1) {
      if (((Boolean)SettingsHolder.VANILLA_STORE_PLAYER_BLOCKS.getValue()).booleanValue() && this.vanillaDropManager.isVanillaBlockDropMaterial(var1.getBlock().getType())) {
         this.vanillaDropManager.addPlayerPlacedBlockLocation(var1.getBlock().getLocation());
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onBlockPisonExtend(BlockPistonExtendEvent var1) {
      if (((Boolean)SettingsHolder.VANILLA_STORE_PLAYER_BLOCKS.getValue()).booleanValue()) {
         BlockFace blockface = var1.getDirection();

         for(Block block : var1.getBlocks()) {
            if (this.vanillaDropManager.isPlayerPlacedBlock(block)) {
               this.vanillaDropManager.removePlayerPlacedBlockLocation(block.getLocation());
               this.vanillaDropManager.addPlayerPlacedBlockLocation(block.getRelative(blockface).getLocation());
            }
         }
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onBlockPistonRetract(BlockPistonRetractEvent var1) {
      if (((Boolean)SettingsHolder.VANILLA_STORE_PLAYER_BLOCKS.getValue()).booleanValue()) {
         BlockFace blockface = var1.getDirection();

         for(Block block : var1.getBlocks()) {
            if (this.vanillaDropManager.isPlayerPlacedBlock(block)) {
               this.vanillaDropManager.removePlayerPlacedBlockLocation(block.getLocation());
               this.vanillaDropManager.addPlayerPlacedBlockLocation(block.getRelative(blockface).getLocation());
            }
         }
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onBlockBurn(BlockBurnEvent var1) {
      if (((Boolean)SettingsHolder.VANILLA_STORE_PLAYER_BLOCKS.getValue()).booleanValue() && this.vanillaDropManager.isPlayerPlacedBlock(var1.getBlock())) {
         this.vanillaDropManager.removePlayerPlacedBlockLocation(var1.getBlock().getLocation());
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onBlockExplode(BlockExplodeEvent var1) {
      if (((Boolean)SettingsHolder.VANILLA_STORE_PLAYER_BLOCKS.getValue()).booleanValue()) {
         for(Block block : var1.blockList()) {
            if (this.vanillaDropManager.isPlayerPlacedBlock(block)) {
               this.vanillaDropManager.removePlayerPlacedBlockLocation(block.getLocation());
            }
         }
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onEntityExplode(EntityExplodeEvent var1) {
      if (((Boolean)SettingsHolder.VANILLA_STORE_PLAYER_BLOCKS.getValue()).booleanValue()) {
         for(Block block : var1.blockList()) {
            if (this.vanillaDropManager.isPlayerPlacedBlock(block)) {
               this.vanillaDropManager.removePlayerPlacedBlockLocation(block.getLocation());
            }
         }
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onBlockFade(BlockFadeEvent var1) {
      if (((Boolean)SettingsHolder.VANILLA_STORE_PLAYER_BLOCKS.getValue()).booleanValue() && this.vanillaDropManager.isPlayerPlacedBlock(var1.getBlock())) {
         this.vanillaDropManager.removePlayerPlacedBlockLocation(var1.getBlock().getLocation());
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onLeavesDecay(LeavesDecayEvent var1) {
      if (((Boolean)SettingsHolder.VANILLA_STORE_PLAYER_BLOCKS.getValue()).booleanValue() && this.vanillaDropManager.isPlayerPlacedBlock(var1.getBlock())) {
         this.vanillaDropManager.removePlayerPlacedBlockLocation(var1.getBlock().getLocation());
      }

   }

   @EventHandler
   public void onEntityDeath(EntityDeathEvent var1) {
      Player player = var1.getEntity().getKiller();
      if (player != null) {
         for(VanillaDrop vanilladrop1 : this.vanillaDropManager.getVanillaDrops().stream().filter((var1x) -> {
            return var1x instanceof MobVanillaDrop && ((MobVanillaDrop)var1x).getEntityTypes().contains(var1.getEntityType());
         }).collect(Collectors.toList())) {
            MobVanillaDrop mobvanilladrop = (MobVanillaDrop)vanilladrop1;
            if (mobvanilladrop.getPermission() == null || player.hasPermission(mobvanilladrop.getPermission())) {
               if (mobvanilladrop.doOverrideDrops()) {
                  var1.getDrops().clear();
               }

               if (mobvanilladrop.doOverrideExp()) {
                  var1.setDroppedExp(0);
               }

               if (mobvanilladrop.getExperience() != 0) {
                  var1.setDroppedExp(var1.getDroppedExp() + mobvanilladrop.getExperience());
               }

               this.main.getServer().getPluginManager().callEvent(new VanillaDropDropEvent(mobvanilladrop, var1.getEntity().getLocation(), mobvanilladrop.getDrops(), new Object[0]));
            }
         }
      }

   }

   @EventHandler
   public void onPlayerFish(PlayerFishEvent var1) {
      if ((var1.getState() == State.CAUGHT_FISH || var1.getState() == State.CAUGHT_ENTITY) && var1.getCaught() instanceof Item) {
         Item item = (Item)var1.getCaught();
         ItemStack itemstack = item.getItemStack();

         for(FishingVanillaDrop fishingvanilladrop : this.vanillaDropManager.getVanillaDrops().stream().filter((var0) -> {
            return var0 instanceof FishingVanillaDrop;
         }).map((var0) -> {
            return (FishingVanillaDrop)var0;
         }).collect(Collectors.toList())) {
            if (fishingvanilladrop.getFishTypes().contains(itemstack.getType()) && (fishingvanilladrop.getPermission() == null || var1.getPlayer().hasPermission(fishingvanilladrop.getPermission()))) {
               if (fishingvanilladrop.doOverrideDrops() && item.isValid()) {
                  item.remove();
               }

               if (fishingvanilladrop.doOverrideExp()) {
                  var1.setExpToDrop(0);
               }

               if (fishingvanilladrop.getExperience() != 0) {
                  var1.setExpToDrop(var1.getExpToDrop() + fishingvanilladrop.getExperience());
               }

               Vector vector = var1.getPlayer().getLocation().toVector().subtract(item.getLocation().toVector()).normalize().multiply(new Vector(0.5D, 1.0D, 0.5D));
               this.main.getServer().getPluginManager().callEvent(new VanillaDropDropEvent(fishingvanilladrop, item.getLocation(), fishingvanilladrop.getDrops(), new Object[]{vector}));
            }
         }
      }

   }
}
