package cn.smallaswater.land.lands;


import cn.smallaswater.land.lands.data.LandData;
import cn.smallaswater.land.lands.data.sub.LandSubData;

import java.util.LinkedList;

/**
 * @author 若水
 */
public class LandList extends LinkedList{

    private LinkedList<LandData> data;

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
