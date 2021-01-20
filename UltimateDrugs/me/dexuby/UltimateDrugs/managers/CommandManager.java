// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.managers;

import org.bukkit.command.CommandExecutor;
import java.util.ArrayList;
import me.dexuby.UltimateDrugs.commands.CustomCommand;
import java.util.List;
import me.dexuby.UltimateDrugs.handlers.CommandHandler;
import me.dexuby.UltimateDrugs.Main;

public class CommandManager
{
    private Main main;
    private CommandHandler commandHandler;
    private List<CustomCommand> registeredCommands;
    
    public CommandManager(final Main main) {
        this.registeredCommands = new ArrayList<CustomCommand>();
        this.main = main;
        this.commandHandler = new CommandHandler(main);
    }
    
    public List<CustomCommand> getRegisteredCommands() {
        return this.registeredCommands;
    }
    
    public void registerCommand(final CustomCommand customCommand) {
        this.main.getCommand(customCommand.getName()).setExecutor((CommandExecutor)this.commandHandler);
        this.registeredCommands.add(customCommand);
    }
}
