package cn.smallaswater.land;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import cn.smallaswater.land.module.LandModule;
import updata.AutoData;

import java.io.File;
import java.nio.file.Files;


/**
 * @author 若水
 */
public class LandMainClass extends PluginBase {

    private LandModule module;


    public static LandMainClass MAIN_CLASS;

    @Override
    public void onLoad() {
        MAIN_CLASS = this;
    }

    @Override
    public void onEnable() {
        if(Server.getInstance().getPluginManager().getPlugin("AutoUpData") != null){
            if(AutoData.defaultUpData(this,getFile(),"SmallasWater","Land")){
                return;
            }
        }

        File resourcePack = new File(Server.getInstance().getDataPath() + "resource_packs/Land-ResourcePack.zip");
        if (!resourcePack.exists()) {
            try {
                Files.copy(this.getResource("Land-ResourcePack.zip"), resourcePack.toPath());
                this.getServer().getScheduler().scheduleTask(this, () ->
                        this.getLogger().warning("Copy of resource pack file complete! Please restart the server to ensure normal reading of the resource pack!"));
            } catch (Exception e) {
                this.getLogger().warning("Resource pack file replication failed! This may cause some functions to fail!", e);
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
