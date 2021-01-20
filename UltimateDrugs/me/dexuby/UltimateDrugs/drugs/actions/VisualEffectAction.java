// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.actions;

import org.bukkit.World;
import org.bukkit.Location;
import me.dexuby.UltimateDrugs.packetwrappers.WrapperPlayServerWorldBorder;
import org.bukkit.WorldType;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.dexuby.UltimateDrugs.packetwrappers.WrapperPlayServerRespawn;
import me.dexuby.UltimateDrugs.packetwrappers.WrapperPlayServerEntityDestroy;
import me.dexuby.UltimateDrugs.packetwrappers.WrapperPlayServerUpdateHealth;
import me.dexuby.UltimateDrugs.packetwrappers.WrapperPlayServerCamera;
import org.bukkit.GameMode;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import java.util.List;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.dexuby.UltimateDrugs.packetwrappers.WrapperPlayServerEntityMetadata;
import me.dexuby.UltimateDrugs.packetwrappers.WrapperPlayServerSpawnEntityLiving;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.command.CommandSender;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import me.dexuby.UltimateDrugs.drugs.Drug;
import java.util.UUID;
import org.bukkit.Bukkit;
import me.dexuby.UltimateDrugs.Main;

public class VisualEffectAction extends TimedConsumeAction
{
    private VisualEffectType type;
    private Main main;
    
    public VisualEffectAction(final int n, final double n2, final int n3, final int n4, final String s, final int n5, final VisualEffectType type) {
        super(n, n2, n3, n4, s, n5);
        this.type = type;
        this.main = (Main)Bukkit.getPluginManager().getPlugin("UltimateDrugs");
    }
    
    @Override
    public void execute(final UUID uuid, final Drug drug) {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isValid() || !player.isOnline()) {
                return;
            }
            if (this.getTargetSelector() != null) {
                for (final Entity entity : ReflectionUtils.getEntitiesFromSelector((CommandSender)player, this.getTargetSelector())) {
                    if (entity instanceof Player) {
                        this.apply((Player)entity);
                    }
                }
            }
            else {
                this.apply(player);
            }
        }
    }
    
    private void apply(final Player player) {
        final int versionId = ReflectionUtils.getVersionId();
        if (player == null || !player.isValid() || !player.isOnline()) {
            return;
        }
        switch (this.type) {
            case CREEPER_VISION:
            case SPIDER_VISION:
            case ENDERMAN_VISION: {
                final Location location = player.getLocation();
                final World world = player.getWorld();
                final GameMode gameMode = player.getGameMode();
                final boolean sprinting = player.isSprinting();
                int type = 0;
                switch (this.type) {
                    case CREEPER_VISION: {
                        if (versionId >= 250) {
                            type = 12;
                            break;
                        }
                        if (versionId >= 240) {
                            type = 11;
                            break;
                        }
                        type = 10;
                        break;
                    }
                    case SPIDER_VISION: {
                        if (versionId >= 262) {
                            type = 80;
                            break;
                        }
                        if (versionId >= 260) {
                            type = 79;
                            break;
                        }
                        if (versionId >= 250) {
                            type = 73;
                            break;
                        }
                        if (versionId >= 240) {
                            type = 72;
                            break;
                        }
                        type = 69;
                        break;
                    }
                    default: {
                        if (versionId >= 250) {
                            type = 20;
                            break;
                        }
                        if (versionId >= 240) {
                            type = 19;
                            break;
                        }
                        type = 18;
                        break;
                    }
                }
                new WrapperPlayServerSpawnEntityLiving().setEntityId(-69).setUniqueId(UUID.randomUUID()).setType(type).setX(location.getX()).setY(location.getY()).setZ(location.getZ()).sendPacket(player);
                final WrapperPlayServerEntityMetadata setEntityId = new WrapperPlayServerEntityMetadata().setEntityId(-69);
                final WrappedDataWatcher wrappedDataWatcher = new WrappedDataWatcher((List)setEntityId.getMetadata());
                wrappedDataWatcher.setObject(0, WrappedDataWatcher.Registry.get((Class)Byte.class), (Object)32);
                setEntityId.setMetadata(wrappedDataWatcher.getWatchableObjects()).sendPacket(player);
                player.setGameMode(GameMode.SPECTATOR);
                new WrapperPlayServerCamera().setCameraId(-69).sendPacket(player);
                new WrapperPlayServerUpdateHealth().setHealth(-10.0f).sendPacket(player);
                player.setGameMode(gameMode);
                new WrapperPlayServerEntityDestroy().setEntityIds(new int[] { -69 }).sendPacket(player);
                if (versionId >= 260) {
                    new WrapperPlayServerRespawn().updateWorldSettings(world).setSeed(world.getSeed()).setGameMode(EnumWrappers.NativeGameMode.valueOf(gameMode.name())).setPreviousGameMode(EnumWrappers.NativeGameMode.valueOf(gameMode.name())).setIsFlat(world.getWorldType() == WorldType.FLAT).sendPacket(player);
                }
                else {
                    new WrapperPlayServerRespawn().setDimension(world.getEnvironment().getId()).setGameMode(EnumWrappers.NativeGameMode.valueOf(gameMode.name())).setWorldType(world.getWorldType()).sendPacket(player);
                }
                player.teleport(location);
                player.updateInventory();
                player.setSprinting(sprinting);
                break;
            }
            case RED_BORDER: {
                new WrapperPlayServerWorldBorder().setAction(EnumWrappers.WorldBorderAction.SET_SIZE).setRadius(250000.0).setOldRadius(250000.0).sendPacket(player);
                new WrapperPlayServerWorldBorder().setAction(EnumWrappers.WorldBorderAction.SET_CENTER).setCenterX(player.getLocation().getX()).setCenterZ(player.getLocation().getZ()).sendPacket(player);
                new WrapperPlayServerWorldBorder().setAction(EnumWrappers.WorldBorderAction.SET_WARNING_BLOCKS).setWarningDistance(Integer.MAX_VALUE).sendPacket(player);
                break;
            }
        }
    }
    
    @Override
    public void end(final Player player) {
        switch (this.type) {
            case CREEPER_VISION:
            case SPIDER_VISION:
            case ENDERMAN_VISION: {
                new WrapperPlayServerCamera().setCameraId(player.getEntityId()).sendPacket(player);
                break;
            }
            case RED_BORDER: {
                new WrapperPlayServerWorldBorder().setAction(EnumWrappers.WorldBorderAction.SET_WARNING_BLOCKS).setWarningDistance(0).sendPacket(player);
                break;
            }
        }
    }
    
    public VisualEffectType getType() {
        return this.type;
    }
    
    @Override
    public VisualEffectAction clone() {
        return new VisualEffectAction(this.getDelay(), this.getChance(), this.getRepetitions(), this.getRepetitionTickDelay(), this.getTargetSelector(), this.getDuration(), this.type);
    }
}
