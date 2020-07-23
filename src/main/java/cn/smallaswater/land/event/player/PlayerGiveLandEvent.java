package cn.smallaswater.land.event.player;

import cn.nukkit.Player;
import cn.smallaswater.land.event.LandPlayerDataEvent;
import cn.smallaswater.land.lands.data.LandData;

/**
 * @author SmallasWater
 */
public class PlayerGiveLandEvent extends LandPlayerDataEvent {


    private String master;

    public PlayerGiveLandEvent(Player player, LandData data, String master) {
        super(player, data);
        this.master = master;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }
}
