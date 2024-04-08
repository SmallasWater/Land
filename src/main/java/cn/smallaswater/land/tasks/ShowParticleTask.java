package cn.smallaswater.land.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.particle.DustParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.SpawnParticleEffectPacket;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.BlockColor;
import cn.smallaswater.land.LandMainClass;
import cn.smallaswater.land.lands.data.LandData;
import cn.smallaswater.land.lands.data.sub.LandSubData;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.utils.DataTool;
import cn.smallaswater.land.utils.Vector;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 若水
 */
public class ShowParticleTask extends PluginTask<LandMainClass> {

    public static ConcurrentHashMap<LandData, ShowConfig> showList = new ConcurrentHashMap<>();

    public ShowParticleTask(LandMainClass owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        if (owner.getModule().getConfig().isEnableShowPlayerAroundLandRange()) {
            int range = owner.getModule().getConfig().getShowPlayerAroundLandRange();
            for (Player player : Server.getInstance().getOnlinePlayers().values()) {
                LinkedList<LandData> list = DataTool.getAroundLandData(player, range);
                for (LandData landData : list) {
                    if (!showList.containsKey(landData)) {
                        ShowConfig value = new ShowConfig();
                        value.setTime(1);
                        ArrayList<Player> players = new ArrayList<>();
                        players.add(player);
                        value.setPlayers(players);
                        showList.put(landData, value);
                    }
                    ShowConfig showConfig = showList.get(landData);
                    List<Player> players = showConfig.getPlayers();
                    if (players == null) {
                        players = new ArrayList<>();
                        showConfig.setPlayers(players);
                    }
                    if (!players.contains(player)) {
                        players.add(player);
                    }
                }
            }
        }
        Iterator<Map.Entry<LandData, ShowConfig>> iterator = showList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<LandData, ShowConfig> entry = iterator.next();
            if (entry.getValue().getTime() > 0) {
                entry.getValue().setTime(entry.getValue().getTime() - 1);
                Server.getInstance().getScheduler().scheduleAsyncTask(LandMainClass.MAIN_CLASS, new AsyncTask() {
                    @Override
                    public void onRun() {
                        showParticle(entry.getKey(), entry.getValue().getPlayers());
                    }
                });
            } else {
                iterator.remove();
            }
        }
    }

    private void showParticle(LandData data, List<Player> players) {
        Level level = data.getVector().getLevel();
        Vector vector = data.getVector().clone().sort();
        String particleName = "Land:BorderWhite";
        BlockColor color = new BlockColor(255, 255, 255);
        if (data instanceof LandSubData) {
            particleName = "Land:BorderGreen";
            color = new BlockColor(84, 255, 159);
        }

        ArrayList<Vector3> posList = new ArrayList<>();
        int x, y, z;
        for (y = vector.getStartY(); y <= vector.getEndY(); y++) {
            if (y == vector.getStartY() || y == vector.getEndY()) {
                for (z = vector.getStartZ(); z <= vector.getEndZ(); z++) {
                    posList.add(new Vector3(vector.getStartX(), y, z));
                    posList.add(new Vector3(vector.getEndX(), y, z));
                }
                for (x = vector.getStartX(); x < vector.getEndX(); x++) {
                    posList.add(new Vector3(x, y, vector.getStartZ()));
                    posList.add(new Vector3(x, y, vector.getEndZ()));
                }
            } else {
                posList.add(new Vector3(vector.getStartX(), y, vector.getStartZ()));
                posList.add(new Vector3(vector.getEndX(), y, vector.getStartZ()));
                posList.add(new Vector3(vector.getEndX(), y, vector.getEndZ()));
                posList.add(new Vector3(vector.getStartX(), y, vector.getEndZ()));
            }
        }

        //获取中心
        int cx = (vector.getEndX() - vector.getStartX())/2 + vector.getStartX();
        int cy = (vector.getEndY() - vector.getStartY())/2 + vector.getStartY();
        int cz = (vector.getEndZ() - vector.getStartZ())/2 + vector.getStartZ();

        for (Vector3 vector3 : posList) {
            if (vector3.x > cx) {
                vector3.x += 1;
            }
            if (vector3.y > cy) {
                vector3.y += 1;
            }
            if (vector3.z > cz) {
                vector3.z += 1;
            }

            if (LandModule.getModule().getConfig().isEnableEnhancedResourcePack()) {
                SpawnParticleEffectPacket pk = new SpawnParticleEffectPacket();
                pk.identifier = particleName;
                pk.dimensionId = level.getDimensionData().getDimensionId();
                pk.position = vector3.asVector3f();
                if (players != null && !players.isEmpty()) {
                    Server.broadcastPacket(players, pk);
                } else {
                    level.addChunkPacket(vector3.getChunkX(), vector3.getChunkZ(), pk);
                }
            } else {
                level.addParticle(new DustParticle(vector3, color), players);
            }
        }
    }

    @Data
    public static class ShowConfig {
        private int time = 5;
        @Nullable
        private List<Player> players = null;
    }

}
