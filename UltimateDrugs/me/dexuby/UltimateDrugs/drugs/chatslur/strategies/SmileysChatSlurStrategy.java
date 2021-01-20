// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.chatslur.strategies;

import java.util.Random;

public class SmileysChatSlurStrategy implements ChatSlurStrategy
{
    private final Random random;
    private final String[] smileys;
    
    public SmileysChatSlurStrategy() {
        this.random = new Random();
        this.smileys = new String[] { ":)", "(:", ":D", ";)", "(;", ";D" };
    }
    
    @Override
    public String convert(final String str) {
        final StringBuilder sb = new StringBuilder();
        if (str.contains(" ")) {
            final String[] split = str.split(" ");
            for (int i = 0; i < split.length; ++i) {
                final String s = split[i];
                if (this.random.nextDouble() <= 0.4) {
                    sb.append(this.smileys[this.random.nextInt(this.smileys.length - 1)]).append(" ").append(s);
                }
                else {
                    sb.append(s);
                }
                if (i < split.length - 1) {
                    sb.append(" ");
                }
            }
        }
        else {
            sb.append(str);
        }
        sb.append(" ").append(this.smileys[this.random.nextInt(this.smileys.length - 1)]);
        return sb.toString();
    }
}
