// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.utils;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.google.common.base.Preconditions;
import org.bukkit.Location;
import com.sk89q.worldguard.WorldGuard;
import me.dexuby.UltimateDrugs.drugs.Drug;
import org.bukkit.entity.Player;

public class WGUtils
{
    public static boolean isInDisabledRegion(final Player player, final Drug drug) {
        return WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().getApplicableRegions(adapt(player.getLocation())).getRegions().stream().anyMatch(protectedRegion -> drug.getConsumeConditions().getDisabledRegionList().contains(protectedRegion.getId()));
    }
    
    private static com.sk89q.worldedit.util.Location adapt(final Location location) {
        Preconditions.checkNotNull((Object)location);
        return new com.sk89q.worldedit.util.Location((Extent)new BukkitWorld(location.getWorld()), Vector3.at(location.getX(), location.getY(), location.getZ()), location.getYaw(), location.getPitch());
    }
}
