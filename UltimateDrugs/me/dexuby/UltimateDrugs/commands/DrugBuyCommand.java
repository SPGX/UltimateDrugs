// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.commands;

import me.dexuby.UltimateDrugs.guis.GUI;
import org.bukkit.entity.Player;
import java.util.List;
import org.bukkit.command.CommandSender;
import me.dexuby.UltimateDrugs.managers.ConfigManager;
import me.dexuby.UltimateDrugs.Main;

public class DrugBuyCommand extends CustomCommand
{
    private Main main;
    private ConfigManager configManager;
    
    public DrugBuyCommand(final Main main) {
        super("dr", new String[] { "buy" }, "Used to buy drugs.", "/dr buy <player>", "ultimatedrugs.buy", 0, 1);
        this.main = main;
        this.configManager = main.getConfigManager();
    }
    
    @Override
    public void executeCommand(final CommandSender commandSender, final List<String> list) {
        Player player;
        if (list.size() > 0) {
            player = this.main.getServer().getPlayer((String)list.get(0));
            if (player == null) {
                commandSender.sendMessage(this.configManager.getLangString("player-not-found"));
                return;
            }
        }
        else {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(this.configManager.getLangString("no-console-command"));
                return;
            }
            player = (Player)commandSender;
        }
        final GUI guiById = this.main.getGUIManager().getGUIById("drug-buy");
        if (guiById != null) {
            guiById.open(player, new Object[0]);
        }
        else {
            commandSender.sendMessage(this.configManager.getLangString("gui-not-found"));
        }
    }
    
    @Override
    public List<String> getTabCompletion(final String[] array) {
        return null;
    }
}
