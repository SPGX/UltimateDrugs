package me.dexuby.UltimateDrugs.packetwrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerUpdateHealth extends AbstractPacket {
  public static final PacketType PACKET_TYPE = PacketType.Play.Server.UPDATE_HEALTH;
  
  public WrapperPlayServerUpdateHealth() {
    super(new PacketContainer(PACKET_TYPE), PACKET_TYPE);
    this.handle.getModifier().writeDefaults();
  }
  
  public WrapperPlayServerUpdateHealth(PacketContainer paramPacketContainer) {
    super(paramPacketContainer, PACKET_TYPE);
  }
  
  public float getHealth() {
    return ((Float)this.handle.getFloat().read(0)).floatValue();
  }
  
  public WrapperPlayServerUpdateHealth setHealth(float paramFloat) {
    this.handle.getFloat().write(0, Float.valueOf(paramFloat));
    return this;
  }
  
  public int getFood() {
    return ((Integer)this.handle.getIntegers().read(0)).intValue();
  }
  
  public WrapperPlayServerUpdateHealth setFood(int paramInt) {
    this.handle.getIntegers().write(0, Integer.valueOf(paramInt));
    return this;
  }
  
  public float getFoodSaturation() {
    return ((Float)this.handle.getFloat().read(1)).floatValue();
  }
  
  public WrapperPlayServerUpdateHealth setFoodSaturation(float paramFloat) {
    this.handle.getFloat().write(1, Float.valueOf(paramFloat));
    return this;
  }
}
