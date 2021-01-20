// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.growing;

import org.bukkit.World;

public class ChunkLocation
{
    private World world;
    private int x;
    private int z;
    
    public ChunkLocation(final World world, final int x, final int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }
    
    public World getWorld() {
        return this.world;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getZ() {
        return this.z;
    }
    
    @Override
    public String toString() {
        return String.format("WORLD:%s|X:%d|Z:%d", this.world.getName(), this.x, this.z);
    }
}
