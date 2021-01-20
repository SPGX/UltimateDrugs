// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.actions;

import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.command.CommandSender;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import me.dexuby.UltimateDrugs.drugs.Drug;
import java.util.UUID;

public class FeedAction extends ConsumeAction
{
    private int amount;
    
    public FeedAction(final int n, final double n2, final int n3, final int n4, final String s, final int amount) {
        super(n, n2, n3, n4, s);
        this.amount = amount;
    }
    
    @Override
    public void execute(final UUID uuid, final Drug drug) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null || !player.isValid() || !player.isOnline() || player.isDead()) {
            return;
        }
        if (this.getTargetSelector() != null) {
            for (final Entity entity : ReflectionUtils.getEntitiesFromSelector((CommandSender)player, this.getTargetSelector())) {
                if (entity instanceof Player) {
                    this.apply((Player)entity);
                }
            }
        }
        else {
            this.apply(player);
        }
    }
    
    private void apply(final Player player) {
        if (this.amount > 0 && player.getFoodLevel() >= 20) {
            return;
        }
        if (this.amount > 0) {
            player.setFoodLevel(Math.min(player.getFoodLevel() + this.amount, 20));
        }
        else {
            player.setFoodLevel(Math.max(player.getFoodLevel() + this.amount, 0));
        }
    }
    
    public double getAmount() {
        return this.amount;
    }
    
    @Override
    public FeedAction clone() {
        return new FeedAction(this.getDelay(), this.getChance(), this.getRepetitions(), this.getRepetitionTickDelay(), this.getTargetSelector(), this.amount);
    }
}
