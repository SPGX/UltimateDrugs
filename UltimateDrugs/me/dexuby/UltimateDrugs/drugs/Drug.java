// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs;

import me.dexuby.UltimateDrugs.drugs.growing.Growing;
import java.util.Map;
import me.dexuby.UltimateDrugs.drugs.actions.ConsumeAction;
import me.dexuby.UltimateDrugs.drugs.chatslur.ChatSlurType;
import java.util.Collection;
import org.bukkit.inventory.ItemStack;

public class Drug
{
    private String id;
    private boolean legal;
    private double sellPrice;
    private double buyPrice;
    private int effectDuration;
    private ItemStack drugItemStack;
    private ConsumeOption consumeOption;
    private boolean removeOnConsume;
    private int exchangeItemLimit;
    private Collection<ExchangeItem> consumeExchangeItems;
    private ConsumeConditions consumeConditions;
    private boolean resetOnConsume;
    private ChatSlurType chatSlurType;
    private double globalSoundVolumeModifier;
    private double globalSoundPitchModifier;
    private boolean removeOnDisconnect;
    private boolean removeOnDeath;
    private boolean pauseWhileOffline;
    private Collection<ConsumeAction> consumeActions;
    private Map<MultiplierType, Double> multipliers;
    private Collection<DrugCraftingRecipe> craftingRecipes;
    private Collection<DrugFurnaceRecipe> furnaceRecipes;
    private Collection<DrugBrewingRecipe> drugBrewingRecipes;
    private Collection<DrugRelatedGood> drugRelatedGoods;
    private Growing growing;
    private String namedisplay;
    public Drug(final String id, final boolean legal, final double sellPrice, final double buyPrice, final int effectDuration, final ItemStack drugItemStack, final ConsumeOption consumeOption, final boolean removeOnConsume, final int exchangeItemLimit, final Collection<ExchangeItem> consumeExchangeItems, final ConsumeConditions consumeConditions, final boolean resetOnConsume, final ChatSlurType chatSlurType, final double globalSoundVolumeModifier, final double globalSoundPitchModifier, final boolean removeOnDisconnect, final boolean removeOnDeath, final boolean pauseWhileOffline, final Collection<ConsumeAction> consumeActions, final Map<MultiplierType, Double> multipliers, final Collection<DrugCraftingRecipe> craftingRecipes, final Collection<DrugFurnaceRecipe> furnaceRecipes, final Collection<DrugBrewingRecipe> drugBrewingRecipes, final Collection<DrugRelatedGood> drugRelatedGoods, final Growing growing, String namedisplay) {
        this.id = id;
        this.legal = legal;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.effectDuration = effectDuration;
        this.drugItemStack = drugItemStack;
        this.consumeOption = consumeOption;
        this.removeOnConsume = removeOnConsume;
        this.exchangeItemLimit = exchangeItemLimit;
        this.consumeExchangeItems = consumeExchangeItems;
        this.consumeConditions = consumeConditions;
        this.resetOnConsume = resetOnConsume;
        this.chatSlurType = chatSlurType;
        this.globalSoundVolumeModifier = globalSoundVolumeModifier;
        this.globalSoundPitchModifier = globalSoundPitchModifier;
        this.removeOnDisconnect = removeOnDisconnect;
        this.removeOnDeath = removeOnDeath;
        this.pauseWhileOffline = pauseWhileOffline;
        this.consumeActions = consumeActions;
        this.multipliers = multipliers;
        this.craftingRecipes = craftingRecipes;
        this.furnaceRecipes = furnaceRecipes;
        this.drugBrewingRecipes = drugBrewingRecipes;
        this.drugRelatedGoods = drugRelatedGoods;
        this.growing = growing;
        this.namedisplay = namedisplay;
    }
    
    public String getDisplayName() {
    	return this.namedisplay;
    }
    
    public String getId() {
        return this.id;
    }
    
    public boolean isLegal() {
        return this.legal;
    }
    
    public double getSellPrice() {
        return this.sellPrice;
    }
    
    public double getBuyPrice() {
        return this.buyPrice;
    }
    
    public int getEffectDuration() {
        return this.effectDuration;
    }
    
    public ItemStack getDrugItemStack() {
        return this.drugItemStack;
    }
    
    public ConsumeOption getConsumeOption() {
        return this.consumeOption;
    }
    
    public boolean doRemoveOnConsume() {
        return this.removeOnConsume;
    }
    
    public int getExchangeItemLimit() {
        return this.exchangeItemLimit;
    }
    
    public Collection<ExchangeItem> getConsumeExchangeItems() {
        return this.consumeExchangeItems;
    }
    
    public ConsumeConditions getConsumeConditions() {
        return this.consumeConditions;
    }
    
    public boolean doResetOnConsume() {
        return this.resetOnConsume;
    }
    
    public ChatSlurType getChatSlurType() {
        return this.chatSlurType;
    }
    
    public double getGlobalSoundVolumeModifier() {
        return this.globalSoundVolumeModifier;
    }
    
    public double getGlobalSoundPitchModifier() {
        return this.globalSoundPitchModifier;
    }
    
    public boolean doRemoveOnDisconnect() {
        return this.removeOnDisconnect;
    }
    
    public boolean doRemoveOnDeath() {
        return this.removeOnDeath;
    }
    
    public boolean doPauseWhileOffline() {
        return this.pauseWhileOffline;
    }
    
    public Collection<ConsumeAction> getConsumeActions() {
        return this.consumeActions;
    }
    
    public Map<MultiplierType, Double> getMultipliers() {
        return this.multipliers;
    }
    
    public Collection<DrugCraftingRecipe> getCraftingRecipes() {
        return this.craftingRecipes;
    }
    
    public Collection<DrugFurnaceRecipe> getFurnaceRecipes() {
        return this.furnaceRecipes;
    }
    
    public Collection<DrugBrewingRecipe> getBrewingRecipes() {
        return this.drugBrewingRecipes;
    }
    
    public Collection<DrugRelatedGood> getDrugRelatedGoods() {
        return this.drugRelatedGoods;
    }
    
    public Growing getGrowing() {
        return this.growing;
    }
}
