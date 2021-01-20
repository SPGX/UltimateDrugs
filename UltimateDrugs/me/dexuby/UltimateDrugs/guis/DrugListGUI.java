// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.guis;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Iterator;
import me.dexuby.UltimateDrugs.utils.ItemBuilder;
import org.bukkit.Material;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import me.dexuby.UltimateDrugs.utils.TextUtils;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import java.util.HashMap;
import me.dexuby.UltimateDrugs.drugs.Drug;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Inventory;
import me.dexuby.UltimateDrugs.managers.ConfigManager;
import me.dexuby.UltimateDrugs.Main;

public class DrugListGUI extends GUI
{
    private Main main;
    private ConfigManager configManager;
    
    public DrugListGUI(final Main main) {
        super("drug-list", true);
        this.main = main;
        this.configManager = main.getConfigManager();
    }
    
    @Override
    public Inventory getInventory() {
        return this.main.getServer().createInventory((InventoryHolder)new GUIHolder(this), (int)Math.min(Math.round(Math.ceil(this.main.getDrugManager().getDrugs().size() / 9.0) * 9.0), 54L), this.main.getConfigManager().getLangString("drug-list-gui-title"));
    }
    
    @Override
    public void open(final Player player, final Object... array) {
        final int n = (int)((array.length > 0) ? array[0] : 0);
        final Inventory inventory = this.getInventory();
        List<ItemStack> list = new ArrayList<ItemStack>();
        for (final Drug drug : this.main.getDrugManager().getDrugs()) {
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("%drug_unboosted_sell_price%", (drug.getSellPrice() > 0.0) ? Double.toString(drug.getSellPrice()) : this.configManager.getLangString("not-sellable"));
            hashMap.put("%drug_sell_price%", (drug.getSellPrice() > 0.0) ? Double.toString(drug.getSellPrice() * this.main.getDrugBoosterManager().getBoosterMultiplier(drug, player)) : this.configManager.getLangString("not-sellable"));
            hashMap.put("%drug_buy_price%", (drug.getBuyPrice() > 0.0) ? Double.toString(drug.getBuyPrice()) : this.configManager.getLangString("not-buyable"));
            hashMap.put("%drug_booster_multiplier%", Double.toString(this.main.getDrugBoosterManager().getBoosterMultiplier(drug, player)));
            final ItemStack setItemStackStringTag = ReflectionUtils.setItemStackStringTag(drug.getDrugItemStack().clone(), "drug-id", drug.getId());
            if (setItemStackStringTag != null) {
                final List<String> lore = this.main.getDrugBoosterManager().hasActiveBooster(drug) ? TextUtils.replaceVariables(this.configManager.getLangStringList("drug-list-boosted-item-lore"), hashMap) : TextUtils.replaceVariables(this.configManager.getLangStringList("drug-list-unboosted-item-lore"), hashMap);
                final ItemMeta itemMeta = setItemStackStringTag.getItemMeta();
                if (itemMeta.hasLore()) {
                    final List lore2 = itemMeta.getLore();
                    lore2.addAll(lore);
                    itemMeta.setLore(lore2);
                }
                else {
                    itemMeta.setLore((List)lore);
                }
                setItemStackStringTag.setItemMeta(itemMeta);
                list.add(setItemStackStringTag);
            }
        }
        if (n != 0) {
            list = list.subList(45 * n, list.size());
        }
        final boolean b = list.size() > 45;
        if (b) {
            list = list.subList(0, 45);
        }
        int n2 = 0;
        final Iterator<ItemStack> iterator2 = list.iterator();
        while (iterator2.hasNext()) {
            inventory.setItem(n2, (ItemStack)iterator2.next());
            ++n2;
        }
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
            if (itemStackStringTag != null && itemStackStringTag.length() > 0) {
                this.main.getGUIManager().getGUIById("drug-info").open(player, this.main.getDrugManager().getDrugById(itemStackStringTag));
                return;
            }
            if (inventoryClickEvent.getSlot() == inventoryClickEvent.getInventory().getSize() - 1) {
                final ItemStack currentItem = inventoryClickEvent.getCurrentItem();
                if (currentItem.getType() == Material.ARROW) {
                    final String itemStackStringTag2 = ReflectionUtils.getItemStackStringTag(currentItem, "next-page");
                    if (itemStackStringTag2 != null) {
                        final int int1 = Integer.parseInt(itemStackStringTag2);
                        player.closeInventory();
                        this.open(player, int1);
                    }
                }
            }
            else if (inventoryClickEvent.getSlot() == inventoryClickEvent.getInventory().getSize() - 9) {
                final ItemStack currentItem2 = inventoryClickEvent.getCurrentItem();
                if (currentItem2.getType() == Material.ARROW) {
                    final String itemStackStringTag3 = ReflectionUtils.getItemStackStringTag(currentItem2, "previous-page");
                    if (itemStackStringTag3 != null) {
                        final int int2 = Integer.parseInt(itemStackStringTag3);
                        player.closeInventory();
                        this.open(player, int2);
                    }
                }
            }
        }
    }
}
