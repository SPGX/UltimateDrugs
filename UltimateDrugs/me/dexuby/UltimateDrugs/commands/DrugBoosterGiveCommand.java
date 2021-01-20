// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.commands;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import me.dexuby.UltimateDrugs.drugs.Drug;
import java.util.Arrays;
import org.bukkit.entity.Player;
import me.dexuby.UltimateDrugs.drugs.DrugBooster;
import java.util.UUID;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import me.dexuby.UltimateDrugs.managers.ConfigManager;
import me.dexuby.UltimateDrugs.Main;

public class DrugBoosterGiveCommand extends CustomCommand
{
    private Main main;
    private ConfigManager configManager;
    
    public DrugBoosterGiveCommand(final Main main) {
        super("dr", new String[] { "booster", "give" }, "Used to give boosters to players", "/dr booster give <player> <owner only> <all drugs> <affected drugs> <multiplier> <stackable> <duration>", "ultimatedrugs.booster.give", 7, 7);
        this.main = main;
        this.configManager = main.getConfigManager();
    }
    
    public void executeCommand(CommandSender paramCommandSender, List<String> paramList) {
        String str = paramList.get(0);
        Player player = this.main.getServer().getPlayer(str);
        if (player != null) {
          boolean bool1 = Boolean.parseBoolean(paramList.get(1));
          boolean bool2 = Boolean.parseBoolean(paramList.get(2));
          String str1 = paramList.get(3);
          ArrayList<? super String> arrayList = new ArrayList<Object>();
          if (str1.indexOf(',') != -1) {
            Collections.addAll(arrayList, str1.split(","));
          } else if (!str1.equals("null")) {
            arrayList.add(str1);
          } 
          double d = Double.parseDouble(paramList.get(4));
          boolean bool3 = Boolean.parseBoolean(paramList.get(5));
          long l = Long.parseLong(paramList.get(6));
          DrugBooster drugBooster = new DrugBooster(UUID.randomUUID().toString(), player.getUniqueId(), bool1, bool2, (Collection<String>) arrayList, d, bool3, false, l, -1L);
          this.main.getDrugBoosterManager().addDrugBooster(drugBooster);
          this.configManager.saveDrugBooster(drugBooster);
          paramCommandSender.sendMessage(this.configManager.getLangString("booster-give-successful"));
        } else {
          paramCommandSender.sendMessage(this.configManager.getLangString("player-not-found"));
        } 
      }
    
    public List<String> getTabCompletion(String[] paramArrayOfString) {
        switch (paramArrayOfString.length) {
          default:
            return null;
          case 2:
          case 3:
          case 6:
            return Arrays.asList(new String[] { "true", "false" });
          case 4:
            return (List<String>)this.main.getDrugManager().getDrugs().stream().map(Drug::getId).filter(paramString -> paramString.startsWith(paramArrayOfString[3])).collect(Collectors.toList());
          case 5:
            return Collections.singletonList("1.5");
          case 7:
            break;
        } 
        return Collections.singletonList("60");
      }
}
