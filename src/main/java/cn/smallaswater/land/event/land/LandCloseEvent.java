package cn.smallaswater.land.event.land;

import cn.smallaswater.land.event.LandDataEvent;
import cn.smallaswater.land.lands.data.LandData;

/**
 * @author SmallasWater
 */
public class LandCloseEvent extends LandDataEvent {
    public LandCloseEvent(LandData data) {
        super(data);
    }


}
