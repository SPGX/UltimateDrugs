// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.guis;

import me.dexuby.UltimateDrugs.managers.DrugBoosterManager;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.Iterator;
import java.util.Collection;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import java.util.Map;
import me.dexuby.UltimateDrugs.utils.TextUtils;
import me.dexuby.UltimateDrugs.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import me.dexuby.UltimateDrugs.utils.TimeUtils;
import java.util.HashMap;
import me.dexuby.UltimateDrugs.drugs.DrugBooster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Inventory;
import me.dexuby.UltimateDrugs.managers.ConfigManager;
import me.dexuby.UltimateDrugs.Main;

public class DrugBoostersGUI extends GUI
{
    private Main main;
    private ConfigManager configManager;
    
    public DrugBoostersGUI(final Main main) {
        super("drug-boosters", true);
        this.main = main;
        this.configManager = main.getConfigManager();
    }
    
    @Override
    public Inventory getInventory() {
        return this.main.getServer().createInventory((InventoryHolder)new GUIHolder(this), 27, this.main.getConfigManager().getLangString("drug-boosters-gui-title"));
    }
    
    @Override
    public void open(final Player player, final Object... array) {
        final Collection<DrugBooster> availablePlayerDrugBoosters = this.main.getDrugBoosterManager().getAvailablePlayerDrugBoosters(player);
        final Inventory inventory = this.main.getServer().createInventory((InventoryHolder)new GUIHolder(this), Math.max((int)Math.round(Math.ceil(availablePlayerDrugBoosters.size() / 9.0) * 9.0), 9), this.main.getConfigManager().getLangString("drug-boosters-gui-title"));
        for (final DrugBooster drugBooster : availablePlayerDrugBoosters) {
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("%drug_booster_id%", drugBooster.getId());
            hashMap.put("%drug_booster_all_drugs%", drugBooster.isActive() ? this.configManager.getLangString("yes") : this.configManager.getLangString("no"));
            hashMap.put("%drug_booster_affected_drugs%", (drugBooster.getAffectedDrugs().size() > 0) ? String.join("\n", drugBooster.getAffectedDrugs()) : this.configManager.getLangString("none"));
            hashMap.put("%drug_booster_multiplier%", Double.toString(drugBooster.getMultiplier()));
            hashMap.put("%drug_booster_stackable%", drugBooster.isStackable() ? this.configManager.getLangString("yes") : this.configManager.getLangString("no"));
            hashMap.put("%drug_booster_duration%", TimeUtils.convertSecondsToFormattedTime(drugBooster.getDuration()));
            inventory.addItem(new ItemStack[] { ReflectionUtils.setItemStackStringTag(new ItemBuilder(Material.NETHER_STAR).displayName(TextUtils.replaceVariables(this.configManager.getLangString("drug-booster-item-display-name"), hashMap)).lore(TextUtils.replaceVariables(this.configManager.getLangStringList("drug-booster-item-lore"), hashMap)).build(), "booster-id", drugBooster.getId()) });
        }
        player.openInventory(inventory);
    }
    
    @Override
    public void onClick(final InventoryClickEvent inventoryClickEvent) {
        final Player player = (Player)inventoryClickEvent.getWhoClicked();
        if (inventoryClickEvent.getCurrentItem() != null) {
            final DrugBoosterManager drugBoosterManager = this.main.getDrugBoosterManager();
            final String itemStackStringTag = ReflectionUtils.getItemStackStringTag(inventoryClickEvent.getCurrentItem(), "booster-id");
            if (itemStackStringTag != null && !itemStackStringTag.equals("")) {
                final DrugBooster drugBoosterById = drugBoosterManager.getDrugBoosterById(itemStackStringTag);
                final HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("%drug_booster_owner%", player.getName());
                hashMap.put("%drug_booster_id%", drugBoosterById.getId());
                hashMap.put("%drug_booster_all_drugs%", drugBoosterById.isActive() ? this.configManager.getLangString("yes") : this.configManager.getLangString("no"));
                hashMap.put("%drug_booster_affected_drugs%", (drugBoosterById.getAffectedDrugs().size() > 0) ? String.join("\n", drugBoosterById.getAffectedDrugs()) : this.configManager.getLangString("none"));
                hashMap.put("%drug_booster_multiplier%", Double.toString(drugBoosterById.getMultiplier()));
                hashMap.put("%drug_booster_stackable%", drugBoosterById.isStackable() ? this.configManager.getLangString("yes") : this.configManager.getLangString("no"));
                hashMap.put("%drug_booster_duration%", TimeUtils.convertSecondsToFormattedTime(drugBoosterById.getDuration()));
                drugBoosterManager.activateBooster(drugBoosterById);
                player.sendMessage(TextUtils.replaceVariables(this.configManager.getLangString("booster-activated"), hashMap));
                this.main.getServer().broadcastMessage(TextUtils.replaceVariables(this.configManager.getLangString("booster-activation-broadcast"), hashMap));
                player.closeInventory();
            }
        }
    }
}
