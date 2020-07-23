package cn.smallaswater.land.event.player;

import cn.nukkit.Player;
import cn.smallaswater.land.event.LandPlayerDataEvent;
import cn.smallaswater.land.lands.data.LandData;

/**
 * @author SmallasWater
 */
public class PlayerKickMemberLandEvent extends LandPlayerDataEvent {

    private String member;

    public PlayerKickMemberLandEvent(Player player,String member, LandData data) {
        super(player, data);
        this.member = member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getMember() {
        return member;
    }
}
