// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.actions;

import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.Attribute;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.command.CommandSender;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import me.dexuby.UltimateDrugs.drugs.Drug;
import java.util.UUID;

public class HealAction extends ConsumeAction
{
    private double amount;
    
    public HealAction(final int n, final double n2, final int n3, final int n4, final String s, final double amount) {
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
        final AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        final double b = (attribute != null) ? attribute.getValue() : 20.0;
        if (this.amount > 0.0 && player.getHealth() >= b) {
            return;
        }
        if (this.amount > 0.0) {
            player.setHealth(Math.min(player.getHealth() + this.amount, b));
        }
        else {
            player.setHealth(Math.max(player.getHealth() + this.amount, 0.0));
        }
    }
    
    public double getAmount() {
        return this.amount;
    }
    
    @Override
    public HealAction clone() {
        return new HealAction(this.getDelay(), this.getChance(), this.getRepetitions(), this.getRepetitionTickDelay(), this.getTargetSelector(), this.amount);
    }
}
