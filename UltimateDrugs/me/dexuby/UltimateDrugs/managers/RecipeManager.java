/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ShapelessRecipe
 */
package me.dexuby.UltimateDrugs.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import me.dexuby.UltimateDrugs.Main;
import me.dexuby.UltimateDrugs.drugs.DrugCraftingRecipe;
import me.dexuby.UltimateDrugs.drugs.DrugRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class RecipeManager {
    private Main main;
    private Collection<DrugRecipe> registeredDrugRecipes = new ArrayList<DrugRecipe>();

    public RecipeManager(Main main) {
        this.main = main;
    }

    public Collection<DrugRecipe> getRegisteredDrugRecipes() {
        return this.registeredDrugRecipes;
    }

    public DrugRecipe getDrugRecipeById(String string) {
        return this.registeredDrugRecipes.stream().filter(drugRecipe -> drugRecipe.getId().equals(string)).findFirst().orElse(null);
    }

    public List<DrugCraftingRecipe> getAllShapelessCraftingRecipes() {
        return this.registeredDrugRecipes.stream().filter(drugRecipe -> drugRecipe instanceof DrugCraftingRecipe && ((DrugCraftingRecipe)drugRecipe).getRecipe() instanceof ShapelessRecipe).map(drugRecipe -> (DrugCraftingRecipe)drugRecipe).collect(Collectors.toList());
    }

    void registerDrugRecipe(DrugRecipe drugRecipe) {
        this.registeredDrugRecipes.add(drugRecipe);
    }

    void unregisterDrugRecipe(String string) {
        if (this.registeredDrugRecipes.stream().anyMatch(drugRecipe -> drugRecipe.getId().equals(string))) {
            this.registeredDrugRecipes.remove(this.registeredDrugRecipes.stream().filter(drugRecipe -> drugRecipe.getId().equals(string)).findFirst().orElse(null));
        }
    }
}

