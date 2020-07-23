package cn.smallaswater.land.event;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.smallaswater.land.lands.data.LandData;

/**
 * @author 若水
 */
public class LandPlayerDataEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlers() {
        return HANDLERS;
    }

    public LandPlayerDataEvent(Player player, LandData data){
        this.data = data;
        this.player = player;
    }


    public LandData data;

    public LandData getData() {
        return data;
    }

}
