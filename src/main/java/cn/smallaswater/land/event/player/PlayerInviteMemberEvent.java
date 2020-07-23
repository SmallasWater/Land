package cn.smallaswater.land.event.player;

import cn.nukkit.Player;
import cn.smallaswater.land.event.LandPlayerDataEvent;
import cn.smallaswater.land.lands.data.LandData;


/**
 * @author SmallasWater
 */
public class PlayerInviteMemberEvent extends LandPlayerDataEvent {

    private Player from;

    public PlayerInviteMemberEvent(Player player, LandData data, Player from) {
        super(player, data);
        this.from = from;
    }

    public Player getFrom() {
        return from;
    }
}
