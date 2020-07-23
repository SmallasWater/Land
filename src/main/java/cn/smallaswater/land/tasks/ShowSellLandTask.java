package cn.smallaswater.land.tasks;

import cn.nukkit.scheduler.Task;
import cn.smallaswater.land.lands.data.LandData;
import cn.smallaswater.land.utils.DataTool;
import cn.smallaswater.land.module.LandModule;

/**
 * @author 若水
 */
public class ShowSellLandTask extends Task {
    @Override
    public void onRun(int i) {
        for(LandData data: DataTool.getSellLandAll()){
            if(data.isSell()){
                if(DataTool.getOutDay(data) >= LandModule.getModule().getConfig().getShowTime()){
                    data.setSell(false);
                }
            }
        }
    }
}
