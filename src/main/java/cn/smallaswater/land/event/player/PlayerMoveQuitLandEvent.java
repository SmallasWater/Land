package cn.smallaswater.land.event.player;

import cn.nukkit.Player;
import cn.smallaswater.land.event.LandPlayerDataEvent;
import cn.smallaswater.land.lands.data.LandData;

/**
 * @author SmallasWater
 */
public class PlayerMoveQuitLandEvent extends LandPlayerDataEvent {


    private String message;
    public PlayerMoveQuitLandEvent(Player player, LandData data, String message) {
        super(player, data);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
