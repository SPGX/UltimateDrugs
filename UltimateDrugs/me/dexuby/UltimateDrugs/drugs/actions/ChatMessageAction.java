// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.actions;

import me.dexuby.UltimateDrugs.utils.TextUtils;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.command.CommandSender;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import java.util.HashMap;
import org.bukkit.Bukkit;
import me.dexuby.UltimateDrugs.drugs.Drug;
import java.util.UUID;

public class ChatMessageAction extends ConsumeAction
{
    private String chatMessage;
    
    public ChatMessageAction(final int n, final double n2, final int n3, final int n4, final String s, final String chatMessage) {
        super(n, n2, n3, n4, s);
        this.chatMessage = chatMessage;
    }
    
    @Override
    public void execute(final UUID uuid, final Drug drug) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isValid() && player.isOnline()) {
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("%player%", player.getName());
            hashMap.put("%drug_id%", drug.getId());
            if (this.getTargetSelector() != null) {
                for (final Entity entity : ReflectionUtils.getEntitiesFromSelector((CommandSender)player, this.getTargetSelector())) {
                    if (entity instanceof Player) {
                        hashMap.put("%target%", entity.getName());
                    }
                    this.apply((CommandSender)entity, hashMap);
                }
            }
            else {
                this.apply((CommandSender)player, hashMap);
            }
        }
    }
    
    private void apply(final CommandSender commandSender, final Map<String, String> map) {
        commandSender.sendMessage(TextUtils.replaceVariables(this.chatMessage, map));
    }
    
    public String getChatMessage() {
        return this.chatMessage;
    }
    
    @Override
    public ChatMessageAction clone() {
        return new ChatMessageAction(this.getDelay(), this.getChance(), this.getRepetitions(), this.getRepetitionTickDelay(), this.getTargetSelector(), this.chatMessage);
    }
}
