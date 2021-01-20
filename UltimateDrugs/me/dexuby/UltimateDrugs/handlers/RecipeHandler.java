package me.dexuby.UltimateDrugs.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import me.dexuby.UltimateDrugs.Main;
import me.dexuby.UltimateDrugs.drugs.DrugBrewingRecipe;
import me.dexuby.UltimateDrugs.drugs.DrugCraftingRecipe;
import me.dexuby.UltimateDrugs.drugs.DrugRecipe;
import me.dexuby.UltimateDrugs.managers.RecipeManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class RecipeHandler implements Listener {
   private Main main;
   private RecipeManager recipeManager;
   private Map<Block, BukkitTask> activeBrewingStands = new HashMap<Block, BukkitTask>();
   private Set<UUID> blockedPlayers = new HashSet<UUID>();

   public RecipeHandler(Main var1) {
      this.main = var1;
      this.recipeManager = var1.getRecipeManager();
   }

   @EventHandler
   public void onPrepareItemCraft(PrepareItemCraftEvent var1) {
      if (var1.getRecipe() instanceof ShapelessRecipe) {
         ShapelessRecipe shapelessrecipe = (ShapelessRecipe)var1.getRecipe();
         DrugRecipe drugrecipe = this.recipeManager.getDrugRecipeById(shapelessrecipe.getKey().getKey());
         if (drugrecipe instanceof DrugCraftingRecipe) {
            List<DrugCraftingRecipe> list = this.main.getRecipeManager().getAllShapelessCraftingRecipes();
            ArrayList arraylist = new ArrayList();

            for(ItemStack itemstack : var1.getInventory().getMatrix()) {
               if (itemstack != null && itemstack.getType() != Material.AIR) {
                  arraylist.add(itemstack);
               }
            }

            for(DrugCraftingRecipe drugcraftingrecipe : list) {
               ArrayList arraylist1 = new ArrayList(drugcraftingrecipe.getIngredients());
               ArrayList arraylist2 = new ArrayList(arraylist);
               Iterator iterator = arraylist1.iterator();

               label48:
               while(iterator.hasNext()) {
                  ItemStack itemstack1 = (ItemStack)iterator.next();
                  Iterator iterator1 = arraylist2.iterator();

                  while(true) {
                     if (!iterator1.hasNext()) {
                        continue label48;
                     }

                     ItemStack itemstack2 = (ItemStack)iterator1.next();
                     if (itemstack1.isSimilar(itemstack2)) {
                        break;
                     }
                  }

                  iterator1.remove();
                  iterator.remove();
               }

               if (arraylist1.size() == 0 && arraylist2.size() == 0) {
                  var1.getInventory().setResult(drugcraftingrecipe.getRecipe().getResult());
                  return;
               }
            }

            var1.getInventory().setResult((ItemStack)null);
         }
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onInventoryClick(InventoryClickEvent var1) {
      Inventory inventory = var1.getClickedInventory();
      if (inventory != null && inventory.getType() == InventoryType.BREWING) {
         InventoryHolder inventoryholder = inventory.getHolder();
         InventoryView inventoryview = var1.getView();
         if (inventoryholder instanceof BrewingStand) {
            if (this.main.getDrugManager().getDrugs().stream().noneMatch((var0) -> {
               return var0.getBrewingRecipes().size() > 0;
            })) {
               return;
            }

            var1.setCancelled(true);
            BrewingStand brewingstand = (BrewingStand)inventoryholder;
            Player player = (Player)var1.getWhoClicked();
            if (this.blockedPlayers.contains(player.getUniqueId())) {
               return;
            }

            int i = var1.getSlot();
            ItemStack itemstack = var1.getCursor();
            ItemStack itemstack1 = inventory.getItem(i);
            if (itemstack != null && itemstack.getType() != Material.AIR) {
               this.blockedPlayers.add(player.getUniqueId());
               inventoryview.setCursor((ItemStack)null);
               if (itemstack1 != null && itemstack1.getType() != Material.AIR) {
                  this.main.getServer().getScheduler().runTaskLater(this.main, () -> {
                     ItemStack itemstack6 = itemstack.clone();
                     inventoryview.setCursor(itemstack1);
                     inventory.setItem(i, itemstack6);
                     player.updateInventory();
                     this.blockedPlayers.remove(player.getUniqueId());
                  }, 1L);
               } else {
                  this.main.getServer().getScheduler().runTaskLater(this.main, () -> {
                     inventory.setItem(i, itemstack);
                     inventoryview.setCursor((ItemStack)null);
                     player.updateInventory();
                     this.blockedPlayers.remove(player.getUniqueId());
                  }, 1L);
               }
            } else if (itemstack1 != null && itemstack1.getType() != Material.AIR) {
               this.blockedPlayers.add(player.getUniqueId());
               ItemStack itemstack2 = itemstack1.clone();
               inventory.setItem(i, (ItemStack)null);
               this.main.getServer().getScheduler().runTaskLater(this.main, () -> {
                  inventoryview.setCursor(itemstack2);
                  player.updateInventory();
                  this.blockedPlayers.remove(player.getUniqueId());
               }, 1L);
            }

            if (i == 3) {
               Block block = brewingstand.getBlock();
               if ((itemstack != null && itemstack.getType() != Material.AIR || itemstack1 != null && itemstack1.getType() != Material.AIR) && itemstack != null && itemstack.getType() != Material.AIR && this.activeBrewingStands.containsKey(block)) {
                  BukkitTask bukkittask = this.activeBrewingStands.get(brewingstand.getBlock());
                  this.activeBrewingStands.remove(block);
                  bukkittask.cancel();
               }

               if (itemstack != null && itemstack.getType() != Material.AIR) {
                  DrugBrewingRecipe drugbrewingrecipe = this.getDrugBrewingRecipeByInput(itemstack);
                  if (drugbrewingrecipe != null) {
                     this.checkAndStartBrewing(drugbrewingrecipe, player, inventory, itemstack, brewingstand, block, true);
                  }
               }
            }
         }

      }
   }

   private void checkAndStartBrewing(final DrugBrewingRecipe var1, final Player var2, final Inventory var3, ItemStack var4, final BrewingStand var5, final Block var6, boolean var7) {
      List list = var1.getResults();

      for(int j = 0; j < list.size() && j < 3; ++j) {
         ItemStack itemstack3 = var3.getItem(j);
         if (itemstack3 != null && itemstack3.getType() != Material.AIR && !itemstack3.isSimilar((ItemStack)list.get(j))) {
            return;
         }
      }

      ItemStack itemstack4 = var3.getItem(4);
      if (itemstack4 != null || var1.getFuel() == null) {
         if (var1.getFuel() != null) {
            if (!var1.getFuel().isSimilar(itemstack4)) {
               return;
            }

            if (itemstack4.getAmount() > 1) {
               itemstack4.setAmount(itemstack4.getAmount() - 1);
            } else {
               var3.setItem(4, (ItemStack)null);
            }
         }

         if (var7) {
            if (var4.getAmount() > 1) {
               var4.setAmount(var4.getAmount() - 1);
            } else {
               this.main.getServer().getScheduler().runTaskLater(this.main, () -> {
                  var3.setItem(3, (ItemStack)null);
                  var2.updateInventory();
               }, 1L);
            }
         } else {
            ItemStack itemstack5 = var3.getItem(3);
            if (itemstack5 == null || itemstack5.getType() == Material.AIR) {
               return;
            }

            if (itemstack5.getAmount() > 1) {
               itemstack5.setAmount(itemstack5.getAmount() - 1);
            } else {
               var3.setItem(3, (ItemStack)null);
            }
         }

         final int[] aint = new int[]{400};
         BukkitTask bukkittask1 = (new BukkitRunnable() {
            public void run() {
               if (var6.getState() instanceof BrewingStand) {
                  BrewingStand brewingstand1 = (BrewingStand)var6.getState();
                  brewingstand1.setBrewingTime(aint[0]);
                  brewingstand1.update();
                  if (aint[0] <= 0) {
                     List list1 = var1.getResults();

                     for(int k = 0; k < list1.size() && k < 3; ++k) {
                        ItemStack itemstack6 = var3.getItem(k);
                        if (itemstack6 != null && itemstack6.getType() != Material.AIR) {
                           if (itemstack6.isSimilar((ItemStack)list1.get(k))) {
                              if (itemstack6.getAmount() < itemstack6.getType().getMaxStackSize()) {
                                 itemstack6.setAmount(itemstack6.getAmount() + 1);
                              } else {
                                 this.cancel();
                              }
                           } else {
                              this.cancel();
                           }
                        } else {
                           var3.setItem(k, (ItemStack)list1.get(k));
                        }
                     }

                     ItemStack itemstack7 = var3.getItem(3);
                     if (itemstack7 != null && itemstack7.getType() != Material.AIR) {
                        RecipeHandler.this.checkAndStartBrewing(var1, var2, var3, (ItemStack)null, var5, var6, false);
                     }

                     this.cancel();
                  }

                  aint[0] -= var1.getSpeed();
               } else {
                  RecipeHandler.this.activeBrewingStands.remove(var6);
                  this.cancel();
               }

            }
         }).runTaskTimer(this.main, 1L, 1L);
         this.activeBrewingStands.put(var5.getBlock(), bukkittask1);
      }
   }

   private DrugBrewingRecipe getDrugBrewingRecipeByInput(ItemStack var1) {
      return (DrugBrewingRecipe)this.recipeManager.getRegisteredDrugRecipes().stream().filter((var1x) -> {
         return var1x instanceof DrugBrewingRecipe && ((DrugBrewingRecipe)var1x).getInput().isSimilar(var1);
      }).map((var0) -> {
         return (DrugBrewingRecipe)var0;
      }).findFirst().orElse(null);
   }

   @EventHandler
   public void onBlockBreak(BlockBreakEvent var1) {
      Block block1 = var1.getBlock();
      if (this.activeBrewingStands.containsKey(block1)) {
         BukkitTask bukkittask2 = this.activeBrewingStands.get(block1);
         this.activeBrewingStands.remove(block1);
         bukkittask2.cancel();
      }

   }
}
