// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.growing.processors;

import me.dexuby.UltimateDrugs.drugs.growing.Plant;
import java.util.Queue;
import me.dexuby.UltimateDrugs.Main;

public abstract class PlantProcessor
{
    private final Main mainInstance;
    private Queue<Plant> plants;
    
    PlantProcessor(final Main mainInstance) {
        this.mainInstance = mainInstance;
    }
    
    public PlantProcessor setPlants(final Queue<Plant> plants) {
        this.plants = plants;
        return this;
    }
    
    Main getMainInstance() {
        return this.mainInstance;
    }
    
    Queue<Plant> getPlants() {
        return this.plants;
    }
    
    public abstract void start();
}
