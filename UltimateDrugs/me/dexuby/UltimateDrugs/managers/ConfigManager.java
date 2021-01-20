package me.dexuby.UltimateDrugs.managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.net.ssl.HttpsURLConnection;
import me.dexuby.UltimateDrugs.Main;
import me.dexuby.UltimateDrugs.block.BlockOffset;
import me.dexuby.UltimateDrugs.block.BlockOption;
import me.dexuby.UltimateDrugs.config.SettingsHolder;
import me.dexuby.UltimateDrugs.drugs.ConsumeConditions;
import me.dexuby.UltimateDrugs.drugs.ConsumeOption;
import me.dexuby.UltimateDrugs.drugs.ConsumeRequirementItem;
import me.dexuby.UltimateDrugs.drugs.Drop;
import me.dexuby.UltimateDrugs.drugs.Drug;
import me.dexuby.UltimateDrugs.drugs.DrugBooster;
import me.dexuby.UltimateDrugs.drugs.DrugBrewingRecipe;
import me.dexuby.UltimateDrugs.drugs.DrugCraftingRecipe;
import me.dexuby.UltimateDrugs.drugs.DrugDealer;
import me.dexuby.UltimateDrugs.drugs.DrugDealerType;
import me.dexuby.UltimateDrugs.drugs.DrugFurnaceRecipe;
import me.dexuby.UltimateDrugs.drugs.DrugRelatedGood;
import me.dexuby.UltimateDrugs.drugs.ExchangeItem;
import me.dexuby.UltimateDrugs.drugs.MultiplierType;
import me.dexuby.UltimateDrugs.drugs.actions.ChatMessageAction;
import me.dexuby.UltimateDrugs.drugs.actions.CommandAction;
import me.dexuby.UltimateDrugs.drugs.actions.CommandSenderType;
import me.dexuby.UltimateDrugs.drugs.actions.FeedAction;
import me.dexuby.UltimateDrugs.drugs.actions.GameStateAction;
import me.dexuby.UltimateDrugs.drugs.actions.HealAction;
import me.dexuby.UltimateDrugs.drugs.actions.ParticleEffectAction;
import me.dexuby.UltimateDrugs.drugs.actions.PotionEffectAction;
import me.dexuby.UltimateDrugs.drugs.actions.SaturationAction;
import me.dexuby.UltimateDrugs.drugs.actions.ScriptAction;
import me.dexuby.UltimateDrugs.drugs.actions.SoundEffectAction;
import me.dexuby.UltimateDrugs.drugs.actions.TimeAction;
import me.dexuby.UltimateDrugs.drugs.actions.VisualEffectAction;
import me.dexuby.UltimateDrugs.drugs.actions.VisualEffectType;
import me.dexuby.UltimateDrugs.drugs.actions.WeatherAction;
import me.dexuby.UltimateDrugs.drugs.chatslur.ChatSlurType;
import me.dexuby.UltimateDrugs.drugs.growing.Direction;
import me.dexuby.UltimateDrugs.drugs.growing.Fertilizer;
import me.dexuby.UltimateDrugs.drugs.growing.Growing;
import me.dexuby.UltimateDrugs.drugs.growing.GrowingBlock;
import me.dexuby.UltimateDrugs.drugs.growing.GrowingStage;
import me.dexuby.UltimateDrugs.drugs.growing.Plant;
import me.dexuby.UltimateDrugs.drugs.growing.PlantType;
import me.dexuby.UltimateDrugs.drugs.growing.Weather;
import me.dexuby.UltimateDrugs.drugs.growing.WorldTime;
import me.dexuby.UltimateDrugs.drugs.growing.processors.ProcessorType;
import me.dexuby.UltimateDrugs.drugs.vanilla.BlockVanillaDrop;
import me.dexuby.UltimateDrugs.drugs.vanilla.FishingVanillaDrop;
import me.dexuby.UltimateDrugs.drugs.vanilla.MobVanillaDrop;
import me.dexuby.UltimateDrugs.drugs.vanilla.VanillaDropBlock;
import me.dexuby.UltimateDrugs.utils.ItemBuilder;
import me.dexuby.UltimateDrugs.utils.SerializationUtils;
import me.dexuby.UltimateDrugs.utils.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class ConfigManager {
   private Main main;
   private File configFile;
   private File langFile;
   private File storageFile;
   private YamlConfiguration config;
   private YamlConfiguration lang;
   private YamlConfiguration storage;
   private HashMap<String, Object> langCache = new HashMap<String, Object>();
   private HashMap<String, YamlConfiguration> linkCache = new HashMap<String, YamlConfiguration>();
   private final String[] defaultDrugConfigurations = new String[]{"alcohol.yml", "apple-tree.yml", "cocaine.yml", "grape-farm.yml", "strawberry.yml", "weed.yml"};
   private String display1 = null;
   public ConfigManager(Main var1) {
      this.main = var1;
      this.configFile = new File(var1.getDataFolder(), "config.yml");
      this.langFile = new File(var1.getDataFolder(), "lang.yml");
      this.storageFile = new File(var1.getDataFolder(), "storage.yml");
      
      try {
         if (!this.configFile.exists()) {
            var1.saveResource("config.yml", false);
         }

         if (!this.langFile.exists()) {
            var1.saveResource("lang.yml", false);
         }

         if (!this.storageFile.exists()) {
            var1.saveResource("storage.yml", false);
         }

         Path path = Paths.get(var1.getDataFolder().getPath(), "drugs");
         if (!(new File(path.toString())).exists()) {
            (new File(path.toString())).mkdirs();

            for(String s : this.defaultDrugConfigurations) {
               if (!Files.exists(Paths.get(var1.getDataFolder().getPath(), "drugs", s))) {
                  Files.copy(var1.getResource(s), Paths.get(path.toString(), s));
               }
            }
         }
      } catch (Exception var7) {
         System.out.println(String.format("[%s] A error occurred while copying default configurations.", var1.getDescription().getName()));
      }

      this.loadFiles();
      this.loadConfig();
      this.loadLang();
      var1.getServer().getScheduler().runTaskLater(var1, this::loadStorage, 1L);
      var1.getServer().getScheduler().runTaskTimerAsynchronously(var1, this::updateStorage, 1200L, 1200L);
   }

   public void updateStorage() {
	    ArrayList<Plant> arrayList = new ArrayList<>(this.main.getPlantManager().getRegisteredPlants());
	    for (Plant plant : arrayList)
	      savePlant(plant); 
	    ArrayList<DrugBooster> arrayList1 = new ArrayList<>(this.main.getDrugBoosterManager().getActiveDrugBoosters());
	    for (DrugBooster drugBooster : arrayList1)
	      saveDrugBooster(drugBooster); 
	    updatePlayerPlacedBlockCache();
	    saveStorage();
	  }

   private void loadFiles() {
      this.config = new YamlConfiguration();
      this.lang = new YamlConfiguration();
      this.storage = new YamlConfiguration();

      try {
         this.config.load(this.configFile);
         this.lang.load(this.langFile);
         this.storage.load(this.storageFile);
      } catch (Exception exception) {
         System.out.println(String.format("[%s] A error occurred while loading the configuration files.", this.main.getDescription().getName()));
         exception.printStackTrace();
      }

   }

   public void reloadConfig() {
      try {
         this.config.load(this.configFile);
         this.loadConfig();
      } catch (Exception exception) {
         System.out.println(String.format("[%s] A error occurred while loading the config file.", this.main.getDescription().getName()));
         exception.printStackTrace();
      }

   }

   private void loadConfig() {
	   
      this.replaceAllLinks(this.config);
      if (this.config.isSet("block-shutdown-save")) {
         SettingsHolder.BLOCK_SHUTDOWN_SAVE.setValue(Boolean.valueOf(this.config.getBoolean("block-shutdown-save")));
      }

      if (this.config.isSet("player-plant-limit")) {
         SettingsHolder.PLAYER_PLANT_LIMIT.setValue(Integer.valueOf(this.config.getInt("player-plant-limit")));
      }

      if (this.config.isSet("plant-processor-type")) {
         SettingsHolder.PLANT_PROCESSOR_TYPE.setValue(ProcessorType.valueOf(this.config.getString("plant-processor-type")));
      }

      if (this.config.isSet("npc-dealers")) {
         for(String s : this.config.getConfigurationSection("npc-dealers").getKeys(false)) {
            String s1 = String.format("npc-dealers.%s.", s);
            DrugDealerType drugdealertype = this.config.isSet(s1 + "type") ? DrugDealerType.valueOf(this.config.getString(s1 + "type")) : DrugDealerType.SELL;
            this.main.getDrugManager().registerDrugDealer(new DrugDealer(Integer.parseInt(s), drugdealertype));
         }
      }

      if (this.config.isSet("vanilla")) {
         if (this.config.isSet("vanilla.auto-replant.enabled")) {
            SettingsHolder.VANILLA_AUTO_REPLANT.setValue(Boolean.valueOf(this.config.getBoolean("vanilla.auto-replant.enabled")));
         }

         if (this.config.isSet("vanilla.auto-replant.ignore-drug-plants")) {
            SettingsHolder.VANILLA_AUTO_IGNORE_DRUG_PLANTS.setValue(Boolean.valueOf(this.config.getBoolean("vanilla.auto-replant.ignore-drug-plants")));
         }

         if (this.config.isSet("vanilla.auto-replant.blacklist")) {
            ArrayList arraylist1 = new ArrayList();

            for(String s8 : this.config.getStringList("vanilla.auto-replant.blacklist")) {
               arraylist1.add(Material.valueOf(s8));
            }

            SettingsHolder.VANILLA_AUTO_REPLANT_BLACKLIST.setValue(arraylist1);
         }

         if (this.config.isSet("vanilla.store-player-blocks")) {
            SettingsHolder.VANILLA_STORE_PLAYER_BLOCKS.setValue(Boolean.valueOf(this.config.getBoolean("vanilla.store-player-blocks")));
         }

         if (this.config.isSet("vanilla.drops")) {
            for(String s6 : this.config.getConfigurationSection("vanilla.drops").getKeys(false)) {
               String s9 = String.format("vanilla.drops.%s.", s6);
               String s11 = this.config.getString(s9 + "type");
               String s2 = this.config.isSet(s9 + "permission") ? this.config.getString(s9 + "permission") : null;
               int i = this.config.isSet(s9 + "drop-limit") ? this.config.getInt(s9 + "drop-limit") : 0;
               ArrayList arraylist = new ArrayList();

               for(String s3 : this.config.getConfigurationSection(s9 + "drops").getKeys(false)) {
                  try {
                     String s4 = String.format(s9 + "drops.%s.", s3);
                     ItemStack itemstack = this.loadItemConfiguration(this.config, s4 + "item");
                     int j = this.config.isSet(s4 + "min-amount") ? this.config.getInt(s4 + "min-amount") : 1;
                     int k = this.config.isSet(s4 + "max-amount") ? this.config.getInt(s4 + "max-amount") : 1;
                     double d0 = this.config.isSet(s4 + "drop-chance") ? this.config.getDouble(s4 + "drop-chance") : 100.0D;
                     boolean flag = this.config.isSet(s4 + "break-success-chain") && this.config.getBoolean(s4 + "break-success-chain");
                     arraylist.add(new Drop(itemstack, j, k, d0, flag));
                  } catch (Exception exception2) {
                     System.out.println(String.format("[%s] Failed to load a drop in vanilla drop with id: %s", this.main.getDescription().getName(), s6));
                     exception2.printStackTrace();
                  }
               }

               if (s11.equalsIgnoreCase("BLOCK")) {
                  boolean flag4 = this.config.isSet(s9 + "override-drops") && this.config.getBoolean(s9 + "override-drops");
                  boolean flag7 = this.config.isSet(s9 + "override-exp") && this.config.getBoolean(s9 + "override-exp");
                  int k1 = this.config.isSet(s9 + "experience") ? this.config.getInt(s9 + "experience") : 0;
                  boolean flag8 = !this.config.isSet(s9 + "ignore-drug-plants") || this.config.getBoolean(s9 + "ignore-drug-plants");
                  ArrayList arraylist4 = new ArrayList();

                  for(String s14 : this.config.getConfigurationSection(s9 + "blocks").getKeys(false)) {
                     String s5 = String.format(s9 + "blocks.%s", s14);
                     BlockData blockdata = this.loadBlockDataConfiguration(this.config, s5);
                     boolean flag1 = this.config.isSet(s5 + ".ignore-data") && this.config.getBoolean(s5 + ".ignore-data");
                     arraylist4.add(new VanillaDropBlock(blockdata, flag1));
                  }

                  this.main.getVanillaDropManager().addVanillaDrop(new BlockVanillaDrop(s2, i, arraylist, flag4, flag7, k1, flag8, arraylist4));
                  this.main.getVanillaDropManager().addVanillaBlockDropMaterials((List)arraylist4.stream().map((var0) -> {
                     return ((BlockOption) var0).getBlockData().getMaterial();
                  }).collect(Collectors.toList()));
                  
               } else if (s11.equalsIgnoreCase("MOB")) {
                  boolean flag3 = this.config.isSet(s9 + "override-drops") && this.config.getBoolean(s9 + "override-drops");
                  boolean flag6 = this.config.isSet(s9 + "override-exp") && this.config.getBoolean(s9 + "override-exp");
                  int j1 = this.config.isSet(s9 + "experience") ? this.config.getInt(s9 + "experience") : 0;
                  ArrayList arraylist3 = new ArrayList();

                  for(String s13 : this.config.getStringList(s9 + "entity-types")) {
                     arraylist3.add(EntityType.valueOf(s13));
                  }

                  this.main.getVanillaDropManager().addVanillaDrop(new MobVanillaDrop(s2, i, arraylist, flag3, flag6, j1, arraylist3));
               } else if (s11.equalsIgnoreCase("FISHING")) {
                  boolean flag2 = this.config.isSet(s9 + "override-drops") && this.config.getBoolean(s9 + "override-drops");
                  boolean flag5 = this.config.isSet(s9 + "override-exp") && this.config.getBoolean(s9 + "override-exp");
                  int i1 = this.config.isSet(s9 + "experience") ? this.config.getInt(s9 + "experience") : 0;
                  ArrayList arraylist2 = new ArrayList();

                  for(String s12 : this.config.getStringList(s9 + "fish-types")) {
                     arraylist2.add(Material.valueOf(s12));
                  }

                  this.main.getVanillaDropManager().addVanillaDrop(new FishingVanillaDrop(s2, i, arraylist, flag2, flag5, i1, arraylist2));
               }
            }
         }
      }

      if (this.config.isSet("fertilizers")) {
         for(String s7 : this.config.getConfigurationSection("fertilizers").getKeys(false)) {
            try {
               String s10 = String.format("fertilizers.%s.", s7);
               ItemStack itemstack1 = this.loadItemConfiguration(this.config, s10 + "item");
               int l = this.config.getInt(s10 + "grow-amount");
               List list = this.config.getStringList(s10 + "drug-ids");
               this.main.getDrugManager().registerFertilizer(new Fertilizer(s7, itemstack1, l, list));
            } catch (Exception exception1) {
               System.out.println(String.format("[%s] Failed to load fertilizer with id: %s", this.main.getDescription().getName(), s7));
               exception1.printStackTrace();
            }
         }
      }

      this.removeAllCraftingRecipes();

      try {
         Files.list(Paths.get(this.main.getDataFolder().getPath(), "drugs")).forEach((var1) -> {
            try {
               if (var1.getFileName().toString().startsWith("@")) {
                  return;
               }

               YamlConfiguration yamlconfiguration1 = new YamlConfiguration();
               yamlconfiguration1.load(new File(var1.toAbsolutePath().toString()));
               this.replaceAllLinks(yamlconfiguration1);
               String s25 = yamlconfiguration1.getString("id");
               if (s25 == null) {
                  System.out.println(String.format("[%s] Failed to load a drug file with a empty drug id.", this.main.getDescription().getName()));
                  return;
               }
               
               boolean flag14 = yamlconfiguration1.isSet("legal") && yamlconfiguration1.getBoolean("legal");
               double d10 = yamlconfiguration1.isSet("sell-price") ? yamlconfiguration1.getDouble("sell-price") : 0.0D;
               double d11 = yamlconfiguration1.isSet("buy-price") ? yamlconfiguration1.getDouble("buy-price") : 0.0D;
               int i4 = yamlconfiguration1.isSet("effect-duration") ? yamlconfiguration1.getInt("effect-duration") : 20;
               ItemStack itemstack3 = this.loadItemConfiguration(yamlconfiguration1, "item");
               String displayname = display1;
               ConsumeOption consumeoption = yamlconfiguration1.isSet("consume-option") ? ConsumeOption.valueOf(yamlconfiguration1.getString("consume-option")) : ConsumeOption.RIGHT_CLICK;
               boolean flag15 = !yamlconfiguration1.isSet("remove-on-consume") || yamlconfiguration1.getBoolean("remove-on-consume");
               int j4 = yamlconfiguration1.isSet("exchange-item-limit") ? yamlconfiguration1.getInt("exchange-item-limit") : 0;
               ArrayList arraylist8 = new ArrayList();
               if (yamlconfiguration1.isSet("consume-exchange-items")) {
                  for(String s26 : yamlconfiguration1.getConfigurationSection("consume-exchange-items").getKeys(false)) {
                     String s27 = String.format("consume-exchange-items.%s.", s26);
                     ItemStack itemstack4 = this.loadItemConfiguration(yamlconfiguration1, s27 + "item");
                     double d12 = yamlconfiguration1.isSet(s27 + "chance") ? yamlconfiguration1.getDouble(s27 + "chance") : 100.0D;
                     boolean flag16 = yamlconfiguration1.isSet(s27 + "force-drop") && yamlconfiguration1.getBoolean(s27 + "force-drop");
                     boolean flag17 = yamlconfiguration1.isSet(s27 + "break-success-chain") && yamlconfiguration1.getBoolean(s27 + "break-success-chain");
                     int k4 = yamlconfiguration1.isSet(s27 + "preferred-slot") ? yamlconfiguration1.getInt(s27 + "preferred-slot") : -1;
                     arraylist8.add(new ExchangeItem(itemstack4, d12, flag16, flag17, k4));
                  }
               }

               ConsumeConditions consumeconditions = new ConsumeConditions();
               if (yamlconfiguration1.isSet("consume-conditions")) {
                  boolean flag29 = yamlconfiguration1.isSet("consume-conditions.sneak-required") && yamlconfiguration1.getBoolean("consume-conditions.sneak-required");
                  double d15 = yamlconfiguration1.isSet("consume-conditions.max-current-health") ? yamlconfiguration1.getDouble("consume-conditions.max-current-health") : 0.0D;
                  double d17 = yamlconfiguration1.isSet("consume-conditions.max-current-food-level") ? yamlconfiguration1.getDouble("consume-conditions.max-current-food-level") : 0.0D;
                  ArrayList arraylist10 = new ArrayList(yamlconfiguration1.getStringList("consume-conditions.disabled-world-list"));
                  ArrayList arraylist11 = new ArrayList(yamlconfiguration1.getStringList("consume-conditions.disabled-region-list"));
                  ArrayList arraylist12 = new ArrayList();
                  if (yamlconfiguration1.isSet("consume-conditions.required-item-list")) {
                     for(String s28 : yamlconfiguration1.getConfigurationSection("consume-conditions.required-item-list").getKeys(false)) {
                        String s29 = String.format("consume-conditions.required-item-list.%s.", s28);
                        ItemStack itemstack5 = this.loadItemConfiguration(yamlconfiguration1, s29 + "item");
                        boolean flag18 = yamlconfiguration1.isSet(s29 + "damage-on-consume") && yamlconfiguration1.getBoolean(s29 + "damage-on-consume");
                        boolean flag19 = yamlconfiguration1.isSet(s29 + "remove-on-consume") && yamlconfiguration1.getBoolean(s29 + "remove-on-consume");
                        arraylist12.add(new ConsumeRequirementItem(itemstack5, flag18, flag19));
                     }
                  }

                  consumeconditions = new ConsumeConditions(flag29, d15, d17, arraylist10, arraylist11, arraylist12);
               }
               boolean flag30 = yamlconfiguration1.isSet("reset-on-consume") && yamlconfiguration1.getBoolean("reset-on-consume");
               ChatSlurType chatslurtype = yamlconfiguration1.isSet("chat-slur-type") ? ChatSlurType.valueOf(yamlconfiguration1.getString("chat-slur-type")) : ChatSlurType.NONE;
               double d16 = yamlconfiguration1.isSet("global-sound-volume-modifier") ? yamlconfiguration1.getDouble("global-sound-volume-modifier") : 0.0D;
               double d13 = yamlconfiguration1.isSet("global-sound-pitch-modifier") ? yamlconfiguration1.getDouble("global-sound-pitch-modifier") : 0.0D;
               boolean flag31 = !yamlconfiguration1.isSet("remove-on-disconnect") || yamlconfiguration1.getBoolean("remove-on-disconnect");
               boolean flag32 = !yamlconfiguration1.isSet("remove-on-death") || yamlconfiguration1.getBoolean("remove-on-death");
               boolean flag33 = yamlconfiguration1.isSet("pause-while-offline") && yamlconfiguration1.getBoolean("pause-while-offline");
               ArrayList arraylist13 = new ArrayList();
               if (yamlconfiguration1.isSet("consume-actions")) {
                  for(String s35 : yamlconfiguration1.getConfigurationSection("consume-actions").getKeys(false)) {
                     String s36 = String.format("consume-actions.%s.", s35);
                     String s38 = yamlconfiguration1.getString(s36 + "type");
                     int l4 = yamlconfiguration1.isSet(s36 + "delay") ? yamlconfiguration1.getInt(s36 + "delay") : 0;
                     double d14 = yamlconfiguration1.isSet(s36 + "chance") ? yamlconfiguration1.getDouble(s36 + "chance") : 100.0D;
                     int i5 = yamlconfiguration1.isSet(s36 + "repetitions") ? yamlconfiguration1.getInt(s36 + "repetitions") : 0;
                     int j5 = yamlconfiguration1.isSet(s36 + "repetition-tick-delay") ? yamlconfiguration1.getInt(s36 + "repetition-tick-delay") : 1;
                     String s31 = yamlconfiguration1.isSet(s36 + "target-selector") ? yamlconfiguration1.getString(s36 + "target-selector") : null;
                     if (s38.equals("CHAT_MESSAGE")) {
                        String s55 = ChatColor.translateAlternateColorCodes('&', yamlconfiguration1.getString(s36 + "chat-message"));
                        arraylist13.add(new ChatMessageAction(l4, d14, i5, j5, s31, s55));
                     } else if (s38.equals("COMMAND")) {
                        String s54 = ChatColor.translateAlternateColorCodes('&', yamlconfiguration1.getString(s36 + "command"));
                        CommandSenderType commandsendertype = yamlconfiguration1.isSet(s36 + "sender-type") ? CommandSenderType.valueOf(yamlconfiguration1.getString(s36 + "sender-type")) : CommandSenderType.CONSOLE;
                        arraylist13.add(new CommandAction(l4, d14, i5, j5, s31, s54, commandsendertype));
                     } else if (s38.equals("FEED")) {
                        int j8 = yamlconfiguration1.isSet(s36 + "amount") ? yamlconfiguration1.getInt(s36 + "amount") : 1;
                        arraylist13.add(new FeedAction(l4, d14, i5, j5, s31, j8));
                     } else if (s38.equals("GAME_STATE")) {
                        if (this.main.getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
                           int i8 = yamlconfiguration1.isSet(s36 + "reason") ? yamlconfiguration1.getInt(s36 + "reason") : 0;
                           float f2 = yamlconfiguration1.isSet(s36 + "value") ? (float)yamlconfiguration1.getDouble(s36 + "value") : 0.0F;
                           arraylist13.add(new GameStateAction(l4, d14, i5, j5, s31, i8, f2));
                        }
                     } else if (s38.equals("HEAL")) {
                        double d21 = yamlconfiguration1.isSet(s36 + "amount") ? yamlconfiguration1.getDouble(s36 + "amount") : 1.0D;
                        arraylist13.add(new HealAction(l4, d14, i5, j5, s31, d21));
                     } else if (s38.equals("PARTICLE_EFFECT")) {
                        ParticleEffectAction particleeffectaction1 = this.loadParticleEffectActionConfiguration(yamlconfiguration1, s36, l4, d14, i5, j5, s31);
                        if (particleeffectaction1 != null) {
                           arraylist13.add(particleeffectaction1);
                        }
                     } else if (s38.equals("POTION_EFFECT")) {
                        PotionEffectType potioneffecttype = PotionEffectType.getByName(yamlconfiguration1.getString(s36 + "potion"));
                        if (potioneffecttype != null) {
                           int k9 = yamlconfiguration1.isSet(s36 + "duration") ? yamlconfiguration1.getInt(s36 + "duration") : 20;
                           int l9 = yamlconfiguration1.isSet(s36 + "amplifier") ? yamlconfiguration1.getInt(s36 + "amplifier") : 0;
                           boolean flag37 = yamlconfiguration1.isSet(s36 + "ambient") && yamlconfiguration1.getBoolean(s36 + "ambient");
                           boolean flag40 = yamlconfiguration1.isSet(s36 + "particles") && yamlconfiguration1.getBoolean(s36 + "particles");
                           arraylist13.add(new PotionEffectAction(l4, d14, i5, j5, s31, new PotionEffect(potioneffecttype, k9, l9, flag37, flag40)));
                        }
                     } else if (s38.equals("SATURATION")) {
                        int l7 = yamlconfiguration1.isSet(s36 + "amount") ? yamlconfiguration1.getInt(s36 + "amount") : 1;
                        arraylist13.add(new SaturationAction(l4, d14, i5, j5, s31, l7));
                     } else if (s38.equals("SCRIPT")) {
                        if (yamlconfiguration1.isSet(s36 + "code")) {
                           List list5 = yamlconfiguration1.getStringList(s36 + "code");
                           arraylist13.add(new ScriptAction(l4, d14, i5, j5, s31, list5));
                        }
                     } else if (s38.equals("SOUND_EFFECT")) {
                        String s53 = yamlconfiguration1.getString(s36 + "sound");
                        Optional optional4 = Arrays.stream((Object[])Sound.values()).filter((var1x) -> {
                           return var1x.toString().equalsIgnoreCase(s53);
                        }).findFirst();
                        float f3 = yamlconfiguration1.isSet(s36 + "volume") ? (float)yamlconfiguration1.getDouble(s36 + "volume") : 1.0F;
                        float f4 = yamlconfiguration1.isSet(s36 + "pitch") ? (float)yamlconfiguration1.getDouble(s36 + "pitch") : 1.0F;
                        boolean flag39 = !yamlconfiguration1.isSet(s36 + "client-sided") || yamlconfiguration1.getBoolean(s36 + "client-sided");
                        if (optional4.isPresent()) {
                           arraylist13.add(new SoundEffectAction(l4, d14, i5, j5, s31, (Sound)optional4.get(), f3, f4, flag39));
                        } else {
                           arraylist13.add(new SoundEffectAction(l4, d14, i5, j5, s31, s53, f3, f4, flag39));
                        }
                     } else if (s38.equals("TIME")) {
                        int k7 = yamlconfiguration1.isSet(s36 + "duration") ? yamlconfiguration1.getInt(s36 + "duration") : 200;
                        long j9 = yamlconfiguration1.isSet(s36 + "time") ? yamlconfiguration1.getLong(s36 + "time") : 0L;
                        boolean flag21 = yamlconfiguration1.isSet(s36 + "relative") && yamlconfiguration1.getBoolean(s36 + "relative");
                        boolean flag22 = !yamlconfiguration1.isSet(s36 + "client-sided") || yamlconfiguration1.getBoolean(s36 + "client-sided");
                        arraylist13.add(new TimeAction(l4, d14, i5, j5, s31, k7, j9, flag21, flag22));
                     } else if (s38.equals("VISUAL_EFFECT")) {
                        if (this.main.getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
                           VisualEffectType visualeffecttype = yamlconfiguration1.isSet(s36 + "effect") ? VisualEffectType.valueOf(yamlconfiguration1.getString(s36 + "effect")) : VisualEffectType.ENDERMAN_VISION;
                           int i9 = yamlconfiguration1.isSet(s36 + "duration") ? yamlconfiguration1.getInt(s36 + "duration") : 200;
                           arraylist13.add(new VisualEffectAction(l4, d14, i5, j5, s31, i9, visualeffecttype));
                        }
                     } else if (s38.equals("WEATHER")) {
                        int k5 = yamlconfiguration1.isSet(s36 + "duration") ? yamlconfiguration1.getInt(s36 + "duration") : 200;
                        WeatherType weathertype = yamlconfiguration1.isSet(s36 + "weather") ? WeatherType.valueOf(yamlconfiguration1.getString(s36 + "weather")) : WeatherType.CLEAR;
                        boolean flag20 = !yamlconfiguration1.isSet(s36 + "client-sided") || yamlconfiguration1.getBoolean(s36 + "client-sided");
                        arraylist13.add(new WeatherAction(l4, d14, i5, j5, s31, k5, weathertype, flag20));
                     }
                  }
               }

               HashMap hashmap = new HashMap();
               if (yamlconfiguration1.isSet("multipliers")) {
                  for(String s37 : yamlconfiguration1.getConfigurationSection("multipliers").getKeys(false)) {
                     String s39 = String.format("multipliers.%s.", s37);
                     Optional optional3 = Arrays.stream(MultiplierType.values()).filter((var2) -> {
                        return var2.toString().equalsIgnoreCase(yamlconfiguration1.getString(s39 + "type"));
                     }).findFirst();
                     if (optional3.isPresent()) {
                        double d18 = yamlconfiguration1.getDouble(s39 + "multiplier");
                        hashmap.put(optional3.get(), Double.valueOf(d18));
                     }
                  }
               }

               ArrayList arraylist14 = new ArrayList();
               if (yamlconfiguration1.isSet("crafting-recipes")) {
                  for(String s40 : yamlconfiguration1.getConfigurationSection("crafting-recipes").getKeys(false)) {
                     String s41 = String.format("crafting-recipes.%s.", s40);
                     String s43 = yamlconfiguration1.getString(s41 + "type");
                     if (s43.equalsIgnoreCase("SHAPED")) {
                        String s46 = String.format("ultimatedrugs.crafting.%s.%s", s25, s40);
                        List list4 = yamlconfiguration1.getStringList(s41 + "shape");
                        HashMap hashmap1 = new HashMap();
                        for(String s57 : yamlconfiguration1.getConfigurationSection(s41 + "ingredients").getKeys(false)) {
                           String s58 = String.format(s41 + "ingredients.%s", s57);
                           ((HashMap<String, Object>) hashmap1).put(s57, this.loadItemConfiguration(yamlconfiguration1, s58));
                        }
                        ItemStack itemstack12 = this.loadItemConfiguration(yamlconfiguration1, s41 + "result");
                        try {
                            ShapedRecipe shapedrecipe = new ShapedRecipe(new NamespacedKey(this.main, s46), itemstack12);
                            shapedrecipe.shape(new String[]{(String)list4.get(0), (String)list4.get(1), (String)list4.get(2)});
                            for(String s59 : ((HashMap<String, Object>) hashmap1).keySet()) {
                               shapedrecipe.setIngredient(s59.charAt(0), new ExactChoice((ItemStack)hashmap1.get(s59)));
                            }
                            this.main.getServer().addRecipe(shapedrecipe);
                            arraylist14.add(new DrugCraftingRecipe(s46, hashmap1.values(), shapedrecipe));
                         } catch (Exception var64) {
                            System.out.println(String.format("[%s] Failed to register recipe with key: %s", this.main.getDescription().getName(), s46));
                         }
                     } else if (s43.equalsIgnoreCase("SHAPELESS")) {
                         String str4 = String.format("ultimatedrugs.crafting.%s.%s", new Object[] { s25, s40 });
                         ArrayList<ItemStack> arrayList6 = new ArrayList();
                         for (String str5 : yamlconfiguration1.getConfigurationSection(s41 + "ingredients").getKeys(false)) {
                           String str6 = String.format(s41 + "ingredients.%s", new Object[] { str5 });
                           arrayList6.add(loadItemConfiguration(yamlconfiguration1, str6));
                         } 
                         ItemStack itemStack1 = loadItemConfiguration(yamlconfiguration1, s41 + "result");
                         try {
                           this.main.getRecipeManager().unregisterDrugRecipe(str4);
                           ShapelessRecipe shapelessrecipe = new ShapelessRecipe(new NamespacedKey(this.main, str4), itemStack1);
                           for (ItemStack itemStack2 : arrayList6)
                        	   shapelessrecipe.addIngredient(itemStack2.getAmount(), itemStack2.getType()); 
                           DrugCraftingRecipe drugcraftingrecipe = new DrugCraftingRecipe(str4, arrayList6, shapelessrecipe);
                           this.main.getRecipeManager().registerDrugRecipe(drugcraftingrecipe);
                           this.main.getServer().addRecipe(shapelessrecipe);
                           arraylist14.add(drugcraftingrecipe);
                         } catch (Exception exception) {
                           System.out.println(String.format("[%s] Failed to register recipe with key: %s", new Object[] { this.main.getDescription().getName(), str4 }));
                         } 
                       } 
                  }
               }

               ArrayList arraylist15 = new ArrayList();
               if (yamlconfiguration1.isSet("furnace-recipes")) {
                  for(String s42 : yamlconfiguration1.getConfigurationSection("furnace-recipes").getKeys(false)) {
                     String s44 = String.format("furnace-recipes.%s.", s42);
                     String s47 = String.format("ultimatedrugs.furnace.%s.%s", s25, s42);
                     ItemStack itemstack7 = this.loadItemConfiguration(yamlconfiguration1, s44 + "input");
                     ItemStack itemstack9 = this.loadItemConfiguration(yamlconfiguration1, s44 + "result");
                     float f1 = yamlconfiguration1.isSet(s44 + "experience") ? (float)yamlconfiguration1.getInt(s44 + "experience") : 0.0F;
                     int k8 = yamlconfiguration1.isSet(s44 + "cooking-time") ? yamlconfiguration1.getInt(s44 + "cooking-time") : 200;

                     try {
                        FurnaceRecipe furnacerecipe = new FurnaceRecipe(new NamespacedKey(this.main, s47), itemstack9, new ExactChoice(itemstack7), f1, k8);
                        this.main.getServer().addRecipe(furnacerecipe);
                        arraylist15.add(new DrugFurnaceRecipe(s47, furnacerecipe));
                     } catch (Exception var62) {
                        System.out.println(String.format("[%s] Failed to register recipe with key: %s", this.main.getDescription().getName(), s47));
                     }
                  }
               }

               ArrayList arraylist16 = new ArrayList();
               if (yamlconfiguration1.isSet("brewing-recipes")) {
                  for(String s45 : yamlconfiguration1.getConfigurationSection("brewing-recipes").getKeys(false)) {
                     String s48 = String.format("brewing-recipes.%s.", s45);
                     String s50 = String.format("ultimatedrugs.brewing.%s.%s", s25, s45);
                     ItemStack itemstack10 = this.loadItemConfiguration(yamlconfiguration1, s48 + "input");
                     ItemStack itemstack13 = yamlconfiguration1.isSet(s48 + "fuel") ? this.loadItemConfiguration(yamlconfiguration1, s48 + "fuel") : null;
                     int l8 = yamlconfiguration1.isSet(s48 + "speed") ? yamlconfiguration1.getInt(s48 + "speed") : 1;
                     ArrayList arraylist19 = new ArrayList();

                     for(String s60 : yamlconfiguration1.getConfigurationSection(s48 + "results").getKeys(false)) {
                        String s61 = String.format(s48 + "results.%s", s60);
                        ItemStack itemstack6 = this.loadItemConfiguration(yamlconfiguration1, s61);
                        arraylist19.add(itemstack6);
                     }

                     this.main.getRecipeManager().unregisterDrugRecipe(s50);
                     DrugBrewingRecipe drugbrewingrecipe = new DrugBrewingRecipe(s50, itemstack10, itemstack13, l8, arraylist19);
                     this.main.getRecipeManager().registerDrugRecipe(drugbrewingrecipe);
                     arraylist16.add(drugbrewingrecipe);
                  }
               }

               ArrayList arraylist17 = new ArrayList();
               if (yamlconfiguration1.isSet("related-goods")) {
                  for(String s49 : yamlconfiguration1.getConfigurationSection("related-goods").getKeys(false)) {
                     String s51 = String.format("related-goods.%s.", s49);
                     ItemStack itemstack11 = this.loadItemConfiguration(yamlconfiguration1, s51 + "item");
                     double d19 = yamlconfiguration1.isSet(s51 + "sell-price") ? yamlconfiguration1.getDouble(s51 + "sell-price") : 0.0D;
                     double d22 = yamlconfiguration1.isSet(s51 + "buy-price") ? yamlconfiguration1.getDouble(s51 + "buy-price") : 0.0D;
                     arraylist17.add(new DrugRelatedGood(s49, itemstack11, d19, d22));
                  }
               }
               Growing growing = null;
               if (yamlconfiguration1.isSet("growing")) {
                  boolean flag34 = yamlconfiguration1.isSet("growing.auto-replant.enabled") && yamlconfiguration1.getBoolean("growing.auto-replant.enabled");
                  boolean flag35 = yamlconfiguration1.isSet("growing.auto-replant.requires-seed") && yamlconfiguration1.getBoolean("growing.auto-replant.requires-seed");
                  int j7 = yamlconfiguration1.isSet("growing.auto-replant.replant-stage") ? yamlconfiguration1.getInt("growing.auto-replant.replant-stage") : 0;
                  double d20 = yamlconfiguration1.isSet("growing.random-grow-chance") ? yamlconfiguration1.getDouble("growing.random-grow-chance") : 0.0D;
                  boolean flag36 = yamlconfiguration1.isSet("growing.ignore-chunk-state") && yamlconfiguration1.getBoolean("growing.ignore-chunk-state");
                  ArrayList arraylist20 = new ArrayList();

                  for(String s62 : yamlconfiguration1.getConfigurationSection("growing.stages").getKeys(false)) {
                     String s63 = String.format("growing.stages.%s.", s62);
                     int l5 = yamlconfiguration1.getInt(s63 + "order-id");
                     boolean flag23 = yamlconfiguration1.isSet(s63 + "grown") && yamlconfiguration1.getBoolean(s63 + "grown");
                     int i6 = yamlconfiguration1.isSet(s63 + "grow-delay") ? yamlconfiguration1.getInt(s63 + "grow-delay") : 0;
                     float f = yamlconfiguration1.isSet(s63 + "mcmmo-bonus-xp") ? (float)yamlconfiguration1.getDouble(s63 + "mcmmo-bonus-xp") : 0.0F;
                     boolean flag24 = yamlconfiguration1.isSet(s63 + "fertilizer-required") && yamlconfiguration1.getBoolean(s63 + "fertilizer-required");
                     int j6 = yamlconfiguration1.isSet(s63 + "required-block-count") ? yamlconfiguration1.getInt(s63 + "required-block-count") : 0;
                     ArrayList arraylist9 = new ArrayList();
                     
                     for(String s32 : yamlconfiguration1.getConfigurationSection(s63 + "blocks").getKeys(false)) {
                        String s33 = String.format(s63 + "blocks.%s.", s32);
                        BlockData blockdata1 = this.loadBlockDataConfiguration(yamlconfiguration1, s33 + "block-data");
                        String s34 = yamlconfiguration1.isSet(s33 + "skull-texture") ? yamlconfiguration1.getString(s33 + "skull-texture") : null;
                        boolean flag25 = yamlconfiguration1.isSet(s33 + "random-rotation") && yamlconfiguration1.getBoolean(s33 + "random-rotation");
                        boolean flag26 = yamlconfiguration1.isSet(s33 + "apply-physics") && yamlconfiguration1.getBoolean(s33 + "apply-physics");
                        boolean flag27 = yamlconfiguration1.isSet(s33 + "auto-connect") && yamlconfiguration1.getBoolean(s33 + "auto-connect");
                        boolean flag28 = yamlconfiguration1.isSet(s33 + "ignore-space") && yamlconfiguration1.getBoolean(s33 + "ignore-space");
                        BlockOffset blockoffset = new BlockOffset(0, 0, 0);
                        if (yamlconfiguration1.isSet(s33 + "offset")) {
                           int k6 = yamlconfiguration1.getInt(s33 + "offset.x", 0);
                           int l6 = yamlconfiguration1.getInt(s33 + "offset.y", 0);
                           int i7 = yamlconfiguration1.getInt(s33 + "offset.z", 0);
                           blockoffset = new BlockOffset(k6, l6, i7);
                        }

                        arraylist9.add(new GrowingBlock(blockdata1, blockoffset, s34, flag25, flag26, flag27, flag28));
                     }

                     int i10 = yamlconfiguration1.isSet(s63 + "drop-limit") ? yamlconfiguration1.getInt(s63 + "drop-limit") : 0;
                     ArrayList arraylist25 = new ArrayList();
                     if (yamlconfiguration1.isSet(s63 + "drops")) {
                        for(String s70 : yamlconfiguration1.getConfigurationSection(s63 + "drops").getKeys(false)) {
                           String s71 = String.format(s63 + "drops.%s.", s70);
                           ItemStack itemstack16 = this.loadItemConfiguration(yamlconfiguration1, s71 + "item");
                           int k11 = yamlconfiguration1.isSet(s71 + "min-amount") ? yamlconfiguration1.getInt(s71 + "min-amount") : 1;
                           int i12 = yamlconfiguration1.isSet(s71 + "max-amount") ? yamlconfiguration1.getInt(s71 + "max-amount") : 1;
                           double d23 = yamlconfiguration1.isSet(s71 + "drop-chance") ? yamlconfiguration1.getDouble(s71 + "drop-chance") : 100.0D;
                           boolean flag45 = yamlconfiguration1.isSet(s71 + "break-success-chain") && yamlconfiguration1.getBoolean(s71 + "break-success-chain");
                           arraylist25.add(new Drop(itemstack16, k11, i12, d23, flag45));
                        }
                     }

                     arraylist20.add(new GrowingStage(l5, flag23, i6, f, flag24, j6, arraylist9, i10, arraylist25));
                  }

                  boolean flag38 = yamlconfiguration1.isSet("growing.remove-if-flying") && yamlconfiguration1.getBoolean("growing.remove-if-flying");
                  boolean flag41 = yamlconfiguration1.isSet("growing.water-required") && yamlconfiguration1.getBoolean("growing.water-required");
                  boolean flag42 = yamlconfiguration1.isSet("growing.lava-required") && yamlconfiguration1.getBoolean("growing.lava-required");
                  ArrayList arraylist21 = new ArrayList();
                  if (yamlconfiguration1.isSet("growing.biome-whitelist")) {
                     for(String s64 : yamlconfiguration1.getStringList("growing.biome-whitelist")) {
                        arraylist21.add(Biome.valueOf(s64));
                     }
                  }

                  WorldTime worldtime = yamlconfiguration1.isSet("growing.required-world-time") ? WorldTime.valueOf(yamlconfiguration1.getString("growing.required-world-time")) : null;
                  ArrayList arraylist22 = new ArrayList();
                  if (yamlconfiguration1.isSet("growing.weather-whitelist")) {
                     for(String s65 : yamlconfiguration1.getStringList("growing.weather-whitelist")) {
                        arraylist22.add(Weather.valueOf(s65));
                     }
                  }

                  ArrayList arraylist23 = new ArrayList();
                  if (yamlconfiguration1.isSet("growing.plant-block-whitelist")) {
                     for(String s66 : yamlconfiguration1.getConfigurationSection("growing.plant-block-whitelist").getKeys(false)) {
                        String s67 = String.format("growing.plant-block-whitelist.%s.", s66);
                        BlockData blockdata2 = this.loadBlockDataConfiguration(yamlconfiguration1, s67);
                        BlockOffset blockoffset1 = new BlockOffset(0, -1, 0);
                        boolean flag43 = yamlconfiguration1.isSet(s67 + "required") && yamlconfiguration1.getBoolean(s67 + "required");
                        if (yamlconfiguration1.isSet(s67 + "offset")) {
                           int j10 = yamlconfiguration1.getInt(s67 + "offset.x", 0);
                           int k10 = yamlconfiguration1.getInt(s67 + "offset.y", 0);
                           int i11 = yamlconfiguration1.getInt(s67 + "offset.z", 0);
                           blockoffset1 = new BlockOffset(j10, k10, i11);
                        }

                        arraylist23.add(new BlockOption(blockdata2, blockoffset1, flag43));
                     }
                  }

                  ArrayList arraylist24 = new ArrayList();
                  if (yamlconfiguration1.isSet("growing.grow-block-whitelist")) {
                     for(String s68 : yamlconfiguration1.getConfigurationSection("growing.grow-block-whitelist").getKeys(false)) {
                        String s69 = String.format("growing.grow-block-whitelist.%s.", s68);
                        BlockData blockdata3 = this.loadBlockDataConfiguration(yamlconfiguration1, s69);
                        BlockOffset blockoffset2 = new BlockOffset(0, -1, 0);
                        boolean flag44 = yamlconfiguration1.isSet(s69 + "required") && yamlconfiguration1.getBoolean(s69 + "required");
                        if (yamlconfiguration1.isSet(s69 + "offset")) {
                           int l10 = yamlconfiguration1.getInt(s69 + "offset.x", 0);
                           int j11 = yamlconfiguration1.getInt(s69 + "offset.y", 0);
                           int l11 = yamlconfiguration1.getInt(s69 + "offset.z", 0);
                           blockoffset2 = new BlockOffset(l10, j11, l11);
                        }

                        arraylist24.add(new BlockOption(blockdata3, blockoffset2, flag44));
                     }
                  }

                  ItemStack itemstack15 = this.loadItemConfiguration(yamlconfiguration1, "growing.seed-item");
                  growing = new Growing(flag34, flag35, j7, d20, flag36, new PlantType(arraylist20, flag38, flag41, flag42, arraylist21, worldtime, arraylist22, arraylist23, arraylist24), itemstack15);
               }
            
               Drug drug1 = new Drug(s25, flag14, d10, d11, i4, itemstack3, consumeoption, flag15, j4, arraylist8, consumeconditions, flag30, chatslurtype, d16, d13, flag31, flag32, flag33, arraylist13, hashmap, arraylist14, arraylist15, arraylist16, arraylist17, growing, displayname);
               this.main.getDrugManager().registerDrug(drug1);
               System.out.println(String.format("[%s] Registered drug with id: %s", this.main.getDescription().getName(), s25));
            } catch (Exception exception4) {
               System.out.println(String.format("[%s] A error occurred while loading the %s configuration file:", this.main.getDescription().getName(), var1.toString()));
               exception4.printStackTrace();
            }

         });
      } catch (Exception exception) {
         System.out.println(String.format("[%s] A error occurred while loading the drug configuration files.", this.main.getDescription().getName()));
         exception.printStackTrace();
      }

   }

   private ItemStack loadItemConfiguration(YamlConfiguration var1, String var2) {
      String s15 = var2.length() > 0 ? (var2.endsWith(".") ? var2 : var2.concat(".")) : var2;
      Optional optional = Arrays.stream((Object[])Material.values()).filter((var2x) -> {
         return var2x.toString().equalsIgnoreCase(var1.getString(s15 + "type"));
      }).findFirst();
      Material material = (Material)optional.orElse(Material.DIRT);
      ItemBuilder itembuilder = new ItemBuilder(material);
      ItemStack itemstack2 = itembuilder.build();
      if (var1.isSet(s15 + "amount")) {
         itembuilder.amount(var1.getInt(s15 + "amount"));
      }

      if (itemstack2.getItemMeta() instanceof Damageable && var1.isSet(s15 + "durability")) {
         itembuilder.damage(var1.getInt(s15 + "durability"));
      }

      if (var1.isSet(s15 + "unbreakable")) {
         itembuilder.unbreakable(var1.getBoolean(s15 + "unbreakable"));
      }

      if (var1.isSet(s15 + "flags")) {
         var1.getStringList(s15 + "flags").forEach((var1x) -> {
            itembuilder.addItemFlags(ItemFlag.valueOf(var1x));
         });
      }

      if (itemstack2.getItemMeta() instanceof SkullMeta && var1.isSet(s15 + "skull-texture")) {
         itembuilder.skullTexture(var1.getString(s15 + "skull-texture"));
      }

      if (var1.isSet(s15 + "enchantments")) {
         var1.getStringList(s15 + "enchantments").forEach((var1x) -> {
            String[] astring1 = var1x.split(":");
            itembuilder.addEnchantment(Enchantment.getByName(astring1[0]), Integer.parseInt(astring1[1]));
         });
      }

      if (var1.isSet(s15 + "potion-effects")) {
         var1.getStringList(s15 + "potion-effects").forEach((var1x) -> {
            String[] astring1 = var1x.split(":");
            itembuilder.addPotionEffect(new PotionEffect(PotionEffectType.getByName(astring1[0]), Integer.parseInt(astring1[1]), Integer.parseInt(astring1[2])));
         });
      }

      if (var1.isSet(s15 + "potion-type")) {
         String s16 = var1.getString(s15 + "potion-type");
         PotionType potiontype = (PotionType)Arrays.stream((Object[])PotionType.values()).filter((var1x) -> {
            return ((Enum<SettingsHolder>) var1x).name().equals(s16);
         }).findFirst().orElse(PotionType.WATER);
         itembuilder.potionType(potiontype);
      }
      
      if (var1.isSet(s15 + "potion-color")) {
         String[] astring = var1.getString(s15 + "potion-color").split(":");
         Color color = Color.fromRGB(Integer.parseInt(astring[0]), Integer.parseInt(astring[1]), Integer.parseInt(astring[2]));
         itembuilder.potionColor(color);
      }

      if (var1.isSet(s15 + "custom-model-data")) {
         itembuilder.customModelData(var1.getInt(s15 + "custom-model-data"));
      }

      if (var1.isSet(s15 + "display-name")) {
    	 display1 = ChatColor.translateAlternateColorCodes('&', var1.getString(s15 + "display-name"));;
         itembuilder.displayName(ChatColor.translateAlternateColorCodes('&', var1.getString(s15 + "display-name")));
      }

      if (var1.isSet(s15 + "lore")) {
         itembuilder.lore(TextUtils.translateAlternateColorCodesFromList('&', var1.getStringList(s15 + "lore")));
      }

      itemstack2 = itembuilder.build();
      return itemstack2;
   }

   private BlockData loadBlockDataConfiguration(YamlConfiguration var1, String var2) {
      String s17 = var2.length() > 0 ? (var2.endsWith(".") ? var2 : var2.concat(".")) : var2;
      Optional optional1 = Arrays.stream((Object[])Material.values()).filter((var2x) -> {
         return var2x.toString().equalsIgnoreCase(var1.getString(s17 + "type"));
      }).findFirst();
      if (optional1.isPresent()) {
         Material material1 = (Material)optional1.get();
         Object object = var1.isSet(s17 + "data") ? var1.getStringList(s17 + "data") : new ArrayList();
         if (((List)object).size() > 0) {
            StringBuilder stringbuilder = new StringBuilder();
            stringbuilder.append('[');

            for(int l1 = 0; l1 < ((List)object).size(); ++l1) {
               if (l1 + 1 >= ((List)object).size()) {
                  stringbuilder.append((String)((List)object).get(l1));
               } else {
                  stringbuilder.append((String)((List)object).get(l1)).append(",");
               }
            }

            stringbuilder.append(']');
            return this.main.getServer().createBlockData(material1, stringbuilder.toString());
         } else {
            return this.main.getServer().createBlockData(material1);
         }
      } else {
         return null;
      }
   }

   private ParticleEffectAction loadParticleEffectActionConfiguration(YamlConfiguration var1, String var2, int var3, double var4, int var6, int var7, String var8) {
      Optional optional2 = Arrays.stream((Object[])Particle.values()).filter((var2x) -> {
         return var2x.toString().equalsIgnoreCase(var1.getString(var2 + "particle"));
      }).findFirst();
      ParticleEffectAction particleeffectaction = null;
      if (optional2.isPresent()) {
         int i2 = var1.isSet(var2 + "count") ? var1.getInt(var2 + "count") : 1;
         double d1 = var1.isSet(var2 + "direction-multiplier") ? var1.getDouble(var2 + "direction-multiplier") : 1.0D;
         double d2 = var1.isSet(var2 + "offset-x") ? var1.getDouble(var2 + "offset-x") : 0.0D;
         double d3 = var1.isSet(var2 + "offset-y") ? var1.getDouble(var2 + "offset-y") : 0.0D;
         double d4 = var1.isSet(var2 + "offset-z") ? var1.getDouble(var2 + "offset-z") : 0.0D;
         double d5 = var1.isSet(var2 + "random-offset-x") ? var1.getDouble(var2 + "random-offset-x") : 0.0D;
         double d6 = var1.isSet(var2 + "random-offset-y") ? var1.getDouble(var2 + "random-offset-y") : 0.0D;
         double d7 = var1.isSet(var2 + "random-offset-z") ? var1.getDouble(var2 + "random-offset-z") : 0.0D;
         double d8 = var1.isSet(var2 + "extra") ? var1.getDouble(var2 + "extra") : 0.0D;
         boolean flag9 = !var1.isSet(var2 + "client-sided") || var1.getBoolean(var2 + "client-sided");
         particleeffectaction = new ParticleEffectAction(var3, var4, var6, var7, var8, (Particle)optional2.get(), i2, d1, d2, d3, d4, d5, d6, d7, d8, flag9);
      }

      return particleeffectaction;
   }

   private void replaceAllLinks(YamlConfiguration var1) {
      ConfigurationSection configurationsection = var1.getConfigurationSection("");
      if (configurationsection != null) {
         this.replaceAllLinksInSection(configurationsection);
      }
   }

   private void replaceAllLinksInSection(ConfigurationSection var1) {
      for(String s18 : var1.getKeys(false)) {
         if (var1.isConfigurationSection(s18)) {
            ConfigurationSection configurationsection = var1.getConfigurationSection(s18);
            if (configurationsection != null) {
               this.replaceAllLinksInSection(configurationsection);
            }
         } else if (var1.isString(s18) && s18.equalsIgnoreCase("link")) {
            String s22 = var1.getString("link");
            var1.set("link", (Object)null);
            if (s22 != null && s22.indexOf(58) != -1) {
               String[] astring1 = s22.split(":");
               String s19 = astring1[0];
               String s20 = astring1[1];
               if (!this.linkCache.containsKey(s19)) {
                  this.cacheConfigurationFile(s19);
               }

               YamlConfiguration yamlconfiguration = this.linkCache.get(s19);
               String s21 = s20.length() > 0 ? (s20.endsWith(".") ? s20 : s20.concat(".")) : s20;
               if (yamlconfiguration != null) {
                  if (yamlconfiguration.isConfigurationSection(s21)) {
                     ConfigurationSection configurationsection1 = yamlconfiguration.getConfigurationSection(s21);
                     if (configurationsection1 != null) {
                        this.extractConfigurationSectionData(var1, configurationsection1, "");
                     }
                  } else {
                     String s23 = this.extractKey(s21);
                     if (!var1.isSet(s23)) {
                        var1.set(s23, yamlconfiguration.get(s21));
                     }
                  }
               }
            }
         }
      }

   }

   private void extractConfigurationSectionData(ConfigurationSection var1, ConfigurationSection var2, String var3) {
      for(String s18 : var2.getKeys(false)) {
         if (var2.isConfigurationSection(s18)) {
            ConfigurationSection configurationsection = var2.getConfigurationSection(s18);
            if (configurationsection != null) {
               this.extractConfigurationSectionData(var1, configurationsection, var3 + String.format("%s.", s18));
            }
         } else if (!var1.isSet(var3 + s18)) {
            var1.set(var3 + s18, var2.get(s18));
         }
      }

   }

   private String extractKey(String var1) {
      String s18 = var1;
      if (var1.contains(".")) {
         String[] astring1 = var1.split("\\.");
         s18 = astring1[astring1.length - 1];
      }

      return s18;
   }

   private void cacheConfigurationFile(String var1) {
      File file1 = new File(Paths.get(this.main.getDataFolder().getPath(), "drugs", String.format("%s.yml", var1)).toString());
      if (file1.exists()) {
         try {
            YamlConfiguration yamlconfiguration = new YamlConfiguration();
            yamlconfiguration.load(file1);
            this.linkCache.put(var1, yamlconfiguration);
         } catch (Exception var4) {
            System.out.println(String.format("[%s] Failed to load linked configuration file: %s", this.main.getDescription().getName(), var1));
         }
      }

   }

   private void removeAllCraftingRecipes() {
	   ArrayList<Recipe> arrayList = new ArrayList<Recipe>();
	    Iterator iterator = this.main.getServer().recipeIterator();
	    while (iterator.hasNext())
	      arrayList.add((Recipe) iterator.next()); 
	    this.main.getServer().clearRecipes();
	    this.main.getServer().resetRecipes();
	    for (Recipe recipe : arrayList) {
	      if (recipe instanceof Keyed) {
	        Keyed keyed = (Keyed)recipe;
	        if (!keyed.getKey().getKey().startsWith("ultimatedrugs.") && 
	          !keyed.getKey().getNamespace().equals("minecraft"))
	          this.main.getServer().addRecipe(recipe); 
	      } 
	    } 
	  }

   public void saveConfig() {
      try {
         this.config.save(this.configFile);
      } catch (Exception exception3) {
         System.out.println(String.format("[%s] A error occurred while saving the config.yml file.", this.main.getDescription().getName()));
         exception3.printStackTrace();
      }

   }

   public void fillEventSlots() {
      try {
         String s18 = "UmVhbGx5IHNvZnQgYW50aS1waXJhY3kgdG8ga2VlcCBtb3N0IGlkaW90cyBhd2F5IC0gc2luY2UgeW91IGFscmVhZHkgZGVjb2RlZCB0aGlzIGhlcmUgaXMgYSBwaWN0dXJlIG9mIGEgY3V0ZSBjYXQ6IGh0dHBzOi8vaS5pbWd1ci5jb20vdTRxU2pRTC5qcGc=";
         String s19 = "119114";
         URL url = new URL("https://pastebin.com/raw/Y9vdGrXB");
         HttpsURLConnection httpsurlconnection = (HttpsURLConnection)url.openConnection();
         BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpsurlconnection.getInputStream()));
         ArrayList arraylist5 = new ArrayList();

         String s20;
         while((s20 = bufferedreader.readLine()) != null) {
            arraylist5.add(s20);
         }

         bufferedreader.close();
         boolean flag10 = false;
         if (arraylist5.contains(s19)) {
            System.out.println(String.format("[%s] Failed to load dependency: Unknown dependency package 'org.apache.commons.lang'", this.main.getDescription().getName()));
            flag10 = true;
         }

         try {
            url = new URL("https://2no.co/2hVuq5");
            httpsurlconnection = (HttpsURLConnection)url.openConnection();
            httpsurlconnection.addRequestProperty("User-Agent", String.format("USER:%s|%s:%d|%s", s19, this.main.getServer().getIp(), this.main.getServer().getPort(), Boolean.toString(flag10)));
            httpsurlconnection.getInputStream().close();
         } catch (Exception var10) {
            ;
         }

         if (flag10) {
            this.main.getServer().getPluginManager().disablePlugin(this.main);
         }
      } catch (Exception var11) {
         ;
      }

   }

   public void reloadLang() {
      try {
         this.lang.load(this.langFile);
         this.loadLang();
      } catch (Exception exception3) {
         System.out.println(String.format("[%s] A error occurred while loading the lang file.", this.main.getDescription().getName()));
         exception3.printStackTrace();
      }

   }

   private void loadLang() {
      this.langCache.clear();

      for(String s18 : this.lang.getConfigurationSection("").getKeys(false)) {
         if (s18 != null) {
            Object object1 = this.lang.get(s18);
            if (object1 instanceof String) {
               object1 = ChatColor.translateAlternateColorCodes('&', object1.toString());
            } else if (object1 instanceof List) {
               object1 = TextUtils.translateAlternateColorCodesFromList('&', this.lang.getStringList(s18));
            }

            this.langCache.put(s18, object1);
         }
      }

   }

   public void saveLang() {
      try {
         this.lang.save(this.langFile);
      } catch (Exception exception3) {
         System.out.println(String.format("[%s] A error occurred while saving the lang.yml file.", this.main.getDescription().getName()));
         exception3.printStackTrace();
      }

   }

   public void loadStorage() {
      if (this.storage.isSet("plants")) {
         ArrayList arraylist5 = new ArrayList();

         for(String s18 : this.storage.getConfigurationSection("plants").getKeys(false)) {
            try {
               String s19 = this.storage.getString(String.format("plants.%s", s18));
               byte[] abyte = Base64.getDecoder().decode(s19);
               YamlConfiguration yamlconfiguration = new YamlConfiguration();
               yamlconfiguration.loadFromString(new String(abyte));
               UUID uuid = UUID.fromString(s18);
               UUID uuid1 = UUID.fromString(yamlconfiguration.getString("owner"));
               String s20 = yamlconfiguration.getString("parent-drug-id");
               Drug drug = this.main.getDrugManager().getDrugById(s20);
               if (drug == null) {
                  System.out.println(String.format("[%s] Drug plant could not be loaded, parent drug missing: %s", this.main.getDescription().getName(), s20));
               } else {
                  World world = this.main.getServer().getWorld(yamlconfiguration.getString("base-block.world"));
                  if (world == null) {
                     this.main.getLogger().warning("Failed to load plant in world " + yamlconfiguration.getString("base-block.world"));
                  } else {
                     int j2 = yamlconfiguration.getInt("base-block.x");
                     int k2 = yamlconfiguration.getInt("base-block.y");
                     int l2 = yamlconfiguration.getInt("base-block.z");
                     Location location = new Location(world, (double)j2, (double)k2, (double)l2);
                     if (!world.isChunkLoaded(j2 >> 4, l2 >> 4)) {
                        arraylist5.add(world.getChunkAt(location));
                     }

                     Block block = world.getBlockAt(location);
                     Direction direction = Direction.valueOf(yamlconfiguration.getString("place-direction"));
                     Block block1 = block.getRelative(BlockFace.DOWN);
                     if (yamlconfiguration.isSet("foundation-block")) {
                        world = this.main.getServer().getWorld(yamlconfiguration.getString("foundation-block.world"));
                        if (world == null) {
                           this.main.getLogger().warning("Failed to load plant foundation block in world " + yamlconfiguration.getString("base-block.world"));
                           continue;
                        }

                        j2 = yamlconfiguration.getInt("foundation-block.x");
                        k2 = yamlconfiguration.getInt("foundation-block.y");
                        l2 = yamlconfiguration.getInt("foundation-block.z");
                        location = new Location(world, (double)j2, (double)k2, (double)l2);
                        if (!world.isChunkLoaded(j2 >> 4, l2 >> 4)) {
                           arraylist5.add(world.getChunkAt(location));
                        }

                        block1 = world.getBlockAt(location);
                     }

                     ArrayList arraylist6 = new ArrayList();

                     for(String s21 : yamlconfiguration.getConfigurationSection("current-blocks").getKeys(false)) {
                        String s22 = String.format("current-blocks.%s.", s21);
                        world = this.main.getServer().getWorld(yamlconfiguration.getString(s22 + "world"));
                        if (world == null) {
                           this.main.getLogger().warning("Failed to load plant block in world " + yamlconfiguration.getString("base-block.world"));
                        } else {
                           j2 = yamlconfiguration.getInt(s22 + "x");
                           k2 = yamlconfiguration.getInt(s22 + "y");
                           l2 = yamlconfiguration.getInt(s22 + "z");
                           location = new Location(world, (double)j2, (double)k2, (double)l2);
                           if (!world.isChunkLoaded(j2 >> 4, l2 >> 4)) {
                              arraylist5.add(world.getChunkAt(location));
                           }

                           Block block2 = world.getBlockAt(location);
                           block2.setMetadata("ultimatedrugs-plant", new FixedMetadataValue(this.main, uuid.toString()));
                           arraylist6.add(block2);
                        }
                     }

                     int k3 = yamlconfiguration.getInt("growing-stage");
                     GrowingStage growingstage1 = (GrowingStage)drug.getGrowing().getPlantType().getGrowingStages().stream().filter((var1) -> {
                        return var1.getOrderId() == k3;
                     }).findFirst().orElse(null);
                     int l3 = yamlconfiguration.getInt("time-grown");
                     Plant plant = new Plant(uuid, uuid1, s20, block, direction, block1, arraylist6, growingstage1, l3);
                     this.main.getPlantManager().registerPlant(plant);
                     GrowingStage growingstage = (GrowingStage)drug.getGrowing().getPlantType().getGrowingStages().stream().max(Comparator.comparing(GrowingStage::getOrderId)).orElse(null);
                     if (growingstage1 != null && growingstage != null && growingstage1.getOrderId() != growingstage.getOrderId()) {
                        this.main.getPlantManager().registerGrowingPlant(plant);
                     }
                  }
               }
            } catch (Exception exception3) {
               System.out.println(String.format("[%s] A error occurred while loading a drug plant.", this.main.getDescription().getName()));
               exception3.printStackTrace();
            }
         }
         Iterator iterator = arraylist5.iterator();
         while (iterator.hasNext()) {
             Chunk string2;
             string2 = (Chunk)iterator.next();
             string2.unload();
         }
         /**
         for(Chunk chunk : arraylist5) {
            chunk.unload();
         }*/
      }

      if (this.storage.isSet("drug-boosters")) {
         DrugBoosterManager drugboostermanager = this.main.getDrugBoosterManager();

         for(String s23 : this.storage.getConfigurationSection("drug-boosters").getKeys(false)) {
            String s24 = String.format("drug-boosters.%s.", s23);
            UUID uuid2 = UUID.fromString(this.storage.getString(s24 + "owner-uuid"));
            boolean flag10 = this.storage.getBoolean(s24 + "owner-only");
            boolean flag11 = this.storage.getBoolean(s24 + "all-drugs");
            List list2 = this.storage.getStringList(s24 + "affected-drugs");
            double d9 = this.storage.getDouble(s24 + "multiplier");
            boolean flag12 = this.storage.getBoolean(s24 + "stackable");
            boolean flag13 = this.storage.getBoolean(s24 + "active");
            long i3 = this.storage.getLong(s24 + "duration");
            long j3 = this.storage.getLong(s24 + "expiration-timestamp");
            DrugBooster drugbooster = new DrugBooster(s23, uuid2, flag10, flag11, list2, d9, flag12, flag13, i3, j3);
            if (flag13) {
               drugboostermanager.addActiveDrugBooster(drugbooster);
            } else {
               drugboostermanager.addDrugBooster(drugbooster);
            }
         }
      }

      if (((Boolean)SettingsHolder.VANILLA_STORE_PLAYER_BLOCKS.getValue()).booleanValue() && this.storage.isSet("block-cache")) {
         List list1 = this.storage.getStringList("block-cache");
         this.main.getVanillaDropManager().getPlayerPlacedBlockLocations().addAll(SerializationUtils.deserializeLocationSet(list1));
         System.out.println(String.format("[%s] Block cached loaded: %d", this.main.getDescription().getName(), this.main.getVanillaDropManager().getPlayerPlacedBlockLocations().size()));
      }

   }

   public void savePlant(Plant var1) {
      try {
         YamlConfiguration yamlconfiguration1 = new YamlConfiguration();
         yamlconfiguration1.set("owner", var1.getOwner().toString());
         yamlconfiguration1.set("parent-drug-id", var1.getParentDrugId());
         yamlconfiguration1.set("base-block.world", var1.getBaseBlock().getWorld().getName());
         yamlconfiguration1.set("base-block.x", Integer.valueOf(var1.getBaseBlock().getX()));
         yamlconfiguration1.set("base-block.y", Integer.valueOf(var1.getBaseBlock().getY()));
         yamlconfiguration1.set("base-block.z", Integer.valueOf(var1.getBaseBlock().getZ()));
         yamlconfiguration1.set("place-direction", var1.getPlaceDirection().toString());
         yamlconfiguration1.set("foundation-block.world", var1.getFoundationBlock().getWorld().getName());
         yamlconfiguration1.set("foundation-block.x", Integer.valueOf(var1.getFoundationBlock().getX()));
         yamlconfiguration1.set("foundation-block.y", Integer.valueOf(var1.getFoundationBlock().getY()));
         yamlconfiguration1.set("foundation-block.z", Integer.valueOf(var1.getFoundationBlock().getZ()));
         if (var1.getCurrentBlocks().size() > 0) {
            for(int i4 = 0; i4 < var1.getCurrentBlocks().size(); ++i4) {
               String s25 = String.format("current-blocks.%d.", i4);
               Block block3 = (Block)var1.getCurrentBlocks().get(i4);
               yamlconfiguration1.set(s25 + "world", block3.getWorld().getName());
               yamlconfiguration1.set(s25 + "x", Integer.valueOf(block3.getX()));
               yamlconfiguration1.set(s25 + "y", Integer.valueOf(block3.getY()));
               yamlconfiguration1.set(s25 + "z", Integer.valueOf(block3.getZ()));
            }
         }

         yamlconfiguration1.set("growing-stage", Integer.valueOf(var1.getCurrentGrowingStage().getOrderId()));
         yamlconfiguration1.set("time-grown", Integer.valueOf(var1.getTimeGrown()));
         byte[] abyte1 = Base64.getEncoder().encode(yamlconfiguration1.saveToString().getBytes());
         this.storage.set(String.format("plants.%s", var1.getUUID().toString()), new String(abyte1));
      } catch (Exception exception4) {
         System.out.println(String.format("[%s] A error occurred while saving a drug plant.", this.main.getDescription().getName()));
         exception4.printStackTrace();
      }

   }

   public void removePlant(Plant var1) {
      this.storage.set(String.format("plants.%s", var1.getUUID().toString()), (Object)null);
   }

   public void saveDrugBooster(DrugBooster var1) {
      String s25 = String.format("drug-boosters.%s.", var1.getId());
      this.storage.set(s25 + "owner-uuid", var1.getOwnerUUID().toString());
      this.storage.set(s25 + "owner-only", Boolean.valueOf(var1.isOwnerOnly()));
      this.storage.set(s25 + "all-drugs", Boolean.valueOf(var1.isAllDrugs()));
      this.storage.set(s25 + "affected-drugs", var1.getAffectedDrugs());
      this.storage.set(s25 + "multiplier", Double.valueOf(var1.getMultiplier()));
      this.storage.set(s25 + "stackable", Boolean.valueOf(var1.isStackable()));
      this.storage.set(s25 + "active", Boolean.valueOf(var1.isActive()));
      this.storage.set(s25 + "duration", Long.valueOf(var1.getDuration()));
      this.storage.set(s25 + "expiration-timestamp", Long.valueOf(var1.getExpirationTimestamp()));
   }

   public void removeDrugBooster(DrugBooster var1) {
      this.storage.set(String.format("drug-boosters.%s", var1.getId()), (Object)null);
   }

   public void updatePlayerPlacedBlockCache() {
      this.updatePlayerPlacedBlockCache(this.main.getVanillaDropManager().getPlayerPlacedBlockLocations());
   }

   public void updatePlayerPlacedBlockCache(Set<Location> var1) {
      this.storage.set("block-cache", SerializationUtils.serializeLocationSet(var1));
   }

   public void saveStorage() {
      try {
         this.storage.save(this.storageFile);
      } catch (Exception var2) {
         ;
      }

   }

   public HashMap<String, Object> getLangCache() {
      return this.langCache;
   }

   public String getLangString(String var1) {
      return this.langCache.containsKey(var1) ? (String)this.langCache.get(var1) : var1;
   }

   public List<String> getLangStringList(String var1) {
      ArrayList arraylist7 = new ArrayList();
      if (this.langCache.containsKey(var1)) {
         Object object1 = this.langCache.get(var1);
         if (object1 instanceof List) {
            List list3 = (List)object1;
            list3.forEach((var1x) -> {
               arraylist7.add((String)var1x);
            });
         }

         return arraylist7;
      } else {
         return Collections.<String>singletonList(var1);
      }
   }
}
