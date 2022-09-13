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
        Block block = event.getBlock();
        LandData data;
        ArrayList<Position> checkPositions = new ArrayList<>();
        checkPositions.add(block.add(3, 0, 0));
        checkPositions.add(block.add(-3, 0, 0));
        checkPositions.add(block.add(0, 3, 0));
        checkPositions.add(block.add(0, -3, 0));
        checkPositions.add(block.add(0, 0, 3));
        checkPositions.add(block.add(0, 0, -3));
        checkPositions.addAll(event.getBlocks());
        checkPositions.addAll(event.getDestroyedBlocks());
        for (Position position : checkPositions) {
            data = DataTool.getPlayerLandData(position);
            if (data != null) {
                if (data.getLandOtherSet().isOpen(OtherLandSetting.RED_STONE_OUT)) {
                    for (Player player : DataTool.getAroundPlayers(block, 5)) {
                        player.sendMessage(LandModule.getModule().getConfig().getTitle() + LandModule.getModule().getLanguage().translateString("usingThePistonOutside", data.getLandName()));
                    }
                    event.setCancelled();
                    return;
                }
            }
        }
    }
}
