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

public class SaturationAction extends ConsumeAction
{
    private int amount;
    
    public SaturationAction(final int n, final double n2, final int n3, final int n4, final String s, final int amount) {
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
        if (this.amount > 0 && player.getSaturation() >= 20.0f) {
            return;
        }
        if (this.amount > 0) {
            player.setSaturation(Math.min(player.getSaturation() + this.amount, 20.0f));
        }
        else {
            player.setSaturation(Math.max(player.getSaturation() + this.amount, 0.0f));
        }
    }
    
    public double getAmount() {
        return this.amount;
    }
    
    @Override
    public SaturationAction clone() {
        return new SaturationAction(this.getDelay(), this.getChance(), this.getRepetitions(), this.getRepetitionTickDelay(), this.getTargetSelector(), this.amount);
    }
}
