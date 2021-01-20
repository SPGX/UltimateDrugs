// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.guis;

import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.Iterator;
import java.util.List;
import me.dexuby.UltimateDrugs.utils.ItemBuilder;
import java.util.Collection;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import me.dexuby.UltimateDrugs.drugs.growing.GrowingBlock;
import java.util.Map;
import me.dexuby.UltimateDrugs.utils.TextUtils;
import me.dexuby.UltimateDrugs.drugs.Drop;
import java.util.HashMap;
import java.util.ArrayList;
import me.dexuby.UltimateDrugs.drugs.growing.GrowingStage;
import me.dexuby.UltimateDrugs.drugs.Drug;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Inventory;
import me.dexuby.UltimateDrugs.managers.ConfigManager;
import me.dexuby.UltimateDrugs.Main;

public class DrugGrowingInfoGUI extends GUI
{
    private Main main;
    private ConfigManager configManager;
    
    public DrugGrowingInfoGUI(final Main main) {
        super("drug-growing-info", true);
        this.main = main;
        this.configManager = main.getConfigManager();
    }
    
    @Override
    public Inventory getInventory() {
        return this.main.getServer().createInventory((InventoryHolder)new GUIHolder(this), 0, this.main.getConfigManager().getLangString("drug-growing-info-gui-title"));
    }
    
    @Override
    public void open(final Player player, final Object... array) {
        if (array.length > 0) {
            final Drug drug = (Drug)array[0];
            final Inventory inventory = this.main.getServer().createInventory((InventoryHolder)new GUIHolder(this), (int)Math.round(Math.ceil(drug.getGrowing().getPlantType().getGrowingStages().size() / 9.0) * 9.0), this.main.getConfigManager().getLangString("drug-growing-info-gui-title"));
            int n = 0;
            for (final GrowingStage growingStage : drug.getGrowing().getPlantType().getGrowingStages()) {
                final ArrayList<String> list = new ArrayList<String>();
                final HashMap<String, String> hashMap = new HashMap<String, String>();
                for (final Drop drop : growingStage.getDrops()) {
                    hashMap.put("%drop_item_type%", drop.getItemStack().getType().name());
                    hashMap.put("%drop_item_display_name%", (drop.getItemStack().getItemMeta() != null) ? (drop.getItemStack().getItemMeta().hasDisplayName() ? drop.getItemStack().getItemMeta().getDisplayName() : drop.getItemStack().getType().name()) : drop.getItemStack().getType().name());
                    hashMap.put("%drop_min_amount%", Integer.toString(drop.getMinAmount()));
                    hashMap.put("%drop_max_amount%", Integer.toString(drop.getMaxAmount()));
                    hashMap.put("%drop_drop_chance%", Double.toString(drop.getDropChance()));
                    list.add(TextUtils.replaceVariables(this.configManager.getLangString("drug-growing-info-drop-layout"), hashMap));
                }
                hashMap.clear();
                hashMap.put("%growing_stage_order_id%", Integer.toString(growingStage.getOrderId()));
                hashMap.put("%growing_stage_grown%", growingStage.isGrown() ? this.configManager.getLangString("yes") : this.configManager.getLangString("no"));
                hashMap.put("%growing_stage_delay%", Integer.toString(growingStage.getGrowDelay()));
                hashMap.put("%growing_stage_drops%", list.toString());
                ItemStack texturedSkull;
                if (growingStage.getGrowingBlocks().get(0).getSkullTexture() != null) {
                    texturedSkull = ReflectionUtils.getTexturedSkull(growingStage.getGrowingBlocks().get(0).getSkullTexture());
                }
                else {
                    final Material material = growingStage.getGrowingBlocks().get(0).getBlockData().getMaterial();
                    if (material.isItem()) {
                        texturedSkull = new ItemStack(material);
                    }
                    else {
                        texturedSkull = new ItemStack(Material.BARRIER);
                    }
                }
                final ArrayList<String> list2 = new ArrayList<String>();
                for (final String s : this.configManager.getLangStringList("drug-growing-info-item-lore")) {
                    if (s.contains("%growing_stage_drops%")) {
                        final String replace = s.replace("%growing_stage_drops%", "");
                        if (replace.length() > 0) {
                            list2.add(replace);
                        }
                        list2.addAll((Collection<? extends String>)list);
                    }
                    else {
                        list2.add(s);
                    }
                }
                inventory.setItem(n, new ItemBuilder(texturedSkull).displayName(TextUtils.replaceVariables(this.configManager.getLangString("drug-growing-info-item-display-name"), hashMap)).lore(TextUtils.replaceVariables(list2, hashMap)).build());
                ++n;
            }
            player.openInventory(inventory);
        }
    }
    
    @Override
    public void onClick(final InventoryClickEvent inventoryClickEvent) {
    }
}
