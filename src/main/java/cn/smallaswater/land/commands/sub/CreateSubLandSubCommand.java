package cn.smallaswater.land.commands.sub;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.smallaswater.land.event.land.LandCreateEvent;
import cn.smallaswater.land.event.player.PlayerCreateLandEvent;
import cn.smallaswater.land.lands.data.LandData;
import cn.smallaswater.land.lands.data.sub.LandSubData;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.players.MemberSetting;
import cn.smallaswater.land.players.PlayerSetting;
import cn.smallaswater.land.utils.DataTool;
import cn.smallaswater.land.utils.Language;
import cn.smallaswater.land.utils.Vector;


import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author 若水
 */
public class CreateSubLandSubCommand extends CreateSubCommand {

    private LandData data;

    public CreateSubLandSubCommand(String subCommandName) {
        super(subCommandName);
    }

    @Override
    protected boolean createLandData(String name, Player sender, Vector vector, Language language) {
        LandSubData data = new LandSubData(this.data.getSubData().size(),this.data.getLandName(),
                name,sender.getName(),vector, new MemberSetting(new LinkedHashMap<>())
                , PlayerSetting.getDefaultSetting(),this.data.getJoinMessage(),this.data.getQuitMessage(), DataTool.getDefaultPosition(vector));
        PlayerCreateLandEvent event = new PlayerCreateLandEvent(sender, data);
        Server.getInstance().getPluginManager().callEvent(event);
        if(event.isCancelled()){
            return false;
        }
        LandCreateEvent event1 = new LandCreateEvent(data);
        Server.getInstance().getPluginManager().callEvent(event1);
        if(event1.isCancelled()){
            return false;
        }
        this.data.getSubData().add(data);
        LandModule.getModule().saveList();
        return true;

    }

    @Override
    protected boolean isInLand(Player player) {
        LandData data = DataTool.getPlayerTouchArea(getPos().get(player.getName()).get(0),true);
        //主领地名称
        LandData string = DataTool.inLandAll(new Vector(getPos().get(player.getName()).get(0),getPos().get(player.getName()).get(1)));
        if(string != null && data != null){
            if (string.equals(data)) {
                this.data = data;
                return true;
            }
        }else if(data == null){
            player.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().inLandData);
            return false;
        }
        player.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().subInMaster.replace("%name%",data.getLandName()));
        return false;
    }

    @Override
    protected double getMoney(Vector vector) {
        return DataTool.getLandMoney(vector,true);
    }

    @Override
    protected int getMaxLand() {
        return LandModule.getModule().getConfig().getSubMax();
    }

    @Override
    protected String getLandName(LinkedList<Position> positions){
        LandData data = DataTool.checkOverlap(new Vector(positions.get(0),positions.get(1)),this.data);
        if(data != null) {
            return data.getLandName();
        }
        return null;
    }

    @Override
    protected LinkedHashMap<String, LinkedList<Position>> getPos() {
        return LandModule.getModule().subPos;
    }

    @Override
    protected boolean canExistsLand(String name){
        return data.getSubLandByName(name) != null;
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "创建子领地";
    }

}
