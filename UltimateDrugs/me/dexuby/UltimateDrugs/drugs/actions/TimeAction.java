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

public class TimeAction extends TimedConsumeAction
{
    private long time;
    private boolean relative;
    private boolean clientSided;
    
    public TimeAction(final int n, final double n2, final int n3, final int n4, final String s, final int n5, final long time, final boolean relative, final boolean clientSided) {
        super(n, n2, n3, n4, s, n5);
        this.time = time;
        this.relative = relative;
        this.clientSided = clientSided;
    }
    
    @Override
    public void execute(final UUID uuid, final Drug drug) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null || !player.isValid() || !player.isOnline()) {
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
        if (this.clientSided) {
            player.setPlayerTime(this.time, this.relative);
        }
        else {
            player.getWorld().setTime(this.time);
        }
    }
    
    @Override
    public void end(final Player player) {
        if (this.clientSided) {
            player.resetPlayerTime();
        }
    }
    
    public long getTime() {
        return this.time;
    }
    
    public boolean isRelative() {
        return this.relative;
    }
    
    public boolean isClientSided() {
        return this.clientSided;
    }
    
    @Override
    public TimeAction clone() {
        return new TimeAction(this.getDelay(), this.getChance(), this.getRepetitions(), this.getRepetitionTickDelay(), this.getTargetSelector(), this.getDuration(), this.time, this.relative, this.clientSided);
    }
}
