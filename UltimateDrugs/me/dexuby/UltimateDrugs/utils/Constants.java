// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import org.bukkit.Material;
import java.util.Set;

public class Constants
{
    public static final Set<Material> AUTO_CONNECT_MATERIALS;
    public static final Set<String> POSITIVE_EFFECT_TYPES;
    public static final Set<String> NEGATIVE_EFFECT_TYPES;
    
    static {
        AUTO_CONNECT_MATERIALS = new HashSet<Material>(Arrays.asList(Material.COCOA, Material.ATTACHED_PUMPKIN_STEM, Material.ATTACHED_MELON_STEM));
        POSITIVE_EFFECT_TYPES = new HashSet<String>(Arrays.asList("ABSORPTION", "CONDUIT_POWER", "DAMAGE_RESISTANCE", "DOLPHINS_GRACE", "FAST_DIGGING", "FIRE_RESISTANCE", "HEALTH_BOOST", "INCREASE_DAMAGE", "INVISIBILITY", "JUMP", "LUCK", "NIGHT_VISION", "REGENERATION", "SATURATION", "SLOW_FALLING", "SPEED", "WATER_BREATHING"));
        NEGATIVE_EFFECT_TYPES = new HashSet<String>(Arrays.asList("BLINDNESS", "CONFUSION", "GLOWING", "HUNGER", "LEVITATION", "POISON", "SLOW", "SLOW_DIGGING", "UNLUCK", "WEAKNESS", "WITHER"));
    }
}
