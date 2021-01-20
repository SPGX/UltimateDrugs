// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.guis;

import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.Iterator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import me.dexuby.UltimateDrugs.drugs.DrugCraftingRecipe;
import org.bukkit.entity.Player;
import me.dexuby.UltimateDrugs.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Inventory;
import me.dexuby.UltimateDrugs.Main;

public class DrugCraftingPreviewGUI extends GUI
{
    private Main main;
    
    public DrugCraftingPreviewGUI(final Main main) {
        super("drug-crafting-preview", true);
        this.main = main;
    }
    
    @Override
    public Inventory getInventory() {
        final Inventory inventory = this.main.getServer().createInventory((InventoryHolder)new GUIHolder(this), 45, this.main.getConfigManager().getLangString("drug-crafting-preview-gui-title"));
        for (int i = 0; i < 45; ++i) {
            inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).displayName("§0").build());
        }
        int n = 10;
        int n2 = 0;
        for (int j = 0; j < 9; ++j) {
            inventory.clear(n);
            if (++n2 < 3) {
                ++n;
            }
            else {
                n += 7;
                n2 = 0;
            }
        }
        return inventory;
    }
    
    @Override
    public void open(final Player player, final Object... array) {
        final Inventory inventory = this.getInventory();
        if (array.length > 0) {
            final DrugCraftingRecipe drugCraftingRecipe = (DrugCraftingRecipe)array[0];
            if (drugCraftingRecipe != null) {
                if (drugCraftingRecipe.getRecipe() instanceof ShapedRecipe) {
                    final ShapedRecipe shapedRecipe = (ShapedRecipe)drugCraftingRecipe.getRecipe();
                    int n = 10;
                    int n2 = 0;
                    final String[] shape = shapedRecipe.getShape();
                    for (int length = shape.length, i = 0; i < length; ++i) {
                        for (final char c : shape[i].toCharArray()) {
                            if (shapedRecipe.getIngredientMap().containsKey(c)) {
                                inventory.setItem(n, (ItemStack)shapedRecipe.getIngredientMap().get(c));
                                if (++n2 < 3) {
                                    ++n;
                                }
                                else {
                                    n += 7;
                                    n2 = 0;
                                }
                            }
                        }
                    }
                }
                else {
                    int n3 = 10;
                    int n4 = 0;
                    for (final ItemStack itemStack : drugCraftingRecipe.getIngredients()) {
                        for (int k = 0; k < itemStack.getAmount(); ++k) {
                            final ItemStack clone = itemStack.clone();
                            clone.setAmount(1);
                            inventory.setItem(n3, clone);
                            if (++n4 < 3) {
                                ++n3;
                            }
                            else {
                                n3 += 7;
                                n4 = 0;
                            }
                        }
                    }
                }
                inventory.setItem(24, drugCraftingRecipe.getRecipe().getResult());
            }
        }
        player.openInventory(inventory);
    }
    
    @Override
    public void onClick(final InventoryClickEvent inventoryClickEvent) {
    }
}
