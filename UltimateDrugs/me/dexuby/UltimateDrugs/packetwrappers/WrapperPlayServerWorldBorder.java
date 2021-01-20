package me.dexuby.UltimateDrugs.packetwrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;

public class WrapperPlayServerWorldBorder extends AbstractPacket {
  public static final PacketType PACKET_TYPE = PacketType.Play.Server.WORLD_BORDER;
  
  public WrapperPlayServerWorldBorder() {
    super(new PacketContainer(PACKET_TYPE), PACKET_TYPE);
    this.handle.getModifier().writeDefaults();
  }
  
  public WrapperPlayServerWorldBorder(PacketContainer paramPacketContainer) {
    super(paramPacketContainer, PACKET_TYPE);
  }
  
  public EnumWrappers.WorldBorderAction getAction() {
    return (EnumWrappers.WorldBorderAction)this.handle.getWorldBorderActions().read(0);
  }
  
  public WrapperPlayServerWorldBorder setAction(EnumWrappers.WorldBorderAction paramWorldBorderAction) {
    this.handle.getWorldBorderActions().write(0, paramWorldBorderAction);
    return this;
  }
  
  public int getPortalTeleportBoundary() {
    return ((Integer)this.handle.getIntegers().read(0)).intValue();
  }
  
  public WrapperPlayServerWorldBorder setPortalTeleportBoundary(int paramInt) {
    this.handle.getIntegers().write(0, Integer.valueOf(paramInt));
    return this;
  }
  
  public double getCenterX() {
    return ((Double)this.handle.getDoubles().read(0)).doubleValue();
  }
  
  public WrapperPlayServerWorldBorder setCenterX(double paramDouble) {
    this.handle.getDoubles().write(0, Double.valueOf(paramDouble));
    return this;
  }
  
  public double getCenterZ() {
    return ((Double)this.handle.getDoubles().read(1)).doubleValue();
  }
  
  public WrapperPlayServerWorldBorder setCenterZ(double paramDouble) {
    this.handle.getDoubles().write(1, Double.valueOf(paramDouble));
    return this;
  }
  
  public double getOldRadius() {
    return ((Double)this.handle.getDoubles().read(2)).doubleValue();
  }
  
  public WrapperPlayServerWorldBorder setOldRadius(double paramDouble) {
    this.handle.getDoubles().write(2, Double.valueOf(paramDouble));
    return this;
  }
  
  public double getRadius() {
    return ((Double)this.handle.getDoubles().read(3)).doubleValue();
  }
  
  public WrapperPlayServerWorldBorder setRadius(double paramDouble) {
    this.handle.getDoubles().write(3, Double.valueOf(paramDouble));
    return this;
  }
  
  public long getSpeed() {
    return ((Long)this.handle.getLongs().read(0)).longValue();
  }
  
  public WrapperPlayServerWorldBorder setSpeed(long paramLong) {
    this.handle.getLongs().write(0, Long.valueOf(paramLong));
    return this;
  }
  
  public int getWarningTime() {
    return ((Integer)this.handle.getIntegers().read(1)).intValue();
  }
  
  public WrapperPlayServerWorldBorder setWarningTime(int paramInt) {
    this.handle.getIntegers().write(1, Integer.valueOf(paramInt));
    return this;
  }
  
  public int getWarningDistance() {
    return ((Integer)this.handle.getIntegers().read(2)).intValue();
  }
  
  public WrapperPlayServerWorldBorder setWarningDistance(int paramInt) {
    this.handle.getIntegers().write(2, Integer.valueOf(paramInt));
    return this;
  }
}
