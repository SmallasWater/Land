package cn.smallaswater.land.lands.data;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPistonEvent;
import cn.nukkit.level.Position;
import cn.smallaswater.land.lands.settings.OtherLandSetting;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.utils.DataTool;

import java.util.ArrayList;

/**
 * @author SmallasWater
 * Create on 2021/8/4 10:49
 * Package cn.smallaswater.land.lands.data
 */
public class LandListenerPn implements Listener {


    @EventHandler
    public void onBlockPiston(BlockPistonEvent event) {
        Block blockPistonBase = event.getBlock();
        ArrayList<Position> checkPositions = new ArrayList<>();
        Block checkBlock = blockPistonBase;
        int maxDistance = event.isExtending() ? 12 : 2;
        switch (event.getDirection()) {
            case UP:
                do {
                    checkBlock = checkBlock.up();
                    checkPositions.add(checkBlock);
                } while (checkBlock.getId() != Block.AIR && Math.abs(blockPistonBase.y - checkBlock.y) < maxDistance);
                break;
            case DOWN:
                do {
                    checkBlock = checkBlock.down();
                    checkPositions.add(checkBlock);
                } while (checkBlock.getId() != Block.AIR && Math.abs(blockPistonBase.y - checkBlock.y) < maxDistance);
                break;
            case NORTH:
                do {
                    checkBlock = checkBlock.north();
                    checkPositions.add(checkBlock);
                } while (checkBlock.getId() != Block.AIR && Math.abs(blockPistonBase.z - checkBlock.z) < maxDistance);
                break;
            case SOUTH:
                do {
                    checkBlock = checkBlock.south();
                    checkPositions.add(checkBlock);
                } while (checkBlock.getId() != Block.AIR && Math.abs(blockPistonBase.z - checkBlock.z) < maxDistance);
                break;
            case WEST:
                do {
                    checkBlock = checkBlock.west();
                    checkPositions.add(checkBlock);
                } while (checkBlock.getId() != Block.AIR && Math.abs(blockPistonBase.x - checkBlock.x) < maxDistance);
                break;
            case EAST:
                do {
                    checkBlock = checkBlock.east();
                    checkPositions.add(checkBlock);
                } while (checkBlock.getId() != Block.AIR && Math.abs(blockPistonBase.x - checkBlock.x) < maxDistance);
                break;
        }
        checkPositions.addAll(event.getBlocks());
        checkPositions.addAll(event.getDestroyedBlocks());
        for (Position position : checkPositions) {
            LandData data = DataTool.getPlayerLandData(position);
            if (data != null) {
                if (data.getLandOtherSet().isOpen(OtherLandSetting.RED_STONE_OUT)) {
                    for (Player player : DataTool.getAroundPlayers(blockPistonBase, 5)) {
                        player.sendMessage(LandModule.getModule().getConfig().getTitle() + LandModule.getModule().getLanguage().translateString("usingThePistonOutside", data.getLandName()));
                    }
                    event.setCancelled();
                    return;
                }
            }
        }
    }
}
