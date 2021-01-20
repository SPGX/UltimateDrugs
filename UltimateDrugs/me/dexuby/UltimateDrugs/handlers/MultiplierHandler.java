// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.handlers;

import org.bukkit.event.EventHandler;
import java.util.Map;
import org.bukkit.entity.Projectile;
import java.util.Objects;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import me.dexuby.UltimateDrugs.drugs.MultiplierType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import me.dexuby.UltimateDrugs.managers.DrugManager;
import me.dexuby.UltimateDrugs.Main;
import org.bukkit.event.Listener;

public class MultiplierHandler implements Listener
{
    private Main main;
    private DrugManager drugManager;
    
    public MultiplierHandler(final Main main) {
        this.main = main;
        this.drugManager = main.getDrugManager();
    }
    
    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (entityDamageByEntityEvent.getDamager() instanceof Player) {
            final Player player = (Player)entityDamageByEntityEvent.getDamager();
            if (this.drugManager.isPlayerOnDrugs(player)) {
                final Map<MultiplierType, Double> playerMultipliers = this.drugManager.getPlayerMultipliers(player);
                if (playerMultipliers.containsKey(MultiplierType.DAMAGE)) {
                    entityDamageByEntityEvent.setDamage(entityDamageByEntityEvent.getDamage() * playerMultipliers.get(MultiplierType.DAMAGE));
                }
                if (entityDamageByEntityEvent.getEntity() instanceof Player && playerMultipliers.containsKey(MultiplierType.PVP_DAMAGE)) {
                    entityDamageByEntityEvent.setDamage(entityDamageByEntityEvent.getDamage() * playerMultipliers.get(MultiplierType.PVP_DAMAGE));
                }
                if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) == null && playerMultipliers.containsKey(MultiplierType.HAND_DAMAGE)) {
                    entityDamageByEntityEvent.setDamage(entityDamageByEntityEvent.getDamage() * playerMultipliers.get(MultiplierType.HAND_DAMAGE));
                }
                if (playerMultipliers.containsKey(MultiplierType.LIFE_STEAL)) {
                    final double n = entityDamageByEntityEvent.getFinalDamage() * playerMultipliers.get(MultiplierType.LIFE_STEAL);
                    final double value = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
                    if (player.getHealth() < value) {
                        if (player.getHealth() + n > value) {
                            player.setHealth(value);
                        }
                        else {
                            player.setHealth(player.getHealth() + n);
                        }
                    }
                }
            }
        }
        else if (entityDamageByEntityEvent.getDamager() instanceof Projectile) {
            final Projectile projectile = (Projectile)entityDamageByEntityEvent.getDamager();
            if (projectile.getShooter() instanceof Player) {
                final Player player2 = (Player)projectile.getShooter();
                if (player2 != null && player2.isValid() && player2.isOnline() && this.drugManager.isPlayerOnDrugs(player2)) {
                    final Map<MultiplierType, Double> playerMultipliers2 = this.drugManager.getPlayerMultipliers(player2);
                    if (playerMultipliers2.containsKey(MultiplierType.PROJECTILE_DAMAGE)) {
                        entityDamageByEntityEvent.setDamage(entityDamageByEntityEvent.getDamage() * playerMultipliers2.get(MultiplierType.PROJECTILE_DAMAGE));
                    }
                }
            }
        }
        if (entityDamageByEntityEvent.getEntity() instanceof Player) {
            final Player player3 = (Player)entityDamageByEntityEvent.getEntity();
            if (this.drugManager.isPlayerOnDrugs(player3)) {
                final Map<MultiplierType, Double> playerMultipliers3 = this.drugManager.getPlayerMultipliers(player3);
                if (playerMultipliers3.containsKey(MultiplierType.INCOMING_DAMAGE)) {
                    entityDamageByEntityEvent.setDamage(entityDamageByEntityEvent.getDamage() * playerMultipliers3.get(MultiplierType.INCOMING_DAMAGE));
                }
            }
        }
    }
}
