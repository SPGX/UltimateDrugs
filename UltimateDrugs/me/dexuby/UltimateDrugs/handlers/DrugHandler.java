package me.dexuby.UltimateDrugs.handlers;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.dexuby.UltimateDrugs.Main;
import me.dexuby.UltimateDrugs.api.DrugConsumeEvent;
import me.dexuby.UltimateDrugs.drugs.ActiveDrugEffect;
import me.dexuby.UltimateDrugs.drugs.ConsumeConditions;
import me.dexuby.UltimateDrugs.drugs.ConsumeRequirementItem;
import me.dexuby.UltimateDrugs.drugs.Drug;
import me.dexuby.UltimateDrugs.drugs.ExchangeItem;
import me.dexuby.UltimateDrugs.drugs.actions.ConsumeAction;
import me.dexuby.UltimateDrugs.drugs.actions.TimedConsumeAction;
import me.dexuby.UltimateDrugs.drugs.chatslur.ChatSlurConverter;
import me.dexuby.UltimateDrugs.drugs.chatslur.ChatSlurType;
import me.dexuby.UltimateDrugs.managers.ConfigManager;
import me.dexuby.UltimateDrugs.managers.DrugManager;
import me.dexuby.UltimateDrugs.utils.ItemBuilder;
import me.dexuby.UltimateDrugs.utils.MaterialUtils;
import me.dexuby.UltimateDrugs.utils.WGUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DrugHandler implements Listener {
   private Main main;
   private DrugManager drugManager;
   private ConfigManager configManager;

   public DrugHandler(Main var1) {
      this.main = var1;
      this.drugManager = var1.getDrugManager();
      this.configManager = var1.getConfigManager();
   }

   @EventHandler
   public void onDrugConsume(DrugConsumeEvent var1) {
      if (!var1.isCancelled()) {
         final Player player = var1.getPlayer();
         final Drug drug = var1.getDrug();
         ConsumeConditions consumeconditions = drug.getConsumeConditions();
         boolean flag = false;
         if (this.drugManager.isPlayerOnDrug(player, drug)) {
            if (!drug.doResetOnConsume()) {
               player.sendMessage(this.configManager.getLangString("already-on-drug"));
               var1.setCancelled(true);
               return;
            }

            flag = true;
         }

         if ((!consumeconditions.isSneakRequired() || player.isSneaking()) && !consumeconditions.getDisabledWorldList().contains(player.getWorld().getName()) && (consumeconditions.getDisabledRegionList().size() <= 0 || !this.main.getServer().getPluginManager().isPluginEnabled("WorldGuard") || !this.main.getServer().getPluginManager().isPluginEnabled("WorldEdit") || !WGUtils.isInDisabledRegion(player, drug))) {
            if (consumeconditions.getMaxCurrentHealth() > 0.0D && player.getHealth() > consumeconditions.getMaxCurrentHealth()) {
               var1.setCancelled(true);
            } else if (consumeconditions.getMaxCurrentFoodLevel() > 0.0D && (double)player.getFoodLevel() > consumeconditions.getMaxCurrentFoodLevel()) {
               var1.setCancelled(true);
            } else {
               boolean flag1 = true;

               for(ConsumeRequirementItem consumerequirementitem : consumeconditions.getRequiredItemList()) {
                  if (!consumerequirementitem.doDamageOnConsume()) {
                     if (!player.getInventory().containsAtLeast(consumerequirementitem.getItemStack(), consumerequirementitem.getItemStack().getAmount())) {
                        flag1 = false;
                        break;
                     }
                  } else {
                     boolean flag2 = false;
                     ItemStack[] aitemstack = player.getInventory().getContents();

                     for(int i = 0; i < aitemstack.length - 1; ++i) {
                        if (aitemstack[i] != null && (new ItemBuilder(aitemstack[i].clone())).damage(0).build().isSimilar((new ItemBuilder(consumerequirementitem.getItemStack().clone())).damage(0).build())) {
                           flag2 = true;
                        }
                     }

                     if (!flag2) {
                        flag1 = false;
                        break;
                     }
                  }
               }

               if (!flag1) {
                  var1.setCancelled(true);
               } else {
                  final SecureRandom securerandom = new SecureRandom();
                  if (drug.doRemoveOnConsume() || drug.getConsumeExchangeItems().size() > 0) {
                     ItemStack itemstack2 = player.getInventory().getItemInMainHand();
                     if (player.getGameMode() != GameMode.CREATIVE) {
                        if (itemstack2.getAmount() > 1) {
                           itemstack2.setAmount(itemstack2.getAmount() - 1);
                        } else {
                           player.getInventory().setItemInMainHand((ItemStack)null);
                        }
                     }

                     if (drug.getConsumeExchangeItems().size() > 0) {
                        int l = 0;
                        int i1 = drug.getExchangeItemLimit();
                        boolean flag3 = false;

                        for(ExchangeItem exchangeitem : drug.getConsumeExchangeItems()) {
                           if (i1 > 0 && l >= i1) {
                              break;
                           }

                           if (flag3 && exchangeitem.doBreakSuccessChain()) {
                              flag3 = false;
                           } else if (securerandom.nextDouble() <= exchangeitem.getChance() / 100.0D) {
                              ++l;
                              flag3 = true;
                              ItemStack itemstack = exchangeitem.getItem().clone();
                              if (exchangeitem.doForceDrop()) {
                                 player.getWorld().dropItemNaturally(player.getLocation(), itemstack);
                              } else {
                                 int j = exchangeitem.getPreferredSlot();
                                 if (j >= 0) {
                                    ItemStack itemstack1 = player.getInventory().getItem(j);
                                    if (itemstack1 == null || itemstack1.isSimilar(itemstack)) {
                                       if (itemstack1 == null) {
                                          player.getInventory().setItem(j, itemstack);
                                          continue;
                                       }

                                       if (itemstack1.getAmount() + itemstack.getAmount() <= itemstack1.getMaxStackSize()) {
                                          itemstack1.setAmount(itemstack1.getAmount() + itemstack.getAmount());
                                          continue;
                                       }

                                       if (itemstack1.getAmount() < itemstack1.getMaxStackSize()) {
                                          int k = itemstack1.getMaxStackSize() - itemstack1.getAmount();
                                          itemstack1.setAmount(itemstack1.getMaxStackSize());
                                          itemstack.setAmount(itemstack.getAmount() - k);
                                       }
                                    }
                                 }

                                 if (MaterialUtils.isAir(player.getInventory().getItemInMainHand().getType())) {
                                    player.getInventory().setItemInMainHand(itemstack);
                                 } else if (player.getInventory().firstEmpty() != -1) {
                                    player.getInventory().addItem(new ItemStack[]{itemstack});
                                 } else {
                                    player.getWorld().dropItemNaturally(player.getLocation(), itemstack);
                                 }
                              }
                           } else {
                              flag3 = false;
                           }
                        }
                     }
                  }

                  label213:
                  for(ConsumeRequirementItem consumerequirementitem1 : consumeconditions.getRequiredItemList()) {
                     ItemStack[] aitemstack1 = player.getInventory().getContents();

					HashMap<Integer, ItemStack> hashmap = null;
					for(int k1 = 0; k1 < aitemstack1.length - 1; ++k1) {
                        if (aitemstack1[k1] != null) {
                           if (consumerequirementitem1.doDamageOnConsume()) {
                              if ((new ItemBuilder(aitemstack1[k1].clone())).damage(0).build().isSimilar((new ItemBuilder(consumerequirementitem1.getItemStack().clone())).damage(0).build())) {
                                 hashmap.put(Integer.valueOf(k1), aitemstack1[k1]);
                              }
                           } else if (aitemstack1[k1].isSimilar(consumerequirementitem1.getItemStack())) {
                              hashmap.put(Integer.valueOf(k1), aitemstack1[k1]);
                           }
                        }
                     }

                     if (consumerequirementitem1.doRemoveOnConsume()) {
                        int l1 = 0;

                        for(Entry entry1 : hashmap.entrySet()) {
                           if (((ItemStack)entry1.getValue()).getAmount() + l1 >= consumerequirementitem1.getItemStack().getAmount()) {
                              if (((ItemStack)entry1.getValue()).getAmount() + l1 == consumerequirementitem1.getItemStack().getAmount()) {
                                 player.getInventory().setItem(((Integer)entry1.getKey()).intValue(), (ItemStack)null);
                              } else {
                                 ItemStack itemstack4 = player.getInventory().getItem(((Integer)entry1.getKey()).intValue());
                                 itemstack4.setAmount(itemstack4.getAmount() - (consumerequirementitem1.getItemStack().getAmount() - l1));
                              }
                              break;
                           }

                           l1 += ((ItemStack)entry1.getValue()).getAmount();
                           player.getInventory().setItem(((Integer)entry1.getKey()).intValue(), (ItemStack)null);
                        }
                     }

                     if (consumerequirementitem1.doDamageOnConsume()) {
                        Iterator iterator = hashmap.entrySet().iterator();

                        Entry entry;
                        ItemStack itemstack3;
                        while(true) {
                           if (!iterator.hasNext()) {
                              continue label213;
                           }

                           entry = (Entry)iterator.next();
                           itemstack3 = (ItemStack)entry.getValue();
                           if (itemstack3.getItemMeta() instanceof Damageable) {
                              break;
                           }
                        }

                        Damageable damageable = (Damageable)itemstack3.getItemMeta();
                        if (damageable.getDamage() >= itemstack3.getType().getMaxDurability() - 1) {
                           player.getInventory().setItem(((Integer)entry.getKey()).intValue(), (ItemStack)null);
                        } else {
                           damageable.setDamage(damageable.getDamage() + 1);
                           itemstack3.setItemMeta((ItemMeta)damageable);
                        }
                     }
                  }

                  if (flag) {
                     ActiveDrugEffect activedrugeffect = this.drugManager.getActiveDrugEffect(player, drug);
                     if (activedrugeffect != null) {
                        activedrugeffect.getTask().cancel();
                        this.drugManager.removeActiveDrugEffect(player, drug);
                        this.drugManager.forceEndTimedConsumeActions(player, drug.getId());
                     }
                  }

                  final ArrayList arraylist = new ArrayList(drug.getConsumeActions());
                  if (arraylist.size() > 0) {
                     for(ConsumeAction consumeaction1 : drug.getConsumeActions()) {
                        int j1 = consumeaction1.getRepetitions();
                        int i2 = consumeaction1.getRepetitionTickDelay() > 0 ? consumeaction1.getRepetitionTickDelay() : 1;
                        if (j1 > 0) {
                           int k2 = consumeaction1.getDelay() + i2;

                           for(int l2 = 0; l2 < j1; ++l2) {
                              if (k2 <= drug.getEffectDuration()) {
                                 ConsumeAction consumeaction3 = consumeaction1.clone();
                                 consumeaction3.setDelay(k2);
                                 arraylist.add(consumeaction3);
                                 k2 += i2;
                              }
                           }
                        } else if (j1 == -1) {
                           for(int j2 = consumeaction1.getDelay() + i2; j2 <= drug.getEffectDuration(); j2 += i2) {
                              ConsumeAction consumeaction2 = consumeaction1.clone();
                              consumeaction2.setDelay(j2);
                              arraylist.add(consumeaction2);
                           }
                        }
                     }
                  }

                  final ConsumeAction consumeaction = (ConsumeAction)arraylist.stream().max(Comparator.comparing(ConsumeAction::getDelay)).orElse((Object)null);
                  final int[] aint = new int[]{0};
                  BukkitTask bukkitTask = (new BukkitRunnable() {
                	  public void run() {
                		  if (!drug.doPauseWhileOffline() || (drug.doPauseWhileOffline() && player.isOnline())) {
                			  ArrayList<ConsumeAction> arrayList = new ArrayList();
                			  for (ConsumeAction consumeAction : arrayList.stream().filter(param1ConsumeAction -> (param1ConsumeAction.getDelay() == aint[0])).collect(Collectors.toList())) {
                			  if (securerandom.nextDouble() <= consumeAction.getChance() / 100.0D) arrayList.add(consumeAction);  }
                			  if (arrayList.size() > 0) DrugHandler.this.main.getServer().getScheduler().runTask(DrugHandler.this.main, () -> arrayList.forEach(null)); 
                			  if (consumeaction == null || aint[0] >= consumeaction.getDelay()) cancel();  aint[0] = aint[0] + 1; }  } }).runTaskTimerAsynchronously(this.main, 0L, 1L);
                  
                  this.main.getDrugManager().addActiveDrugEffect(player, drug, bukkitTask, Long.valueOf(System.currentTimeMillis() + (drug.getEffectDuration() / 20 * 1000)));
                  }
            }
         } else {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onAsyncPlayerChat(AsyncPlayerChatEvent var1) {
	      Player player = var1.getPlayer();
	      if (this.drugManager.isPlayerOnDrugs(player)) {
	         String s = var1.getMessage();

	         for(ActiveDrugEffect activedrugeffect : this.drugManager.getActiveDrugEffects(player).stream().filter((var0) -> {
	            return var0.getDrug().getChatSlurType() != ChatSlurType.NONE;
	         }).collect(Collectors.toList())) {
	            s = (new ChatSlurConverter(s)).setSlurStrategy(activedrugeffect.getDrug().getChatSlurType().getChatSlurStrategy()).convert();
	         }

	         var1.setMessage(s);
	      }

	   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent var1) {
      Player player1 = var1.getPlayer();

      for(ActiveDrugEffect activedrugeffect1 : this.drugManager.getActiveDrugEffects(player1)) {
         if (activedrugeffect1.getDrug().doRemoveOnDisconnect()) {
            activedrugeffect1.getTask().cancel();
            this.drugManager.removeActiveDrugEffect(player1, activedrugeffect1.getDrug());
         }
      }

      this.drugManager.forceEndTimedConsumeActions(player1.getUniqueId());
   }

   @EventHandler
   public void onPlayerDeath(PlayerDeathEvent var1) {
      Player player1 = var1.getEntity();

      for(ActiveDrugEffect activedrugeffect1 : this.drugManager.getActiveDrugEffects(player1)) {
         if (activedrugeffect1.getDrug().doRemoveOnDeath()) {
            activedrugeffect1.getTask().cancel();
            this.drugManager.removeActiveDrugEffect(player1, activedrugeffect1.getDrug());
         }
      }

      this.drugManager.forceEndTimedConsumeActions(player1.getUniqueId());
   }
}
