// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.actions;

import me.dexuby.UltimateDrugs.utils.TextUtils;
import java.util.Iterator;
import javax.script.ScriptEngine;
import org.bukkit.entity.Player;
import java.util.Map;
import org.bukkit.entity.Entity;
import org.bukkit.command.CommandSender;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import me.dexuby.UltimateDrugs.api.UltimateDrugsAPI;
import javax.script.ScriptEngineManager;
import org.bukkit.OfflinePlayer;
import java.util.HashMap;
import org.bukkit.Bukkit;
import me.dexuby.UltimateDrugs.drugs.Drug;
import java.util.UUID;
import java.util.List;

public class ScriptAction extends ConsumeAction
{
    private List<String> codeLines;
    
    public ScriptAction(final int n, final double n2, final int n3, final int n4, final String s, final List<String> codeLines) {
        super(n, n2, n3, n4, s);
        (this.codeLines = codeLines).add(0, "");
    }
    
    @Override
    public void execute(final UUID uuid, final Drug drug) {
        final Player player = Bukkit.getPlayer(uuid);
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        final HashMap<String, String> hashMap = new HashMap<String, String>() {
            {
                this.put("%player_name%", (offlinePlayer.getName() != null) ? offlinePlayer.getName() : "Unknown");
                this.put("%player_uuid%", offlinePlayer.getUniqueId().toString());
                this.put("%drug_id%", drug.getId());
            }
        };
        final ScriptEngine engineByName = new ScriptEngineManager().getEngineByName("JavaScript");
        engineByName.put("UltimateDrugs", UltimateDrugsAPI.getInstance());
        engineByName.put("Server", Bukkit.getServer());
        engineByName.put("Player", (player != null) ? player : offlinePlayer);
        if (this.getTargetSelector() != null) {
            for (final Entity entity : ReflectionUtils.getEntitiesFromSelector((CommandSender)player, this.getTargetSelector())) {
                engineByName.put("Target", entity);
                hashMap.put("%target_name%", entity.getName());
                hashMap.put("%target_uuid%", entity.getUniqueId().toString());
                this.apply(engineByName, hashMap);
            }
        }
        else {
            this.apply(engineByName, hashMap);
        }
    }
    
    private void apply(final ScriptEngine scriptEngine, final Map<String, String> map) {
        try {
            this.codeLines.add(0, "load(\"nashorn:mozilla_compat.js\");");
            scriptEngine.eval(TextUtils.replaceVariables(String.join("\n", this.codeLines), map));
        }
        catch (Exception ex) {
            System.out.println(String.format("[%s] Failed to execute script:", "UltimateDrugs"));
            ex.printStackTrace();
        }
    }
    
    public List<String> getCodeLines() {
        return this.codeLines;
    }
    
    @Override
    public ScriptAction clone() {
        return new ScriptAction(this.getDelay(), this.getChance(), this.getRepetitions(), this.getRepetitionTickDelay(), this.getTargetSelector(), this.codeLines);
    }
}
