package cn.smallaswater.land.tasks;


import cn.nukkit.scheduler.PluginTask;
import cn.smallaswater.land.LandMainClass;
import cn.smallaswater.land.lands.LandList;
import cn.smallaswater.land.lands.data.LandData;

/**
 * @author SmallasWater
 * Create on 2021/1/23 22:18
 * Package cn.smallaswater.land.tasks
 */
public class AutoSaveLandTask extends PluginTask<LandMainClass> {
    public AutoSaveLandTask(LandMainClass owner) {
        super(owner);
    }

    private int saveCount = 0;

    @Override
    public void onRun(int i) {
        getOwner().getLogger().info("[LAND] saving...");
        LandList list = getOwner().getModule().getList();
        for(LandData data: list.getData()){
            if(data.save()){
                saveCount++;
            }
        }
        getOwner().getLogger().info("[LAND] has been saved");
        saveCount = 0;

    }
}
