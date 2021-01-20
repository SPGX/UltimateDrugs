// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.commands;

import org.bukkit.block.Block;
import java.util.Map;
import me.dexuby.UltimateDrugs.utils.TextUtils;
import java.util.HashMap;
import java.util.Set;
import org.bukkit.entity.Player;
import java.util.List;
import org.bukkit.command.CommandSender;
import me.dexuby.UltimateDrugs.managers.ConfigManager;
import me.dexuby.UltimateDrugs.Main;

public class DrugBlockInfoCommand extends CustomCommand
{
    private Main main;
    private ConfigManager configManager;
    
    public DrugBlockInfoCommand(final Main main) {
        super("dr", new String[] { "blockinfo" }, "Used to display the block data of the block you are looking at.", "/dr blockinfo", "ultimatedrugs.blockinfo", 0, 0);
        this.main = main;
        this.configManager = main.getConfigManager();
    }
    
    @Override
    public void executeCommand(final CommandSender commandSender, final List<String> list) {
        if (commandSender instanceof Player) {
            final Player player = (Player)commandSender;
            final Block targetBlock = player.getTargetBlock((Set)null, 6);
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("%block_data%", targetBlock.getBlockData().getAsString());
            player.sendMessage(TextUtils.replaceVariables(this.configManager.getLangString("block-info"), hashMap));
        }
        else {
            commandSender.sendMessage(this.configManager.getLangString("no-console-command"));
        }
    }
    
    @Override
    public List<String> getTabCompletion(final String[] array) {
        return null;
    }
}
