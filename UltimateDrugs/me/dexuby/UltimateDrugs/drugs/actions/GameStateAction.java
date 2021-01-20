// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.actions;

import me.dexuby.UltimateDrugs.packetwrappers.WrapperPlayServerGameStateChange;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.command.CommandSender;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import me.dexuby.UltimateDrugs.drugs.Drug;
import java.util.UUID;

public class GameStateAction extends ConsumeAction
{
    private int reason;
    private float value;
    
    public GameStateAction(final int n, final double n2, final int n3, final int n4, final String s, final int reason, final float value) {
        super(n, n2, n3, n4, s);
        this.reason = reason;
        this.value = value;
    }
    
    @Override
    public void execute(final UUID uuid, final Drug drug) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null || !player.isValid() || !player.isOnline()) {
            return;
        }
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
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
    }
    
    private void apply(final Player player) {
        new WrapperPlayServerGameStateChange().setReason(this.reason).setValue(this.value).sendPacket(player);
    }
    
    public int getReason() {
        return this.reason;
    }
    
    public float getValue() {
        return this.value;
    }
    
    @Override
    public GameStateAction clone() {
        return new GameStateAction(this.getDelay(), this.getChance(), this.getRepetitions(), this.getRepetitionTickDelay(), this.getTargetSelector(), this.reason, this.value);
    }
}
