package cn.smallaswater.land.lands.data;


import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import cn.smallaswater.land.LandMainClass;
import cn.smallaswater.land.event.land.LandAddMemberEvent;
import cn.smallaswater.land.event.land.LandCloseEvent;
import cn.smallaswater.land.event.player.PlayerJoinLandEvent;
import cn.smallaswater.land.event.player.PlayerQuitLandEvent;
import cn.smallaswater.land.lands.data.sub.LandSubData;
import cn.smallaswater.land.lands.settings.LandSetting;
import cn.smallaswater.land.lands.settings.OtherLandSetting;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.players.MemberSetting;
import cn.smallaswater.land.players.PlayerSetting;
import cn.smallaswater.land.utils.DataTool;
import cn.smallaswater.land.utils.Vector;

import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;


/**
 * @author 若水
 */
public class LandData  {

    private int landId;

    private String landName;

    private String master;

    private Vector vector;

    private MemberSetting member;

    private PlayerSetting defaultSetting;

    private String joinMessage;

    private String quitMessage;

    private Position transfer;

    private Config config;

    private boolean isSell = false;

    private String sellDay;

    private double money = -1.0;

    private double sellMoney = -1.0;

    private Date createTime;

    private LinkedList<LandSubData> subData = new LinkedList<>();

    private LandOtherSet landOtherSet;

    private String sellMessage = "领地出售中~~~";

    public LandData(int landId,String landName,String master,Vector vector,
                    MemberSetting member,
                    PlayerSetting defaultSetting,
                    String joinMessage,String quitMessage,Position transfer){
        this.landId = landId;
        this.landName = landName;
        this.master = master;
        this.vector = vector;
        this.member = member;
        this.defaultSetting = defaultSetting;
        this.joinMessage = joinMessage;
        this.quitMessage = quitMessage;
        this.transfer = transfer;
        this.createTime = new Date();
        this.landOtherSet = new LandOtherSet();
    }

    public void setLandOtherSet(LandOtherSet landOtherSet) {
        this.landOtherSet = landOtherSet;
    }

    public LandOtherSet getLandOtherSet() {
        return landOtherSet;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public final Config getConfig() {
        return config;
    }

    public Vector getVector() {
        return vector;
    }

    public int getLandId() {
        return landId;
    }

    public void setLandId(int landId) {
        this.landId = landId;
    }

    public Position getTransfer() {
        return transfer;
    }

    public LinkedHashMap<String,Object> getAll(){
        LinkedHashMap<String,Object> obj = getSaves();
        LinkedList<LinkedHashMap<String,Object>> subs = new LinkedList<>();
        for(LandSubData subData:subData){
            subs.add(subData.getSaves());
        }
        obj.put("subLand",subs);
        return obj;
    }

    public void setSellDay(String sellDay) {
        this.sellDay = sellDay;
    }

    public String getSellDay() {
        return sellDay;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setSell(boolean sell) {
        isSell = sell;
    }

    public boolean isSell() {
        return isSell;
    }

    public void setSellMoney(double sellMoney) {
        this.sellMoney = sellMoney;
    }

    public void setSellMessage(String sellMessage) {
        this.sellMessage = sellMessage;
    }

    public String getSellMessage() {
        return sellMessage;
    }

    public double getSellMoney() {
        return sellMoney;
    }

    public boolean contains(LandSubData data){
        return subData.contains(data);
    }

    public LinkedList<LandSubData> getSubData() {
        return subData;
    }

    public LandSubData getSubLandByName(String landName){
        for(LandSubData data:subData){
            if(data.getLandName().equalsIgnoreCase(landName)){
                return data;
            }
        }
        return null;
    }

    public double getMoney() {
        return money;
    }



    public void setMoney(double money) {
        this.money = money;
    }

    protected LinkedHashMap<String,Object> getSaves(){
        LinkedHashMap<String,Object> obj = new LinkedHashMap<>();
        obj.put("landId",landId);
        obj.put("landName",getLandName().trim());
        obj.put("master",getMaster().trim());
        obj.put("vector",getVector().getConfig());
        obj.put("member",getMember().getMember());
        obj.put("defaultSetting",getDefaultSetting().getSettings());
        obj.put("otherLandSetting",landOtherSet.getOtherLandSetting());
        obj.put("joinMessage",getJoinMessage().trim());
        obj.put("quitMessage",getQuitMessage().trim());
        obj.put("transfer", DataTool.getMapByTransfer(getTransfer()));
        obj.put("isSell",isSell());
        obj.put("sellDay",getSellDay());
        obj.put("money",getMoney());
        obj.put("sellMoney",getSellMoney());
        obj.put("sellMessage",getSellMessage().trim());
        obj.put("createTime",DataTool.getDateToString(createTime));
        return obj;
    }



    public boolean save(){
        if(config == null){
            config = new Config(LandModule.getModule().getModuleInfo().getDataFolder()+"/lands/"+(landName).trim()+".yml",2);
        }
        config.setAll(getAll());
        if(config.getAll().size() == 0){
            LandMainClass.MAIN_CLASS.getLogger().error("[领地]"+landName+" 在保存的过程中出现问题 原因: 数据丢失[未对配置文件进行更改]");
            return false;
        }
        config.save();
        return true;

    }

    public boolean isError(){
        try {
            String name = vector.level.getFolderName();
            return Server.getInstance().getLevelByName(name) == null;
        }catch (Exception e){
            return true;
        }

    }

    public void addSubLand(LandSubData data){
        if(!subData.contains(data)){
            subData.add(data);
        }
    }

    public void removeSubLand(String landName){
        for(LandSubData data:subData){
            if(data.getLandName().equalsIgnoreCase(landName)){
                data.close();
            }
        }
    }

    public void removeSubLand(LandSubData data){
        if(subData.contains(data)){
            data.close();
        }
    }

    public void setQuitMessage(String quitMessage) {
        this.quitMessage = quitMessage;
    }

    public void setTransfer(Position transfer) {
        this.transfer = transfer;
    }

    public void setSubData(LinkedList<LandSubData> subData) {
        this.subData = subData;
    }

    public void setMaster(String master) {
        Player player1 = Server.getInstance().getPlayer(master);
        Player player2 = Server.getInstance().getPlayer(this.master);
        if(player1 != null){
            player1.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().translateString("givePlayerLandTarget")
                    .replace("%p%","You").replace("%name%",getLandName()));
        }
        if(player2 != null) {
            player2.sendMessage(LandModule.getModule().getConfig().getTitle() + LandModule.getModule().getLanguage().translateString("givePlayerLandMaster")
                    .replace("%p%", master).replace("%name%", getLandName()));
        }
        this.master = master;
    }

    public void setMember(MemberSetting member) {
        this.member = member;
    }

    public void setDefaultSetting(PlayerSetting defaultSetting) {
        this.defaultSetting = defaultSetting;
    }

    public void setDefaultSetting(String setting, boolean value){
        this.defaultSetting.setSetting(setting, value);
    }

    public void setJoinMessage(String joinMessage) {
        this.joinMessage = joinMessage;
    }

    public String getMaster() {
        return master;
    }

    public void addMember(String member){
        LandAddMemberEvent event1 = new LandAddMemberEvent(this,member);
        Server.getInstance().getPluginManager().callEvent(event1);
        if(event1.isCancelled()){
            return;
        }
        member = event1.getMember();
        Player player = Server.getInstance().getPlayer(member);
        if(player != null){
            PlayerJoinLandEvent event = new PlayerJoinLandEvent(player,this);
            Server.getInstance().getPluginManager().callEvent(event);
            if(event.isCancelled()){
                return;
            }
            player.sendMessage(LandModule.getModule().getLanguage().translateString("playerJoinLandMessageTarget").replace("%p%",master).replace("%name%",landName));
        }
        player = Server.getInstance().getPlayer(master);
        if(player != null){
            player.sendMessage(LandModule.getModule().getLanguage().translateString("playerJoinLandMessageMaster").replace("%p%",member).replace("%name%",getLandName()));
        }
        this.member.put(member,PlayerSetting.getPlayerDefaultSetting());
    }

    public void setPlayerSetting(String player,PlayerSetting setting){
        member.put(player,setting);
    }

    public void setPlayerSetting(String player, LandSetting setting, boolean value){
        if(!member.containsKey(player)){
            member.put(player,PlayerSetting.getPlayerDefaultSetting());
        }
        PlayerSetting setting1 = member.get(player);
        setting1.setSetting(setting.getName(),value);
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    public void setLandName(String landName) {
        this.landName = landName;
    }

    public void removeMember(String member){
        Player player = Server.getInstance().getPlayer(member);
        if(player != null){
            PlayerQuitLandEvent event = new PlayerQuitLandEvent(player,this);
            Server.getInstance().getPluginManager().callEvent(event);
            if(event.isCancelled()){
                return;
            }
            player.sendMessage(LandModule.getModule().getLanguage().translateString("playerQuitLandMessageTarget").replace("%p%",master).replace("%name%",landName));
        }
        player = Server.getInstance().getPlayer(master);
        if(player != null){
            player.sendMessage(LandModule.getModule().getLanguage().translateString("playerQuitLandMessageMaster").replace("%p%",member).replace("%name%",getLandName()));
        }
        this.member.remove(member);
    }


    /**
     * TODO 领地的设置 防怪等
     * */
    public boolean hasPermission(OtherLandSetting setting){
        return landOtherSet.isOpen(setting);

    }

    private boolean hasPermission(String player, String name) {
        if(player != null) {
            Player player1 = Server.getInstance().getPlayer(player);
            if (player1 != null) {
                if (player1.isOp()) {
                    return true;
                }
            }
            if (master.equalsIgnoreCase(player)) {
                return true;
            } else if (member.containsKey(player)) {
                PlayerSetting setting1 = member.get(player);
                return setting1.getSetting(name);
            }
        }
        return defaultSetting.getSetting(name);
    }

    public boolean hasPermission(String player, LandSetting setting){
        return hasPermission(player, setting.getName());
    }

    public MemberSetting getMember() {
        return member;
    }

    public void close(){
        LandCloseEvent event = new LandCloseEvent(this);
        Server.getInstance().getPluginManager().callEvent(event);
        if(event.isCancelled()){
            return;
        }
        if(LandModule.getModule().getList().contains(this)){
            LandModule.getModule().getList().getData().remove(this);
        }
        File file = new File(LandModule.getModule().getModuleInfo().getDataFolder()+"/lands/"+landName+".yml");
        if(file.exists()){
            if(!file.delete()){
                LandMainClass.MAIN_CLASS.getLogger().error("delete "+landName+".yml file error");
            }
        }
    }

    public PlayerSetting getDefaultSetting() {
        return defaultSetting;
    }

    public String getJoinMessage() {
        return joinMessage;
    }

    public String getLandName() {
        return landName;
    }

    public String getQuitMessage() {
        return quitMessage;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LandData){
            return ((LandData) obj).getLandName().equalsIgnoreCase(getLandName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getLandName().hashCode();
    }

}


