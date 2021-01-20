// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs;

import java.util.ArrayList;
import java.util.List;

public class ConsumeConditions
{
    private boolean sneakRequired;
    private double maxCurrentHealth;
    private double maxCurrentFoodLevel;
    private List<String> disabledWorldList;
    private List<String> disabledRegionList;
    private List<ConsumeRequirementItem> requiredItemList;
    
    public ConsumeConditions() {
        this.sneakRequired = false;
        this.maxCurrentHealth = 0.0;
        this.maxCurrentFoodLevel = 0.0;
        this.disabledWorldList = new ArrayList<String>();
        this.disabledRegionList = new ArrayList<String>();
        this.requiredItemList = new ArrayList<ConsumeRequirementItem>();
    }
    
    public ConsumeConditions(final boolean sneakRequired, final double maxCurrentHealth, final double maxCurrentFoodLevel, final List<String> disabledWorldList, final List<String> disabledRegionList, final List<ConsumeRequirementItem> requiredItemList) {
        this.sneakRequired = sneakRequired;
        this.maxCurrentHealth = maxCurrentHealth;
        this.maxCurrentFoodLevel = maxCurrentFoodLevel;
        this.disabledWorldList = disabledWorldList;
        this.disabledRegionList = disabledRegionList;
        this.requiredItemList = requiredItemList;
    }
    
    public boolean isSneakRequired() {
        return this.sneakRequired;
    }
    
    public double getMaxCurrentHealth() {
        return this.maxCurrentHealth;
    }
    
    public double getMaxCurrentFoodLevel() {
        return this.maxCurrentFoodLevel;
    }
    
    public List<String> getDisabledWorldList() {
        return this.disabledWorldList;
    }
    
    public List<String> getDisabledRegionList() {
        return this.disabledRegionList;
    }
    
    public List<ConsumeRequirementItem> getRequiredItemList() {
        return this.requiredItemList;
    }
}
