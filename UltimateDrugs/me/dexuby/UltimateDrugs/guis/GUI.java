// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.guis;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class GUI
{
    private String id;
    private boolean cancelInteractions;
    
    GUI(final String id, final boolean cancelInteractions) {
        this.id = id;
        this.cancelInteractions = cancelInteractions;
    }
    
    public String getId() {
        return this.id;
    }
    
    public boolean doCancelInteractions() {
        return this.cancelInteractions;
    }
    
    public abstract Inventory getInventory();
    
    public abstract void open(final Player p0, final Object... p1);
    
    public abstract void onClick(final InventoryClickEvent p0);
    
    public void onInventoryClick(final InventoryClickEvent inventoryClickEvent) {
    }
    
    public void onInventoryClose(final InventoryCloseEvent inventoryCloseEvent) {
    }
}
