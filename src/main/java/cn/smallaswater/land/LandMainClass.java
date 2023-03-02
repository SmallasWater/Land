package cn.smallaswater.land;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.resourcepacks.ResourcePack;
import cn.nukkit.resourcepacks.ResourcePackManager;
import cn.nukkit.resourcepacks.ZippedResourcePack;
import cn.smallaswater.land.lands.utils.LandConfig;
import cn.smallaswater.land.module.LandModule;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import updata.AutoData;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * @author 若水
 */
public class LandMainClass extends PluginBase {

    private LandModule module;


    public static LandMainClass MAIN_CLASS;

    @Override
    public void onLoad() {
        MAIN_CLASS = this;

        this.saveResource("Resource/Land-ResourcePack.zip", true);
    }

    @Override
    public void onEnable() {
        if(Server.getInstance().getPluginManager().getPlugin("AutoUpData") != null){
            if(AutoData.defaultUpData(this,getFile(),"SmallasWater","Land")){
                return;
            }
        }

        /*File resourcePack = new File(Server.getInstance().getDataPath() + "resource_packs/Land-ResourcePack.zip");
        if (!resourcePack.exists()) {
            try {
                Files.copy(this.getResource("Land-ResourcePack.zip"), resourcePack.toPath());
                this.getServer().getScheduler().scheduleTask(this, () ->
                        this.getLogger().warning("Copy of resource pack file complete! Please restart the server to ensure normal reading of the resource pack!"));
            } catch (Exception e) {
                this.getLogger().warning("Resource pack file replication failed! This may cause some functions to fail!", e);
            }
        }*/

        module = new LandModule();
        module.moduleRegister();

        if (LandConfig.getLandConfig().isEnableEnhancedResourcePack()) {
            File file = new File(this.getDataFolder() + "/Resource/Land-ResourcePack.zip");
            if (file.exists()) {
                ResourcePackManager manager = this.getServer().getResourcePackManager();
                synchronized (manager) {
                    try {
                        List<ResourcePack> packs = new ObjectArrayList<>();
                        packs.add(new ZippedResourcePack(file));

                        Field resourcePacksById = ResourcePackManager.class.getDeclaredField("resourcePacksById");
                        resourcePacksById.setAccessible(true);
                        Map<UUID, ResourcePack> byId = (Map<UUID, ResourcePack>) resourcePacksById.get(manager);
                        packs.forEach(pack -> byId.put(pack.getPackId(), pack));

                        Field resourcePacks = ResourcePackManager.class.getDeclaredField("resourcePacks");
                        resourcePacks.setAccessible(true);
                        packs.addAll(Arrays.asList((ResourcePack[]) resourcePacks.get(manager)));
                        resourcePacks.set(manager, packs.toArray(new ResourcePack[0]));
                    } catch (Exception e) {
                        this.getLogger().error("Resource pack loading failed! This may cause some functions to fail!", e);
                    }
                }
            }
        }
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
