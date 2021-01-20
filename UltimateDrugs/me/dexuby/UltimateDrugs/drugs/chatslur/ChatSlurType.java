// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.chatslur;

import me.dexuby.UltimateDrugs.drugs.chatslur.strategies.StutterChatSlurStrategy;
import me.dexuby.UltimateDrugs.drugs.chatslur.strategies.SmileysChatSlurStrategy;
import me.dexuby.UltimateDrugs.drugs.chatslur.strategies.ChatSlurStrategy;

public enum ChatSlurType
{
    NONE((ChatSlurStrategy)null), 
    SMILEYS((ChatSlurStrategy)new SmileysChatSlurStrategy()), 
    STUTTER((ChatSlurStrategy)new StutterChatSlurStrategy());
    
    private ChatSlurStrategy slurStrategy;
    
    private ChatSlurType(final ChatSlurStrategy slurStrategy) {
        this.slurStrategy = slurStrategy;
    }
    
    public ChatSlurStrategy getChatSlurStrategy() {
        return this.slurStrategy;
    }
}
