package cn.smallaswater.land.players;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 若水
 */
public class MemberSetting {

    private LinkedHashMap<String,PlayerSetting> member;

    public MemberSetting(LinkedHashMap<String,PlayerSetting> member){
        this.member = member;
    }

    public MemberSetting(Map map){
        LinkedHashMap<String,PlayerSetting> maps = new LinkedHashMap<>();
        PlayerSetting map1;
        for(Object o:map.keySet()){
            Map map2 = (Map) map.get(o);
            map1 = PlayerSetting.getPlayerSetting(map2);
            maps.put(o.toString(),map1);
        }
        this.member = maps;
    }

    public Set<String> keySet(){
        return member.keySet();
    }


    public void clear(){
        member.clear();
    }

    public boolean containsKey(String name){
        return member.containsKey(name);
    }

    public void put(String name,PlayerSetting playerSetting){
        member.put(name,playerSetting);
    }

    public void remove(String name){
        member.remove(name);
    }

    public PlayerSetting get(String name){
        if (member.containsKey(name)) {
            return member.get(name);
        }
        return null;
    }


    public LinkedHashMap<String, LinkedHashMap<String, Boolean>> getMember() {
        LinkedHashMap<String,LinkedHashMap<String, Boolean>> members = new LinkedHashMap<>();
        for(String name:this.member.keySet()){
            members.put(name,member.get(name).getSettings());
        }
        return members;
    }
}
