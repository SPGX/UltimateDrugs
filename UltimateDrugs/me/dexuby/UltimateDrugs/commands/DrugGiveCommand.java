// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.commands;

import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import me.dexuby.UltimateDrugs.drugs.Drug;
import org.bukkit.entity.Player;
import java.util.Map;
import me.dexuby.UltimateDrugs.utils.TextUtils;
import java.util.HashMap;
import org.bukkit.inventory.ItemStack;
import me.dexuby.UltimateDrugs.utils.NumberUtils;
import java.util.List;
import org.bukkit.command.CommandSender;
import me.dexuby.UltimateDrugs.managers.ConfigManager;
import me.dexuby.UltimateDrugs.Main;

public class DrugGiveCommand extends CustomCommand
{
    private Main main;
    private ConfigManager configManager;
    
    public DrugGiveCommand(final Main main) {
        super("dr", new String[] { "give" }, "Used to give a player a configured drug.", "/dr give <player> <drug> <amount>", "ultimatedrugs.give", 2, 3);
        this.main = main;
        this.configManager = main.getConfigManager();
    }
    
    @Override
    public void executeCommand(final CommandSender commandSender, final List<String> list) {
        final Player player = this.main.getServer().getPlayer((String)list.get(0));
        if (player != null) {
            final Drug drugById = this.main.getDrugManager().getDrugById(list.get(1));
            if (drugById != null) {
                final ItemStack clone = drugById.getDrugItemStack().clone();
                if (list.size() >= 3) {
                    if (!NumberUtils.isValidInteger(list.get(2))) {
                        commandSender.sendMessage(this.configManager.getLangString("invalid-number"));
                        return;
                    }
                    clone.setAmount(Integer.parseInt(list.get(2)));
                }
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(new ItemStack[] { clone });
                    final HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("%target%", player.getName());
                    hashMap.put("%drug_id%", drugById.getId());
                    hashMap.put("%amount%", (list.size() >= 3) ? list.get(2) : "1");
                    commandSender.sendMessage(TextUtils.replaceVariables(this.configManager.getLangString("give-successful"), hashMap));
                }
                else {
                    commandSender.sendMessage(this.configManager.getLangString("target-inventory-is-full"));
                }
            }
            else {
                commandSender.sendMessage(this.configManager.getLangString("drug-not-found"));
            }
        }
        else {
            commandSender.sendMessage(this.configManager.getLangString("player-not-found"));
        }
    }
    
    public List<String> getTabCompletion(String[] paramArrayOfString) {
        switch (paramArrayOfString.length) {
          default:
            return null;
          case 2:
            return (List<String>)this.main.getDrugManager().getDrugs().stream()
              .map(Drug::getId)
              .filter(paramString -> paramString.startsWith(paramArrayOfString[1]))
              .collect(Collectors.toList());
          case 3:
            break;
        } 
        return Collections.singletonList("1");
      }
}
