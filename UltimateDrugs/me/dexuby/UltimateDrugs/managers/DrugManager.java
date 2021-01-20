/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package me.dexuby.UltimateDrugs.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;
import me.dexuby.UltimateDrugs.Main;
import me.dexuby.UltimateDrugs.drugs.ActiveDrugEffect;
import me.dexuby.UltimateDrugs.drugs.Drug;
import me.dexuby.UltimateDrugs.drugs.DrugDealer;
import me.dexuby.UltimateDrugs.drugs.DrugRelatedGood;
import me.dexuby.UltimateDrugs.drugs.MultiplierType;
import me.dexuby.UltimateDrugs.drugs.actions.TimedConsumeAction;
import me.dexuby.UltimateDrugs.drugs.growing.Fertilizer;
import me.dexuby.UltimateDrugs.drugs.vanilla.ActiveTimedConsumeAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class DrugManager {
    private Main main;
    private List<Drug> drugs = new ArrayList<Drug>();
    private List<Fertilizer> fertilizers = new ArrayList<Fertilizer>();
    private List<DrugDealer> drugDealers = new ArrayList<DrugDealer>();
    private Map<UUID, List<ActiveDrugEffect>> activePlayerDrugs = new HashMap<>();
    private Map<String, Drug> drugCache = new HashMap<String, Drug>();
    
    private Map<UUID, List<ActiveTimedConsumeAction>> timedActionsToRemove = new HashMap<>();
    private Map<UUID, List<ActiveTimedConsumeAction>> timedActionsToAdd = new HashMap<>();
    private Map<UUID, List<ActiveTimedConsumeAction>> activeTimedConsumeActions = new HashMap<>();
	public DrugManager(Main main) {
        this.main = main;
        main.getServer().getScheduler().runTaskTimer((Plugin)main, () -> {
            for (Map.Entry<UUID, List<ActiveTimedConsumeAction>> entry : this.timedActionsToAdd.entrySet()) {
                if (this.activeTimedConsumeActions.containsKey(entry.getKey())) {
                    this.activeTimedConsumeActions.get(entry.getKey()).addAll((Collection<ActiveTimedConsumeAction>)entry.getValue());
                    continue;
                }
                this.activeTimedConsumeActions.put(entry.getKey(), entry.getValue());
            }
            this.timedActionsToAdd.clear();
            for (Map.Entry<UUID, List<ActiveTimedConsumeAction>> entry : this.timedActionsToRemove.entrySet()) {
                if (!this.activeTimedConsumeActions.containsKey(entry.getKey())) continue;
                for (ActiveTimedConsumeAction object : entry.getValue()) {
                    this.activeTimedConsumeActions.get(entry.getKey()).removeIf(activeTimedConsumeAction2 -> activeTimedConsumeAction2.getDrugId().equals(object.getDrugId()) && activeTimedConsumeAction2.getAction().equals(object.getAction()));
                }
            }
            this.timedActionsToRemove.clear();
            if (this.activeTimedConsumeActions.entrySet().size() > 0) {
                for (Map.Entry<UUID, List<ActiveTimedConsumeAction>> entry : this.activeTimedConsumeActions.entrySet()) {
                    if (entry.getValue().size() == 0) continue;
                    Player player = main.getServer().getPlayer(entry.getKey());
                    for (ActiveTimedConsumeAction activeTimedConsumeAction : entry.getValue()) {
                        if (System.currentTimeMillis() < activeTimedConsumeAction.getExpirationTimestamp()) continue;
                        activeTimedConsumeAction.getAction().end(player);
                        this.activeTimedConsumeActions.get(entry.getKey()).remove(activeTimedConsumeAction);
                    }
                }
            }
        }, 1L, 1L);
    }

    void registerDrug(Drug drug) {
        this.drugs.add(drug);
    }

    public List<Drug> getDrugs() {
        return this.drugs;
    }

    public void clearDrugCache() {
        this.drugCache.clear();
    }

    public Drug getDrugById(String string) {
        Drug drug2;
        if (this.drugCache.containsKey(string)) {
            drug2 = this.drugCache.get(string);
        } else {
            drug2 = this.drugs.stream().filter(drug -> drug.getId().equals(string)).findFirst().orElse(null);
            if (drug2 != null) {
                this.drugCache.put(string, drug2);
            }
        }
        return drug2;
    }

    public List<Drug> getSellableDrugs() {
        return this.drugs.stream().filter(drug -> drug.getSellPrice() > 0.0).collect(Collectors.toList());
    }

    public List<DrugRelatedGood> getDrugRelatedGoods() {
        ArrayList<DrugRelatedGood> arrayList = new ArrayList<DrugRelatedGood>();
        this.drugs.stream().map(Drug::getDrugRelatedGoods).forEach(arrayList::addAll);
        return arrayList;
    }

    public List<Drug> getBuyableDrugs() {
        return this.drugs.stream().filter(drug -> drug.getBuyPrice() > 0.0).collect(Collectors.toList());
    }

    public Drug getDrugByItemStack(ItemStack itemStack) {
        return this.drugs.stream().filter(drug -> drug.getDrugItemStack().isSimilar(itemStack)).findFirst().orElse(null);
    }

    public Drug getDrugBySeed(ItemStack itemStack) {
        return this.drugs.stream().filter(drug -> drug.getGrowing() != null && drug.getGrowing().getSeedItemStack().isSimilar(itemStack)).findFirst().orElse(null);
    }

    public boolean isUseableDrugItem(ItemStack itemStack) {
        return this.drugs.stream().anyMatch(drug -> drug.getDrugItemStack().isSimilar(itemStack) || drug.getGrowing() != null && drug.getGrowing().getSeedItemStack().isSimilar(itemStack) || this.fertilizers.stream().anyMatch(fertilizer -> fertilizer.getItemStack().isSimilar(itemStack)));
    }

    void registerFertilizer(Fertilizer fertilizer) {
        this.fertilizers.add(fertilizer);
    }

    public List<Fertilizer> getFertilizers() {
        return this.fertilizers;
    }

    public Fertilizer getFertilizerById(String string) {
        return this.fertilizers.stream().filter(fertilizer -> fertilizer.getId().equals(string)).findFirst().orElse(null);
    }

    public Fertilizer getFertilizerByItemStack(ItemStack itemStack) {
        return this.fertilizers.stream().filter(fertilizer -> fertilizer.getItemStack().isSimilar(itemStack)).findFirst().orElse(null);
    }

    void registerDrugDealer(DrugDealer drugDealer) {
        this.drugDealers.add(drugDealer);
    }

    public DrugDealer getDrugDealerByNPCId(int n) {
        return this.drugDealers.stream().filter(drugDealer -> drugDealer.getNpcId() == n).findFirst().orElse(null);
    }

    public List<ActiveDrugEffect> getActiveDrugEffects(Player player) {
        List list = this.activePlayerDrugs.getOrDefault(player.getUniqueId(), new ArrayList());
        list.removeIf(activeDrugEffect -> System.currentTimeMillis() >= ((ActiveDrugEffect) activeDrugEffect).getExpirationTimestamp());
        return list;
    }

    public ActiveDrugEffect getActiveDrugEffect(Player player, Drug drug) {
        List<ActiveDrugEffect> list = this.getActiveDrugEffects(player);
        return list.stream().filter(activeDrugEffect -> activeDrugEffect.getDrug().getId().equals(drug.getId())).findFirst().orElse(null);
    }

    public boolean isPlayerOnDrugs(Player player) {
        return this.getActiveDrugEffects(player).size() > 0;
    }

    public boolean isPlayerOnDrug(Player player, Drug drug) {
        return this.getActiveDrugEffects(player).stream().anyMatch(activeDrugEffect -> activeDrugEffect.getDrug().getId().equals(drug.getId()));
    }

    public void addActiveDrugEffect(Player player, Drug drug, BukkitTask bukkitTask, Long l) {
        List<ActiveDrugEffect> list = this.getActiveDrugEffects(player);
        list.add(new ActiveDrugEffect(drug, bukkitTask, l));
        this.activePlayerDrugs.put(player.getUniqueId(), list);
    }

    public void removeActiveDrugEffect(Player player, String string) {
        List<ActiveDrugEffect> list = this.getActiveDrugEffects(player);
        list.removeIf(activeDrugEffect -> activeDrugEffect.getDrug().getId().equals(string));
        this.activePlayerDrugs.put(player.getUniqueId(), list);
    }

    public void removeActiveDrugEffect(Player player, Drug drug) {
        this.removeActiveDrugEffect(player, drug.getId());
    }

    public Map<MultiplierType, Double> getPlayerMultipliers(Player player) {
        HashMap<MultiplierType, Double> hashMap = new HashMap<MultiplierType, Double>();
        List<ActiveDrugEffect> list = this.getActiveDrugEffects(player);
        for (ActiveDrugEffect activeDrugEffect : list) {
            Drug drug = activeDrugEffect.getDrug();
            if (drug == null) continue;
            for (MultiplierType multiplierType : drug.getMultipliers().keySet()) {
                if (hashMap.containsKey((Object)multiplierType)) {
                    double d = (Double)hashMap.get((Object)multiplierType);
                    hashMap.put(multiplierType, d + (drug.getMultipliers().get((Object)multiplierType) - 1.0));
                    continue;
                }
                hashMap.put(multiplierType, drug.getMultipliers().get((Object)multiplierType));
            }
        }
        return hashMap;
    }

    public void addTimedConsumeAction(UUID uUID, String string, TimedConsumeAction timedConsumeAction) {
        ActiveTimedConsumeAction activeTimedConsumeAction = new ActiveTimedConsumeAction(string, timedConsumeAction, System.currentTimeMillis() + (long)(timedConsumeAction.getDuration() / 20 * 1000));
        if (this.timedActionsToAdd.containsKey(uUID)) {
            this.timedActionsToAdd.get(uUID).add(activeTimedConsumeAction);
        } else {
            this.timedActionsToAdd.put(uUID, new ArrayList<ActiveTimedConsumeAction>(Collections.singletonList(activeTimedConsumeAction)));
        }
    }

    public void forceEndTimedConsumeActions(UUID uUID) {
        Player player = this.main.getServer().getPlayer(uUID);
        if (this.activeTimedConsumeActions.containsKey(uUID)) {
            this.activeTimedConsumeActions.get(uUID).forEach(activeTimedConsumeAction -> {
                activeTimedConsumeAction.getAction().end(player);
                if (this.timedActionsToRemove.containsKey(uUID)) {
                    this.timedActionsToRemove.get(uUID).add((ActiveTimedConsumeAction)activeTimedConsumeAction);
                } else {
                    this.timedActionsToRemove.put(uUID, new ArrayList<ActiveTimedConsumeAction>(Collections.singletonList(activeTimedConsumeAction)));
                }
            });
        }
    }

    public void forceEndTimedConsumeActions(Player player, String string) {
        if (this.activeTimedConsumeActions.containsKey(player.getUniqueId())) {
            this.activeTimedConsumeActions.get(player.getUniqueId()).stream().filter(activeTimedConsumeAction -> activeTimedConsumeAction.getDrugId().equals(string)).forEach(activeTimedConsumeAction -> {
                activeTimedConsumeAction.getAction().end(player);
                if (this.timedActionsToRemove.containsKey(player.getUniqueId())) {
                    this.timedActionsToRemove.get(player.getUniqueId()).add((ActiveTimedConsumeAction)activeTimedConsumeAction);
                } else {
                    this.timedActionsToRemove.put(player.getUniqueId(), new ArrayList<ActiveTimedConsumeAction>(Collections.singletonList(activeTimedConsumeAction)));
                }
            });
        }
    }

    public void forceEndAllTimedConsumeActions() {
        for (Map.Entry<UUID, List<ActiveTimedConsumeAction>> entry : this.activeTimedConsumeActions.entrySet()) {
            entry.getValue().forEach(activeTimedConsumeAction -> activeTimedConsumeAction.getAction().end(this.main.getServer().getPlayer((UUID)entry.getKey())));
        }
    }
}

