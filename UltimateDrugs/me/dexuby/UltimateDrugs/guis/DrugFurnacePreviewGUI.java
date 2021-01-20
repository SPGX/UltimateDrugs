// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.guis;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import me.dexuby.UltimateDrugs.drugs.DrugFurnaceRecipe;
import org.bukkit.entity.Player;
import me.dexuby.UltimateDrugs.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Inventory;
import me.dexuby.UltimateDrugs.Main;

public class DrugFurnacePreviewGUI extends GUI
{
    private Main main;
    
    public DrugFurnacePreviewGUI(final Main main) {
        super("drug-furnace-preview", true);
        this.main = main;
    }
    
    @Override
    public Inventory getInventory() {
        final Inventory inventory = this.main.getServer().createInventory((InventoryHolder)new GUIHolder(this), 27, this.main.getConfigManager().getLangString("drug-furnace-preview-gui-title"));
        for (int i = 0; i < 27; ++i) {
            inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).displayName("§0").build());
        }
        int n = 11;
        for (int j = 0; j < 3; ++j) {
            inventory.clear(n);
            n += 2;
        }
        return inventory;
    }
    
    @Override
    public void open(final Player player, final Object... array) {
        final Inventory inventory = this.getInventory();
        if (array.length > 0) {
            final DrugFurnaceRecipe drugFurnaceRecipe = (DrugFurnaceRecipe)array[0];
            if (drugFurnaceRecipe != null) {
                inventory.setItem(11, drugFurnaceRecipe.getFurnaceRecipe().getInput());
                inventory.setItem(13, new ItemStack(Material.COAL));
                inventory.setItem(15, drugFurnaceRecipe.getFurnaceRecipe().getResult());
            }
        }
        player.openInventory(inventory);
    }
    
    @Override
    public void onClick(final InventoryClickEvent inventoryClickEvent) {
    }
}
