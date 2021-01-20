// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.commands;

import me.dexuby.UltimateDrugs.managers.DrugManager;
import java.util.List;
import org.bukkit.command.CommandSender;
import me.dexuby.UltimateDrugs.managers.ConfigManager;
import me.dexuby.UltimateDrugs.Main;

public class ReloadCommand extends CustomCommand
{
    private Main main;
    private ConfigManager configManager;
    
    public ReloadCommand(final Main main) {
        super("dr", new String[] { "reload" }, "Used to reload the plugin files.", "/dr reload", "ultimatedrugs.reload", 0, 0);
        this.main = main;
        this.configManager = main.getConfigManager();
    }
    
    @Override
    public void executeCommand(final CommandSender commandSender, final List<String> list) {
        final DrugManager drugManager = this.main.getDrugManager();
        drugManager.getDrugs().clear();
        drugManager.clearDrugCache();
        this.main.getVanillaDropManager().getVanillaDrops().clear();
        this.configManager.getLangCache().clear();
        this.configManager.reloadConfig();
        this.configManager.reloadLang();
        commandSender.sendMessage(this.configManager.getLangString("files-reloaded"));
    }
    
    @Override
    public List<String> getTabCompletion(final String[] array) {
        return null;
    }
}
