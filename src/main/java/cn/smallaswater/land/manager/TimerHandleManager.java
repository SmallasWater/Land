package cn.smallaswater.land.manager;

import cn.nukkit.Player;
import cn.smallaswater.land.handle.TimeHandle;
import cn.smallaswater.land.module.LandModule;


import java.util.LinkedHashMap;

/**
 * @author SmallasWater
 * Create on 2021/6/2 13:05
 */
public class TimerHandleManager {

    public static LinkedHashMap<Player, TimeHandle> PLAYER_TIMER = new LinkedHashMap<>();


    public static TimeHandle getTimeHandle(Player player){
        if(!PLAYER_TIMER.containsKey(player)){
            PLAYER_TIMER.put(player,new TimeHandle());
        }
        return PLAYER_TIMER.get(player);
    }

    public static void clearPlayerKey(Player player){
        PLAYER_TIMER.remove(player);
    }
}
