// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.growing;

import me.dexuby.UltimateDrugs.block.BlockOption;
import org.bukkit.block.Biome;
import java.util.Collection;
import java.util.List;

public class PlantType
{
    private List<GrowingStage> growingStages;
    private boolean removeIfFlying;
    private boolean waterRequired;
    private boolean lavaRequired;
    private Collection<Biome> biomeWhitelist;
    private WorldTime requiredWorldTime;
    private Collection<Weather> weatherWhitelist;
    private Collection<BlockOption> plantBlockWhitelist;
    private Collection<BlockOption> growBlockWhitelist;
    
    public PlantType(final List<GrowingStage> growingStages, final boolean removeIfFlying, final boolean waterRequired, final boolean lavaRequired, final Collection<Biome> biomeWhitelist, final WorldTime requiredWorldTime, final Collection<Weather> weatherWhitelist, final Collection<BlockOption> plantBlockWhitelist, final Collection<BlockOption> growBlockWhitelist) {
        this.growingStages = growingStages;
        this.removeIfFlying = removeIfFlying;
        this.waterRequired = waterRequired;
        this.lavaRequired = lavaRequired;
        this.biomeWhitelist = biomeWhitelist;
        this.requiredWorldTime = requiredWorldTime;
        this.weatherWhitelist = weatherWhitelist;
        this.plantBlockWhitelist = plantBlockWhitelist;
        this.growBlockWhitelist = growBlockWhitelist;
    }
    
    public List<GrowingStage> getGrowingStages() {
        return this.growingStages;
    }
    
    public boolean doRemoveIfFlying() {
        return this.removeIfFlying;
    }
    
    public boolean isWaterRequired() {
        return this.waterRequired;
    }
    
    public boolean isLavaRequired() {
        return this.lavaRequired;
    }
    
    public Collection<Biome> getBiomeWhitelist() {
        return this.biomeWhitelist;
    }
    
    public WorldTime getRequiredWorldTime() {
        return this.requiredWorldTime;
    }
    
    public Collection<Weather> getWeatherWhitelist() {
        return this.weatherWhitelist;
    }
    
    public Collection<BlockOption> getPlantBlockWhitelist() {
        return this.plantBlockWhitelist;
    }
    
    public Collection<BlockOption> getGrowBlockWhitelist() {
        return this.growBlockWhitelist;
    }
}
