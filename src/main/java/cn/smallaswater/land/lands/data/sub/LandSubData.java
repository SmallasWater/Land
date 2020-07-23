package cn.smallaswater.land.lands.data.sub;

import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.smallaswater.land.event.land.LandCloseEvent;
import cn.smallaswater.land.lands.data.LandData;
import cn.smallaswater.land.players.MemberSetting;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.players.LandSetting;
import cn.smallaswater.land.players.PlayerSetting;
import cn.smallaswater.land.utils.Vector;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author 若水
 */
public class LandSubData extends LandData{

    private String masterData;

    private boolean isSellPlayer;

    public LandSubData(String masterData, String landName, String master, Vector vector, MemberSetting member,
                       PlayerSetting defaultSetting,
                       String joinMessage, String quitMessage, Position transfer) {
        super(landName, master, vector, member, defaultSetting, joinMessage, quitMessage, transfer);
        this.masterData = masterData;
    }

    public LandData getMasterData() {
        return LandModule.getModule().getList().getLandDataByName(masterData);
    }


    public static LandSubData getSubDataByLand(String landName,LandData data){
        return new LandSubData(landName,data.getLandName(),data.getMaster(),
                data.getVector(),data.getMember(),data.getDefaultSetting(),data.getJoinMessage(),data.getQuitMessage(),data.getTransfer());
    }

    @Override
    public LinkedList<LandSubData> getSubData() {
        return new LinkedList<>();
    }

    @Override
    public LandSubData getSubLandByName(String landName) {
        return null;
    }

    @Override
    public void removeSubLand(LandSubData data) {}

    @Override
    public void setSubData(LinkedList<LandSubData> subData) {}

    @Override
    public void removeSubLand(String landName) {}

    @Override
    public void addSubLand(LandSubData data) {}

    @Override
    public void save() {
        getMasterData().save();
    }

    public void setSellPlayer(boolean sellPlayer) {
        isSellPlayer = sellPlayer;
    }

    public boolean isSellPlayer() {
        return isSellPlayer;
    }

    @Override
    public void close() {
        LandCloseEvent event = new LandCloseEvent(this);
        Server.getInstance().getPluginManager().callEvent(event);
        if(event.isCancelled()){
            return;
        }
        getMasterData().getSubData().remove(this);
        save();
    }

    @Override
    public LinkedHashMap<String, Object> getSaves() {
        LinkedHashMap<String,Object> obj =  super.getSaves();
        obj.put("landName",getLandName());
        obj.put("sellPlayer",isSellPlayer());
        obj.put("mastLandData",masterData);
        return obj;
    }


    @Override
    public boolean hasPermission(String player, LandSetting setting) {
        boolean b = super.hasPermission(player, setting);
        LandData data = getMasterData();
        if(data != null) {
            //主领地主人拥有绝对权限
            if (data.getMaster().equalsIgnoreCase(player)) {
                b = true;
            }
        }else{
            b = false;
        }

        return b;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LandSubData){
            return ((LandSubData) obj).getLandName().equalsIgnoreCase(getLandName())
                    && ((LandSubData) obj).getMasterData().equals(getMasterData());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getLandName().hashCode() + masterData.hashCode();
    }
}
