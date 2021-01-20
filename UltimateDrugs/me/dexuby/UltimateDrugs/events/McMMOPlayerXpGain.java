// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.events;

import org.bukkit.event.EventHandler;
import java.util.Map;
import org.bukkit.entity.Player;
import me.dexuby.UltimateDrugs.drugs.MultiplierType;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import me.dexuby.UltimateDrugs.managers.DrugManager;
import me.dexuby.UltimateDrugs.Main;
import org.bukkit.event.Listener;

public class McMMOPlayerXpGain implements Listener
{
    private Main main;
    private DrugManager drugManager;
    
    public McMMOPlayerXpGain(final Main main) {
        this.main = main;
        this.drugManager = main.getDrugManager();
    }
    
    @EventHandler
    public void onMcMMOPlayerXpGain(final McMMOPlayerXpGainEvent mcMMOPlayerXpGainEvent) {
        final Player player = mcMMOPlayerXpGainEvent.getPlayer();
        if (this.drugManager.isPlayerOnDrugs(player)) {
            final Map<MultiplierType, Double> playerMultipliers = this.drugManager.getPlayerMultipliers(player);
            if (playerMultipliers.containsKey(MultiplierType.MCMMO_EXP_EARNING)) {
                mcMMOPlayerXpGainEvent.setRawXpGained((float)(mcMMOPlayerXpGainEvent.getRawXpGained() * playerMultipliers.get(MultiplierType.MCMMO_EXP_EARNING)));
            }
        }
    }
}
