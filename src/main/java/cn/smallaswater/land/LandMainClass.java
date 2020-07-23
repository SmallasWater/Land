package cn.smallaswater.land;

import cn.nukkit.plugin.PluginBase;
import cn.smallaswater.land.module.LandModule;



/**
 * @author 若水
 */
public class LandMainClass extends PluginBase {

    private LandModule module;

    public static LandMainClass MAIN_CLASS;
    @Override
    public void onEnable() {
        MAIN_CLASS = this;
        module = new LandModule();
        module.moduleRegister();

    }

    @Override
    public void onDisable() {
        if(module != null){
            module.moduleDisable();
        }
    }
}
