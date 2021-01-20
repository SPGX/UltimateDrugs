// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.actions;

import me.dexuby.UltimateDrugs.drugs.Drug;
import java.util.UUID;

public abstract class ConsumeAction
{
    private int delay;
    private double chance;
    private int repetitions;
    private int repetitionTickDelay;
    private String targetSelector;
    
    ConsumeAction(final int delay, final double chance, final int repetitions, final int repetitionTickDelay, final String targetSelector) {
        this.delay = delay;
        this.chance = chance;
        this.repetitions = repetitions;
        this.repetitionTickDelay = repetitionTickDelay;
        this.targetSelector = targetSelector;
    }
    
    public int getDelay() {
        return this.delay;
    }
    
    public void setDelay(final int delay) {
        this.delay = delay;
    }
    
    public double getChance() {
        return this.chance;
    }
    
    public int getRepetitions() {
        return this.repetitions;
    }
    
    public int getRepetitionTickDelay() {
        return this.repetitionTickDelay;
    }
    
    public String getTargetSelector() {
        return this.targetSelector;
    }
    
    public abstract void execute(final UUID p0, final Drug p1);
    
    public abstract ConsumeAction clone();
}
