// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.config;

import me.dexuby.UltimateDrugs.drugs.growing.processors.ProcessorType;
import java.util.ArrayList;

public enum SettingsHolder
{
    VANILLA_AUTO_REPLANT((Object)false), 
    VANILLA_AUTO_IGNORE_DRUG_PLANTS((Object)true), 
    VANILLA_AUTO_REPLANT_BLACKLIST((Object)new ArrayList()), 
    VANILLA_STORE_PLAYER_BLOCKS((Object)false), 
    BLOCK_SHUTDOWN_SAVE((Object)false), 
    PLAYER_PLANT_LIMIT((Object)(-1)), 
    PLANT_PROCESSOR_TYPE((Object)ProcessorType.SYNC), 
    STRUCTURE_PREVIEW((Object)true), 
    STRUCTURE_PREVIEW_UPDATE_DELAY((Object)5L);
    
    private Object value;
    
    private SettingsHolder(final Object value) {
        this.value = value;
    }
    
    public Object getValue() {
        return this.value;
    }
    
    public void setValue(final Object value) {
        this.value = value;
    }
}
