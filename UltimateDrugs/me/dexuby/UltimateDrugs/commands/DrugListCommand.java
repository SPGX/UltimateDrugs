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

public class DrugListCommand extends CustomCommand
{
    private Main main;
    private ConfigManager configManager;
    
    public DrugListCommand(final Main main) {
        super("dr", new String[] { "list" }, "Used to list all configured drugs.", "/dr list", "ultimatedrugs.list", 0, 0);
        this.main = main;
        this.configManager = main.getConfigManager();
    }
    
    @Override
    public void executeCommand(final CommandSender commandSender, final List<String> list) {
        if (commandSender instanceof Player) {
            final Player player = (Player)commandSender;
            final GUI guiById = this.main.getGUIManager().getGUIById("drug-list");
            if (guiById != null) {
                guiById.open(player, new Object[0]);
            }
            else {
                player.sendMessage(this.configManager.getLangString("gui-not-found"));
            }
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
