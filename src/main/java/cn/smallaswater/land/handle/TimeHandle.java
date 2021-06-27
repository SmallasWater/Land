package cn.smallaswater.land.handle;

import java.util.LinkedHashMap;


/**
 * @author SmallasWater
 */
public class TimeHandle {

    private LinkedHashMap<String,Integer> times = new LinkedHashMap<>();

    public LinkedHashMap<String, Integer> getTimes() {
        return times;
    }

    public int getCold(String name){
        if(times.containsKey(name)){
            return times.get(name);
        }
        return 0;
    }

    public void loading(){
        for(String name: times.keySet()){
            times.put(name,times.get(name) - 1);
            if(times.get(name) <= 0){
                times.remove(name);
            }
        }
    }

    public void setCold(String name,int timer) {
        if(times.containsKey(name)){
            times.put(name,timer);
        }

    }

    public void addTimer(String name, int time){
        times.put(name,time);
    }

    public boolean hasCold(String name){
        return times.containsKey(name);
    }


}
