package cn.smallaswater.land.event.player;

import cn.nukkit.Player;
import cn.smallaswater.land.event.LandPlayerDataEvent;
import cn.smallaswater.land.lands.data.LandData;

/**
 * @author SmallasWater
 */
public class PlayerMoveJoinLandEvent extends LandPlayerDataEvent {

    private String message;

    public PlayerMoveJoinLandEvent(Player player, LandData data, String messae) {
        super(player, data);
        this.message = messae;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
