/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.scheduler.BukkitTask
 */
package me.dexuby.UltimateDrugs.drugs;

import me.dexuby.UltimateDrugs.drugs.Drug;
import org.bukkit.scheduler.BukkitTask;

public class ActiveDrugEffect {
    private Drug drug;
    private BukkitTask task;
    private long expirationTimestamp;

    public ActiveDrugEffect(Drug drug, BukkitTask bukkitTask, long l) {
        this.drug = drug;
        this.task = bukkitTask;
        this.expirationTimestamp = l;
    }

    public Drug getDrug() {
        return this.drug;
    }

    public BukkitTask getTask() {
        return this.task;
    }

    public long getExpirationTimestamp() {
        return this.expirationTimestamp;
    }
}

