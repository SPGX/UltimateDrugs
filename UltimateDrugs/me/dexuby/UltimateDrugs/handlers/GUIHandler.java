// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.handlers;

import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.EventHandler;
import me.dexuby.UltimateDrugs.guis.GUI;
import me.dexuby.UltimateDrugs.guis.GUIHolder;
import org.bukkit.event.inventory.InventoryClickEvent;
import me.dexuby.UltimateDrugs.Main;
import org.bukkit.event.Listener;

public class GUIHandler implements Listener
{
    private Main main;
    
    public GUIHandler(final Main main) {
        this.main = main;
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent inventoryClickEvent) {
        if (inventoryClickEvent.getClickedInventory() != null && inventoryClickEvent.getView().getTopInventory().getHolder() instanceof GUIHolder) {
            final GUIHolder obj = (GUIHolder)inventoryClickEvent.getView().getTopInventory().getHolder();
            final GUI gui = obj.getGUI();
            if (gui.doCancelInteractions()) {
                inventoryClickEvent.setCancelled(true);
            }
            if (inventoryClickEvent.getClickedInventory().getHolder() != null && inventoryClickEvent.getClickedInventory().getHolder().equals(obj)) {
                gui.onClick(inventoryClickEvent);
            }
            else {
                gui.onInventoryClick(inventoryClickEvent);
            }
        }
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent var1) {
       Player player = (Player)var1.getPlayer();
       if (this.main.getGUIManager().hasPreviousGUI(player.getUniqueId())) {
          this.main.getServer().getScheduler().runTaskLater(this.main, () -> {
             player.openInventory(this.main.getGUIManager().pollPreviousGUI(player.getUniqueId()));
          }, 1L);
       }

       if (var1.getInventory().getHolder() instanceof GUIHolder) {
          GUIHolder guiholder = (GUIHolder)var1.getInventory().getHolder();
          guiholder.getGUI().onInventoryClose(var1);
       }

    }
}
