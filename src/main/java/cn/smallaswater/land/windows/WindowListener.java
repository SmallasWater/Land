package cn.smallaswater.land.windows;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;
import cn.smallaswater.land.LandMainClass;
import cn.smallaswater.land.event.player.*;
import cn.smallaswater.land.handle.TimeHandle;
import cn.smallaswater.land.lands.data.LandData;
import cn.smallaswater.land.lands.data.LandOtherSet;
import cn.smallaswater.land.lands.data.sub.LandSubData;
import cn.smallaswater.land.lands.settings.OtherLandSetting;
import cn.smallaswater.land.lands.utils.ScreenSetting;
import cn.smallaswater.land.manager.KeyHandleManager;
import cn.smallaswater.land.manager.TimerHandleManager;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.players.PlayerSetting;
import cn.smallaswater.land.utils.DataTool;
import cn.smallaswater.land.utils.Language;
import cn.smallaswater.land.lands.settings.LandSetting;

import java.util.*;


/**
 * @author 若水
 */
public class WindowListener implements Listener {

    private final LinkedHashMap<Player,Integer> type = new LinkedHashMap<>();

    private final LinkedHashMap<Player, LandData> lastData = new LinkedHashMap<>();

    static LinkedHashMap<String, ScreenSetting> screenSetting = new LinkedHashMap<>();

    private static LinkedList<Player> screenKey = new LinkedList<>();

    static LinkedHashMap<Player, LinkedList<LandData>> screenLands = new LinkedHashMap<>();

    @EventHandler
    public void onWindow(PlayerFormRespondedEvent event){
        if (event.getResponse() != null) {
            Player p = event.getPlayer();
            int formId = event.getFormID();
            if(formId == CreateWindow.MENU
                    || formId == CreateWindow.SETTING
                    || formId == CreateWindow.CHOSE
                    || formId == CreateWindow.LIST
                    || formId == CreateWindow.SETTING_LAND
                    || formId == CreateWindow.MEMBERS
                    || formId == CreateWindow.SET_LAND
                    || formId == CreateWindow.BUY_LAND_MENU
                    || formId == CreateWindow.KICK_MENU
                    || formId == CreateWindow.GIVE_MENU
                    || formId == CreateWindow.SELL_LANDS
                    || formId == CreateWindow.INVITE_PLAYER
                    || formId == CreateWindow.JOIN_QUIT_TEXT
                    || formId == CreateWindow.LIST_SUB
                    || formId == CreateWindow.IS_SELL_MENU
                    || formId == CreateWindow.SCREEN_MENU
                    || formId == CreateWindow.LAND_ALL_SETTING
                    || formId == CreateWindow.SCREEN_LIST
                    || formId == CreateWindow.LAND_ALL_DEFAULT_SETTING) {
                if (event.getWindow() instanceof FormWindowSimple) {
                    onListenerSimpleWindow(p, (FormWindowSimple) event.getWindow(), formId, event.getWindow().wasClosed());
                }
                if (event.getWindow() instanceof FormWindowCustom) {
                    onListenerCustomWindow(p, (FormWindowCustom) event.getWindow(), formId, event.getWindow().wasClosed());
                }
                if (event.getWindow() instanceof FormWindowModal) {
                    onListenerModuleWindow(p, (FormWindowModal) event.getWindow(), formId, event.getWindow().wasClosed());
                }
            }
        }
    }
    private void givePlayerLand(String player,LandData data,Player p){
        Language language = LandModule.getModule().getLanguage();
        PlayerGiveLandEvent event = new PlayerGiveLandEvent(p,data,player);
        Server.getInstance().getPluginManager().callEvent(event);
        if(event.isCancelled()){
            return;
        }
        player = event.getMaster();
        int lands = DataTool.getLands(player).size();
        int max =  LandModule.getModule().getConfig().getMaxLand();
        if(data instanceof LandSubData){
            lands = ((LandSubData) data).getMasterData().getSubData().size();
            max = LandModule.getModule().getConfig().getSubMax();
            if(((LandSubData) data).isSellPlayer()){
                p.sendMessage(LandModule.getModule().getConfig().getTitle()+language.notHavePermission);
                return;
            }
        }
        if (lands >= max) {
            p.sendMessage(LandModule.getModule().getConfig().getTitle()+language.playerLandMax.replace("%count%", max + ""));
        } else {
            Player player1 = Server.getInstance().getPlayer(player);
            if(data.getMaster().equalsIgnoreCase(player)){
                p.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().
                        invitePlayerInArray.replace("%name%",data.getLandName()).replace("%p%",player));
                return;
            }
            if(player1 != null){
                PlayerGetLandEvent event1 = new PlayerGetLandEvent(player1,data,p.getName());
                Server.getInstance().getPluginManager().callEvent(event);
                if(event1.isCancelled()){
                    return;
                }
            }

            if (data.getMember().containsKey(player)) {
                data.removeMember(player);
            }
            p.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().givePlayerLandTarget
                    .replace("%p%",player).replace("%name%",data.getLandName()));

            data.setMaster(player);
        }
    }

    private void kickPlayerLand(String player,boolean isTrue,LandData data,Player p){
        if(player != null) {
            Language language = LandModule.getModule().getLanguage();
            if (isTrue) {
                PlayerKickMemberLandEvent event = new PlayerKickMemberLandEvent(p,player,data);
                Server.getInstance().getPluginManager().callEvent(event);
                if(event.isCancelled()){
                    return;
                }
                player = event.getMember();
                if(data.getMember().containsKey(player)) {
                    data.removeMember(player);

                    if(!(data instanceof LandSubData)){
                        for(LandSubData data1:data.getSubData()){
                            if(!data1.isSellPlayer()) {
                                if (data1.getMaster().equalsIgnoreCase(player)) {
                                    data1.setMaster(p.getName());
                                } else {
                                    if (data1.getMember().containsKey(player)) {
                                        data1.removeMember(player);
                                    }
                                }
                            }
                        }
                    }

                }else{
                    return;
                }
                Player player1 = Server.getInstance().getPlayer(player);
                if (player1 != null) {
                    player1.sendMessage(LandModule.getModule().getConfig().getTitle()+language.kickLandMessage.replace("%p%", p.getName()).replace("%name%", data.getLandName()));
                }
            }

        }
    }

    private void onListenerModuleWindow(Player p, FormWindowModal modal, int formId, boolean wasClose) {
        LandData data = null;
        if (LandModule.getModule().clickData.containsKey(p)) {
            data = LandModule.getModule().clickData.get(p);
        }
        Language language = LandModule.getModule().getLanguage();
        if (data != null) {
            if (existsData(p, data, language)) {
                return;
            }
            String player = LandModule.getModule().clickPlayer.get(p);
            LandModule.getModule().clickPlayer.remove(p);
            if (!wasClose) {
                switch(formId){
                    case CreateWindow.GIVE_MENU:
                        if (modal.getResponse().getClickedButtonText().equals(LandModule.getModule().getLanguage().choseTrue)) {
                            givePlayerLand(player,data,p);
                            return;
                        } else {
                            p.sendMessage(LandModule.getModule().getConfig().getTitle() + language.cancelChose);
                        }
                        break;

                    case CreateWindow.CHOSE:
                        boolean isMaster = data.getMaster().equalsIgnoreCase(p.getName());
                        if(data instanceof LandSubData) {
                            isMaster = ((LandSubData) data).getMasterData().getMaster().equalsIgnoreCase(p.getName());
                            if(data.getMaster().equalsIgnoreCase(p.getName())){
                                isMaster = true;
                            }
                        }


                        if(isMaster){
                            if(modal.getResponse().getClickedButtonText().equalsIgnoreCase(language.choseTrue)){
                                double get = DataTool.getGettingMoney(data);
                                PlayerSellLandEvent event = new PlayerSellLandEvent(p,data,get);
                                Server.getInstance().getPluginManager().callEvent(event);
                                if(event.isCancelled()){
                                    return;
                                }
                                get = event.getMoney();
                                LandModule.getModule().getMoney().addMoney(p.getName(),get);
                                Iterator<String> modei = new ArrayList<>(data.getMember().keySet()).iterator();
                                String mode;
                                while (modei.hasNext()){
                                    mode = modei.next();
                                    Player player1 = Server.getInstance().getPlayer(mode);
                                    if(player1 != null){
                                        player1.sendMessage(LandModule.getModule().getConfig().getTitle()+language.kickLandMessage.replace("%p%",p.getName()).replace("%name%",data.getLandName()));
                                    }
                                    data.removeMember(mode);
                                }

                                p.sendMessage(LandModule.getModule().getConfig().getTitle()+language.sellLandMessage.replace("%name%",data.getLandName()).replace("%money%",get+""));
                                if(data instanceof LandSubData){
                                    if(data.getMaster().equalsIgnoreCase(((LandSubData) data).getMasterData().getMaster())){
                                        data.close();
                                    }else{
                                        data.setMaster(((LandSubData) data).getMasterData().getMaster());
                                    }
                                }else {
                                    data.close();
                                }
                            }else{
                                p.sendMessage(LandModule.getModule().getConfig().getTitle() + language.cancelChose);
                            }
                        }else{
                            if(modal.getResponse().getClickedButtonText().equalsIgnoreCase(language.choseTrue)){
                                data.removeMember(p.getName());
                            }
                        }
                        break;
                    case CreateWindow.KICK_MENU:
                        kickPlayerLand(player,modal.getResponse().getClickedButtonText().equalsIgnoreCase(language.choseTrue),data,p);
                        break;
                    default: break;
                }
            }
        } else {
            p.sendMessage(LandModule.getModule().getConfig().getTitle() + language.dataNotExists.replace("%name%", ""));
        }
    }

    private void onBackSimpleWindow(Player p,int formId){
        if(formId == CreateWindow.SCREEN_LIST){
            CreateWindow.sendScreenMenu(p);
            return;
        }
        LandData data = lastData.get(p);
        LandData newData = LandModule.getModule().clickData.get(p);

        if(data != null && newData != null) {
            if (LandModule.getModule().getList().contains(data)) {
                if (formId == CreateWindow.SETTING) {
                    if(screenKey.contains(p)){
                        CreateWindow.sendScreenList(p);
                        screenKey.remove(p);
                        LandModule.getModule().clickData.remove(p);
                        return;
                    }
                    if(type.containsKey(p) && type.get(p) == 4){
                        type.remove(p);
                        CreateWindow.sendLandDataList(p);
                        return;
                    }
                    LandModule.getModule().clickData.put(p,data);
                    if (newData instanceof LandSubData) {
                        CreateWindow.sendSubLandList(p);
                        return;
                    }
                    CreateWindow.sendMenu(p);
                } else if (formId == CreateWindow.MEMBERS ||
                        formId == CreateWindow.INVITE_PLAYER ||
                        formId == CreateWindow.LIST_SUB ||
                        formId == CreateWindow.LAND_ALL_SETTING) {
                    LandModule.getModule().clickData.put(p,data);
                    CreateWindow.sendSetLandMenu(p);
                } else if (formId == CreateWindow.SET_LAND) {
                    CreateWindow.sendSettingLandMenu(p);
                }else  if(formId == CreateWindow.BUY_LAND_MENU){
                    CreateWindow.sendSellingLandDataList(p);
                }
            }
        }else{
            if(formId == CreateWindow.BUY_LAND_MENU){
                CreateWindow.sendSellingLandDataList(p);
            }
        }
    }

    private void isLandSell(String message,String money,Player p,LandData data){
        Language language = LandModule.getModule().getLanguage();
        double m = -1.0;
        if("".equalsIgnoreCase(money)){
            p.sendMessage(LandModule.getModule().getConfig().getTitle() + language.integerError);
            return;
        }
        try {
            m = Double.parseDouble(money);
        }catch (Exception ignore){}
        if(m < 0 && m != -1){
            p.sendMessage(LandModule.getModule().getConfig().getTitle() + language.integerError);
            return;
        }
        if(data.isSell()){
            data.setSell(false);
            data.setSellDay("");
            p.sendMessage(LandModule.getModule().getConfig().getTitle()+language.setSellFalseText.replace("%name%",data.getLandName()));
        }else{
            if(!"".equalsIgnoreCase(message)){
                data.setSellMessage(message);
            }
            data.setSell(true);
            data.setSellDay(DataTool.getDateToString(new Date()));
            if(m != -1){
                data.setSellMoney(m);
                p.sendMessage(LandModule.getModule().getConfig().getTitle()+language.setSellMoneyMessage.replace("%name%",data.getLandName())
                        .replace("%money%",String.format("%.2f",m).replace("%name%",data.getLandName())));
            }
            p.sendMessage(LandModule.getModule().getConfig().getTitle()+language.setSellText.replace("%name%",data.getLandName())
                    .replace("%day%",LandModule.getModule().getConfig().getShowTime()+""));
        }
    }

    private void onListenerCustomWindow(Player p, FormWindowCustom custom, int formId, boolean wasClose){
        Language language = LandModule.getModule().getLanguage();
        if(!wasClose){
            if(formId == CreateWindow.SCREEN_MENU) {
                String type = custom.getResponse().getDropdownResponse(1).getElementContent();
                boolean isSort = custom.getResponse().getToggleResponse(2);
                boolean showSell = custom.getResponse().getToggleResponse(3);
                String text = custom.getResponse().getInputResponse(4);
                screenSetting.put(p.getName(), new ScreenSetting(type, isSort, showSell, text));
                CreateWindow.sendScreenList(p);
                return;
            }
            LandData data = null;
            if(LandModule.getModule().clickData.containsKey(p)){
                data = LandModule.getModule().clickData.get(p);
            }
            if (data != null){
                if (existsData(p, data, language)) {
                    return;
                }
                switch (formId){
                    case CreateWindow.LAND_ALL_DEFAULT_SETTING:
                        int i = 0;
                        LandOtherSet set = data.getLandOtherSet();
                        for (OtherLandSetting setting : OtherLandSetting.values()) {
                            set.setOpen(setting,custom.getResponse().getToggleResponse(i));
                            i++;
                        }
                        p.sendMessage(LandModule.getModule().getConfig().getTitle()+language.saveSetting.replace("%p%", "领地"));
                        break;
                    case CreateWindow.IS_SELL_MENU:
                        String message = custom.getResponse().getInputResponse(1);
                        String money = custom.getResponse().getInputResponse(0);
                        isLandSell(message,money,p,data);
                        break;
                    case CreateWindow.JOIN_QUIT_TEXT:
                        data.setJoinMessage(custom.getResponse().getInputResponse(0));
                        data.setQuitMessage(custom.getResponse().getInputResponse(1));
                        p.sendMessage(LandModule.getModule().getConfig().getTitle() + language.saveText.replace("%name%", data.getLandName()));
                        return;
                    case CreateWindow.SETTING_LAND:
                        String playerName = LandModule.getModule().clickPlayer.get(p);
                        LandModule.getModule().clickPlayer.remove(p);
                        PlayerSetting playerSetting = new PlayerSetting(new LinkedHashMap<>());
                        i = 1;
                        for (LandSetting setting : LandSetting.values()) {
                            playerSetting.setSetting(setting.getName(),custom.getResponse().getToggleResponse(i));
                            i++;
                        }
                        PlayerLandSettingEvent event = new PlayerLandSettingEvent(p,playerSetting,data,playerName);
                        Server.getInstance().getPluginManager().callEvent(event);
                        if(event.isCancelled()){
                            return;
                        }
                        playerSetting = event.getPlayerSetting();
                        playerName = event.getMember();
                        if (playerName != null) {
                            data.setPlayerSetting(playerName,playerSetting);
                            p.sendMessage(LandModule.getModule().getConfig().getTitle()+language.saveSetting.replace("%p%", playerName));
                        } else {
                            data.setDefaultSetting(playerSetting);
                            p.sendMessage(LandModule.getModule().getConfig().getTitle()+language.saveSetting.replace("%p%", language.other));
                        }
                        break;
                    default:break;
                }
            }else{

                p.sendMessage(LandModule.getModule().getConfig().getTitle() + language.dataNotExists.replace("%name%", ""));
            }
        }else{
            CreateWindow.sendSetLandMenu(p);
        }
    }

    private void transfer(Player player,LandData data){
        if(!data.hasPermission(player.getName(), LandSetting.TRANSFER)){
            player.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().notHavePermission.replace("%title%",LandModule.getModule().getConfig().getTitle()));
        }else{
            if(LandModule.getModule().getConfig().isEnableTransferTime()) {
                TimeHandle handle = TimerHandleManager.getTimeHandle(player);
                if (handle.hasCold("transferCold")) {
                    player.sendMessage(LandModule.getModule().getConfig().getTitle() + LandModule.getModule().getLanguage().transferCold.replace("%time%", handle.getCold("transferCold") + ""));
                    return;
                }
                KeyHandleManager.addKey(player, "transfer");
                handle.addTimer("transferCold", LandModule.getModule().getConfig().getTransferCold());
                player.sendMessage(LandModule.getModule().getConfig().getTitle() + LandModule.getModule().getLanguage().transferTime.replace("%time%", LandModule.getModule().getConfig().getTransferTime() + ""));
                LandMainClass.MAIN_CLASS.getServer().getScheduler().scheduleDelayedTask(LandMainClass.MAIN_CLASS, () -> {
                    if (!player.isOnline()) {
                        return;
                    }
                    if (!KeyHandleManager.isKey(player, "transferClose")) {
                        KeyHandleManager.removeKey(player, "transfer");
                        player.teleport(data.getTransfer());
                    } else {
                        KeyHandleManager.removeKey(player, "transferClose");
                        KeyHandleManager.removeKey(player, "transfer");
                    }
                }, LandModule.getModule().getConfig().getTransferTime() * 20);

            }else{
                player.teleport(data.getTransfer());
            }
        }
    }
    private boolean sendFrom(int id,Player p,LandData data,Language language){
        switch (id){
            case CreateWindow.INVITE_BUTTON:
                CreateWindow.sendInvitePlayerMenu(p);
                return true;
            case CreateWindow.KICK_BUTTON:
                type.put(p, CreateWindow.KICK_BUTTON);
                CreateWindow.sendMemberList(p);
                return true;
            case CreateWindow.SET_PLAYER_BUTTON:
                type.put(p, CreateWindow.SET_LAND_ALL_SETTING);
                CreateWindow.sendLandAllSettingMenu(p);
                return true;
            case CreateWindow.SELL_LAND_BUTTON:
                CreateWindow.sendQuitOrSellMenu(p);
                return true;
            case CreateWindow.GIVE_LAND_BUTTON:
                CreateWindow.sendInvitePlayerMenu(p);
                type.put(p, CreateWindow.GIVE_LAND_BUTTON);
                LandModule.getModule().clickPlayer.remove(p);
                return true;
            case CreateWindow.SET_TRANSFER_BUTTON:
                LandData name = DataTool.getPlayerTouchArea(p.getPosition());
                if (name != null) {
                    if (name.equals(data)) {
                        data.setTransfer(p.getPosition());
                        p.sendMessage(LandModule.getModule().getConfig().getTitle() + language.saveTransfer.replace("%name%", data.getLandName()));
                        return true;
                    }
                }
                p.sendMessage(LandModule.getModule().getConfig().getTitle() + language.saveTransferError.replace("%name%", data.getLandName()));
                break;
            case CreateWindow.SET_TEXT_BUTTON:
                CreateWindow.onJoinQuitTextMenu(p);
                break;
            case CreateWindow.SHOW_PARTICLE_BUTTON:
                LandModule.getModule().showTime.put(data,LandModule.getModule().getConfig().getTime());
                return true;
            case CreateWindow.SET_SUB_LAND_BUTTON:
                if(data instanceof LandSubData){
                    break;
                }
                CreateWindow.sendSubLandList(p);
                return true;

            default:break;
        }
        return false;
    }

    private void onBuyLand(int id,Player p,LandData data,Language language,FormWindowSimple simple,int formId){
        if(sendBack(p, simple, formId, language)){
            return;
        }
        if(id == 0){
            transfer(p,data);
        }
        if(id == 1){
            //购买
            if(p.getName().equalsIgnoreCase(data.getMaster())){
                p.sendMessage(LandModule.getModule().getConfig().getTitle()+language.buyLandMe);
                return;
            }
            if(data.isSell()){
                int lands = DataTool.getLands(p.getName()).size();
                int max =  LandModule.getModule().getConfig().getMaxLand();
                if(data instanceof LandSubData){
                    lands = ((LandSubData) data).getMasterData().getSubData().size();
                    max = LandModule.getModule().getConfig().getSubMax();
                }
                if (lands >= max) {
                    p.sendMessage(LandModule.getModule().getConfig().getTitle()+language.playerLandMax.replace("%count%", max + ""));
                    return;
                }

                double money =  DataTool.getLandMoney(data.getVector(),(data instanceof LandSubData));
                if(data.getSellMoney() != -1){
                    money = data.getSellMoney();
                }
                if(LandModule.getModule().getMoney().myMoney(p) > money){
                    PlayerGetLandEvent event = new PlayerGetLandEvent(p,data,data.getMaster());
                    Server.getInstance().getPluginManager().callEvent(event);
                    if(event.isCancelled()){
                        return;
                    }
                    LandModule.getModule().getMoney().reduceMoney(p,money);
                    LandModule.getModule().getMoney().addMoney(data.getMaster(),money);
                    Player master = Server.getInstance().getPlayer(data.getMaster());
                    if(master != null){
                        master.sendMessage(LandModule.getModule().getConfig().getTitle()+language.buyLandMaster.replace("%player%",p.getName()).replace("%name%",data.getLandName()).replace("%money%",String.format("%.2f",money)));
                    }
                    p.sendMessage(LandModule.getModule().getConfig().getTitle()+language.buyLandTrue.replace("%name%",data.getLandName()).replace("%money%",String.format("%.2f",money)));
                    if(data instanceof LandSubData){
                        ((LandSubData) data).setSellPlayer(!data.getMaster().equalsIgnoreCase(((LandSubData) data).getMasterData().getMaster()));
                    }
                    data.setMaster(p.getName());
                    data.setSell(false);
                }else{
                    p.sendMessage(LandModule.getModule().getConfig().getTitle()+language.buyLandNotHaveMoney.replace("%name%",data.getLandName()).replace("%money%",String.format("%.2f",money)));
                }

            }else{
                p.sendMessage(LandModule.getModule().getConfig().getTitle()+language.buyLandFalse.replace("%name%",data.getLandName()));
            }

        }
    }


    private void onListenerSimpleWindowSetMenu(Player p,LandData data,Language language,FormWindowSimple simple,int formId){
        int id = simple.getResponse().getClickedButtonId();
        switch (formId){
            case CreateWindow.SET_LAND:
                if(sendBack(p, simple, formId, language)){
                    return;
                }
                if(sendFrom(id,p,data,language)){
                    return;
                }

                if(simple.getResponse().getClickedButton().getText().equalsIgnoreCase(language.inSellButton) ||
                        simple.getResponse().getClickedButton().getText().equalsIgnoreCase(language.inSellFalseButton) ){
                    if(data instanceof LandSubData){
                        if(!data.getMaster().equalsIgnoreCase(((LandSubData) data).getMasterData().getMaster())){
                            p.sendMessage(language.notHavePermission);
                            return;
                        }
                    }
                    if(data.isSell()){
                        data.setSell(false);
                        data.setSellDay("");
                        p.sendMessage(LandModule.getModule().getConfig().getTitle()+language.setSellFalseText.replace("%name%",data.getLandName()));
                    }else{
                        CreateWindow.sendSellSetting(p);
                    }
                    return;
                }
                break;
            case CreateWindow.BUY_LAND_MENU:
                onBuyLand(id,p,data,language,simple,formId);
                break;
            case CreateWindow.SETTING:
                if (sendBack(p, simple, formId, language)) {
                    return;
                }
                if(id == 0){
                    transfer(p,data);
                }
                if (data.getMaster().equalsIgnoreCase(p.getName())){
                    if(id == 1){
                        CreateWindow.sendSetLandMenu(p);
                    }
                    return;
                }
                if(data.getMember().containsKey(p.getName())){
                    if(id == 1){
                        CreateWindow.sendQuitOrSellMenu(p);
                    }
                    if(id == 2){
                        CreateWindow.sendSetLandMenu(p);
                    }
                }else{
                    if(id == 1){
                        CreateWindow.sendSetLandMenu(p);
                    }
                }
                break;
            default:break;
        }
    }

    private void onListenerSimpleWindowSendMenu(Player p,LandData data,Language language,FormWindowSimple simple,int formId){
        if (formId == CreateWindow.INVITE_PLAYER) {
            if(sendBack(p, simple, formId, language)){
                return;
            }
            String player = simple.getResponse().getClickedButton().getText();
            Player player1 = Server.getInstance().getPlayer(player);
            if (type.containsKey(p)) {
                if (type.get(p) == CreateWindow.GIVE_LAND_BUTTON) {
                    type.remove(p);
                    if (player1 != null) {
                        LandModule.getModule().clickPlayer.put(p, player1.getName());
                        CreateWindow.sendGiveLandMenu(p, player1.getName());
                        return;
                    } else {
                        p.sendMessage(LandModule.getModule().getConfig().getTitle() + language.playerOffOnline.replace("%p%", player));
                    }
                    type.remove(p);
                    return;
                }
            }
            if (existsData(p, data, language)) {
                return;
            }
            if (player1 != null) {
                PlayerInviteMemberEvent event = new PlayerInviteMemberEvent(p,data,player1);
                Server.getInstance().getPluginManager().callEvent(event);
                if(event.isCancelled()){
                    return;
                }
                LandModule.getModule().invitePlayer(p, player1, data);
            } else {
                p.sendMessage(LandModule.getModule().getConfig().getTitle() + language.playerOffOnline.replace("%p%", player));
            }
        }
        if(formId == CreateWindow.LAND_ALL_SETTING){
            if(sendBack(p, simple, formId, language)){
                return;
            }
            switch (simple.getResponse().getClickedButtonId()){
                case 0:
                    type.put(p, CreateWindow.SET_PLAYER_BUTTON);
                    CreateWindow.sendMemberList(p);
                    break;
                case 1:
                    CreateWindow.sendLandOtherSettingMenu(p);
                    break;
                case 2:
                    CreateWindow.sendLandSettingMenu(p);
                    break;
                default:break;
            }
        }
        if (formId == CreateWindow.MEMBERS) {
            if(sendBack(p, simple, formId, language)){
                return;
            }
            int type = this.type.get(p);
            this.type.remove(p);
            String s = simple.getResponse().getClickedButton().getText();

            if(existsData(p,data,language)){
                return;
            }
            if (data.getMember().containsKey(s)) {
                if (type == CreateWindow.KICK_BUTTON) {
                    //踢出玩家
                    LandModule.getModule().clickPlayer.put(p, s);
                    CreateWindow.sendKickMenu(p, s);
                } else if (type == CreateWindow.SET_PLAYER_BUTTON) {
                    //设置权限
                    LandModule.getModule().clickPlayer.put(p, s);
                    CreateWindow.sendLandSettingMenu(p, s);
                }

            }
        }

    }

    private void onFromSimple(Language language, Player p, FormWindowSimple simple, int formId){
        LandData data;
        if(formId == CreateWindow.MENU){
            try {
                data = DataTool.getPlayerAllLand(p).get(simple.getResponse().getClickedButtonId());
                if(data instanceof LandSubData) {
                    lastData.put(p, ((LandSubData) data).getMasterData());
                }else{
                    lastData.put(p, data);
                }
                putDefaultClickData(p, data, language);
                return;
            }catch (Exception ignore){}

        }
        if(formId == CreateWindow.SCREEN_LIST){
            try {
                if(sendBack(p,simple,formId,language)){
                    return;
                }
                if(screenLands.containsKey(p)){
                    LinkedList<LandData> data1 = screenLands.get(p);
                    data = data1.get(simple.getResponse().getClickedButtonId());
                    screenKey.add(p);
                    if(data instanceof LandSubData) {
                        lastData.put(p, ((LandSubData) data).getMasterData());
                    }else{
                        lastData.put(p, data);
                    }
                    putDefaultClickData(p, data, language);
                }

                return;
            }catch (Exception ignore){
                screenKey.remove(p);
            }
        }
        if(formId == CreateWindow.LIST_SUB){
            if(sendBack(p,simple,formId,language)){
                return;
            }
            LandSubData data1;
            data = LandModule.getModule().clickData.get(p);
            if(data instanceof LandSubData){
                data = ((LandSubData) data).getMasterData();
            }

            try {
                data1 = data.getSubData().get(simple.getResponse().getClickedButtonId());
                LandModule.getModule().clickData.put(p,data1);
                CreateWindow.sendSettingLandMenu(p);
                return;
            }catch (Exception e){

                p.sendMessage(LandModule.getModule().getConfig().getTitle() + language.dataNotExists.replace("%name%", ""));
                return;
            }
        }
        if(formId == CreateWindow.SELL_LANDS){
            try {
                data = DataTool.getSellLandAll().get(simple.getResponse().getClickedButtonId());
                LandModule.getModule().clickData.put(p, data);
                CreateWindow.sendSellLandSetting(p);
                return;
            }catch (Exception e){
                p.sendMessage(LandModule.getModule().getConfig().getTitle() + language.dataNotExists.replace("%name%", ""));
            }

        }
        if(formId == CreateWindow.LIST){
            data = CreateWindow.getPageLandDataList(p).get(simple.getResponse().getClickedButtonId());
            lastData.put(p, data);
            type.put(p,4);
            putDefaultClickData(p, data, language);
            return;
        }
        p.sendMessage(LandModule.getModule().getConfig().getTitle() + language.dataNotExists.replace("%name%", ""));
    }

    private void onListenerSimpleWindow(Player p, FormWindowSimple simple, int formId, boolean wasClose) {
        LandData data = null;
        if(LandModule.getModule().clickData.containsKey(p)){
            data = LandModule.getModule().clickData.get(p);
        }
        Language language = LandModule.getModule().getLanguage();
        if(!wasClose) {
            if (data != null && formId != CreateWindow.MENU && formId != CreateWindow.LIST && formId != CreateWindow.LIST_SUB && formId != CreateWindow.SELL_LANDS ) {
                if (existsData(p, data, language)) {
                    return;
                }
                onListenerSimpleWindowSendMenu(p,data,language,simple,formId);
                onListenerSimpleWindowSetMenu(p,data,language,simple,formId);
            } else {
                onFromSimple(language,p,simple,formId);
            }
        }
    }

    private boolean sendBack(Player p, FormWindowSimple simple, int formId, Language language) {
        if(simple.getResponse().getClickedButton().getImage() != null) {
            if (simple.getResponse().getClickedButton().getImage().getData()
                    .equalsIgnoreCase(CreateWindow.backImage) && simple.getResponse().getClickedButton().getText().equalsIgnoreCase(language.backButton)) {
                onBackSimpleWindow(p, formId);
                return true;
            }
        }

        return false;
    }

    private void putDefaultClickData(Player p, LandData data, Language language) {
        if (existsData(p, data, language)) {
            return;
        }
        LandModule.getModule().clickData.put(p,data);
        CreateWindow.sendSettingLandMenu(p);
    }

    private boolean existsData(Player p, LandData data, Language language) {
        if(data instanceof LandSubData){
            if(((LandSubData) data).getMasterData() == null ||
                    !((LandSubData) data).getMasterData().contains((LandSubData) data)){
                p.sendMessage(LandModule.getModule().getConfig().getTitle() + language.dataNotExists.replace("%name%", data.getLandName()));
                return true;
            }
        }else {
            if (!LandModule.getModule().getList().contains(data)) {
                p.sendMessage(LandModule.getModule().getConfig().getTitle() + language.dataNotExists.replace("%name%", data.getLandName()));
                return true;
            }
        }
        return false;
    }

}
