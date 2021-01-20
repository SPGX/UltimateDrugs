package me.dexuby.UltimateDrugs.packetwrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.PacketConstructor;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import java.util.UUID;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class WrapperPlayServerSpawnEntityLiving extends AbstractPacket {
  public static final PacketType PACKET_TYPE = PacketType.Play.Server.SPAWN_ENTITY_LIVING;
  
  private static PacketConstructor entityConstructor;
  
  public WrapperPlayServerSpawnEntityLiving() {
    super(new PacketContainer(PACKET_TYPE), PACKET_TYPE);
    this.handle.getModifier().writeDefaults();
  }
  
  public WrapperPlayServerSpawnEntityLiving(PacketContainer paramPacketContainer) {
    super(paramPacketContainer, PACKET_TYPE);
  }
  
  public WrapperPlayServerSpawnEntityLiving(Entity paramEntity) {
    super(fromEntity(paramEntity), PACKET_TYPE);
  }
  
  private static PacketContainer fromEntity(Entity paramEntity) {
    if (entityConstructor == null)
      entityConstructor = ProtocolLibrary.getProtocolManager().createPacketConstructor(PACKET_TYPE, new Object[] { paramEntity }); 
    return entityConstructor.createPacket(new Object[] { paramEntity });
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
  
  public WrapperPlayServerSpawnEntityLiving setEntityId(int paramInt) {
    this.handle.getIntegers().write(0, Integer.valueOf(paramInt));
    return this;
  }
  
  public UUID getUniqueId() {
    return (UUID)this.handle.getUUIDs().read(0);
  }
  
  public WrapperPlayServerSpawnEntityLiving setUniqueId(UUID paramUUID) {
    this.handle.getUUIDs().write(0, paramUUID);
    return this;
  }
  
  public EntityType getType() {
    return EntityType.fromId(((Integer)this.handle.getIntegers().read(1)).intValue());
  }
  
  public WrapperPlayServerSpawnEntityLiving setType(EntityType paramEntityType) {
    this.handle.getIntegers().write(1, Integer.valueOf(paramEntityType.getTypeId()));
    return this;
  }
  
  public WrapperPlayServerSpawnEntityLiving setType(int paramInt) {
    this.handle.getIntegers().write(1, Integer.valueOf(paramInt));
    return this;
  }
  
  public double getX() {
    return ((Double)this.handle.getDoubles().read(0)).doubleValue();
  }
  
  public WrapperPlayServerSpawnEntityLiving setX(double paramDouble) {
    this.handle.getDoubles().write(0, Double.valueOf(paramDouble));
    return this;
  }
  
  public double getY() {
    return ((Double)this.handle.getDoubles().read(1)).doubleValue();
  }
  
  public WrapperPlayServerSpawnEntityLiving setY(double paramDouble) {
    this.handle.getDoubles().write(1, Double.valueOf(paramDouble));
    return this;
  }
  
  public double getZ() {
    return ((Double)this.handle.getDoubles().read(2)).doubleValue();
  }
  
  public WrapperPlayServerSpawnEntityLiving setZ(double paramDouble) {
    this.handle.getDoubles().write(2, Double.valueOf(paramDouble));
    return this;
  }
  
  public float getYaw() {
    return ((Byte)this.handle.getBytes().read(0)).byteValue() * 360.0F / 256.0F;
  }
  
  public WrapperPlayServerSpawnEntityLiving setYaw(float paramFloat) {
    this.handle.getBytes().write(0, Byte.valueOf((byte)(int)(paramFloat * 256.0F / 360.0F)));
    return this;
  }
  
  public float getPitch() {
    return ((Byte)this.handle.getBytes().read(1)).byteValue() * 360.0F / 256.0F;
  }
  
  public WrapperPlayServerSpawnEntityLiving setPitch(float paramFloat) {
    this.handle.getBytes().write(1, Byte.valueOf((byte)(int)(paramFloat * 256.0F / 360.0F)));
    return this;
  }
  
  public float getHeadPitch() {
    return ((Byte)this.handle.getBytes().read(2)).byteValue() * 360.0F / 256.0F;
  }
  
  public WrapperPlayServerSpawnEntityLiving setHeadPitch(float paramFloat) {
    this.handle.getBytes().write(2, Byte.valueOf((byte)(int)(paramFloat * 256.0F / 360.0F)));
    return this;
  }
  
  public double getVelocityX() {
    return ((Integer)this.handle.getIntegers().read(2)).intValue() / 8000.0D;
  }
  
  public WrapperPlayServerSpawnEntityLiving setVelocityX(double paramDouble) {
    this.handle.getIntegers().write(2, Integer.valueOf((int)(paramDouble * 8000.0D)));
    return this;
  }
  
  public double getVelocityY() {
    return ((Integer)this.handle.getIntegers().read(3)).intValue() / 8000.0D;
  }
  
  public WrapperPlayServerSpawnEntityLiving setVelocityY(double paramDouble) {
    this.handle.getIntegers().write(3, Integer.valueOf((int)(paramDouble * 8000.0D)));
    return this;
  }
  
  public double getVelocityZ() {
    return ((Integer)this.handle.getIntegers().read(4)).intValue() / 8000.0D;
  }
  
  public WrapperPlayServerSpawnEntityLiving setVelocityZ(double paramDouble) {
    this.handle.getIntegers().write(4, Integer.valueOf((int)(paramDouble * 8000.0D)));
    return this;
  }
  
  public WrappedDataWatcher getMetadata() {
    return (WrappedDataWatcher)this.handle.getDataWatcherModifier().read(0);
  }
  
  public WrapperPlayServerSpawnEntityLiving setMetadata(WrappedDataWatcher paramWrappedDataWatcher) {
    this.handle.getDataWatcherModifier().write(0, paramWrappedDataWatcher);
    return this;
  }
}
