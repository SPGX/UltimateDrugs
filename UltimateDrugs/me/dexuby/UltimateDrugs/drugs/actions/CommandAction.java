package me.dexuby.UltimateDrugs.drugs.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.dexuby.UltimateDrugs.drugs.Drug;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import me.dexuby.UltimateDrugs.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CommandAction extends ConsumeAction {
   private String command;
   private CommandSenderType senderType;

   public CommandAction(int var1, double var2, int var4, int var5, String var6, String var7, CommandSenderType var8) {
      super(var1, var2, var4, var5, var6);
      this.command = var7;
      this.senderType = var8;
   }

   public void execute(UUID var1, Drug var2) {
      OfflinePlayer offlineplayer = Bukkit.getOfflinePlayer(var1);
      HashMap hashmap = new HashMap();
      hashmap.put("%player%", offlineplayer.getName() != null ? offlineplayer.getName() : "Unknown");
      hashmap.put("%drug_id%", var2.getId());
      if (this.getTargetSelector() != null) {
         Object object = this.senderType == CommandSenderType.PLAYER ? Bukkit.getPlayer(var1) : Bukkit.getServer().getConsoleSender();
         if (object == null) {
            return;
         }

         for(Entity entity : ReflectionUtils.getEntitiesFromSelector((CommandSender)object, this.getTargetSelector())) {
            if (entity instanceof Player) {
               Player player = (Player)entity;
               if (this.senderType == CommandSenderType.TARGET) {
                  object = player;
               }

               hashmap.put("%target%", player.getName());
               this.apply((CommandSender)object, hashmap);
            }
         }
      } else {
         Object object1;
         if (this.senderType == CommandSenderType.PLAYER) {
            Player player1 = Bukkit.getPlayer(var1);
            if (player1 == null || !player1.isValid() || !player1.isOnline()) {
               return;
            }

            object1 = player1;
         } else {
            object1 = Bukkit.getServer().getConsoleSender();
         }

         this.apply((CommandSender)object1, hashmap);
      }

   }

   private void apply(CommandSender var1, Map<String, String> var2) {
      Bukkit.getServer().dispatchCommand(var1, TextUtils.replaceVariables(this.command, var2));
   }

   public String getCommand() {
      return this.command;
   }

   public CommandAction clone() {
      return new CommandAction(this.getDelay(), this.getChance(), this.getRepetitions(), this.getRepetitionTickDelay(), this.getTargetSelector(), this.command, this.senderType);
   }
}
