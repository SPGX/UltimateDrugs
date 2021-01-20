// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.utils;

import org.bukkit.Material;

public class MaterialUtils
{
    public static boolean isAir(final Material material) {
        return material == Material.AIR || material == Material.CAVE_AIR || material == Material.VOID_AIR;
    }
}
