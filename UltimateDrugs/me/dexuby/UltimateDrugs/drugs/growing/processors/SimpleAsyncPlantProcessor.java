// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.growing.processors;

import java.security.SecureRandom;
import me.dexuby.UltimateDrugs.managers.PlantManager;
import me.dexuby.UltimateDrugs.Main;

public class SimpleAsyncPlantProcessor extends PlantProcessor
{
    private final Main mainInstance;
    private final PlantManager plantManager;
    private final SecureRandom random;
    
    public SimpleAsyncPlantProcessor(final Main mainInstance) {
        super(mainInstance);
        this.random = new SecureRandom();
        this.mainInstance = mainInstance;
        this.plantManager = mainInstance.getPlantManager();
    }
    
    @Override
    public void start() {
    }
}
