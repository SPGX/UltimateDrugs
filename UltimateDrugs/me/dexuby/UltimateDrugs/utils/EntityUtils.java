// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.utils;

import me.dexuby.UltimateDrugs.drugs.growing.Direction;
import org.bukkit.entity.Entity;

public class EntityUtils
{
    public static Direction getDirection(final Entity entity) {
        float yaw = entity.getLocation().getYaw();
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        if (yaw >= 315.0f || yaw < 45.0f) {
            return Direction.SOUTH;
        }
        if (yaw < 135.0f) {
            return Direction.WEST;
        }
        if (yaw < 225.0f) {
            return Direction.NORTH;
        }
        return Direction.EAST;
    }
}
