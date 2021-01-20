// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.guis;

import java.util.Objects;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.Iterator;
import org.bukkit.Material;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import me.dexuby.UltimateDrugs.utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import me.dexuby.UltimateDrugs.drugs.DrugCraftingRecipe;
import me.dexuby.UltimateDrugs.drugs.Drug;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Inventory;
import me.dexuby.UltimateDrugs.managers.ConfigManager;
import me.dexuby.UltimateDrugs.Main;

public class DrugCraftingRecipeListGUI extends GUI
{
    private Main main;
    private ConfigManager configManager;
    
    public DrugCraftingRecipeListGUI(final Main main) {
        super("drug-crafting-recipe-list", true);
        this.main = main;
        this.configManager = main.getConfigManager();
    }
    
    @Override
    public Inventory getInventory() {
        return this.main.getServer().createInventory((InventoryHolder)new GUIHolder(this), 0, this.main.getConfigManager().getLangString("drug-crafting-recipe-list-gui-title"));
    }
    
    @Override
    public void open(final Player player, final Object... array) {
        if (array.length > 0) {
            final Drug drug = (Drug)array[0];
            final Inventory inventory = this.main.getServer().createInventory((InventoryHolder)new GUIHolder(this), (int)Math.round(Math.ceil((drug.getCraftingRecipes().size() + 1.0) / 9.0) * 9.0), this.main.getConfigManager().getLangString("drug-crafting-recipe-list-gui-title"));
            for (final DrugCraftingRecipe drugCraftingRecipe : drug.getCraftingRecipes()) {
                inventory.addItem(new ItemStack[] { ReflectionUtils.setItemStackStringTag(new ItemBuilder(drugCraftingRecipe.getRecipe().getResult()).appendLore(this.configManager.getLangStringList("crafting-recipe-list-lore")).build(), "recipe-id", drugCraftingRecipe.getId()) });
            }
            inventory.setItem(inventory.getSize() - 1, ReflectionUtils.setItemStackStringTag(new ItemBuilder(Material.BARRIER).displayName(this.configManager.getLangString("back-item-display-name")).lore(this.configManager.getLangStringList("back-item-lore")).build(), "drug-id", drug.getId()));
            player.openInventory(inventory);
        }
    }
    
    @Override
    public void onClick(final InventoryClickEvent inventoryClickEvent) {
        final Player player = (Player)inventoryClickEvent.getWhoClicked();
        if (inventoryClickEvent.getClickedInventory() != null && inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getType() != Material.AIR) {
            final Drug drugById = this.main.getDrugManager().getDrugById(ReflectionUtils.getItemStackStringTag(Objects.requireNonNull(inventoryClickEvent.getClickedInventory().getItem(inventoryClickEvent.getClickedInventory().getSize() - 1)), "drug-id"));
            if (inventoryClickEvent.getSlot() == inventoryClickEvent.getClickedInventory().getSize() - 1) {
                this.main.getGUIManager().getGUIById("drug-info").open(player, drugById);
            }
            else {
                this.main.getGUIManager().getGUIById("drug-crafting-preview").open(player, drugById.getCraftingRecipes().stream().filter(drugCraftingRecipe -> drugCraftingRecipe.getId().equals(ReflectionUtils.getItemStackStringTag(Objects.requireNonNull(inventoryClickEvent.getCurrentItem()), "recipe-id"))).findFirst().orElse(null));
                this.main.getGUIManager().setPreviousGUI(player.getUniqueId(), inventoryClickEvent.getClickedInventory());
            }
        }
    }
}
