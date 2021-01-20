// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.actions;

import org.bukkit.entity.Player;

public abstract class TimedConsumeAction extends ConsumeAction
{
    private int duration;
    
    TimedConsumeAction(final int n, final double n2, final int n3, final int n4, final String s, final int duration) {
        super(n, n2, n3, n4, s);
        this.duration = duration;
    }
    
    public int getDuration() {
        return this.duration;
    }
    
    public abstract void end(final Player p0);
}
