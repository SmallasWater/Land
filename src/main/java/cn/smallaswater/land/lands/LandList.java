package cn.smallaswater.land.lands;


import cn.smallaswater.land.lands.data.LandData;
import cn.smallaswater.land.lands.data.sub.LandSubData;
import cn.smallaswater.land.utils.Vector;

import java.util.*;

/**
 * @author 若水
 */
public class LandList extends LinkedList {

    private LinkedList<LandData> data;

    private final HashMap<String, List<LandData>> chunkIndex = new HashMap<>();

    public LandList(LinkedList<LandData> data){
        this.data = data;
    }

    public void save(){
        for(LandData data:data){
            if(data != null) {
                data.save();
            }
        }
    }

    /**
     * 构建区块索引（加载完所有领地后调用一次）
     */
    public void buildChunkIndex() {
        chunkIndex.clear();
        for (LandData land : data) {
            addToChunkIndex(land);
        }
    }

    /**
     * 将领地注册到其覆盖的所有 chunk
     */
    public void addToChunkIndex(LandData land) {
        if (land instanceof LandSubData) return;
        Vector v = land.getVector();
        if (v == null || v.getLevel() == null) return;
        String world = v.getLevel().getFolderName();
        for (int cx = v.getStartX() >> 4; cx <= v.getEndX() >> 4; cx++) {
            for (int cz = v.getStartZ() >> 4; cz <= v.getEndZ() >> 4; cz++) {
                chunkIndex.computeIfAbsent(world + ":" + cx + ":" + cz,
                    k -> new ArrayList<>()).add(land);
            }
        }
    }

    /**
     * 从索引中移除领地
     */
    public void removeFromChunkIndex(LandData land) {
        if (land instanceof LandSubData) return;
        Vector v = land.getVector();
        if (v == null || v.getLevel() == null) return;
        String world = v.getLevel().getFolderName();
        for (int cx = v.getStartX() >> 4; cx <= v.getEndX() >> 4; cx++) {
            for (int cz = v.getStartZ() >> 4; cz <= v.getEndZ() >> 4; cz++) {
                String key = world + ":" + cx + ":" + cz;
                List<LandData> list = chunkIndex.get(key);
                if (list != null) {
                    list.remove(land);
                    if (list.isEmpty()) {
                        chunkIndex.remove(key);
                    }
                }
            }
        }
    }

    /**
     * 查询指定 chunk 的领地列表
     */
    public List<LandData> getChunkLands(String world, int chunkX, int chunkZ) {
        List<LandData> list = chunkIndex.get(world + ":" + chunkX + ":" + chunkZ);
        return list != null ? list : Collections.emptyList();
    }


    public LandData getLandDataByName(String name){
        for(LandData data:data){
            if(data.getLandName().equalsIgnoreCase(name)){
                return data;
            }
        }
        return null;
    }

    public LinkedList<LandData> getData() {
        return data;
    }

    public void add(LandData data){
        if(!(data instanceof LandSubData)) {
            if (!contains(data)) {
                this.data.add(data);
                addToChunkIndex(data);
            }
        }
    }

    public void remove(LandData data){
        data.close();
    }

    public boolean contains(LandData data){
        return this.data.contains(data);

    }


}
