// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.events;

import org.bukkit.event.EventHandler;
import me.dexuby.UltimateDrugs.drugs.Drug;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import me.dexuby.UltimateDrugs.api.DrugConsumeEvent;
import me.dexuby.UltimateDrugs.drugs.ConsumeOption;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import me.dexuby.UltimateDrugs.managers.DrugManager;
import me.dexuby.UltimateDrugs.Main;
import org.bukkit.event.Listener;

public class PlayerItemConsume implements Listener
{
    private final Main main;
    private final DrugManager drugManager;
    
    public PlayerItemConsume(final Main main) {
        this.main = main;
        this.drugManager = main.getDrugManager();
    }
    
    @EventHandler
    public void onPlayerItemConsume(final PlayerItemConsumeEvent playerItemConsumeEvent) {
        final Player player = playerItemConsumeEvent.getPlayer();
        final Drug drugByItemStack;
        if ((drugByItemStack = this.drugManager.getDrugByItemStack(playerItemConsumeEvent.getItem())) != null) {
            playerItemConsumeEvent.setCancelled(true);
            if (drugByItemStack.getConsumeOption() == ConsumeOption.CONSUME) {
                this.main.getServer().getPluginManager().callEvent((Event)new DrugConsumeEvent(player, drugByItemStack));
            }
        }
    }
}
