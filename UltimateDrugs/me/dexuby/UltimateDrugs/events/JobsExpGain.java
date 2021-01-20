// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.events;

import org.bukkit.event.EventHandler;
import java.util.Map;
import org.bukkit.entity.Player;
import me.dexuby.UltimateDrugs.drugs.MultiplierType;
import com.gamingmesh.jobs.api.JobsExpGainEvent;
import me.dexuby.UltimateDrugs.managers.DrugManager;
import me.dexuby.UltimateDrugs.Main;
import org.bukkit.event.Listener;

public class JobsExpGain implements Listener
{
    private Main main;
    private DrugManager drugManager;
    
    public JobsExpGain(final Main main) {
        this.main = main;
        this.drugManager = main.getDrugManager();
    }
    
    @EventHandler
    public void onJobsExpGain(final JobsExpGainEvent jobsExpGainEvent) {
        if (this.main.getServer().getPlayer(jobsExpGainEvent.getPlayer().getUniqueId()) != null) {
            final Player player = this.main.getServer().getPlayer(jobsExpGainEvent.getPlayer().getUniqueId());
            if (this.drugManager.isPlayerOnDrugs(player)) {
                final Map<MultiplierType, Double> playerMultipliers = this.drugManager.getPlayerMultipliers(player);
                if (playerMultipliers.containsKey(MultiplierType.JOBS_EXP_EARNING)) {
                    jobsExpGainEvent.setExp(jobsExpGainEvent.getExp() * playerMultipliers.get(MultiplierType.JOBS_EXP_EARNING));
                }
            }
        }
    }
}
