// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.managers;

import org.bukkit.block.Block;
import java.util.List;
import org.bukkit.plugin.Plugin;
import me.dexuby.UltimateDrugs.config.SettingsHolder;
import java.util.HashSet;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import java.util.Set;
import me.dexuby.UltimateDrugs.drugs.vanilla.VanillaDrop;
import java.util.Collection;
import me.dexuby.UltimateDrugs.Main;

public class VanillaDropManager
{
    private Main main;
    private Collection<VanillaDrop> vanillaDrops;
    private Set<Material> vanillaBlockDropMaterials;
    private Set<Location> playerPlacedBlockLocations;
    
    public VanillaDropManager(final Main main) {
        this.vanillaDrops = new ArrayList<VanillaDrop>();
        this.vanillaBlockDropMaterials = new HashSet<Material>();
        this.playerPlacedBlockLocations = new HashSet<Location>();
        this.main = main;
        main.getServer().getScheduler().runTaskTimer((Plugin)main, () -> {
            if ((boolean) SettingsHolder.VANILLA_STORE_PLAYER_BLOCKS.getValue()) {
                main.getServer().getScheduler().runTaskAsynchronously((Plugin)main, () -> {
                    main.getConfigManager().updatePlayerPlacedBlockCache(new HashSet<Location>(this.playerPlacedBlockLocations));
                    main.getConfigManager().saveStorage();
                });
            }
        }, 1200L, 1200L);
    }
    
    public Collection<VanillaDrop> getVanillaDrops() {
        return this.vanillaDrops;
    }
    
    public void addVanillaDrop(final VanillaDrop vanillaDrop) {
        this.vanillaDrops.add(vanillaDrop);
    }
    
    public void addVanillaDropMaterial(final Material material) {
        this.vanillaBlockDropMaterials.add(material);
    }
    
    public void addVanillaBlockDropMaterials(final List<Material> list) {
        this.vanillaBlockDropMaterials.addAll(list);
    }
    
    public boolean isVanillaBlockDropMaterial(final Material material) {
        return this.vanillaBlockDropMaterials.contains(material);
    }
    
    public void addPlayerPlacedBlockLocation(final Location location) {
        this.playerPlacedBlockLocations.add(location);
    }
    
    public void removePlayerPlacedBlockLocation(final Location location) {
        this.playerPlacedBlockLocations.remove(location);
    }
    
    public boolean isPlayerPlacedBlock(final Block block) {
        return this.playerPlacedBlockLocations.contains(block.getLocation());
    }
    
    public Set<Location> getPlayerPlacedBlockLocations() {
        return this.playerPlacedBlockLocations;
    }
}
