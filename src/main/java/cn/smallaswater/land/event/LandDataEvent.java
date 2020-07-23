package cn.smallaswater.land.event;


import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.smallaswater.land.lands.data.LandData;

/**
 * @author SmallasWater
 */
public class LandDataEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlers() {
        return HANDLERS;
    }

    public LandDataEvent(LandData data){
        this.data = data;

    }


    public LandData data;

    public LandData getData() {
        return data;
    }
}
