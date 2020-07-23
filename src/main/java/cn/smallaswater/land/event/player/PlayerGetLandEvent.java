package cn.smallaswater.land.event.player;

import cn.nukkit.Player;
import cn.smallaswater.land.event.LandPlayerDataEvent;
import cn.smallaswater.land.lands.data.LandData;


/**
 * @author SmallasWater
 */
public class PlayerGetLandEvent extends LandPlayerDataEvent {

    private String from;

    public PlayerGetLandEvent(Player player, LandData data,String from) {
        super(player, data);
        this.from = from;
    }

    public String getFrom() {
        return from;
    }
}
