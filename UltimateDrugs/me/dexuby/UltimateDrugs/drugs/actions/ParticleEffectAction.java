// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.actions;

import org.bukkit.Location;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.command.CommandSender;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import me.dexuby.UltimateDrugs.drugs.Drug;
import java.util.UUID;
import org.bukkit.Particle;

public class ParticleEffectAction extends ConsumeAction
{
    private Particle particle;
    private int count;
    private double directionMultiplier;
    private double offsetX;
    private double offsetY;
    private double offsetZ;
    private double randomOffsetX;
    private double randomOffsetY;
    private double randomOffsetZ;
    private double extra;
    private boolean clientSided;
    
    public ParticleEffectAction(final int n, final double n2, final int n3, final int n4, final String s, final Particle particle, final int count, final double directionMultiplier, final double offsetX, final double offsetY, final double offsetZ, final double randomOffsetX, final double randomOffsetY, final double randomOffsetZ, final double extra, final boolean clientSided) {
        super(n, n2, n3, n4, s);
        this.particle = particle;
        this.count = count;
        this.directionMultiplier = directionMultiplier;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.randomOffsetX = randomOffsetX;
        this.randomOffsetY = randomOffsetY;
        this.randomOffsetZ = randomOffsetZ;
        this.extra = extra;
        this.clientSided = clientSided;
    }
    
    @Override
    public void execute(final UUID uuid, final Drug drug) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null || !player.isValid() || !player.isOnline()) {
            return;
        }
        if (this.getTargetSelector() != null) {
            final Iterator<Entity> iterator = ReflectionUtils.getEntitiesFromSelector((CommandSender)player, this.getTargetSelector()).iterator();
            while (iterator.hasNext()) {
                this.apply(iterator.next());
            }
        }
        else {
            this.apply((Entity)player);
        }
    }
    
    private void apply(final Entity entity) {
        final Location location = entity.getLocation();
        location.add(this.offsetX, this.offsetY, this.offsetZ);
        location.add(entity.getLocation().getDirection().setY(0).normalize().multiply(this.directionMultiplier));
        if (this.clientSided) {
            if (entity instanceof Player) {
                ((Player)entity).spawnParticle(this.particle, location, this.count, this.randomOffsetX, this.randomOffsetY, this.randomOffsetZ, this.extra);
            }
        }
        else {
            entity.getWorld().spawnParticle(this.particle, location, this.count, this.randomOffsetX, this.randomOffsetY, this.randomOffsetZ, this.extra);
        }
    }
    
    public void execute(final Location location) {
        if (location.getWorld() != null) {
            location.add(this.offsetX, this.offsetY, this.offsetZ);
            location.getWorld().spawnParticle(this.particle, location, this.count, this.randomOffsetX, this.randomOffsetY, this.randomOffsetZ, this.extra);
        }
    }
    
    public Particle getParticle() {
        return this.particle;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public double getDirectionMultiplier() {
        return this.directionMultiplier;
    }
    
    public double getOffsetX() {
        return this.offsetX;
    }
    
    public double getOffsetY() {
        return this.offsetY;
    }
    
    public double getOffsetZ() {
        return this.offsetZ;
    }
    
    public double getRandomOffsetX() {
        return this.randomOffsetX;
    }
    
    public double getRandomOffsetY() {
        return this.randomOffsetY;
    }
    
    public double getRandomOffsetZ() {
        return this.randomOffsetZ;
    }
    
    public double getExtra() {
        return this.extra;
    }
    
    public boolean isClientSided() {
        return this.clientSided;
    }
    
    @Override
    public ParticleEffectAction clone() {
        return new ParticleEffectAction(this.getDelay(), this.getChance(), this.getRepetitions(), this.getRepetitionTickDelay(), this.getTargetSelector(), this.particle, this.count, this.directionMultiplier, this.offsetX, this.offsetY, this.offsetZ, this.randomOffsetX, this.randomOffsetY, this.randomOffsetZ, this.extra, this.clientSided);
    }
}
