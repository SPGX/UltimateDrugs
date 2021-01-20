/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 */
package me.dexuby.UltimateDrugs.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import me.dexuby.UltimateDrugs.Main;
import me.dexuby.UltimateDrugs.commands.CustomCommand;
import me.dexuby.UltimateDrugs.managers.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CommandHandler
implements CommandExecutor,
TabCompleter {
    private Main main;
    private ConfigManager configManager;

    public CommandHandler(Main main) {
        this.main = main;
        this.configManager = main.getConfigManager();
    }

    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
    	 List<CustomCommand> collection = this.main.getCommandManager().getRegisteredCommands().stream().filter(customCommand -> (customCommand.getName().equalsIgnoreCase(string) || command.getAliases().contains(customCommand.getName().toLowerCase())) && customCommand.getSubNames().length <= arrstring.length).collect(Collectors.toList());
        for (CustomCommand customCommand2 : collection) {
            boolean bl = true;
            for (int i = 0; i < customCommand2.getSubNames().length; ++i) {
                if (arrstring[i].equalsIgnoreCase(customCommand2.getSubNames()[i])) continue;
                bl = false;
                break;
            }
            if (!bl) continue;
            if (commandSender.hasPermission(customCommand2.getPermission())) {
                if (arrstring.length < customCommand2.getSubNames().length + customCommand2.getMinArgs() || arrstring.length > customCommand2.getSubNames().length + customCommand2.getMaxArgs()) {
                    commandSender.sendMessage(customCommand2.getUsage());
                    return true;
                }
                ArrayList<String> arrayList = new ArrayList<String>(customCommand2.getSubNames().length > 0 ? Arrays.asList(arrstring).subList(customCommand2.getSubNames().length, arrstring.length) : Arrays.asList(arrstring));
                customCommand2.executeCommand(commandSender, arrayList);
                return true;
            }
            commandSender.sendMessage(this.configManager.getLangString("command-no-permission"));
            return true;
        }
        commandSender.sendMessage(this.configManager.getLangString("command-not-found"));
        return true;
    }

    public List<String> onTabComplete(CommandSender commandSender, Command command, String string, String[] arrstring) {
        if (arrstring.length == 1) {
            return this.main.getCommandManager().getRegisteredCommands().stream().filter(customCommand -> customCommand.getSubNames().length > 0 && customCommand.getSubNames()[0].startsWith(arrstring[0])).map(customCommand -> customCommand.getSubNames()[0]).collect(Collectors.toList());
        }
        List<CustomCommand> collection = this.main.getCommandManager().getRegisteredCommands().stream().filter(customCommand -> (customCommand.getName().equalsIgnoreCase(string) || command.getAliases().contains(customCommand.getName().toLowerCase())) && customCommand.getSubNames().length <= arrstring.length).collect(Collectors.toList());
        for (CustomCommand customCommand2 : collection) {
            boolean bl;
            if (!commandSender.hasPermission(customCommand2.getPermission()) || customCommand2.getSubNames().length <= 0 || !customCommand2.getSubNames()[0].equalsIgnoreCase(arrstring[0])) continue;
            boolean bl2 = bl = customCommand2.getSubNames().length >= arrstring.length;
            if (bl) {
                return Collections.singletonList(customCommand2.getSubNames()[arrstring.length - 1]);
            }
            return customCommand2.getTabCompletion(Arrays.copyOfRange(arrstring, customCommand2.getSubNames().length, arrstring.length));
        }
        return null;
    }
}

