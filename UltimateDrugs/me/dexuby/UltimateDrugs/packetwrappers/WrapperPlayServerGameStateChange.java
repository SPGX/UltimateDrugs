package me.dexuby.UltimateDrugs.packetwrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerGameStateChange extends AbstractPacket {
  public static final PacketType PACKET_TYPE = PacketType.Play.Server.GAME_STATE_CHANGE;
  
  public WrapperPlayServerGameStateChange() {
    super(new PacketContainer(PACKET_TYPE), PACKET_TYPE);
    this.handle.getModifier().writeDefaults();
  }
  
  public WrapperPlayServerGameStateChange(PacketContainer paramPacketContainer) {
    super(paramPacketContainer, PACKET_TYPE);
  }
  
  public int getReason() {
    return ((Integer)this.handle.getIntegers().read(0)).intValue();
  }
  
  public WrapperPlayServerGameStateChange setReason(int paramInt) {
    this.handle.getIntegers().write(0, Integer.valueOf(paramInt));
    return this;
  }
  
  public float getValue() {
    return ((Float)this.handle.getFloat().read(0)).floatValue();
  }
  
  public WrapperPlayServerGameStateChange setValue(float paramFloat) {
    this.handle.getFloat().write(0, Float.valueOf(paramFloat));
    return this;
  }
}
