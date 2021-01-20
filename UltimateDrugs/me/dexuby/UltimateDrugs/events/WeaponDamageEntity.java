// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.events;

import org.bukkit.event.EventHandler;
import java.util.Map;
import me.dexuby.UltimateDrugs.drugs.MultiplierType;
import org.bukkit.entity.Player;
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import me.dexuby.UltimateDrugs.managers.DrugManager;
import me.dexuby.UltimateDrugs.Main;
import org.bukkit.event.Listener;

public class WeaponDamageEntity implements Listener
{
    private Main main;
    private DrugManager drugManager;
    
    public WeaponDamageEntity(final Main main) {
        this.main = main;
        this.drugManager = main.getDrugManager();
    }
    
    @EventHandler
    public void onWeaponDamageEntity(final WeaponDamageEntityEvent weaponDamageEntityEvent) {
        if (weaponDamageEntityEvent.getDamager() instanceof Player) {
            final Player player = (Player)weaponDamageEntityEvent.getDamager();
            if (this.drugManager.isPlayerOnDrugs(player)) {
                final Map<MultiplierType, Double> playerMultipliers = this.drugManager.getPlayerMultipliers(player);
                if (playerMultipliers.containsKey(MultiplierType.WEAPON_DAMAGE)) {
                    weaponDamageEntityEvent.setDamage(weaponDamageEntityEvent.getDamage() * playerMultipliers.get(MultiplierType.WEAPON_DAMAGE));
                }
            }
        }
    }
}
