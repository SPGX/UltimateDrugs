// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs;

public abstract class DrugRecipe
{
    private String id;
    
    DrugRecipe(final String id) {
        this.id = id;
    }
    
    public String getId() {
        return this.id;
    }
}
