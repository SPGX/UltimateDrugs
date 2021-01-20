// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.structures;

import java.util.Iterator;
import org.bukkit.block.BlockFace;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.BlockPosition;
import me.dexuby.UltimateDrugs.packetwrappers.WrapperPlayServerBlockChange;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import me.dexuby.UltimateDrugs.utils.EntityUtils;
import me.dexuby.UltimateDrugs.block.SpaceReportType;
import me.dexuby.UltimateDrugs.block.SpaceReport;
import me.dexuby.UltimateDrugs.utils.BlockUtils;
import me.dexuby.UltimateDrugs.utils.MaterialUtils;
import java.util.Set;
import java.util.ArrayList;
import org.bukkit.block.Block;
import java.util.List;
import me.dexuby.UltimateDrugs.drugs.growing.Direction;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StructurePreviewRunnable extends BukkitRunnable
{
    private final Player player;
    private final Direction direction;
    private final StructureType structureType;
    private final List<Block> blocksToUpdate;
    
    public StructurePreviewRunnable(final Player player, final Direction direction, final StructureType structureType) {
        this.blocksToUpdate = new ArrayList<Block>();
        this.player = player;
        this.direction = direction;
        this.structureType = structureType;
    }
    
    public void run() {
        if (!this.player.isValid() || !this.player.getInventory().getItemInMainHand().isSimilar(this.structureType.getSpawnItem())) {
            this.cancelSafely();
        }
        final Block targetBlock = this.player.getTargetBlock((Set)null, 5);
        if (!MaterialUtils.isAir(targetBlock.getType())) {
            final BlockFace targetBlockFace = BlockUtils.getTargetBlockFace(this.player, true);
            if (targetBlockFace != null) {
                final Block relative = targetBlock.getRelative(targetBlockFace);
                final SpaceReport create = new SpaceReport.Creator().withBlockList(this.structureType.getBlockList()).withDirection(this.direction).withBaseBlock(relative).create(SpaceReportType.DEFAULT);
                this.updateBlocks(create.getFreeBlocks());
                boolean hasSpace = create.hasSpace();
                if (this.structureType.getFoundationBlockOffset() != null && !BlockUtils.getRelativeBlock(EntityUtils.getDirection((Entity)this.player), this.structureType.getFoundationBlockOffset(), relative).getType().isSolid()) {
                    hasSpace = false;
                }
                final Material material = hasSpace ? Material.LIME_STAINED_GLASS : Material.RED_STAINED_GLASS;
                for (final Block block : create.getFreeBlocks()) {
                    new WrapperPlayServerBlockChange().setLocation(new BlockPosition(block.getX(), block.getY(), block.getZ())).setBlockData(WrappedBlockData.createData(material)).sendPacket(this.player);
                    this.blocksToUpdate.add(block);
                }
                return;
            }
        }
        this.updateBlocks(null);
    }
    
    public void cancelSafely() {
        this.updateBlocks(null);
        this.cancel();
    }
    
    private void updateBlocks(final Set<Block> set) {
        this.blocksToUpdate.forEach(block -> {
            if (set == null || !set.contains(block)) {
                block.getState().update();
            }
            return;
        });
        this.blocksToUpdate.clear();
    }
}
