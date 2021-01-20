package me.dexuby.UltimateDrugs.commands;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.dexuby.UltimateDrugs.Main;
import me.dexuby.UltimateDrugs.managers.ConfigManager;
import me.dexuby.UltimateDrugs.utils.NumberUtils;
import me.dexuby.UltimateDrugs.utils.TextUtils;
import org.bukkit.command.CommandSender;

public class HelpCommand extends CustomCommand {
  private Main main;
  
  private ConfigManager configManager;
  
  public HelpCommand(Main paramMain) {
    super("dr", new String[] { "help" }, "Used to display available commands.", "/dr <help>", "ultimatedrugs.help", 0, 1);
    this.main = paramMain;
    this.configManager = paramMain.getConfigManager();
  }
  
  public void executeCommand(CommandSender paramCommandSender, List<String> paramList) {
    int i = 1;
    if (paramList.size() > 0)
      if (NumberUtils.isValidInteger(paramList.get(0))) {
        i = Integer.parseInt(paramList.get(0));
        if (i < 1)
          i = 1; 
      } else {
        paramCommandSender.sendMessage(this.configManager.getLangString("invalid-page-number"));
      }  
    List<CustomCommand> list = (List)this.main.getCommandManager().getRegisteredCommands().stream().filter(paramCustomCommand -> paramCommandSender.hasPermission(paramCustomCommand.getPermission())).collect(Collectors.toList());
    int j = (int)Math.ceil(list.size() / 6.0D);
    if (i > j) {
      paramCommandSender.sendMessage(this.configManager.getLangString("help-page-not-found"));
    } else {
      StringBuilder stringBuilder = new StringBuilder(this.configManager.getLangString("help-page-header"));
      stringBuilder.append("\n");
      for (int k = (i == 1) ? i : ((i - 1) * 6 + 1); k < 6 * i + 1; k++) {
        if (list.size() >= k) {
          CustomCommand customCommand = list.get(k - 1);
          Map<String, String> hashMap1 = new HashMap<>();
          hashMap1.put("%command_name%", customCommand.getName());
          hashMap1.put("%command_args%", String.join(" ", (CharSequence[])customCommand.getSubNames()));
          hashMap1.put("%command_description%", customCommand.getDescription());
          hashMap1.put("%command_usage%", customCommand.getUsage());
          hashMap1.put("%command_permission%", customCommand.getPermission());
          stringBuilder.append(TextUtils.replaceVariables(this.configManager.getLangString("help-command-layout"), hashMap1));
          if (k < 6 * i)
            stringBuilder.append("\n"); 
        } 
      } 
      Map<String, String> hashMap = new HashMap<>();
      hashMap.put("%next_page%", (i + 1 <= j) ? Integer.toString(i + 1) : "/");
      stringBuilder.append("\n");
      stringBuilder.append(TextUtils.replaceVariables(this.configManager.getLangString("help-page-footer"), hashMap));
      paramCommandSender.sendMessage(stringBuilder.toString());
    } 
  }
  
  public List<String> getTabCompletion(String[] paramArrayOfString) {
    return Collections.singletonList("1");
  }
}
