package me.dexuby.UltimateDrugs.drugs;

import org.bukkit.inventory.ItemStack;

public class Drop {
  private ItemStack itemStack;
  
  private int minAmount;
  
  private int maxAmount;
  
  private double dropChance;
  
  private boolean breakSuccessChain;
  
  public Drop(ItemStack paramItemStack, int paramInt1, int paramInt2, double paramDouble, boolean paramBoolean) {
    this.itemStack = paramItemStack;
    this.minAmount = paramInt1;
    this.maxAmount = paramInt2;
    this.dropChance = paramDouble;
    this.breakSuccessChain = paramBoolean;
  }
  
  public ItemStack getItemStack() {
    return this.itemStack;
  }
  
  public int getMinAmount() {
    return this.minAmount;
  }
  
  public int getMaxAmount() {
    return this.maxAmount;
  }
  
  public double getDropChance() {
    return this.dropChance;
  }
  
  public boolean doBreakSuccessChain() {
    return this.breakSuccessChain;
  }
}
