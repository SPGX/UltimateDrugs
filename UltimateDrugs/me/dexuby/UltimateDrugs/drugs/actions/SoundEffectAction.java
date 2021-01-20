// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.drugs.actions;

import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.command.CommandSender;
import me.dexuby.UltimateDrugs.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import me.dexuby.UltimateDrugs.drugs.Drug;
import java.util.UUID;
import org.bukkit.Sound;

public class SoundEffectAction extends ConsumeAction
{
    private Sound sound;
    private String customSound;
    private float volume;
    private float pitch;
    private boolean clientSided;
    
    public SoundEffectAction(final int n, final double n2, final int n3, final int n4, final String s, final Sound sound, final float volume, final float pitch, final boolean clientSided) {
        super(n, n2, n3, n4, s);
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
        this.clientSided = clientSided;
    }
    
    public SoundEffectAction(final int n, final double n2, final int n3, final int n4, final String s, final String customSound, final float volume, final float pitch, final boolean clientSided) {
        super(n, n2, n3, n4, s);
        this.customSound = customSound;
        this.volume = volume;
        this.pitch = pitch;
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
        if (this.clientSided) {
            if (!(entity instanceof Player)) {
                return;
            }
            final Player player = (Player)entity;
            if (this.sound != null) {
                player.playSound(entity.getLocation(), this.sound, this.volume, this.pitch);
            }
            else {
                player.playSound(entity.getLocation(), this.customSound, this.volume, this.pitch);
            }
        }
        else if (this.sound != null) {
            entity.getWorld().playSound(entity.getLocation(), this.sound, this.volume, this.pitch);
        }
        else {
            entity.getWorld().playSound(entity.getLocation(), this.customSound, this.volume, this.pitch);
        }
    }
    
    public Sound getSound() {
        return this.sound;
    }
    
    public float getVolume() {
        return this.volume;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public boolean isClientSided() {
        return this.clientSided;
    }
    
    @Override
    public SoundEffectAction clone() {
        if (this.sound != null) {
            return new SoundEffectAction(this.getDelay(), this.getChance(), this.getRepetitions(), this.getRepetitionTickDelay(), this.getTargetSelector(), this.sound, this.volume, this.pitch, this.clientSided);
        }
        return new SoundEffectAction(this.getDelay(), this.getChance(), this.getRepetitions(), this.getRepetitionTickDelay(), this.getTargetSelector(), this.customSound, this.volume, this.pitch, this.clientSided);
    }
}
