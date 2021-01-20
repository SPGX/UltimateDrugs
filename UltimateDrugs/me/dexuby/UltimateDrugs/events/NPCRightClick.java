// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.events;

import org.bukkit.event.EventHandler;
import me.dexuby.UltimateDrugs.guis.GUI;
import me.dexuby.UltimateDrugs.drugs.DrugDealer;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import me.dexuby.UltimateDrugs.Main;
import org.bukkit.event.Listener;

public class NPCRightClick implements Listener
{
    private Main main;
    
    public NPCRightClick(final Main main) {
        this.main = main;
    }
    
    @EventHandler
    public void onNPCRightClick(final NPCRightClickEvent npcRightClickEvent) {
        final DrugDealer drugDealerByNPCId = this.main.getDrugManager().getDrugDealerByNPCId(npcRightClickEvent.getNPC().getId());
        if (drugDealerByNPCId != null) {
            final GUI guiById = this.main.getGUIManager().getGUIById(String.format("drug-%s", drugDealerByNPCId.getType().toString().toLowerCase()));
            if (guiById != null) {
                guiById.open(npcRightClickEvent.getClicker(), new Object[0]);
            }
        }
    }
}
