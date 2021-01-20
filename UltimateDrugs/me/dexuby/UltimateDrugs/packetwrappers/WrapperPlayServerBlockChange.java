package me.dexuby.UltimateDrugs.packetwrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import org.bukkit.Location;
import org.bukkit.World;

public class WrapperPlayServerBlockChange extends AbstractPacket {
  public static final PacketType PACKET_TYPE = PacketType.Play.Server.BLOCK_CHANGE;
  
  public WrapperPlayServerBlockChange() {
    super(new PacketContainer(PACKET_TYPE), PACKET_TYPE);
    this.handle.getModifier().writeDefaults();
  }
  
  public WrapperPlayServerBlockChange(PacketContainer paramPacketContainer) {
    super(paramPacketContainer, PACKET_TYPE);
  }
  
  public BlockPosition getLocation() {
    return (BlockPosition)this.handle.getBlockPositionModifier().read(0);
  }
  
  public WrapperPlayServerBlockChange setLocation(BlockPosition paramBlockPosition) {
    this.handle.getBlockPositionModifier().write(0, paramBlockPosition);
    return this;
  }
  
  public Location getBukkitLocation(World paramWorld) {
    return getLocation().toVector().toLocation(paramWorld);
  }
  
  public WrappedBlockData getBlockData() {
    return (WrappedBlockData)this.handle.getBlockData().read(0);
  }
  
  public WrapperPlayServerBlockChange setBlockData(WrappedBlockData paramWrappedBlockData) {
    this.handle.getBlockData().write(0, paramWrappedBlockData);
    return this;
  }
}
