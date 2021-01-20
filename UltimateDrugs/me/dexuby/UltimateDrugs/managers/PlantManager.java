/*
 * Decompiled with CFR 0.150.
 */
package me.dexuby.UltimateDrugs.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Collectors;
import me.dexuby.UltimateDrugs.drugs.growing.GrowingStage;
import me.dexuby.UltimateDrugs.drugs.growing.Plant;
import me.dexuby.UltimateDrugs.drugs.growing.PlantType;

public class PlantManager {
    private Collection<Plant> plants = new ArrayList<Plant>();
    private Collection<Plant> growingPlants = new ArrayList<Plant>();
    private Collection<Plant> toAdd = new ArrayList<Plant>();
    private Collection<UUID> toRemove = new ArrayList<UUID>();

    Collection<Plant> getRegisteredPlants() {
        return this.plants;
    }

    public Collection<Plant> getGrowingPlants() {
        return this.growingPlants;
    }

    public Collection<UUID> getToRemove() {
        return this.toRemove;
    }

    public void updateGrowingPlants() {
        this.growingPlants.addAll(this.toAdd);
        this.toAdd.clear();
        this.growingPlants.removeIf(plant -> this.toRemove.contains(plant.getUUID()));
        this.toRemove.clear();
    }

    public GrowingStage getNextGrowingStage(Plant plant, PlantType plantType) {
        return plantType.getGrowingStages().stream().filter(growingStage -> growingStage.getOrderId() > plant.getCurrentGrowingStage().getOrderId()).findFirst().orElse(plant.getCurrentGrowingStage());
    }

    public GrowingStage getHighestGrowingStage(PlantType plantType) {
        return plantType.getGrowingStages().stream().max(Comparator.comparing(GrowingStage::getOrderId)).orElse(null);
    }

    public void registerPlant(Plant plant) {
        this.plants.add(plant);
    }

    public void registerGrowingPlant(Plant plant) {
        this.toAdd.add(plant);
    }

    public void unregisterPlant(UUID uUID) {
        this.plants.removeIf(plant -> plant.getUUID().equals(uUID));
        this.toAdd.removeIf(plant -> plant.getUUID().equals(uUID));
        if (this.growingPlants.stream().anyMatch(plant -> plant.getUUID().equals(uUID)) && !this.toRemove.contains(uUID)) {
            this.toRemove.add(uUID);
        }
    }

    public Plant getPlantByUUID(UUID uUID) {
        return this.plants.stream().filter(plant -> plant.getUUID().equals(uUID)).findFirst().orElse(null);
    }

    public Collection<Plant> getPlantsByOwner(UUID uUID) {
        return this.plants.stream().filter(plant -> plant.getOwner().equals(uUID)).collect(Collectors.toList());
    }
}

