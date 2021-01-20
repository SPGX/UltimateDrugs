// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.guis;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GUIHolder implements InventoryHolder
{
    private GUI gui;
    
    GUIHolder(final GUI gui) {
        this.gui = gui;
    }
    
    public GUI getGUI() {
        return this.gui;
    }
    
    public Inventory getInventory() {
        return this.gui.getInventory();
    }
}
