// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.actions;

import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.command.CommandSender;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import me.dexuby.UltimateDrugs.drugs.Drug;
import java.util.UUID;
import org.bukkit.potion.PotionEffect;

public class PotionEffectAction extends ConsumeAction
{
    private PotionEffect potionEffect;
    
    public PotionEffectAction(final int n, final double n2, final int n3, final int n4, final String s, final PotionEffect potionEffect) {
        super(n, n2, n3, n4, s);
        this.potionEffect = potionEffect;
    }
    
    @Override
    public void execute(final UUID uuid, final Drug drug) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isValid() && player.isOnline()) {
            if (this.getTargetSelector() != null) {
                for (final Entity entity : ReflectionUtils.getEntitiesFromSelector((CommandSender)player, this.getTargetSelector())) {
                    if (entity instanceof LivingEntity) {
                        this.apply((LivingEntity)entity);
                    }
                }
            }
            else {
                this.apply((LivingEntity)player);
            }
        }
    }
    
    private void apply(final LivingEntity livingEntity) {
        livingEntity.addPotionEffect(this.potionEffect);
    }
    
    public PotionEffect getPotionEffect() {
        return this.potionEffect;
    }
    
    @Override
    public PotionEffectAction clone() {
        return new PotionEffectAction(this.getDelay(), this.getChance(), this.getRepetitions(), this.getRepetitionTickDelay(), this.getTargetSelector(), this.potionEffect);
    }
}
