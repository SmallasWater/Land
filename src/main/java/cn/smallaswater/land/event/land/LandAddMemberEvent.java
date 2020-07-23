package cn.smallaswater.land.event.land;

import cn.smallaswater.land.event.LandDataEvent;
import cn.smallaswater.land.lands.data.LandData;

/**
 * @author SmallasWater
 */
public class LandAddMemberEvent extends LandDataEvent {

    private String member;
    public LandAddMemberEvent(LandData data,String member) {
        super(data);
        this.member = member;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }
}
