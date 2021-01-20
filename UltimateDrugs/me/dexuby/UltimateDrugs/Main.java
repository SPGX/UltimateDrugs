package me.dexuby.UltimateDrugs;

import java.net.URLConnection;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import me.dexuby.UltimateDrugs.commands.ReloadCommand;
import me.dexuby.UltimateDrugs.commands.HelpCommand;
import me.dexuby.UltimateDrugs.commands.DrugSellCommand;
import me.dexuby.UltimateDrugs.commands.DrugSeedGiveCommand;
import me.dexuby.UltimateDrugs.commands.DrugListCommand;
import me.dexuby.UltimateDrugs.commands.DrugGiveCommand;
import me.dexuby.UltimateDrugs.commands.DrugFertilizerGiveCommand;
import me.dexuby.UltimateDrugs.commands.DrugBuyCommand;
import me.dexuby.UltimateDrugs.commands.DrugBoostersCommand;
import me.dexuby.UltimateDrugs.commands.DrugBoosterGiveCommand;
import me.dexuby.UltimateDrugs.commands.CustomCommand;
import me.dexuby.UltimateDrugs.commands.DrugBlockInfoCommand;
import me.dexuby.UltimateDrugs.events.WeaponDamageEntity;
import me.dexuby.UltimateDrugs.events.PlayerItemConsume;
import me.dexuby.UltimateDrugs.events.PlayerInteract;
import me.dexuby.UltimateDrugs.events.NPCRightClick;
import me.dexuby.UltimateDrugs.events.McMMOPlayerXpGain;
import me.dexuby.UltimateDrugs.events.JobsPayment;
import me.dexuby.UltimateDrugs.events.JobsExpGain;
import me.dexuby.UltimateDrugs.events.EntityPotionEffect;
import me.dexuby.UltimateDrugs.events.BlockBreak;
import me.dexuby.UltimateDrugs.handlers.VanillaHandler;
import me.dexuby.UltimateDrugs.handlers.RecipeHandler;
import me.dexuby.UltimateDrugs.handlers.ProtocolLibHandler;
import me.dexuby.UltimateDrugs.handlers.PlantHandler;
import me.dexuby.UltimateDrugs.handlers.MultiplierHandler;
import me.dexuby.UltimateDrugs.handlers.GUIHandler;
import org.bukkit.event.Listener;
import me.dexuby.UltimateDrugs.handlers.DrugHandler;
import me.dexuby.UltimateDrugs.config.SettingsHolder;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.Plugin;
import me.dexuby.UltimateDrugs.managers.CommandManager;
import me.dexuby.UltimateDrugs.managers.GUIManager;
import me.dexuby.UltimateDrugs.managers.ConfigManager;
import me.dexuby.UltimateDrugs.managers.VanillaDropManager;
import me.dexuby.UltimateDrugs.managers.StructureManager;
import me.dexuby.UltimateDrugs.managers.RecipeManager;
import me.dexuby.UltimateDrugs.managers.PlantManager;
import me.dexuby.UltimateDrugs.managers.DrugBoosterManager;
import me.dexuby.UltimateDrugs.managers.DrugManager;
import org.bukkit.plugin.PluginManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
    private static Main instance;
    private Economy economy;
    private PluginManager pluginManager;
    private DrugManager drugManager;
    private DrugBoosterManager drugBoosterManager;
    private PlantManager plantManager;
    private RecipeManager recipeManager;
    private StructureManager structureManager;
    private VanillaDropManager vanillaDropManager;
    private ConfigManager configManager;
    private GUIManager guiManager;
    private CommandManager commandManager;
    
    public void onEnable() {
        //loadConfig();
        Main.instance = this;
        this.registerManagers();
        this.registerHandlers();
        this.registerEvents();
        this.registerCommands();
        if (this.pluginManager.isPluginEnabled("Vault")) {
            final RegisteredServiceProvider registration = this.getServer().getServicesManager().getRegistration((Class)Economy.class);
            if (registration != null) {
                this.economy = (Economy)registration.getProvider();
            }
        }
    }
    
    public void onDisable() {
        if (!(boolean)SettingsHolder.BLOCK_SHUTDOWN_SAVE.getValue()) {
            this.configManager.updateStorage();
        }
        this.drugManager.forceEndAllTimedConsumeActions();
    }
    
    private void registerManagers() {
        this.pluginManager = this.getServer().getPluginManager();
        this.drugManager = new DrugManager(this);
        this.drugBoosterManager = new DrugBoosterManager(this);
        this.plantManager = new PlantManager();
        this.recipeManager = new RecipeManager(this);
        this.structureManager = new StructureManager(this);
        this.vanillaDropManager = new VanillaDropManager(this);
        this.configManager = new ConfigManager(this);
        this.guiManager = new GUIManager(this);
        this.commandManager = new CommandManager(this);
    }
    
    private void registerHandlers() {
        this.pluginManager.registerEvents((Listener)new DrugHandler(this), (Plugin)this);
        this.pluginManager.registerEvents((Listener)new GUIHandler(this), (Plugin)this);
        this.pluginManager.registerEvents((Listener)new MultiplierHandler(this), (Plugin)this);
        this.pluginManager.registerEvents((Listener)new PlantHandler(this), (Plugin)this);
        if (this.pluginManager.isPluginEnabled("ProtocolLib")) {
            new ProtocolLibHandler(this);
        }
        this.pluginManager.registerEvents((Listener)new RecipeHandler(this), (Plugin)this);
        this.pluginManager.registerEvents((Listener)new VanillaHandler(this), (Plugin)this);
    }
    
    private void registerEvents() {
        this.pluginManager.registerEvents((Listener)new BlockBreak(this), (Plugin)this);
        this.pluginManager.registerEvents((Listener)new EntityPotionEffect(this), (Plugin)this);
        if (this.pluginManager.isPluginEnabled("Jobs")) {
            this.pluginManager.registerEvents((Listener)new JobsExpGain(this), (Plugin)this);
            this.pluginManager.registerEvents((Listener)new JobsPayment(this), (Plugin)this);
        }
        if (this.pluginManager.isPluginEnabled("mcMMO")) {
            this.pluginManager.registerEvents((Listener)new McMMOPlayerXpGain(this), (Plugin)this);
        }
        if (this.pluginManager.isPluginEnabled("Citizens")) {
            this.pluginManager.registerEvents((Listener)new NPCRightClick(this), (Plugin)this);
        }
        this.pluginManager.registerEvents((Listener)new PlayerInteract(this), (Plugin)this);
        this.pluginManager.registerEvents((Listener)new PlayerItemConsume(this), (Plugin)this);
        if (this.pluginManager.isPluginEnabled("CrackShot")) {
            this.pluginManager.registerEvents((Listener)new WeaponDamageEntity(this), (Plugin)this);
        }
        this.configManager.fillEventSlots();
    }
    
    private void registerCommands() {
        this.commandManager.registerCommand(new DrugBlockInfoCommand(this));
        this.commandManager.registerCommand(new DrugBoosterGiveCommand(this));
        this.commandManager.registerCommand(new DrugBoostersCommand(this));
        this.commandManager.registerCommand(new DrugBuyCommand(this));
        this.commandManager.registerCommand(new DrugFertilizerGiveCommand(this));
        this.commandManager.registerCommand(new DrugGiveCommand(this));
        this.commandManager.registerCommand(new DrugListCommand(this));
        this.commandManager.registerCommand(new DrugSeedGiveCommand(this));
        this.commandManager.registerCommand(new DrugSellCommand(this));
        this.commandManager.registerCommand(new HelpCommand(this));
        this.commandManager.registerCommand(new ReloadCommand(this));
    }
    
    public Economy getEconomy() {
        return this.economy;
    }
    
    public DrugManager getDrugManager() {
        return this.drugManager;
    }
    
    public DrugBoosterManager getDrugBoosterManager() {
        return this.drugBoosterManager;
    }
    
    public PlantManager getPlantManager() {
        return this.plantManager;
    }
    
    public RecipeManager getRecipeManager() {
        return this.recipeManager;
    }
    
    public StructureManager getStructureManager() {
        return this.structureManager;
    }
    
    public VanillaDropManager getVanillaDropManager() {
        return this.vanillaDropManager;
    }
    
    public ConfigManager getConfigManager() {
        return this.configManager;
    }
    
    public GUIManager getGUIManager() {
        return this.guiManager;
    }
    
    public CommandManager getCommandManager() {
        return this.commandManager;
    }
    
    public static Main getInstance() {
        return Main.instance;
    }
}
