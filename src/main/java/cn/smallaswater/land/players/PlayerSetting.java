package cn.smallaswater.land.players;

import cn.smallaswater.land.lands.settings.LandSetting;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 若水
 */
public class PlayerSetting {

    private LinkedHashMap<String,Boolean> settings;


    public PlayerSetting(LinkedHashMap<String,Boolean> settings){
       this.settings = settings;
    }

    public PlayerSetting(Map map){
        LinkedHashMap<String,Boolean> map1 = new LinkedHashMap<>();
        for(Object s:map.keySet()){
            map1.put(s.toString(), (Boolean) map.get(s));
        }
        this.settings = map1;
    }


    public static PlayerSetting getDefaultSetting(){
        PlayerSetting setting = new PlayerSetting(new LinkedHashMap<>());
        for (LandSetting s:LandSetting.values()){
            if(s == LandSetting.TRANSFER || s == LandSetting.MOVE){
                setting.settings.put(s.name,true);
            }else{
                setting.settings.put(s.name,false);
            }

        }
        return setting;
    }

    public static PlayerSetting getPlayerDefaultSetting(){
        PlayerSetting setting = new PlayerSetting(new LinkedHashMap<>());
        for (LandSetting s:LandSetting.values()){
            setting.settings.put(s.name,true);
        }
        return setting;
    }

    public LinkedHashMap<String, Boolean> getSettings() {
        return settings;
    }

    public boolean getSetting(String setting){
        if(settings.containsKey(setting)) {
            return settings.get(setting);
        }
        return true;
    }

    public static PlayerSetting getPlayerSetting(Map map){
        LinkedHashMap<String,Boolean> settings = new LinkedHashMap<>();
        for(LandSetting setting:LandSetting.values()){
            if(map.containsKey(setting.name)){
                settings.put(setting.name, (Boolean) map.get(setting.name));
            }else{
                settings.put(setting.name, false);
            }
        }
        return new PlayerSetting(settings);
    }

    public void setSetting(String setting,boolean value) {
        this.settings.put(setting,value);
    }



}
