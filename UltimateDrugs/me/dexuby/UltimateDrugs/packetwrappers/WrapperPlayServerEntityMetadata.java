package me.dexuby.UltimateDrugs.packetwrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import java.util.List;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayServerEntityMetadata extends AbstractPacket {
  public static final PacketType PACKET_TYPE = PacketType.Play.Server.ENTITY_METADATA;
  
  public WrapperPlayServerEntityMetadata() {
    super(new PacketContainer(PACKET_TYPE), PACKET_TYPE);
    this.handle.getModifier().writeDefaults();
  }
  
  public WrapperPlayServerEntityMetadata(PacketContainer paramPacketContainer) {
    super(paramPacketContainer, PACKET_TYPE);
  }
  
  public Entity getEntity(World paramWorld) {
    return (Entity)this.handle.getEntityModifier(paramWorld).read(0);
  }
  
  public Entity getEntity(PacketEvent paramPacketEvent) {
    return getEntity(paramPacketEvent.getPlayer().getWorld());
  }
  
  public int getEntityId() {
    return ((Integer)this.handle.getIntegers().read(0)).intValue();
  }
  
  public WrapperPlayServerEntityMetadata setEntityId(int paramInt) {
    this.handle.getIntegers().write(0, Integer.valueOf(paramInt));
    return this;
  }
  
  public List<WrappedWatchableObject> getMetadata() {
    return (List<WrappedWatchableObject>)this.handle.getWatchableCollectionModifier().read(0);
  }
  
  public WrapperPlayServerEntityMetadata setMetadata(List<WrappedWatchableObject> paramList) {
    this.handle.getWatchableCollectionModifier().write(0, paramList);
    return this;
  }
}
