package cn.smallaswater.land.event.player;

import cn.nukkit.Player;
import cn.smallaswater.land.event.LandPlayerDataEvent;
import cn.smallaswater.land.lands.data.LandData;

/**
 * @author SmallasWater
 */
public class PlayerDenyLandInviteEvent extends LandPlayerDataEvent {


    public PlayerDenyLandInviteEvent(Player player, LandData data) {
        super(player, data);
    }
}
