// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.chatslur.strategies;

import java.util.Random;

public class StutterChatSlurStrategy implements ChatSlurStrategy
{
    private final Random random;
    
    public StutterChatSlurStrategy() {
        this.random = new Random();
    }
    
    @Override
    public String convert(final String s) {
        if (!s.contains(" ")) {
            return s;
        }
        final StringBuilder sb = new StringBuilder();
        final String[] split = s.split(" ");
        for (int i = 0; i < split.length; ++i) {
            final String str = split[i];
            if (this.random.nextDouble() <= 0.3) {
                sb.append(String.format("%c-%s", str.charAt(0), str));
            }
            else {
                sb.append(str);
            }
            if (i < split.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
