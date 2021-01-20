package me.dexuby.UltimateDrugs.utils;

import java.util.List;
import java.util.Random;
import me.dexuby.UltimateDrugs.block.BlockOffset;
import me.dexuby.UltimateDrugs.drugs.growing.CustomBlockFace;
import me.dexuby.UltimateDrugs.drugs.growing.Direction;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class BlockUtils {
  public static BlockFace getRandomRotatableBlockFace() {
    Random random = new Random();
    BlockFace[] arrayOfBlockFace = { 
        BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH_WEST, BlockFace.SOUTH_EAST, BlockFace.NORTH_WEST, BlockFace.NORTH_EAST, BlockFace.EAST_SOUTH_EAST, BlockFace.EAST_NORTH_EAST, 
        BlockFace.SOUTH_SOUTH_EAST, BlockFace.SOUTH_SOUTH_WEST, BlockFace.WEST_SOUTH_WEST, BlockFace.WEST_NORTH_WEST, BlockFace.NORTH_NORTH_WEST, BlockFace.NORTH_NORTH_EAST };
    return arrayOfBlockFace[random.nextInt(arrayOfBlockFace.length)];
  }
  
  public static BlockFace getRandomDirectionalBlockFace() {
    Random random = new Random();
    BlockFace[] arrayOfBlockFace = { BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST };
    return arrayOfBlockFace[random.nextInt(arrayOfBlockFace.length)];
  }
  
  public static Block getRelativeBlock(Direction paramDirection, BlockOffset paramBlockOffset, Block paramBlock) {
    Block block = paramBlock;
    if (paramBlockOffset.getX() != 0)
      if (paramBlockOffset.getX() > 0) {
        block = block.getRelative(getRelativeBlockFace(paramDirection, CustomBlockFace.LEFT), paramBlockOffset.getX());
      } else {
        block = block.getRelative(getRelativeBlockFace(paramDirection, CustomBlockFace.RIGHT), Math.abs(paramBlockOffset.getX()));
      }  
    if (paramBlockOffset.getY() != 0)
      block = block.getRelative(0, paramBlockOffset.getY(), 0); 
    if (paramBlockOffset.getZ() != 0)
      if (paramBlockOffset.getZ() > 0) {
        block = block.getRelative(getRelativeBlockFace(paramDirection, CustomBlockFace.FRONT), paramBlockOffset.getZ());
      } else {
        block = block.getRelative(getRelativeBlockFace(paramDirection, CustomBlockFace.BACK), Math.abs(paramBlockOffset.getZ()));
      }  
    return block;
  }
  
  private static BlockFace getRelativeBlockFace(Direction paramDirection, CustomBlockFace paramCustomBlockFace) {
    if (paramCustomBlockFace == CustomBlockFace.LEFT || paramCustomBlockFace == CustomBlockFace.RIGHT || paramCustomBlockFace == CustomBlockFace.FRONT || paramCustomBlockFace == CustomBlockFace.BACK) {
      switch (paramDirection) {
        case NORTH:
          if (paramCustomBlockFace == CustomBlockFace.LEFT)
            return BlockFace.WEST; 
          if (paramCustomBlockFace == CustomBlockFace.RIGHT)
            return BlockFace.EAST; 
          if (paramCustomBlockFace == CustomBlockFace.FRONT)
            return BlockFace.SOUTH; 
          return BlockFace.NORTH;
        case EAST:
          if (paramCustomBlockFace == CustomBlockFace.LEFT)
            return BlockFace.NORTH; 
          if (paramCustomBlockFace == CustomBlockFace.RIGHT)
            return BlockFace.SOUTH; 
          if (paramCustomBlockFace == CustomBlockFace.FRONT)
            return BlockFace.WEST; 
          return BlockFace.EAST;
        case SOUTH:
          if (paramCustomBlockFace == CustomBlockFace.LEFT)
            return BlockFace.EAST; 
          if (paramCustomBlockFace == CustomBlockFace.RIGHT)
            return BlockFace.WEST; 
          if (paramCustomBlockFace == CustomBlockFace.FRONT)
            return BlockFace.NORTH; 
          return BlockFace.SOUTH;
        case WEST:
          if (paramCustomBlockFace == CustomBlockFace.LEFT)
            return BlockFace.SOUTH; 
          if (paramCustomBlockFace == CustomBlockFace.RIGHT)
            return BlockFace.NORTH; 
          if (paramCustomBlockFace == CustomBlockFace.FRONT)
            return BlockFace.EAST; 
          return BlockFace.WEST;
      } 
    } else {
      return BlockFace.valueOf(paramCustomBlockFace.toString());
    } 
    return null;
  }
  
  public static BlockFace getTargetBlockFaceOld(Player paramPlayer, boolean paramBoolean) {
    List<Block> list = paramPlayer.getLastTwoTargetBlocks(null, 100);
    if (list.size() != 2 || (paramBoolean && !((Block)list.get(1)).getType().isSolid()))
      return null; 
    Block block1 = list.get(1);
    Block block2 = list.get(0);
    BlockFace blockFace = block1.getFace(block2);
    if (!paramBoolean && !((Block)list.get(1)).getType().isSolid())
      blockFace = BlockFace.SELF; 
    return blockFace;
  }
  
  public static BlockFace getTargetBlockFace(Player paramPlayer, boolean paramBoolean) {
    List<Block> list = paramPlayer.getLastTwoTargetBlocks(null, 100);
    if (list.size() != 2)
      return null; 
    BlockFace blockFace = ((Block)list.get(1)).getFace(list.get(0));
    if (paramBoolean && !((Block)list.get(1)).getType().isSolid())
      blockFace = BlockFace.SELF; 
    return blockFace;
  }
}
