// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.guis;

import java.util.Objects;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import java.util.Map;
import me.dexuby.UltimateDrugs.utils.TextUtils;
import java.util.HashMap;
import me.dexuby.UltimateDrugs.drugs.Drug;
import org.bukkit.entity.Player;
import me.dexuby.UltimateDrugs.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Inventory;
import me.dexuby.UltimateDrugs.managers.ConfigManager;
import me.dexuby.UltimateDrugs.Main;

public class DrugInfoGUI extends GUI
{
    private Main main;
    private ConfigManager configManager;
    
    public DrugInfoGUI(final Main main) {
        super("drug-info", true);
        this.main = main;
        this.configManager = main.getConfigManager();
    }
    
    @Override
    public Inventory getInventory() {
        final Inventory inventory = this.main.getServer().createInventory((InventoryHolder)new GUIHolder(this), 9, this.configManager.getLangString("drug-info-gui-title"));
        inventory.setItem(8, new ItemBuilder(Material.BARRIER).displayName(this.configManager.getLangString("back-item-display-name")).lore(this.configManager.getLangStringList("back-item-lore")).build());
        return inventory;
    }
    
    @Override
    public void open(final Player player, final Object... array) {
        final Inventory inventory = this.getInventory();
        if (array.length == 1) {
            final Drug drug = (Drug)array[0];
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("%drug_id%", drug.getId());
            hashMap.put("%drug_legal%", drug.isLegal() ? this.configManager.getLangString("yes") : this.configManager.getLangString("no"));
            hashMap.put("%drug_effect_duration%", TextUtils.convertSecondsToFormattedTime(drug.getEffectDuration() / 20));
            inventory.setItem(0, ReflectionUtils.setItemStackStringTag(new ItemBuilder(Material.NAME_TAG).displayName(TextUtils.replaceVariables(this.configManager.getLangString("drug-info-item-display-name"), hashMap)).lore(TextUtils.replaceVariables(this.configManager.getLangStringList("drug-info-item-lore"), hashMap)).build(), "drug-id", drug.getId()));
            if (drug.getGrowing() != null) {
                inventory.addItem(new ItemStack[] { new ItemBuilder(Material.GRASS).displayName(this.configManager.getLangString("drug-info-growing-item-display-name")).lore(this.configManager.getLangStringList("drug-info-growing-item-lore")).build() });
            }
            if (drug.getCraftingRecipes().size() > 0) {
                inventory.addItem(new ItemStack[] { new ItemBuilder(Material.CRAFTING_TABLE).displayName(this.configManager.getLangString("drug-info-crafting-item-display-name")).lore(this.configManager.getLangStringList("drug-info-crafting-item-lore")).build() });
            }
            if (drug.getFurnaceRecipes().size() > 0) {
                inventory.addItem(new ItemStack[] { new ItemBuilder(Material.FURNACE).displayName(this.configManager.getLangString("drug-info-furnace-item-display-name")).lore(this.configManager.getLangStringList("drug-info-furnace-item-lore")).build() });
            }
            if (drug.getBrewingRecipes().size() > 0) {
                inventory.addItem(new ItemStack[] { new ItemBuilder(Material.BREWING_STAND).displayName(this.configManager.getLangString("drug-info-brewing-item-display-name")).lore(this.configManager.getLangStringList("drug-info-brewing-item-lore")).build() });
            }
        }
        player.openInventory(inventory);
    }
    
    @Override
    public void onClick(final InventoryClickEvent inventoryClickEvent) {
        final Player player = (Player)inventoryClickEvent.getWhoClicked();
        if (inventoryClickEvent.getCurrentItem() != null) {
            final Drug drugById = this.main.getDrugManager().getDrugById(ReflectionUtils.getItemStackStringTag(Objects.requireNonNull(inventoryClickEvent.getClickedInventory()).getItem(0), "drug-id"));
            if (inventoryClickEvent.getSlot() == 8) {
                this.main.getGUIManager().getGUIById("drug-list").open(player, new Object[0]);
            }
            else if (inventoryClickEvent.getCurrentItem().getType() == Material.GRASS) {
                this.main.getGUIManager().getGUIById("drug-growing-info").open(player, drugById);
                this.main.getGUIManager().setPreviousGUI(player.getUniqueId(), inventoryClickEvent.getClickedInventory());
            }
            else if (inventoryClickEvent.getCurrentItem().getType() == Material.CRAFTING_TABLE) {
                this.main.getGUIManager().getGUIById("drug-crafting-recipe-list").open(player, drugById);
            }
            else if (inventoryClickEvent.getCurrentItem().getType() == Material.FURNACE) {
                this.main.getGUIManager().getGUIById("drug-furnace-recipe-list").open(player, drugById);
            }
            else if (inventoryClickEvent.getCurrentItem().getType() == Material.BREWING_STAND) {
                this.main.getGUIManager().getGUIById("drug-brewing-recipe-list").open(player, drugById);
            }
        }
    }
}
