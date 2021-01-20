// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.guis;

import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import java.util.function.Consumer;
import java.util.Arrays;
import me.dexuby.UltimateDrugs.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Inventory;
import me.dexuby.UltimateDrugs.Main;

public class DrugBrewingPreviewGUI extends GUI
{
    private Main main;
    
    public DrugBrewingPreviewGUI(final Main main) {
        super("drug-brewing-preview", true);
        this.main = main;
    }
    
    @Override
    public Inventory getInventory() {
        final Inventory inventory = this.main.getServer().createInventory((InventoryHolder)new GUIHolder(this), 45, this.main.getConfigManager().getLangString("drug-brewing-preview-gui-title"));
        for (int i = 0; i < 45; ++i) {
            inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).displayName("§0").build());
        }
        Arrays.asList(13, 19, 30, 31, 32).forEach(inventory::clear);
        return inventory;
    }
    
    @Override
    public void open(final Player p0, final Object... p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   me/dexuby/UltimateDrugs/guis/DrugBrewingPreviewGUI.getInventory:()Lorg/bukkit/inventory/Inventory;
        //     4: astore_3       
        //     5: aload_2        
        //     6: arraylength    
        //     7: ifle            85
        //    10: aload_2        
        //    11: iconst_0       
        //    12: aaload         
        //    13: checkcast       Lme/dexuby/UltimateDrugs/drugs/DrugBrewingRecipe;
        //    16: astore          4
        //    18: aload           4
        //    20: ifnull          85
        //    23: aload_3        
        //    24: bipush          13
        //    26: aload           4
        //    28: invokevirtual   me/dexuby/UltimateDrugs/drugs/DrugBrewingRecipe.getInput:()Lorg/bukkit/inventory/ItemStack;
        //    31: invokeinterface org/bukkit/inventory/Inventory.setItem:(ILorg/bukkit/inventory/ItemStack;)V
        //    36: aload           4
        //    38: invokevirtual   me/dexuby/UltimateDrugs/drugs/DrugBrewingRecipe.getFuel:()Lorg/bukkit/inventory/ItemStack;
        //    41: ifnull          57
        //    44: aload_3        
        //    45: bipush          19
        //    47: aload           4
        //    49: invokevirtual   me/dexuby/UltimateDrugs/drugs/DrugBrewingRecipe.getFuel:()Lorg/bukkit/inventory/ItemStack;
        //    52: invokeinterface org/bukkit/inventory/Inventory.setItem:(ILorg/bukkit/inventory/ItemStack;)V
        //    57: iconst_1       
        //    58: newarray        I
        //    60: dup            
        //    61: iconst_0       
        //    62: bipush          30
        //    64: iastore        
        //    65: astore          5
        //    67: aload           4
        //    69: invokevirtual   me/dexuby/UltimateDrugs/drugs/DrugBrewingRecipe.getResults:()Ljava/util/List;
        //    72: aload_3        
        //    73: aload           5
        //    75: invokedynamic   BootstrapMethod #1, accept:(Lorg/bukkit/inventory/Inventory;[I)Ljava/util/function/Consumer;
        //    80: invokeinterface java/util/List.forEach:(Ljava/util/function/Consumer;)V
        //    85: aload_1        
        //    86: aload_3        
        //    87: invokeinterface org/bukkit/entity/Player.openInventory:(Lorg/bukkit/inventory/Inventory;)Lorg/bukkit/inventory/InventoryView;
        //    92: pop            
        //    93: return         
        //    StackMapTable: 00 02 FD 00 39 07 00 36 07 00 81 FA 00 1B
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Could not infer any expression.
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:374)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public void onClick(final InventoryClickEvent inventoryClickEvent) {
    }
}
