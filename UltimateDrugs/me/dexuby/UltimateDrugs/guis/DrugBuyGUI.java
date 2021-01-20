// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.guis;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Material;
import java.util.AbstractMap;
import me.dexuby.UltimateDrugs.drugs.DrugRelatedGood;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import java.util.Map;
import me.dexuby.UltimateDrugs.utils.TextUtils;
import me.dexuby.UltimateDrugs.utils.ItemBuilder;
import java.util.HashMap;
import me.dexuby.UltimateDrugs.drugs.Drug;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Inventory;
import me.dexuby.UltimateDrugs.managers.ConfigManager;
import me.dexuby.UltimateDrugs.Main;

public class DrugBuyGUI extends GUI
{
    private Main main;
    private ConfigManager configManager;
    
    public DrugBuyGUI(final Main main) {
        super("drug-buy", true);
        this.main = main;
        this.configManager = main.getConfigManager();
    }
    
    @Override
    public Inventory getInventory() {
        return this.main.getServer().createInventory((InventoryHolder)new GUIHolder(this), (int)Math.min(Math.round(Math.ceil((this.main.getDrugManager().getBuyableDrugs().size() + (int)this.main.getDrugManager().getDrugRelatedGoods().stream().filter(drugRelatedGood -> drugRelatedGood.getBuyPrice() > 0.0).count()) / 9.0) * 9.0), 54L), this.main.getConfigManager().getLangString("drug-buy-gui-title"));
    }
    
    @Override
    public void open(final Player player, final Object... array) {
        final int n = (int)((array.length > 0) ? array[0] : 0);
        final Inventory inventory = this.getInventory();
        List<ItemStack> list = new ArrayList<ItemStack>();
        for (final Drug drug : this.main.getDrugManager().getDrugs()) {
            if (drug.getBuyPrice() > 0.0) {
                final HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("%buy_price%", Double.toString(drug.getBuyPrice()));
                hashMap.put("%buy_price_16%", Double.toString(drug.getBuyPrice() * 16.0));
                list.add(ReflectionUtils.setItemStackStringTag(new ItemBuilder(drug.getDrugItemStack()).appendLore(TextUtils.replaceVariables(this.configManager.getLangStringList("buy-drug-lore"), hashMap)).build(), "drug-id", drug.getId()));
            }
            for (final DrugRelatedGood drugRelatedGood : drug.getDrugRelatedGoods()) {
                if (drugRelatedGood.getBuyPrice() > 0.0) {
                    final HashMap<String, String> hashMap2 = new HashMap<String, String>();
                    hashMap2.put("%buy_price%", Double.toString(drugRelatedGood.getBuyPrice()));
                    hashMap2.put("%buy_price_16%", Double.toString(drugRelatedGood.getBuyPrice() * 16.0));
                    list.add(
                            ReflectionUtils.setItemStackStringTags((new ItemBuilder(drugRelatedGood
                                .getItemStack()))
                              .appendLore(TextUtils.replaceVariables(this.configManager.getLangStringList("buy-related-good-lore"), hashMap2))
                              .build(), new AbstractMap.SimpleEntry[] { new AbstractMap.SimpleEntry<>("drug-id", drug
                                  .getId()), new AbstractMap.SimpleEntry<>("related-good-id", drugRelatedGood
                                  .getId()) }));
                }
            }
        }
        if (n != 0) {
            list = list.subList(45 * n, list.size());
        }
        final boolean b = list.size() > 45;
        if (b) {
            list = list.subList(0, 45);
        }
        list.forEach(itemStack -> inventory.addItem(new ItemStack[] { itemStack }));
        if (b || n > 0) {
            final ItemStack build = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).displayName("§0").build();
            for (int i = 45; i < 54; ++i) {
                inventory.setItem(i, build);
            }
            if (b) {
                inventory.setItem(inventory.getSize() - 1, new ItemBuilder(Material.ARROW).displayName(this.configManager.getLangString("next-page-item-display-name")).lore(this.configManager.getLangStringList("next-page-item-lore")).setStringTag("next-page", Integer.toString(n + 1)).build());
            }
            if (n > 0) {
                inventory.setItem(inventory.getSize() - 9, new ItemBuilder(Material.ARROW).displayName(this.configManager.getLangString("previous-page-item-display-name")).lore(this.configManager.getLangStringList("previous-page-item-lore")).setStringTag("previous-page", Integer.toString(n - 1)).build());
            }
        }
        player.openInventory(inventory);
    }
    
    @Override
    public void onClick(final InventoryClickEvent inventoryClickEvent) {
        final Player player = (Player)inventoryClickEvent.getWhoClicked();
        if (inventoryClickEvent.getCurrentItem() != null) {
            final String itemStackStringTag = ReflectionUtils.getItemStackStringTag(inventoryClickEvent.getCurrentItem(), "drug-id");
            if (itemStackStringTag != null && !itemStackStringTag.equals("")) {
                final Drug drugById = this.main.getDrugManager().getDrugById(itemStackStringTag);
                if (drugById != null) {
                    final String itemStackStringTag2 = ReflectionUtils.getItemStackStringTag(inventoryClickEvent.getCurrentItem(), "related-good-id");
                    if (itemStackStringTag2 != null && !itemStackStringTag2.equals("")) {
                        final ItemStack itemStack = null;
                        final Player player2 = null;
                        final Drug drug = null;
                        drugById.getDrugRelatedGoods().stream().filter(drugRelatedGood -> drugRelatedGood.getId().equals(itemStackStringTag2)).findFirst().ifPresent(drugRelatedGood2 -> {
                            if (inventoryClickEvent.getClick() == ClickType.SHIFT_LEFT || inventoryClickEvent.getClick() == ClickType.SHIFT_RIGHT) {
                                drugRelatedGood2.getItemStack().clone();
                                itemStack.setAmount(16);
                                this.proceedPurchase(player2, drug, itemStack, drugRelatedGood2.getBuyPrice() * 16.0, false);
                            }
                            else {
                                this.proceedPurchase(player2, drug, drugRelatedGood2.getItemStack().clone(), drugRelatedGood2.getBuyPrice(), false);
                            }
                            return;
                        });
                    }
                    else if (inventoryClickEvent.getClick() == ClickType.SHIFT_LEFT || inventoryClickEvent.getClick() == ClickType.SHIFT_RIGHT) {
                        final ItemStack clone = drugById.getDrugItemStack().clone();
                        clone.setAmount(16);
                        this.proceedPurchase(player, drugById, clone, drugById.getBuyPrice() * 16.0, true);
                    }
                    else {
                        this.proceedPurchase(player, drugById, drugById.getDrugItemStack().clone(), drugById.getBuyPrice(), true);
                    }
                }
            }
            if (inventoryClickEvent.getSlot() == inventoryClickEvent.getInventory().getSize() - 1) {
                final ItemStack currentItem = inventoryClickEvent.getCurrentItem();
                if (currentItem.getType() == Material.ARROW) {
                    final String itemStackStringTag3 = ReflectionUtils.getItemStackStringTag(currentItem, "next-page");
                    if (itemStackStringTag3 != null) {
                        final int int1 = Integer.parseInt(itemStackStringTag3);
                        player.closeInventory();
                        this.open(player, int1);
                    }
                }
            }
            else if (inventoryClickEvent.getSlot() == inventoryClickEvent.getInventory().getSize() - 9) {
                final ItemStack currentItem2 = inventoryClickEvent.getCurrentItem();
                if (currentItem2.getType() == Material.ARROW) {
                    final String itemStackStringTag4 = ReflectionUtils.getItemStackStringTag(currentItem2, "previous-page");
                    if (itemStackStringTag4 != null) {
                        final int int2 = Integer.parseInt(itemStackStringTag4);
                        player.closeInventory();
                        this.open(player, int2);
                    }
                }
            }
        }
    }
    
    private void proceedPurchase(final Player player, final Drug drug, final ItemStack itemStack, double d, final boolean b) {
        d = Math.round(d * 100.0) / 100.0;
        final Economy economy = this.main.getEconomy();
        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(this.configManager.getLangString("inventory-full"));
        }
        else if (economy.has((OfflinePlayer)player, d)) {
            economy.withdrawPlayer((OfflinePlayer)player, d);
            player.getInventory().addItem(new ItemStack[] { itemStack });
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("%drug_id%", drug.getId());
            hashMap.put("%buy_price%", Double.toString(d));
            if (itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName()) {
                hashMap.put("%item_display_name%", itemStack.getItemMeta().getDisplayName());
            }
            if (b) {
                player.sendMessage(TextUtils.replaceVariables(this.configManager.getLangString("drug-purchased"), hashMap));
            }
            else {
                player.sendMessage(TextUtils.replaceVariables(this.configManager.getLangString("drug-good-purchased"), hashMap));
            }
        }
        else {
            player.sendMessage(this.configManager.getLangString("not-enough-money"));
        }
    }
}
