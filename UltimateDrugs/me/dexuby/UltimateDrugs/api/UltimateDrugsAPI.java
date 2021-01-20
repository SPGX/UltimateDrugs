// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.api;

import me.dexuby.UltimateDrugs.managers.DrugManager;
import org.bukkit.entity.Player;
import me.dexuby.UltimateDrugs.Main;

public class UltimateDrugsAPI
{
    private static UltimateDrugsAPI instance;
    private static Main main;
    
    public boolean isPlayerOnDrugs(final Player player) {
        return UltimateDrugsAPI.main.getDrugManager().isPlayerOnDrugs(player);
    }
    
    public DrugManager getDrugManager() {
        return UltimateDrugsAPI.main.getDrugManager();
    }
    
    public static UltimateDrugsAPI getInstance() {
        if (UltimateDrugsAPI.instance == null) {
            UltimateDrugsAPI.instance = new UltimateDrugsAPI();
            UltimateDrugsAPI.main = Main.getInstance();
        }
        return UltimateDrugsAPI.instance;
    }
}
