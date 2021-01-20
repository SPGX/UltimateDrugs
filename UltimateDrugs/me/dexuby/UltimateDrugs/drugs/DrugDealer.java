// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs;

public class DrugDealer
{
    private int npcId;
    private DrugDealerType type;
    
    public DrugDealer(final int npcId, final DrugDealerType type) {
        this.npcId = npcId;
        this.type = type;
    }
    
    public int getNpcId() {
        return this.npcId;
    }
    
    public DrugDealerType getType() {
        return this.type;
    }
}
