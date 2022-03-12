package cn.smallaswater.land.lands.utils;

import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import cn.smallaswater.land.LandMainClass;
import cn.smallaswater.land.utils.LoadMoney;


import java.io.File;
import java.util.ArrayList;

/**
 * @author 若水
 */
public class  LandConfig {

    private String title;

    private double landMoney;

    private double subLandMoney;

    private int sellMoney;

    private ArrayList<String> blackList;

    private ArrayList<String> protectList;

    private int maxLand;

    private int time;

    private int subMax;

    private int showTime;

    private int loadEconomy;

    private Item landTool;

    private boolean autoSave;

    private int autoSaveTime;

    private boolean echoBlackListMessage;

    private boolean echoProtectListMessage;

    private int transferTime,transferCold;

    private boolean enableTransferTime;



    private LandConfig(String title, double landMoney,double subLandMoney, int sellMoney, ArrayList<String> blackList,ArrayList<String> protectList, int maxLand,int subMax, int time,int loadEconomy){
        this.landMoney = landMoney;
        this.blackList = blackList;
        this.maxLand = maxLand;
        this.time = time;
        this.title = title;
        this.sellMoney = sellMoney;
        this.subMax = subMax;
        this.subLandMoney = subLandMoney;
        this.protectList = protectList;
        this.loadEconomy = loadEconomy;
    }

    public void setSubLandMoney(double subLandMoney) {
        this.subLandMoney = subLandMoney;
    }

    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }

    public int getShowTime() {
        return showTime;
    }

    public int getSubMax() {
        return subMax;
    }

    public static LandConfig getLandConfig(){
        if(!new File(LandMainClass.MAIN_CLASS.getDataFolder()+"/config.yml").exists()){
            LandMainClass.MAIN_CLASS.saveDefaultConfig();
            LandMainClass.MAIN_CLASS.reloadConfig();
        }
        Config config = LandMainClass.MAIN_CLASS.getConfig();
        String title = config.getString("title","[领地系统]");
        double landMoney = config.getDouble("landMoney",10D);
        double subLandMoney = config.getDouble("subLandMoney",5D);
        int sellMoney = config.getInt("sellMoney",50);
        ArrayList<String> blackList = new ArrayList<>(config.getStringList("blackList"));
        ArrayList<String> protectList = new ArrayList<>(config.getStringList("protectList"));
        int max = config.getInt("max",5);
        int subMax = config.getInt("subMax",5);
        int time = config.getInt("time",0);
        String economy = config.getString("loadEconomy","default");
        int load = LoadMoney.ECONOMY_API;
        if("default".equalsIgnoreCase(economy)){
            load = -1;
        }
        if("money".equalsIgnoreCase(economy)){
            load = LoadMoney.MONEY;
        }
        if("playerpoint".equalsIgnoreCase(economy)){
            load = LoadMoney.PLAYER_POINT;
        }
        LandConfig config1 = new LandConfig(title,landMoney,subLandMoney,sellMoney,blackList,protectList,max,subMax,time,load);
        config1.setShowTime(config.getInt("售卖领地显示时间",7));
        Item i = Item.fromString(config.getString("landTool","290:0"));
        config1.setLandTool(i);
        config1.setEnableTransferTime(config.getBoolean("transfer-time-setting.enable",true));
        config1.setTransferCold(config.getInt("transfer-time-setting.cold",30));
        config1.setTransferTime(config.getInt("transfer-time-setting.time",5));
        config1.setEchoBlackListMessage(config.getBoolean("echoBlackListMessage",true));
        config1.setEchoProtectListMessage(config.getBoolean("echoProtectListMessage",true));
        config1.setAutoSave(config.getBoolean("auto-save-land.open",true));
        config1.setAutoSaveTime(config.getInt("auto-save-land.time",5));

        return config1;
    }

    public boolean isEnableTransferTime() {
        return enableTransferTime;
    }

    public void setEnableTransferTime(boolean enableTransferTime) {
        this.enableTransferTime = enableTransferTime;
    }

    public void setTransferCold(int transferCold) {
        this.transferCold = transferCold;
    }

    public void setTransferTime(int transferTime) {
        this.transferTime = transferTime;
    }

    public int getTransferCold() {
        return transferCold;
    }

    public int getTransferTime() {
        return transferTime;
    }

    private void setEchoBlackListMessage(boolean echoBlackListMessage) {
        this.echoBlackListMessage = echoBlackListMessage;
    }

    public boolean isEchoBlackListMessage() {
        return echoBlackListMessage;
    }

    private void setEchoProtectListMessage(boolean echoProtectListMessage) {
        this.echoProtectListMessage = echoProtectListMessage;
    }

    public boolean isEchoProtectListMessage() {
        return echoProtectListMessage;
    }

    private void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    private void setAutoSaveTime(int autoSaveTime) {
        this.autoSaveTime = autoSaveTime;
    }

    public int getAutoSaveTime() {
        return autoSaveTime;
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    private void setLandTool(Item landTool) {
        this.landTool = landTool;
    }

    public Item getLandTool() {
        return landTool;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getSellMoney() {
        return sellMoney;
    }



    public double getLandMoney() {
        return landMoney;
    }

    public double getSubLandMoney(){
        return subLandMoney;
    }

    public int getLoadEconomy() {
        return loadEconomy;
    }

    public ArrayList<String> getBlackList() {
        return blackList;
    }

    public int getMaxLand() {
        return maxLand;
    }

    public void setLandMoney(double landMoney) {
        this.landMoney = landMoney;
    }

    public void setMaxLand(int maxLand) {
        this.maxLand = maxLand;
    }

    public ArrayList<String> getProtectList() {
        return protectList;
    }

    public void setProtectList(ArrayList<String> protectList) {
        this.protectList = protectList;
    }

    public void setBlackList(ArrayList<String> blackList) {
        this.blackList = blackList;
    }
}
