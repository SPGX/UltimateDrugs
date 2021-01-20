// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.guis;

import java.util.ArrayList;
import java.util.Collection;
import me.dexuby.UltimateDrugs.managers.DrugBoosterManager;
import me.dexuby.UltimateDrugs.drugs.DrugRelatedGood;
import me.dexuby.UltimateDrugs.drugs.Drug;
import org.bukkit.event.inventory.InventoryCloseEvent;
import java.util.Iterator;
import java.util.Map;
import me.dexuby.UltimateDrugs.utils.TextUtils;
import org.bukkit.OfflinePlayer;
import java.util.HashMap;
import me.dexuby.UltimateDrugs.utils.Pair;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import me.dexuby.UltimateDrugs.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Inventory;
import me.dexuby.UltimateDrugs.managers.DrugManager;
import me.dexuby.UltimateDrugs.managers.ConfigManager;
import me.dexuby.UltimateDrugs.Main;

public class DrugSellGUI extends GUI
{
    private Main main;
    private ConfigManager configManager;
    private DrugManager drugManager;
    
    public DrugSellGUI(final Main main) {
        super("drug-sell", true);
        this.main = main;
        this.configManager = main.getConfigManager();
        this.drugManager = main.getDrugManager();
    }
    
    @Override
    public Inventory getInventory() {
        final Inventory inventory = this.main.getServer().createInventory((InventoryHolder)new GUIHolder(this), 54, this.main.getConfigManager().getLangString("drug-sell-gui-title"));
        final ItemStack build = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).displayName("§0").build();
        for (int i = 45; i < 54; ++i) {
            inventory.setItem(i, build);
        }
        return inventory;
    }
    
    @Override
    public void open(final Player player, final Object... array) {
        final Inventory inventory = this.getInventory();
        this.updateConfirmButton(inventory, player);
        player.openInventory(inventory);
    }
    
    @Override
    public void onClick(InventoryClickEvent inventoryClickEvent) {
        Player player = (Player)inventoryClickEvent.getWhoClicked();
        if (inventoryClickEvent.getClickedInventory() != null) {
            SellStatus sellStatus;
            if (inventoryClickEvent.getSlot() < 45) {
                if (inventoryClickEvent.getCurrentItem() != null) {
                    if (inventoryClickEvent.getClick() == ClickType.SHIFT_LEFT || inventoryClickEvent.getClick() == ClickType.SHIFT_RIGHT) {
                        ItemStack itemStack = inventoryClickEvent.getCurrentItem().clone();
                        for (int i = 0; i < 45 && player.getInventory().firstEmpty() != -1; ++i) {
                            ItemStack itemStack2 = inventoryClickEvent.getClickedInventory().getItem(i);
                            if (itemStack2 == null || !itemStack.isSimilar(itemStack2)) continue;
                            player.getInventory().addItem(new ItemStack[]{itemStack2});
                            inventoryClickEvent.getClickedInventory().clear(i);
                        }
                    } else {
                        if (player.getInventory().firstEmpty() == -1) {
                            return;
                        }
                        player.getInventory().addItem(new ItemStack[]{inventoryClickEvent.getCurrentItem()});
                        inventoryClickEvent.getClickedInventory().clear(inventoryClickEvent.getSlot());
                    }
                    this.updateConfirmButton(inventoryClickEvent.getClickedInventory(), player);
                }
            } else if (inventoryClickEvent.getSlot() == 49 && (sellStatus = this.getInventorySellStatus(inventoryClickEvent.getClickedInventory(), player)).totalAmount > 0.0) {
                for (Pair pair : sellStatus.items) {
                    inventoryClickEvent.getClickedInventory().clear(((Integer)pair.getKey()).intValue());
                }
                HashMap hashMap = new HashMap();
                hashMap.put("%total_sell_amount%", Double.toString(sellStatus.totalAmount));
                this.main.getEconomy().depositPlayer((OfflinePlayer)player, sellStatus.totalAmount);
                player.sendMessage(TextUtils.replaceVariables(this.configManager.getLangString("drug-sell-confirm"), (Map<String, String>)hashMap));
            }
        }
    }
    
    @Override
    public void onInventoryClick(final InventoryClickEvent inventoryClickEvent) {
        final Player player = (Player)inventoryClickEvent.getWhoClicked();
        if (inventoryClickEvent.getCurrentItem() != null) {
            if (inventoryClickEvent.getClick() == ClickType.SHIFT_LEFT || inventoryClickEvent.getClick() == ClickType.SHIFT_RIGHT) {
                final ItemStack clone = inventoryClickEvent.getCurrentItem().clone();
                for (int n = 0; n <= 40 && inventoryClickEvent.getView().getTopInventory().firstEmpty() != -1; ++n) {
                    final ItemStack item = player.getInventory().getItem(n);
                    if (item != null && clone.isSimilar(item)) {
                        inventoryClickEvent.getView().getTopInventory().addItem(new ItemStack[] { item });
                        player.getInventory().clear(n);
                    }
                }
            }
            else {
                if (inventoryClickEvent.getView().getTopInventory().firstEmpty() == -1) {
                    return;
                }
                inventoryClickEvent.getView().getTopInventory().addItem(new ItemStack[] { inventoryClickEvent.getCurrentItem() });
                player.getInventory().clear(inventoryClickEvent.getSlot());
            }
            this.updateConfirmButton(inventoryClickEvent.getView().getTopInventory(), player);
        }
    }
    
    @Override
    public void onInventoryClose(final InventoryCloseEvent inventoryCloseEvent) {
        final Inventory inventory = inventoryCloseEvent.getInventory();
        final Player player = (Player)inventoryCloseEvent.getPlayer();
        for (int i = 0; i <= 44; ++i) {
            final ItemStack item = inventory.getItem(i);
            if (item != null) {
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(new ItemStack[] { item });
                }
                else {
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                }
            }
            inventory.clear(i);
        }
    }
    
    private void updateConfirmButton(final Inventory inventory, final Player player) {
        final SellStatus inventorySellStatus = this.getInventorySellStatus(inventory, player);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("%total_sell_amount%", Double.toString(inventorySellStatus.totalAmount));
        inventory.setItem(49, new ItemBuilder(Material.LIME_DYE).displayName(TextUtils.replaceVariables(this.configManager.getLangString("drug-sell-confirm-display-name"), hashMap)).lore(TextUtils.replaceVariables(this.configManager.getLangStringList("drug-sell-confirm-lore"), hashMap)).build());
    }
    
    private SellStatus getInventorySellStatus(final Inventory inventory, final Player player) {
        final SellStatus sellStatus = new SellStatus();
        final DrugBoosterManager drugBoosterManager = this.main.getDrugBoosterManager();
        for (int i = 0; i < 45; ++i) {
            final ItemStack item = inventory.getItem(i);
            if (item != null) {
                for (final Drug drug : this.drugManager.getDrugs()) {
                    if (drug.getDrugItemStack().isSimilar(item)) {
                        if (drug.getSellPrice() > 0.0) {
                            sellStatus.addToTotalAmount(Math.round(drug.getSellPrice() * item.getAmount() * drugBoosterManager.getBoosterMultiplier(drug, player) * 100.0) / 100.0);
                            sellStatus.addItemStack(i, item);
                            break;
                        }
                        continue;
                    }
                    else {
                        for (final DrugRelatedGood drugRelatedGood : drug.getDrugRelatedGoods()) {
                            if (drugRelatedGood.getSellPrice() <= 0.0) {
                                continue;
                            }
                            if (drugRelatedGood.getItemStack().isSimilar(item)) {
                                sellStatus.addToTotalAmount(Math.round(drugRelatedGood.getSellPrice() * item.getAmount() * 100.0) / 100.0);
                                sellStatus.addItemStack(i, item);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return sellStatus;
    }
    
    private class SellStatus
    {
        private double totalAmount;
        private Collection<Pair<Integer, ItemStack>> items;
        
        private SellStatus() {
            this.items = new ArrayList<Pair<Integer, ItemStack>>();
        }
        
        private void addToTotalAmount(final double n) {
            this.totalAmount += n;
        }
        
        private void addItemStack(final int i, final ItemStack itemStack) {
            this.items.add(new Pair<Integer, ItemStack>(i, itemStack));
        }
    }
}
