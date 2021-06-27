package cn.smallaswater.land.tasks;

import cn.nukkit.Player;
import cn.nukkit.scheduler.PluginTask;
import cn.smallaswater.land.LandMainClass;
import cn.smallaswater.land.handle.TimeHandle;
import cn.smallaswater.land.manager.TimerHandleManager;

import java.util.Map;

/**
 * @author SmallasWater
 * Create on 2021/6/27 13:22
 * Package cn.smallaswater.land.tasks
 */
public class TransferColdTask extends PluginTask<LandMainClass> {
    public TransferColdTask(LandMainClass owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        for (Map.Entry<Player, TimeHandle> timeHandleEntry : TimerHandleManager.PLAYER_TIMER.entrySet()) {
            timeHandleEntry.getValue().loading();
        }

    }
}
