// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.events;

import org.bukkit.event.EventHandler;
import java.util.Map;
import org.bukkit.potion.PotionEffect;
import me.dexuby.UltimateDrugs.drugs.MultiplierType;
import me.dexuby.UltimateDrugs.utils.Constants;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import me.dexuby.UltimateDrugs.managers.DrugManager;
import me.dexuby.UltimateDrugs.Main;
import org.bukkit.event.Listener;

public class EntityPotionEffect implements Listener
{
    private final Main main;
    private final DrugManager drugManager;
    
    public EntityPotionEffect(final Main main) {
        this.main = main;
        this.drugManager = main.getDrugManager();
    }
    
    @EventHandler
    public void onEntityPotionEffect(final EntityPotionEffectEvent entityPotionEffectEvent) {
        if (entityPotionEffectEvent.getEntity() instanceof Player && (entityPotionEffectEvent.getAction() == EntityPotionEffectEvent.Action.ADDED || entityPotionEffectEvent.getAction() == EntityPotionEffectEvent.Action.CHANGED) && entityPotionEffectEvent.getCause() != EntityPotionEffectEvent.Cause.PLUGIN) {
            if (entityPotionEffectEvent.getNewEffect() == null) {
                return;
            }
            final PotionEffect newEffect = entityPotionEffectEvent.getNewEffect();
            final Player player = (Player)entityPotionEffectEvent.getEntity();
            if (this.drugManager.isPlayerOnDrugs(player)) {
                double n = 1.0;
                final Map<MultiplierType, Double> playerMultipliers = this.drugManager.getPlayerMultipliers(player);
                if (Constants.POSITIVE_EFFECT_TYPES.contains(newEffect.getType().getName())) {
                    if (playerMultipliers.containsKey(MultiplierType.POSITIVE_POTION_EFFECT_DURATION)) {
                        n = playerMultipliers.get(MultiplierType.POSITIVE_POTION_EFFECT_DURATION);
                    }
                }
                else if (Constants.NEGATIVE_EFFECT_TYPES.contains(newEffect.getType().getName()) && playerMultipliers.containsKey(MultiplierType.NEGATIVE_POTION_EFFECT_DURATION)) {
                    n = playerMultipliers.get(MultiplierType.NEGATIVE_POTION_EFFECT_DURATION);
                }
                if (n != 1.0) {
                    entityPotionEffectEvent.setCancelled(true);
                    player.removePotionEffect(newEffect.getType());
                    player.addPotionEffect(new PotionEffect(newEffect.getType(), (int)Math.round(newEffect.getDuration() * n), newEffect.getAmplifier(), newEffect.isAmbient(), newEffect.hasParticles(), newEffect.hasIcon()));
                }
            }
        }
    }
}
