package cn.smallaswater.land;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;

import cn.smallaswater.land.module.LandModule;
import updata.AutoData;


/**
 * @author 若水
 */
public class LandMainClass extends PluginBase {

    private LandModule module;


    public static LandMainClass MAIN_CLASS;
    @Override
    public void onEnable() {
        MAIN_CLASS = this;
        if(Server.getInstance().getPluginManager().getPlugin("AutoUpData") != null){
            if(AutoData.defaultUpData(this,getFile(),"SmallasWater","Land")){
                return;
            }
        }

        module = new LandModule();
        module.moduleRegister();

    }

    public LandModule getModule() {
        return module;
    }

    @Override
    public void onDisable() {
        if(module != null){
            module.moduleDisable();
        }
    }
}
