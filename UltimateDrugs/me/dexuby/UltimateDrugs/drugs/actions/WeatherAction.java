// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.actions;

import org.bukkit.World;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.command.CommandSender;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import me.dexuby.UltimateDrugs.drugs.Drug;
import java.util.UUID;
import org.bukkit.WeatherType;

public class WeatherAction extends TimedConsumeAction
{
    private WeatherType weatherType;
    private boolean clientSided;
    
    public WeatherAction(final int n, final double n2, final int n3, final int n4, final String s, final int n5, final WeatherType weatherType, final boolean clientSided) {
        super(n, n2, n3, n4, s, n5);
        this.weatherType = weatherType;
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
            player.setPlayerWeather(this.weatherType);
        }
        else {
            final World world = player.getWorld();
            if (this.weatherType == WeatherType.CLEAR) {
                world.setThundering(false);
                world.setStorm(false);
            }
            else {
                world.setStorm(true);
            }
        }
    }
    
    @Override
    public void end(final Player player) {
        if (this.clientSided) {
            player.resetPlayerWeather();
        }
    }
    
    public WeatherType weatherType() {
        return this.weatherType;
    }
    
    public boolean isClientSided() {
        return this.clientSided;
    }
    
    @Override
    public WeatherAction clone() {
        return new WeatherAction(this.getDelay(), this.getChance(), this.getRepetitions(), this.getRepetitionTickDelay(), this.getTargetSelector(), this.getDuration(), this.weatherType, this.clientSided);
    }
}
