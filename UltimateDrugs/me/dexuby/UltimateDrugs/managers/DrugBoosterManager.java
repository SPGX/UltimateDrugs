/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package me.dexuby.UltimateDrugs.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import me.dexuby.UltimateDrugs.Main;
import me.dexuby.UltimateDrugs.drugs.Drug;
import me.dexuby.UltimateDrugs.drugs.DrugBooster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DrugBoosterManager {
    private Main main;
    private Collection<DrugBooster> inactiveDrugBoosters = new ArrayList<DrugBooster>();
    private Collection<DrugBooster> activeDrugBoosters = new ArrayList<DrugBooster>();

    public DrugBoosterManager(Main main) {
        this.main = main;
        main.getServer().getScheduler().runTaskTimer((Plugin)main, () -> {
            Iterator<DrugBooster> iterator = this.activeDrugBoosters.iterator();
            while (iterator.hasNext()) {
                DrugBooster drugBooster = iterator.next();
                if (System.currentTimeMillis() < drugBooster.getExpirationTimestamp()) continue;
                main.getConfigManager().removeDrugBooster(drugBooster);
                iterator.remove();
                main.getServer().broadcastMessage(main.getConfigManager().getLangString("booster-expired-broadcast"));
            }
        }, 20L, 20L);
    }

    public Collection<DrugBooster> getInactiveDrugBoosters() {
        return this.inactiveDrugBoosters;
    }

    public Collection<DrugBooster> getActiveDrugBoosters() {
        return this.activeDrugBoosters;
    }

    public DrugBooster getDrugBoosterById(String string) {
        ArrayList<DrugBooster> arrayList = new ArrayList<DrugBooster>();
        arrayList.addAll(this.inactiveDrugBoosters);
        arrayList.addAll(this.activeDrugBoosters);
        return arrayList.stream().filter(drugBooster -> drugBooster.getId().equals(string)).findFirst().orElse(null);
    }

    public Collection<DrugBooster> getAvailablePlayerDrugBoosters(Player player) {
        return this.inactiveDrugBoosters.stream().filter(drugBooster -> drugBooster.getOwnerUUID().equals(player.getUniqueId())).collect(Collectors.toList());
    }

    public void addDrugBooster(DrugBooster drugBooster) {
        this.inactiveDrugBoosters.add(drugBooster);
    }

    public void removeDrugBooster(DrugBooster drugBooster) {
        this.inactiveDrugBoosters.remove(drugBooster);
    }

    public void addActiveDrugBooster(DrugBooster drugBooster) {
        this.activeDrugBoosters.add(drugBooster);
    }

    public void removeActiveDrugBooster(DrugBooster drugBooster) {
        this.activeDrugBoosters.remove(drugBooster);
    }

    public void activateBooster(DrugBooster drugBooster) {
        if (!drugBooster.isActive()) {
            this.removeDrugBooster(drugBooster);
            drugBooster.setActive(true);
            drugBooster.setExpirationTimestamp(System.currentTimeMillis() + drugBooster.getDuration() * 1000L);
            this.addActiveDrugBooster(drugBooster);
            this.main.getConfigManager().saveDrugBooster(drugBooster);
        }
    }

    public boolean hasActiveBooster(Drug drug) {
        return this.activeDrugBoosters.stream().anyMatch(drugBooster -> drugBooster.isAllDrugs() || drugBooster.getAffectedDrugs().contains(drug));
    }

    public double getBoosterMultiplier(Drug drug, Player player) {
        double d;
        ArrayList<DrugBooster> arrayList = new ArrayList<DrugBooster>(this.activeDrugBoosters);
        double d2 = this.getTotalMultiplier(arrayList.stream().filter(drugBooster -> !drugBooster.isStackable()).collect(Collectors.toList()), drug, player);
        return d2 > (d = this.getTotalMultiplier(arrayList.stream().filter(DrugBooster::isStackable).collect(Collectors.toList()), drug, player)) ? d2 : d;
    }

    private double getTotalMultiplier(Collection<DrugBooster> collection, Drug drug, Player player) {
        double d = 1.0;
        for (DrugBooster drugBooster : collection) {
            if (drugBooster.isOwnerOnly() && !player.getUniqueId().equals(drugBooster.getOwnerUUID()) || !drugBooster.isAllDrugs() && !drugBooster.getAffectedDrugs().contains(drug.getId())) continue;
            if (drugBooster.isStackable()) {
                d += drugBooster.getMultiplier() - 1.0;
                continue;
            }
            if (!(d < drugBooster.getMultiplier())) continue;
            d = drugBooster.getMultiplier();
        }
        return d;
    }
}

