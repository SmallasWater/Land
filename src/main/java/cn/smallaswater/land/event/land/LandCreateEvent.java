package cn.smallaswater.land.event.land;

import cn.smallaswater.land.event.LandDataEvent;
import cn.smallaswater.land.lands.data.LandData;

/**
 * @author 若水
 */
public class LandCreateEvent extends LandDataEvent {

    public LandCreateEvent(LandData data) {
        super(data);
    }
}
