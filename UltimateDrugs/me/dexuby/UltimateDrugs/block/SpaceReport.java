// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.block;

import org.bukkit.Material;
import me.dexuby.UltimateDrugs.drugs.growing.GrowingBlock;
import java.util.Iterator;
import me.dexuby.UltimateDrugs.utils.MaterialUtils;
import me.dexuby.UltimateDrugs.utils.BlockUtils;
import java.util.HashSet;
import me.dexuby.UltimateDrugs.drugs.growing.Direction;
import java.util.List;
import me.dexuby.UltimateDrugs.drugs.growing.GrowingStage;
import org.bukkit.block.Block;
import java.util.Set;

public class SpaceReport
{
    private final boolean space;
    private final Set<Block> freeBlocks;
    
    public SpaceReport(final boolean space, final Set<Block> freeBlocks) {
        this.space = space;
        this.freeBlocks = freeBlocks;
    }
    
    public boolean hasSpace() {
        return this.space;
    }
    
    public Set<Block> getFreeBlocks() {
        return this.freeBlocks;
    }
    
    public static class Creator
    {
        private GrowingStage growingStage;
        private List<OffsetBlock> blockList;
        private Direction direction;
        private Block baseBlock;
        private boolean ignorePlantBlocks;
        
        public Creator withGrowingStage(final GrowingStage growingStage) {
            this.growingStage = growingStage;
            return this;
        }
        
        public Creator withBlockList(final List<OffsetBlock> blockList) {
            this.blockList = blockList;
            return this;
        }
        
        public Creator withDirection(final Direction direction) {
            this.direction = direction;
            return this;
        }
        
        public Creator withBaseBlock(final Block baseBlock) {
            this.baseBlock = baseBlock;
            return this;
        }
        
        public Creator doIgnorePlantBlocks(final boolean ignorePlantBlocks) {
            this.ignorePlantBlocks = ignorePlantBlocks;
            return this;
        }
        
        public SpaceReport create(final SpaceReportType spaceReportType) {
            return (spaceReportType != SpaceReportType.GROWING) ? this.generateSpaceReport() : this.generateGrowingSpaceReport();
        }
        
        private SpaceReport generateSpaceReport() {
            boolean b = true;
            final HashSet<Block> set = new HashSet<Block>();
            final Iterator<OffsetBlock> iterator = this.blockList.iterator();
            while (iterator.hasNext()) {
                final Block relativeBlock = BlockUtils.getRelativeBlock(this.direction, iterator.next().getBlockOffset(), this.baseBlock);
                if (!MaterialUtils.isAir(relativeBlock.getType())) {
                    b = false;
                }
                else {
                    set.add(relativeBlock);
                }
            }
            return new SpaceReport(b, set);
        }
        
        private SpaceReport generateGrowingSpaceReport() {
            boolean b = true;
            int n = 0;
            final HashSet<Block> set = new HashSet<Block>();
            for (final GrowingBlock growingBlock : this.growingStage.getGrowingBlocks()) {
                if (this.growingStage.getRequiredBlockCount() > 0 && n >= this.growingStage.getRequiredBlockCount()) {
                    break;
                }
                final Block relativeBlock = BlockUtils.getRelativeBlock(this.direction, growingBlock.getBlockOffset(), this.baseBlock);
                final Material type = relativeBlock.getType();
                if (type != Material.AIR && type != Material.CAVE_AIR && type != Material.VOID_AIR && !growingBlock.doIgnoreSpace() && (!this.ignorePlantBlocks || !relativeBlock.hasMetadata("ultimatedrugs-plant"))) {
                    if (this.growingStage.getRequiredBlockCount() == 0) {
                        b = false;
                        break;
                    }
                    continue;
                }
                else {
                    set.add(relativeBlock);
                    ++n;
                }
            }
            if (this.growingStage.getRequiredBlockCount() > 0 && n < this.growingStage.getRequiredBlockCount()) {
                b = false;
            }
            return new SpaceReport(b, set);
        }
    }
}
