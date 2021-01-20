// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.managers;

import me.dexuby.UltimateDrugs.guis.DrugSellGUI;
import me.dexuby.UltimateDrugs.guis.DrugListGUI;
import me.dexuby.UltimateDrugs.guis.DrugInfoGUI;
import me.dexuby.UltimateDrugs.guis.DrugGrowingInfoGUI;
import me.dexuby.UltimateDrugs.guis.DrugFurnaceRecipeListGUI;
import me.dexuby.UltimateDrugs.guis.DrugFurnacePreviewGUI;
import me.dexuby.UltimateDrugs.guis.DrugCraftingRecipeListGUI;
import me.dexuby.UltimateDrugs.guis.DrugCraftingPreviewGUI;
import me.dexuby.UltimateDrugs.guis.DrugBuyGUI;
import me.dexuby.UltimateDrugs.guis.DrugBrewingRecipeListGUI;
import me.dexuby.UltimateDrugs.guis.DrugBrewingPreviewGUI;
import me.dexuby.UltimateDrugs.guis.DrugBoostersGUI;
import java.util.HashMap;
import java.util.ArrayList;
import me.dexuby.UltimateDrugs.Main;
import org.bukkit.inventory.Inventory;
import java.util.UUID;
import java.util.Map;
import me.dexuby.UltimateDrugs.guis.GUI;
import java.util.Collection;

public class GUIManager
{
    private Collection<GUI> registeredGUIs;
    private Map<UUID, Inventory> previousGUI;
    
    public GUIManager(final Main main) {
        this.registeredGUIs = new ArrayList<GUI>();
        this.previousGUI = new HashMap<UUID, Inventory>();
        this.registerGUI(new DrugBoostersGUI(main));
        this.registerGUI(new DrugBrewingPreviewGUI(main));
        this.registerGUI(new DrugBrewingRecipeListGUI(main));
        this.registerGUI(new DrugBuyGUI(main));
        this.registerGUI(new DrugCraftingPreviewGUI(main));
        this.registerGUI(new DrugCraftingRecipeListGUI(main));
        this.registerGUI(new DrugFurnacePreviewGUI(main));
        this.registerGUI(new DrugFurnaceRecipeListGUI(main));
        this.registerGUI(new DrugGrowingInfoGUI(main));
        this.registerGUI(new DrugInfoGUI(main));
        this.registerGUI(new DrugListGUI(main));
        this.registerGUI(new DrugSellGUI(main));
    }
    
    public GUI getGUIById(final String anObject) {
        return this.registeredGUIs.stream().filter(gui -> gui.getId().equals(anObject)).findFirst().orElse(null);
    }
    
    private void registerGUI(final GUI gui) {
        this.registeredGUIs.add(gui);
    }
    
    public boolean hasPreviousGUI(final UUID uuid) {
        return this.previousGUI.containsKey(uuid);
    }
    
    public void setPreviousGUI(final UUID uuid, final Inventory inventory) {
        this.previousGUI.put(uuid, inventory);
    }
    
    public void clearPreviousGUI(final UUID uuid) {
        this.previousGUI.remove(uuid);
    }
    
    public Inventory pollPreviousGUI(final UUID obj) {
        final Map.Entry<UUID, Inventory> entry2 = this.previousGUI.entrySet().stream().filter(entry -> entry.getKey().equals(obj)).findFirst().orElse(null);
        if (entry2 != null) {
            this.clearPreviousGUI(entry2.getKey());
            return entry2.getValue();
        }
        return null;
    }
}
