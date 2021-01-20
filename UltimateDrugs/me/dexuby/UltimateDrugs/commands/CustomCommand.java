// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.commands;

import java.util.List;
import org.bukkit.command.CommandSender;

public abstract class CustomCommand
{
    private String name;
    private String[] subNames;
    private String description;
    private String usage;
    private String permission;
    private int minArgs;
    private int maxArgs;
    
    CustomCommand(final String name, final String[] subNames, final String description, final String usage, final String permission, final int minArgs, final int maxArgs) {
        this.name = name;
        this.subNames = subNames;
        this.description = description;
        this.usage = usage;
        this.permission = permission;
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String[] getSubNames() {
        return this.subNames;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getUsage() {
        return this.usage;
    }
    
    public String getPermission() {
        return this.permission;
    }
    
    public int getMinArgs() {
        return this.minArgs;
    }
    
    public int getMaxArgs() {
        return this.maxArgs;
    }
    
    public abstract void executeCommand(final CommandSender p0, final List<String> p1);
    
    public abstract List<String> getTabCompletion(final String[] p0);
}
