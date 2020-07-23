package cn.smallaswater.land.event.player;

import cn.nukkit.Player;
import cn.smallaswater.land.event.LandPlayerDataEvent;
import cn.smallaswater.land.lands.data.LandData;

/**
 * @author 若水
 */
public class PlayerAcceptLandInviteEvent extends LandPlayerDataEvent {
    public PlayerAcceptLandInviteEvent(Player player, LandData data) {
        super(player, data);
    }
}
