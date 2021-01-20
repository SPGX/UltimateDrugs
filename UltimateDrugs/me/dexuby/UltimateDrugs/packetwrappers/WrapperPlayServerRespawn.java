package me.dexuby.UltimateDrugs.packetwrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.google.common.hash.Hashing;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import org.bukkit.World;
import org.bukkit.WorldType;

public class WrapperPlayServerRespawn extends AbstractPacket {
  public static final PacketType PACKET_TYPE = PacketType.Play.Server.RESPAWN;
  
  public WrapperPlayServerRespawn() {
    super(new PacketContainer(PACKET_TYPE), PACKET_TYPE);
    this.handle.getModifier().writeDefaults();
  }
  
  public WrapperPlayServerRespawn(PacketContainer paramPacketContainer) {
    super(paramPacketContainer, PACKET_TYPE);
  }
  
  public int getDimension() {
    return ((Integer)this.handle.getDimensions().read(0)).intValue();
  }
  
  public WrapperPlayServerRespawn setDimension(int paramInt) {
    this.handle.getDimensions().write(0, Integer.valueOf(paramInt));
    return this;
  }
  
  public EnumWrappers.Difficulty getDifficulty() {
    return (EnumWrappers.Difficulty)this.handle.getDifficulties().read(0);
  }
  
  public WrapperPlayServerRespawn setDifficulty(EnumWrappers.Difficulty paramDifficulty) {
    this.handle.getDifficulties().write(0, paramDifficulty);
    return this;
  }
  
  public EnumWrappers.NativeGameMode getGameMode() {
    return (EnumWrappers.NativeGameMode)this.handle.getGameModes().read(0);
  }
  
  public WrapperPlayServerRespawn setGameMode(EnumWrappers.NativeGameMode paramNativeGameMode) {
    this.handle.getGameModes().write(0, paramNativeGameMode);
    return this;
  }
  
  public WorldType getWorldType() {
    return (WorldType)this.handle.getWorldTypeModifier().read(0);
  }
  
  public WrapperPlayServerRespawn setWorldType(WorldType paramWorldType) {
    this.handle.getWorldTypeModifier().write(0, paramWorldType);
    return this;
  }
  
  public WrapperPlayServerRespawn updateWorldSettings(World paramWorld) {
    ReflectionUtils.prepareRespawnPacketContainer(this.handle, paramWorld);
    return this;
  }
  
  public WrapperPlayServerRespawn setPreviousGameMode(EnumWrappers.NativeGameMode paramNativeGameMode) {
    this.handle.getGameModes().write(1, paramNativeGameMode);
    return this;
  }
  
  public WrapperPlayServerRespawn setSeed(long paramLong) {
    this.handle.getLongs().write(0, Long.valueOf(Hashing.sha256().hashLong(paramLong).asLong()));
    return this;
  }
  
  public WrapperPlayServerRespawn setIsDebug(boolean paramBoolean) {
    this.handle.getBooleans().write(0, Boolean.valueOf(paramBoolean));
    return this;
  }
  
  public WrapperPlayServerRespawn setIsFlat(boolean paramBoolean) {
    this.handle.getBooleans().write(1, Boolean.valueOf(paramBoolean));
    return this;
  }
}
