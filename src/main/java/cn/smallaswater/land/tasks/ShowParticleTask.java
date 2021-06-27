package cn.smallaswater.land.tasks;


import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.particle.DustParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.BlockColor;
import cn.smallaswater.land.LandMainClass;
import cn.smallaswater.land.lands.data.LandData;
import cn.smallaswater.land.lands.data.sub.LandSubData;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.utils.Vector;


/**
 * @author 若水
 */
public class ShowParticleTask extends PluginTask<LandMainClass> {
    public ShowParticleTask(LandMainClass owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        for(LandData data: LandModule.getModule().showTime.keySet()){
            if(data != null){
                if(LandModule.getModule().showTime.get(data) > 0){
                    LandModule.getModule().showTime.put(data,LandModule.getModule().showTime.get(data) - 1);
                    Server.getInstance().getScheduler().scheduleAsyncTask(LandMainClass.MAIN_CLASS, new AsyncTask() {
                        @Override
                        public void onRun() {
                            showParticle(data, data.getVector().level);
                        }
                    });
                }else{
                    LandModule.getModule().showTime.remove(data);
                }
            }
        }

    }

    private void showParticle(LandData data, Level level){
        Vector vector = data.getVector().clone().sort();
        BlockColor color = new BlockColor(255,255,255);
        if(data instanceof LandSubData){
            color = new BlockColor(84,255 ,159);
        }
        int x,y,z;
        for(y = vector.getStartY();y <= vector.getEndY();y++){
            if(y == vector.getStartY() || y == vector.getEndY()){
                for (z = vector.getStartZ(); z <= vector.getEndZ(); z++) {
                    level.addParticle(new DustParticle(new Vector3(vector.getStartX(), y, z), color));
                    level.addParticle(new DustParticle(new Vector3(vector.getEndX(), y, z), color));
                }
                for(x = vector.getStartX(); x<vector.getEndX();x++){
                    level.addParticle(new DustParticle(new Vector3(x, y, vector.getStartZ()), color));
                    level.addParticle(new DustParticle(new Vector3(x, y, vector.getEndZ()), color));
                }
            }else{
                level.addParticle(new DustParticle(new Vector3(vector.getStartX(), y, vector.getStartZ()), color));
                level.addParticle(new DustParticle(new Vector3(vector.getEndX(), y, vector.getStartZ()), color));
                level.addParticle(new DustParticle(new Vector3(vector.getEndX(), y, vector.getEndZ()), color));
                level.addParticle(new DustParticle(new Vector3(vector.getStartX(), y, vector.getEndZ()), color));
            }

        }

    }
}
