// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.chatslur;

import me.dexuby.UltimateDrugs.drugs.chatslur.strategies.ChatSlurStrategy;

public class ChatSlurConverter
{
    private String chatMessage;
    private ChatSlurStrategy slurStrategy;
    
    public ChatSlurConverter(final String chatMessage) {
        this.chatMessage = chatMessage;
    }
    
    public ChatSlurConverter setSlurStrategy(final ChatSlurStrategy slurStrategy) {
        this.slurStrategy = slurStrategy;
        return this;
    }
    
    public String convert() {
        return (this.slurStrategy != null) ? this.slurStrategy.convert(this.chatMessage) : this.chatMessage;
    }
}
