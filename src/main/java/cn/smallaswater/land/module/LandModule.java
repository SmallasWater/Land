package cn.smallaswater.land.module;

import cn.nukkit.Nukkit;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.smallaswater.land.LandMainClass;
import cn.smallaswater.land.handle.KeyHandle;
import cn.smallaswater.land.lands.LandList;
import cn.smallaswater.land.lands.data.LandListener;
import cn.smallaswater.land.lands.data.LandListenerPn;
import cn.smallaswater.land.lands.data.LandOtherSet;
import cn.smallaswater.land.lands.data.sub.LandSubData;
import cn.smallaswater.land.lands.data.LandData;
import cn.smallaswater.land.lands.utils.InviteHandle;
import cn.smallaswater.land.lands.utils.LandConfig;
import cn.smallaswater.land.players.MemberSetting;
import cn.smallaswater.land.players.PlayerSetting;
import cn.smallaswater.land.tasks.AutoSaveLandTask;
import cn.smallaswater.land.tasks.ShowSellLandTask;
import cn.smallaswater.land.tasks.TransferColdTask;
import cn.smallaswater.land.utils.DataTool;
import cn.smallaswater.land.utils.Language;
import cn.smallaswater.land.utils.LoadMoney;
import cn.smallaswater.land.utils.Vector;
import cn.smallaswater.land.tasks.ShowParticleTask;
import cn.smallaswater.land.windows.WindowListener;

import cn.smallaswater.land.commands.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author 若水
 */
public class LandModule {


    private static LandModule module;

    private LandConfig config;

    private LandList landList;

    private Config languageConfig;
    private Language language;

    public ArrayList<KeyHandle> keyHanle = new ArrayList<>();


    public LinkedHashMap<LandData,Integer> showTime = new LinkedHashMap<>();

    public LinkedHashMap<String, LinkedList<Position>> pos = new LinkedHashMap<>();

    public LinkedHashMap<String, LinkedList<Position>> subPos = new LinkedHashMap<>();

    private LoadMoney money;

    public LinkedHashMap<Player,LandData> clickData = new LinkedHashMap<>();

    public LinkedHashMap<Player,String> clickPlayer = new LinkedHashMap<>();

    public LinkedHashMap<String,LinkedList<InviteHandle>> inviteLands = new LinkedHashMap<>();

    public void moduleDisable() {
       saveAll();
    }

    private void saveAll(){
        saveList();
        if(languageConfig != null){
            languageConfig.save();
        }
    }

    public void loadAll(){
        config = null;
        config = getConfig();
        landList = null;
        getList();

        getModuleInfo().saveResource("language/chs.yml");
        getModuleInfo().saveResource("language/eng.yml");
        if ("auto".equalsIgnoreCase(this.config.getLanguage())) {
            this.config.setLanguage(Server.getInstance().getConfig("settings.language", "eng"));
        }
        File languageFile = new File(getModuleInfo().getDataFolder() + "/language/" + this.config.getLanguage() + ".yml");
        if (!languageFile.exists()) {
            this.config.setLanguage("eng");
            languageFile = new File(getModuleInfo().getDataFolder() + "/language/eng.yml");
        }
        this.languageConfig = new Config(languageFile, Config.YAML);
        this.language = new Language(this.languageConfig);
        InputStream internalLanguageResource = LandMainClass.MAIN_CLASS.getResource("language/" + this.config.getLanguage() + ".yml");
        if (internalLanguageResource != null) {
            try {
                Config internalLanguageConfig = new Config(Config.YAML);
                internalLanguageConfig.load(internalLanguageResource);
                this.language.update(internalLanguageConfig);
                internalLanguageResource.close();
            }catch (IOException e) {
                LandMainClass.MAIN_CLASS.getLogger().info("Language update failed " + this.config.getLanguage());
            }
        }
        LandMainClass.MAIN_CLASS.getLogger().info("Language is set to: " + this.config.getLanguage());
    }

    public void saveList(){
        if(landList != null){
            landList.save();
        }
    }

    public void moduleRegister() {
        module = this;
        loadAll();

        money = new LoadMoney();
        if(config.getLoadEconomy() != -1){
            money.setMoney(config.getLoadEconomy());

            if(config.getLoadEconomy() == LoadMoney.ECONOMY_API){
                LandMainClass.MAIN_CLASS.getLogger().info("Land Economy enable:"+ TextFormat.GREEN+" EconomyAPI");
            }
            if(config.getLoadEconomy() == LoadMoney.MONEY){
                LandMainClass.MAIN_CLASS.getLogger().info("Land Economy enable:"+ TextFormat.GREEN+" Money");
            }
            if(config.getLoadEconomy() == LoadMoney.PLAYER_POINT){
                LandMainClass.MAIN_CLASS.getLogger().info("Land Economy enable:"+ TextFormat.GREEN+" PlayerPoint");
            }
            if(config.getLoadEconomy() == LoadMoney.LLAMA_ECONOMY){
                LandMainClass.MAIN_CLASS.getLogger().info("Land Economy enable:"+ TextFormat.GREEN+" LlamaEconomy");
            }
        }else{
            LandMainClass.MAIN_CLASS.getLogger().info("Land Economy enable:"+ TextFormat.GREEN+" auto");
        }
        if(money.getMoney() == -2){
            LandMainClass.MAIN_CLASS.getLogger().info("not to check economy");
            LandMainClass.MAIN_CLASS.getPluginLoader().disablePlugin(LandMainClass.MAIN_CLASS);
            return;
        }

        if(getConfig().isAutoSave()){
            Server.getInstance().getScheduler().scheduleDelayedRepeatingTask(getModuleInfo(),new AutoSaveLandTask(getModuleInfo()),(20 * config.getAutoSaveTime()) * 60,(20 * config.getAutoSaveTime()) * 60);
        }
        Server.getInstance().getScheduler().scheduleRepeatingTask(new TransferColdTask(getModuleInfo()),20);
        Server.getInstance().getScheduler().scheduleRepeatingTask(new ShowParticleTask(getModuleInfo()),20);
        //检测公示期
        Server.getInstance().getScheduler().scheduleRepeatingTask(new ShowSellLandTask(getModuleInfo()),40);

        getModuleInfo().getServer().getScheduler().scheduleRepeatingTask(new Task() {
            @Override
            public void onRun(int i) {
                for(Player player: Server.getInstance().getOnlinePlayers().values()){
                    if(inviteLands.containsKey(player.getName())){
                        LinkedList<InviteHandle> handles = inviteLands.get(player.getName());
                        for(InviteHandle handle:handles){
                            if(handle.getTime() > 0){
                                handle.setTime(handle.getTime() - 1);
                            }else{
                                handle.timeOut();
                            }
                        }
                    }
                }
            }
        },20) ;

        this.registerCommand();
        this.registerListener();
    }

    private void registerCommand(){
        getModuleInfo().getServer().getCommandMap().register("landCommand",new LandCommand("land","land main command"));
        getModuleInfo().getServer().getCommandMap().register("landAdminCommand",new AdminCommand("landadmin","land admin main command"));
    }


    private void registerListener(){
        if("PowerNukkit".equalsIgnoreCase(Nukkit.CODENAME)){
            LandMainClass.MAIN_CLASS.getLogger().info("enable PowerNukkit listener");
            LandMainClass.MAIN_CLASS.getServer().getPluginManager().registerEvents(new LandListenerPn(),LandMainClass.MAIN_CLASS);
        }
        LandMainClass.MAIN_CLASS.getServer().getPluginManager().registerEvents(new LandListener(),LandMainClass.MAIN_CLASS);
        LandMainClass.MAIN_CLASS.getServer().getPluginManager().registerEvents(new WindowListener(),LandMainClass.MAIN_CLASS);
    }



    public void invitePlayer(Player master,Player target,LandData data){
        if(!data.getMaster().equalsIgnoreCase(target.getName()) && !data.getMember().containsKey(target.getName())) {
            if (!inviteLands.containsKey(target.getName())) {
                inviteLands.put(target.getName(), new LinkedList<>());
            }
            LinkedList<InviteHandle> handles = inviteLands.get(target.getName());
            InviteHandle handle = new InviteHandle(master.getName(), target.getName(), data, 60);
            if (!handles.contains(handle)) {
                target.sendMessage(LandModule.getModule().getConfig().getTitle()+getLanguage().invitePlayerTarget.replace("%p%", master.getName()).replace("%name%", data.getLandName()));
                master.sendMessage(LandModule.getModule().getConfig().getTitle()+getLanguage().invitePlayerMaster
                        .replace("%p%", target.getName()).replace("%name%", data.getLandName()).replace("%time%", "60"));
                handles.add(handle);
            } else {
                master.sendMessage(LandModule.getModule().getConfig().getTitle()+getLanguage().invitePlayerExists.replace("%p%", target.getName()));
            }
        }else{
            master.sendMessage(LandModule.getModule().getConfig().getTitle()+getLanguage().invitePlayerInArray.replace("%p%",target.getName()).replace("%name%",data.getLandName()));
        }
    }


    public LoadMoney getMoney() {
        return money;
    }

    public LandMainClass getModuleInfo() {
        return LandMainClass.MAIN_CLASS;
    }

    public LandList getList(){
        if(landList == null) {
            Config config;
            LinkedList<LandData> data = new LinkedList<>();
            if(getDefaultFiles().length > 0) {
                int landId = 1;
                for (String name : getDefaultFiles()) {
                    try {
                        config = new Config(getModuleInfo().getDataFolder() + "/lands/" + name + ".yml", 2);
                        Map<String,Object> m = config.getAll();
                        if (m != null) {
                            LandData data1 = getDataByMap(m);
                            if (data1 != null) {
                                if(data1.getLandId() == 0){
                                    data1.setLandId(landId);
                                }
                                data1.setConfig(config);
                                if (m.containsKey("subLand")) {
                                    List list = (List) m.get("subLand");
                                    data1.setSubData(getSubDates(list));
                                } else {
                                    data1.setSubData(new LinkedList<>());
                                }
                                data.add(data1);
                            } else {
                                getModuleInfo().getLogger().warning("" + name + "land load error， because: yaml error");

                            }
                        } else {
                            getModuleInfo().getLogger().info("check null file" + name + " deleting");
                            File file = new File(getModuleInfo().getDataFolder() + "/lands/" + name + ".yml");
                            if (!file.delete()) {
                                getModuleInfo().getLogger().warning("null file" + name + " delete  error");
                            } else {
                                getModuleInfo().getLogger().info("null file" + name + " delete success");
                            }
                        }
                    }catch (Exception e){
                        getModuleInfo().getLogger().info("check error file" + name.trim() + " deleting");
                        File file = new File(getModuleInfo().getDataFolder() + "/lands/" + name + ".yml");
                        if (!file.delete()) {
                            getModuleInfo().getLogger().warning("error file" + name + "  delete  error");
                        } else {
                            getModuleInfo().getLogger().info("error file" + name + " delete success");
                        }
                        landId++;
                    }
                }
            }


            landList = new LandList(sqrtLand(data));
        }
        return landList;

    }
    public static LinkedList<LandData> sqrtLand(LinkedList<LandData> data){
        LinkedList<LandData> landClass = new LinkedList<>();
        LinkedHashMap<LandData,Integer> map = new LinkedHashMap<>();
        for(LandData c:data){
            map.put(c,c.getLandId());
        }
        List<Map.Entry<LandData,Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        for(Map.Entry<LandData,Integer> mapping:list){
            landClass.add(mapping.getKey());
        }
        return landClass;
    }

    private LandData getDataByMap(Map m){
        try {
            Vector vector = Vector.getVectorByMap((Map) m.get("vector"));
            if(vector == null){
                return null;
            }
            Position position = DataTool.getPositionByMap((Map) m.get("transfer"));
            if(position == null){
                return null;
            }
            LandData data = new LandData(m.containsKey("landId")?Integer.parseInt(m.get("landId").toString()):0
                    ,m.get("landName").toString(),
                    m.get("master").toString(),
                    vector,
                    new MemberSetting((Map) m.get("member")),
                    new PlayerSetting((Map) m.get("defaultSetting")),
                    m.get("joinMessage").toString(),
                    m.get("quitMessage").toString(),
                    position);
            LandOtherSet set = new LandOtherSet();
            if(m.containsKey("otherLandSetting")){
                set = LandOtherSet.getLandOtherSetByMap((Map) m.get("otherLandSetting"));
            }
            data.setLandOtherSet(set);
            data.setSellMessage(m.containsKey("sellMessage") ? m.get("sellMessage").toString():"land selling~~");
            data.setSell(m.containsKey("isSell") && (boolean) m.get("isSell"));
            data.setSellDay((m.containsKey("sellDay") ? (String) m.get("sellDay") : ""));
            data.setMoney(Double.parseDouble((m.containsKey("money") ? m.get("money").toString() : "-1.0")));
            data.setSellMoney(Double.parseDouble((m.containsKey("sellMoney") ? m.get("sellMoney").toString() : "-1.0")));
            data.setCreateTime(m.containsKey("createTime")?DataTool.getDate(m.get("createTime").toString()):new Date());
            return data;
        }catch (Exception e){
            return null;
        }
    }

    private LinkedList<LandSubData> getSubDates(List list){
        LinkedList<LandSubData> subData = new LinkedList<>();
        if(list.size() > 0){
            LandSubData subData1;
            String last;
            Map m;
            int landId = 1;
            for(Object o:list){
                m = (Map) o;
                last = m.get("mastLandData").toString();
                LandData data = getDataByMap(m);
                if(data != null) {
                    if(m.containsKey("landId")){
                        landId = Integer.parseInt(m.get("landId").toString());
                    }
                    subData1 = LandSubData.getSubDataByLand(landId,last,data);
                    subData1.setSellPlayer(m.containsKey("sellPlayer") && (boolean) m.get("sellPlayer"));
                    subData.add(subData1);
                }
                landId++;
            }
        }
        return subData;
    }

    public LandConfig getConfig() {
        if(config == null){
            config = LandConfig.getLandConfig();
        }
        return config;
    }


    public static LandModule getModule() {
        return module;
    }

    private String[] getDefaultFiles() {
        List<String> names = new ArrayList<>();
        File files = new File(getModuleInfo().getDataFolder()+ "/lands");
        if(files.isDirectory()){
            File[] filesArray = files.listFiles();
            if(filesArray != null){
                if(filesArray.length > 0){
                    for(File file : filesArray){
                        String name = file.getName();
                        if(name.split("\\.").length > 1) {
                            names.add(file.getName().substring(0, file.getName().lastIndexOf(".")));
                        }else{
                            if(file.delete()){
                                getModuleInfo().getLogger().info("check error file "+file.getName()+" has been deleted");
                            }
                        }
                    }
                }
            }
        }
        return names.toArray(new String[0]);
    }

    public Language getLanguage() {
        if (this.language == null) {
            this.language = new Language(this.languageConfig);
        }
        return this.language;
    }

}
