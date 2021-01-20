package me.dexuby.UltimateDrugs.packetwrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerEntityDestroy extends AbstractPacket {
  public static final PacketType PACKET_TYPE = PacketType.Play.Server.ENTITY_DESTROY;
  
  public WrapperPlayServerEntityDestroy() {
    super(new PacketContainer(PACKET_TYPE), PACKET_TYPE);
    this.handle.getModifier().writeDefaults();
  }
  
  public WrapperPlayServerEntityDestroy(PacketContainer paramPacketContainer) {
    super(paramPacketContainer, PACKET_TYPE);
  }
  
  public int getCount() {
    return ((int[])this.handle.getIntegerArrays().read(0)).length;
  }
  
  public int[] getEntityIds() {
    return (int[])this.handle.getIntegerArrays().read(0);
  }
  
  public WrapperPlayServerEntityDestroy setEntityIds(int[] paramArrayOfint) {
    this.handle.getIntegerArrays().write(0, paramArrayOfint);
    return this;
  }
}
