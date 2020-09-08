package cn.smallaswater.land.event.player;

import cn.smallaswater.land.event.LandDataEvent;
import cn.smallaswater.land.lands.data.LandData;

public class PlayerInviteTimeOutEvent extends LandDataEvent {

    private final String memberPlayerName;
    private final String masterPlayerName;

    public PlayerInviteTimeOutEvent(String masterPlayerName,String memberPlayerName, LandData data) {
        super(data);
        this.masterPlayerName = masterPlayerName;
        this.memberPlayerName = memberPlayerName;
    }

    public String getMasterPlayerName() {
        return masterPlayerName;
    }

    public String getMemberPlayerName() {
        return memberPlayerName;
    }
}
