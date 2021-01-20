// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.events;

import org.bukkit.event.EventHandler;
import java.util.Map;
import org.bukkit.entity.Player;
import me.dexuby.UltimateDrugs.drugs.MultiplierType;
import com.gamingmesh.jobs.api.JobsPaymentEvent;
import me.dexuby.UltimateDrugs.managers.DrugManager;
import me.dexuby.UltimateDrugs.Main;
import org.bukkit.event.Listener;

public class JobsPayment implements Listener
{
    private Main main;
    private DrugManager drugManager;
    
    public JobsPayment(final Main main) {
        this.main = main;
        this.drugManager = main.getDrugManager();
    }
    
    @EventHandler
    public void onJobsPayment(final JobsPaymentEvent jobsPaymentEvent) {
        if (this.main.getServer().getPlayer(jobsPaymentEvent.getPlayer().getUniqueId()) != null) {
            final Player player = this.main.getServer().getPlayer(jobsPaymentEvent.getPlayer().getUniqueId());
            if (this.drugManager.isPlayerOnDrugs(player)) {
                final Map<MultiplierType, Double> playerMultipliers = this.drugManager.getPlayerMultipliers(player);
                if (playerMultipliers.containsKey(MultiplierType.JOBS_MONEY_EARNING)) {
                    jobsPaymentEvent.setAmount(jobsPaymentEvent.getAmount() * playerMultipliers.get(MultiplierType.JOBS_MONEY_EARNING));
                }
            }
        }
    }
}
