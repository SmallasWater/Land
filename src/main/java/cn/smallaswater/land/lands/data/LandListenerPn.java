package cn.smallaswater.land.lands.data;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.level.Position;
import cn.nukkit.utils.TextFormat;
import cn.smallaswater.land.lands.settings.OtherLandSetting;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.utils.DataTool;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author SmallasWater
 * Create on 2021/8/4 10:49
 * Package cn.smallaswater.land.lands.data
 */
public class LandListenerPn implements Listener {



/*  @EventHandler
    public void onBlockPiston(BlockPistonEvent event){
        Block block = event.getBlock();
        LandData data;
        ArrayList<Position> positions = new ArrayList<>();
        positions.add(block.add(3));
        positions.add(block.add(-3));
        positions.add(block.add(0, 0, 3));
        positions.add(block.add(0, 0, -3));
        positions.addAll(event.getBlocks());
        positions.addAll(event.getDestroyedBlocks());
        LinkedList<Player> players;
        for (Position position : positions) {
            data = DataTool.getPlayerLandData(position);
            if (data != null) {
                if (data.getLandOtherSet().isOpen(OtherLandSetting.RED_STONE_OUT)) {
                    players = DataTool.getAroundPlayers(block, 5);
                    for (Player player : players) {
                        player.sendMessage(TextFormat.colorize('&', LandModule.getModule().getConfig().getTitle() + " &a请不要在领地 &e"+data.getLandName()+" &a外使用活塞"));
                    }
                    event.setCancelled();
                    return;
                }
            }
        }
    }*/
}
