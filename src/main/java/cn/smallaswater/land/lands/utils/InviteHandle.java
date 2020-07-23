package cn.smallaswater.land.lands.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.smallaswater.land.event.player.PlayerAcceptLandInviteEvent;
import cn.smallaswater.land.event.player.PlayerDenyLandInviteEvent;
import cn.smallaswater.land.lands.data.LandData;
import cn.smallaswater.land.lands.data.sub.LandSubData;
import cn.smallaswater.land.utils.DataTool;
import cn.smallaswater.land.module.LandModule;

import java.util.LinkedList;

/**
 * @author 若水
 */
public class InviteHandle {

    /**邀请者*/
    private String master;

    /**被邀请者*/
    private String member;

    private LandData data;

    private int time;
    public InviteHandle(String master,String member,LandData data,int time){
        this.data = data;
        this.master = master;
        this.time = time;
        this.member = member;
    }

    public LandData getData() {
        return data;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }



    public void accept(){
        LinkedList<? extends LandData> data = DataTool.getLands(member);
        int max = LandModule.getModule().getConfig().getMaxLand();
        if(this.data instanceof LandSubData){
            data = ((LandSubData) this.data).getMasterData().getSubData();
            max = LandModule.getModule().getConfig().getSubMax();
        }

        if(data.size() < max){
            Player m = Server.getInstance().getPlayer(member);
            if(this.data instanceof LandSubData){
                if(((LandSubData) this.data).getMasterData() == null){
                    if (m != null) {
                        m.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().dataNotExists.replace("%name%",this.data.getLandName()));
                    }
                    return;
                }
            }else{
                if(!LandModule.getModule().getList().contains(this.data)){
                    if (m != null) {
                        m.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().dataNotExists.replace("%name%",this.data.getLandName()));
                    }
                    return;
                }
            }
            if(m != null) {
                PlayerAcceptLandInviteEvent event = new PlayerAcceptLandInviteEvent(m,this.data);
                Server.getInstance().getPluginManager().callEvent(event);
                if(event.isCancelled()){
                    return;
                }
            }
            this.data.addMember(member);
            LinkedList<InviteHandle> handles = LandModule.getModule().inviteLands.get(member);
            if (handles != null) {
                handles.remove(this);
            }
            m = Server.getInstance().getPlayer(master);
            if (m != null) {
                m.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().acceptMessageMaster
                        .replace("%p%", member).replace("%name%", this.data.getLandName()));
            }
            m = Server.getInstance().getPlayer(member);
            if (m != null) {
                m.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().acceptMessageMember
                        .replace("%p%", master).replace("%name%", this.data.getLandName()));
            }


        }else{
            Player m = Server.getInstance().getPlayer(master);
            if(m != null){
                m.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage()
                        .acceptErrorMaxMaster.replace("%name%",this.data.getLandName()).replace("%p%",member));
            }
            m = Server.getInstance().getPlayer(member);
            if(m != null){
                m.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage()
                        .acceptErrorMaxTarget.replace("%name%",this.data.getLandName()).replace("%p%",master));
            }
        }




    }

    public void timeOut(){
        LinkedList<InviteHandle> handles = LandModule.getModule().inviteLands.get(member);
        if(handles != null){
            handles.remove(this);
        }
    }

    public void setData(LandData data) {
        this.data = data;
    }

    public void deny(){
        Player m = Server.getInstance().getPlayer(master);
        if(m != null){
            PlayerDenyLandInviteEvent event = new PlayerDenyLandInviteEvent(m,data);
            Server.getInstance().getPluginManager().callEvent(event);
            if(event.isCancelled()){
                return;
            }
            m.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().denyMessageMaster
                    .replace("%p%",member).replace("%name%",this.data.getLandName()));
        }
        m = Server.getInstance().getPlayer(member);
        if(m != null) {
            m.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().denyMessageMember
                    .replace("%p%", member).replace("%name%", this.data.getLandName()));
        }
       this.timeOut();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof InviteHandle){
            return ((InviteHandle) obj).master.equalsIgnoreCase(master)
                    && ((InviteHandle) obj).member.equalsIgnoreCase(member);
        }
        return false;
    }

    public String getMaster() {
        return master;
    }

    public String getMember() {
        return member;
    }
}
