package cn.smallaswater.land.windows;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.*;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;
import cn.smallaswater.land.lands.data.LandData;
import cn.smallaswater.land.lands.data.sub.LandSubData;
import cn.smallaswater.land.lands.settings.OtherLandSetting;
import cn.smallaswater.land.lands.utils.ScreenSetting;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.lands.settings.LandSetting;

import cn.smallaswater.land.utils.DataTool;
import cn.smallaswater.land.utils.Language;
import cn.smallaswater.land.utils.Vector;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author 若水
 */
public class CreateWindow {

    static final int MENU = 0x125Ac01;
    static final int SETTING = 0x125Ac02;
    static final int CHOSE = 0x125Ac03;
    static final int LIST = 0x125Ac04;
    static final int SETTING_LAND = 0x125Ac05;
    static final int MEMBERS = 0x125Ac06;
    static final int SET_LAND = 0x125Ac07;
    static final int KICK_MENU = 0x125Ac08;
    static final int GIVE_MENU = 0x125Ac09;
    static final int INVITE_PLAYER = 0x125Ac010;
    static final int JOIN_QUIT_TEXT = 0x125Ac19;
    static final int LIST_SUB = 0x125Ac20;
    static final int SELL_LANDS = 0x125Ac21;
    static final int BUY_LAND_MENU = 0x125Ac22;
    static final int IS_SELL_MENU = 0x125Ac23;
    static final int SCREEN_MENU = 0x125Ac24;
    static final int SCREEN_LIST = 0x125Ac25;
    static final int LAND_ALL_SETTING = 0x125Ac26;
    static final int LAND_ALL_DEFAULT_SETTING = 0x125Ac27;

    static final int INVITE_BUTTON = 0;
    static final int KICK_BUTTON = 1;
    static final int SET_PLAYER_BUTTON = 2;

    static final int SET_LAND_ALL_SETTING = 9;
    static final int SELL_LAND_BUTTON = 3;
    static final int GIVE_LAND_BUTTON = 4;
    static final int SET_TRANSFER_BUTTON = 5;
    static final int SET_TEXT_BUTTON = 6;
    static final int SHOW_PARTICLE_BUTTON = 7;
    static final int SET_SUB_LAND_BUTTON = 8;
    static String backImage = "textures/ui/refresh_light";

    private static Language language = LandModule.getModule().getLanguage();

    public static LinkedHashMap<Player,Integer> PAGES = new LinkedHashMap<>();

//    public static LinkedHashMap<Player,LandData> SCREEN_LAND_LIST = new LinkedHashMap<>();

    /**
     * 玩家领地菜单
     * */
    public static void sendMenu(Player player){
        FormWindowSimple simple = new FormWindowSimple(LandModule.getModule().getConfig().getTitle(),"");
        for(LandData data: DataTool.getPlayerAllLand(player)){
            simple.addButton(getButton(player,data));
        }
        if(simple.getButtons().size() == 0){
            simple.setContent(LandModule.getModule().getLanguage().notHaveLand);
        }
        player.showFormWindow(simple,MENU);
    }

    /**
     * 显示玩家的下一步菜单 需设置 clickdata
     * */
    static void sendSettingLandMenu(Player player){
        LandData data =  LandModule.getModule().clickData.get(player);
        if(data != null) {
            Vector vector = data.getVector();
            if(vector == null){
                player.sendMessage(LandModule.getModule().getConfig().getTitle()+"出现未知问题，打开失败，请重新打开一次");
                return;
            }
            FormWindowSimple simple = new FormWindowSimple(LandModule.getModule().getConfig().getTitle(), "");
            simple.setContent(LandModule.getModule().getLanguage()
                    .landSettingMessage
                    .replace("%id%",data.getLandId()+"")
                    .replace("%name%",data.getLandName())
                    .replace("%master%",data.getMaster())
                    .replace("%member%",data.getMember().keySet().toString())
                    .replace("%pos%",
                            LandModule.getModule().getLanguage().pos
                                    .replace("%x1%",data.getVector().getStartX()+"")
                                    .replace("%x2%",data.getVector().getEndX()+"")
                                    .replace("%y1%",data.getVector().getStartY()+"")
                                    .replace("%y2%",data.getVector().getEndY()+"")
                                    .replace("%z1%",data.getVector().getStartZ()+"")
                                    .replace("%z2%",data.getVector().getEndZ()+""))
                    .replace("%level%",data.getVector().getLevel().getFolderName()));
            if(data.getMember().containsKey(player.getName())){
                simple.addButton(new ElementButton(LandModule.getModule().getLanguage().transferButton,new ElementButtonImageData("path","textures/ui/mashup_world")));
                simple.addButton(new ElementButton(LandModule.getModule().getLanguage().quitLandButton,new ElementButtonImageData("path","textures/ui/invite_hover")));
                if(data instanceof LandSubData){
                    if(((LandSubData) data).getMasterData().getMaster().equalsIgnoreCase(player.getName())){
                        simple.addButton(new ElementButton(LandModule.getModule().getLanguage().setLandButton,new ElementButtonImageData("path","textures/ui/dev_glyph_color")));
                    }
                }
            }else{
                if(data.getMaster().equalsIgnoreCase(player.getName())){
                    simple.addButton(new ElementButton(LandModule.getModule().getLanguage().transferButton,new ElementButtonImageData("path","textures/ui/mashup_world")));
                    simple.addButton(new ElementButton(LandModule.getModule().getLanguage().setLandButton,new ElementButtonImageData("path","textures/ui/dev_glyph_color")));
                }else{
                    simple.addButton(new ElementButton(LandModule.getModule().getLanguage().transferButton,new ElementButtonImageData("path","textures/ui/mashup_world")));
                    if(data instanceof LandSubData){
                        if(((LandSubData) data).getMasterData().getMaster().equalsIgnoreCase(player.getName())){
                            simple.addButton(new ElementButton(LandModule.getModule().getLanguage().setLandButton,new ElementButtonImageData("path","textures/ui/dev_glyph_color")));
                        }
                    }
                }
            }
            simple.addButton(getBackButton());
            player.showFormWindow(simple,SETTING);
        }
    }

    /**TODO 领地查找*/
    public static void sendScreenMenu(Player player){
        FormWindowCustom custom = new FormWindowCustom(LandModule.getModule().getConfig().getTitle()+"--查找");
        custom.addElement(new ElementLabel("查找的选项不同会影响查找结果"));
        custom.addElement(new ElementDropdown("索引设置", ScreenSetting.strings,0));
        custom.addElement(new ElementToggle("自动排序",false));
        custom.addElement(new ElementToggle("是否显示出售中领地",true));
        custom.addElement(new ElementInput("输入索引","请根据索引设置输入"));
        player.showFormWindow(custom,SCREEN_MENU);

    }



    static void sendScreenList(Player player){
        if(WindowListener.screenSetting.containsKey(player.getName())){

            ScreenSetting screenSetting = WindowListener.screenSetting.get(player.getName());
            FormWindowSimple simple = new FormWindowSimple(LandModule.getModule().getConfig().getTitle(),"");

            LinkedList<LandData> landDataList = DataTool.getScreenLandData(screenSetting);

            WindowListener.screenLands.put(player,landDataList);
            if(landDataList.size() == 0){
                simple.setContent("未查找到领地 请确认查询条件是否正确");
            }else{
                simple.setContent("查找到 "+landDataList.size()+" 个符合条件的领地 (最多显示20个)");
            }
            for(LandData data: landDataList){
                simple.addButton(getButton(player,data));
            }
            simple.addButton(getBackButton());
            player.showFormWindow(simple,SCREEN_LIST);

        }

    }






    private static ElementButton getBackButton(){
        return new ElementButton(LandModule.getModule().getLanguage().backButton,new ElementButtonImageData("path","textures/ui/refresh_light"));
    }

    private static ElementButton getButton(Player player,LandData data){
        String master = LandModule.getModule().getLanguage().master;
        if(data.getMember().containsKey(player.getName())){
            master = LandModule.getModule().getLanguage().member;
        }else if(!data.getMaster().equalsIgnoreCase(player.getName())){
            master = LandModule.getModule().getLanguage().other;
        }
        String s = LandModule.getModule().getLanguage()
                .landButtonText;
        if(data instanceof LandSubData){
            s = LandModule.getModule().getLanguage().subLandButton;
        }
        if(data instanceof LandSubData){
            if(((LandSubData) data).getMasterData().getMaster().equalsIgnoreCase(player.getName())){
                if(!data.getMaster().equalsIgnoreCase(player.getName())) {
                    master = master + LandModule.getModule().getLanguage().master;
                }
            }
        }
        s = s.replace("%name%",data.getLandName()).replace("%p%",master);
        return new ElementButton(s,new ElementButtonImageData("path","textures/ui/icon_recipe_nature"));
    }

    public static void sendSellingLandDataList(Player player){
        FormWindowSimple simple = new FormWindowSimple(LandModule.getModule().getConfig().getTitle(), "");
        for(LandData data:DataTool.getSellLandAll()){
            if(data.isSell()){
                simple.addButton(new ElementButton(
                        LandModule.getModule().getLanguage().showSellLandButton
                                .replace("%type%",((data instanceof LandSubData)?LandModule.getModule().getLanguage().subLand
                                        :LandModule.getModule().getLanguage().masterLand))
                                .replace("%name%",data.getLandName())
                                .replace("%player%",data.getMaster())
                                .replace("%day%",(LandModule.getModule().getConfig().getShowTime() - DataTool.getOutDay(data))+"")
                        ,new ElementButtonImageData("path","textures/ui/icon_recipe_nature")));
            }
        }

        player.showFormWindow(simple,SELL_LANDS);
    }

    static void sendSellLandSetting(Player player){
        LandData data = LandModule.getModule().clickData.get(player);
        if(data != null) {
            Vector vector = data.getVector();
            if(vector == null){
                player.sendMessage(LandModule.getModule().getConfig().getTitle()+"出现未知问题，打开失败，请重新打开一次");
                return;
            }
            double money = DataTool.getLandMoney(data.getVector(), (data instanceof LandSubData));
            if(data.getSellMoney() != -1){
                money = data.getSellMoney();
            }
            FormWindowSimple simple = new FormWindowSimple(LandModule.getModule().getConfig().getTitle(), "");
            String content = LandModule.getModule().getLanguage().showSellLandMenu
                    .replace("%id%",data.getLandId()+"")
                    .replace("%name%", data.getLandName())
                    .replace("%size%", data.getVector().size() + "")
                    .replace("%pos%", LandModule.getModule().getLanguage().pos
                            .replace("%x1%", data.getVector().getStartX() + "")
                            .replace("%x2%", data.getVector().getEndX() + "")
                            .replace("%y1%", data.getVector().getStartY() + "")
                            .replace("%y2%", data.getVector().getEndY() + "")
                            .replace("%z1%", data.getVector().getStartZ() + "")
                            .replace("%z2%", data.getVector().getEndZ() + ""))
                    .replace("%level%", data.getVector().getLevel().getFolderName())
                    .replace("%day%", (LandModule.getModule().getConfig().getShowTime() - DataTool.getOutDay(data)) + "")
                    .replace("%player%",data.getMaster())
                    .replace("%money%", String.format("%.2f",money))
                    .replace("%message%",data.getSellMessage());
            simple.setContent(content);
            simple.addButton(new ElementButton(LandModule.getModule().getLanguage().transferButton,new ElementButtonImageData("path","textures/ui/mashup_world")));
            simple.addButton(new ElementButton(LandModule.getModule().getLanguage().buyLandButton, new ElementButtonImageData("path", "textures/ui/MCoin")));
            simple.addButton(getBackButton());
            player.showFormWindow(simple, BUY_LAND_MENU);
        }

    }
    /**
     * 显示寄售设置
     * */
    static void sendSellSetting(Player player){
        FormWindowCustom simple = new FormWindowCustom(LandModule.getModule().getConfig().getTitle());
        simple.addElement(new ElementInput(LandModule.getModule().getLanguage().isSellInputMoney));
        simple.addElement(new ElementInput(LandModule.getModule().getLanguage().isSellInputMessage));
        player.showFormWindow(simple,IS_SELL_MENU);
    }

    static LinkedList<LandData> getPageLandDataList(Player player){
        LinkedList<LandData> landDataList = new LinkedList<>();
        if(!PAGES.containsKey(player)){
            PAGES.put(player,1);
        }
        int page = PAGES.get(player);
        int i = (page - 1) * 10;
        for(int x = 0; x < LandModule.getModule().getList().getData().size();x++){
            if(i < page * 10){
                if(i == LandModule.getModule().getList().getData().size()){
                    break;
                }
                landDataList.add(LandModule.getModule().getList().getData().get(i));
            }
            i++;
        }
        return landDataList;
    }

    public static int getPages(){
        return (int) Math.ceil((float) LandModule.getModule().getList().getData().size() / 10);

    }

    /**
     * 显示所有领地列表
     * */
    public static void sendLandDataList(Player player){
        FormWindowSimple simple = new FormWindowSimple(LandModule.getModule().getConfig().getTitle(), "");
        for(LandData data: getPageLandDataList(player)){
            simple.addButton(getButton(player,data));
        }
        if(simple.getButtons().size() == 0){
            simple.setContent(LandModule.getModule().getLanguage().notHaveLand);
        }else{
            simple.setContent(LandModule.getModule().getLanguage().landLists
                    .replace("%count%",LandModule.getModule().getList().getData().size()+"")
                    .replace("%i%",PAGES.get(player)+"")
                    .replace("%max%",getPages()+""));
        }
        player.showFormWindow(simple,LIST);
    }

    /**
     * 显示所有子领地
     * */
    static void sendSubLandList(Player player){
        FormWindowSimple simple = new FormWindowSimple(LandModule.getModule().getConfig().getTitle(), "");
        LandData data =  LandModule.getModule().clickData.get(player);
        if(data != null){
            for(LandSubData subData:data.getSubData()){
                simple.addButton(getButton(player,subData));
            }
            if(simple.getButtons().size() == 0){
                simple.setContent(LandModule.getModule().getLanguage().notHaveLand);
            }
            LandData last = data;
            if(data instanceof LandSubData){
                 last = ((LandSubData) data).getMasterData();
            }
            if(DataTool.hasMasterBack(player,last)){
                simple.addButton(getBackButton());
            }
            player.showFormWindow(simple,LIST_SUB);
        }
    }

    /**
     * 设置领地权限
     * */
    static void sendLandSettingMenu(Player player){
        sendLandSettingMenu(player,null);
    }

    static void sendLandSettingMenu(Player player, String playerName){
        Language language = LandModule.getModule().getLanguage();
        LandData data =  LandModule.getModule().clickData.get(player);
        if(data != null) {
            FormWindowCustom custom = new FormWindowCustom(LandModule.getModule().getConfig().getTitle());
            custom.addElement(new ElementLabel(language.labelText.replace("%p%",(playerName!=null?playerName:language.other))));
            for(LandSetting setting: LandSetting.values()){
                custom.addElement(new ElementToggle(setting.getName(),data.hasPermission(playerName,setting)));
            }
            player.showFormWindow(custom,SETTING_LAND);
        }
    }

    /**
     * 显示领地设置
     * */
    static void sendSetLandMenu(Player player){
        LandData data = LandModule.getModule().clickData.get(player);

        FormWindowSimple simple = new FormWindowSimple(LandModule.getModule().getConfig().getTitle(), "");
        simple.addButton(new ElementButton(language.inviteButton,new ElementButtonImageData("path","textures/ui/invite_base")));
        simple.addButton(new ElementButton(language.kickButton,new ElementButtonImageData("path","textures/ui/realms_red_x")));
//        simple.addButton(new ElementButton(language.setPlayerButton,new ElementButtonImageData("path","textures/ui/recipe_book_icon")));
        simple.addButton(new ElementButton(language.setting,new ElementButtonImageData("path","textures/ui/recipe_book_icon")));
//        simple.addButton(new ElementButton(language.setOtherButton,new ElementButtonImageData("path","textures/ui/recipe_book_icon")));
        simple.addButton(new ElementButton(LandModule.getModule().getLanguage().sellLandButton.replace("%c%",
                LandModule.getModule().getConfig().getSellMoney()+""),new ElementButtonImageData("path","textures/ui/MCoin")));
        simple.addButton(new ElementButton(LandModule.getModule().getLanguage().giveLandButton,new ElementButtonImageData("path","textures/ui/Friend1")));
        simple.addButton(new ElementButton(LandModule.getModule().getLanguage().setTransfer,new ElementButtonImageData("path","textures/ui/Feedback")));
        simple.addButton(new ElementButton(LandModule.getModule().getLanguage().setTextButton,new ElementButtonImageData("path","textures/ui/copy")));
        simple.addButton(new ElementButton(LandModule.getModule().getLanguage().showParticle,new ElementButtonImageData("path","textures/ui/water_breathing_effect")));
        if(!(data instanceof LandSubData)){
            simple.addButton(new ElementButton(LandModule.getModule().getLanguage().subLandSetting,new ElementButtonImageData("path","textures/ui/dev_glyph_color")));
        }
        if(data.isSell()) {
            simple.addButton(new ElementButton(LandModule.getModule().getLanguage().inSellFalseButton, new ElementButtonImageData("path", "textures/ui/Feedback")));
        }else{
            simple.addButton(new ElementButton(LandModule.getModule().getLanguage().inSellButton, new ElementButtonImageData("path", "textures/ui/Feedback")));
        }
        simple.addButton(getBackButton());
        player.showFormWindow(simple,SET_LAND);
    }

    /**
     * TODO 发送领地设置
     * */
    static void sendLandOtherSettingMenu(Player player){
        LandData data =  LandModule.getModule().clickData.get(player);
        if(data != null) {
            FormWindowCustom custom = new FormWindowCustom(LandModule.getModule().getConfig().getTitle());
            for(OtherLandSetting setting: OtherLandSetting.values()){
                custom.addElement(new ElementToggle(setting.getName(),data.hasPermission(setting)));
            }
            player.showFormWindow(custom,LAND_ALL_DEFAULT_SETTING);
        }
    }

    public static void sendLandAllSettingMenu(Player player){
        FormWindowSimple simple = new FormWindowSimple(LandModule.getModule().getConfig().getTitle(), "");
        simple.addButton(new ElementButton(language.setPlayerButton,new ElementButtonImageData("path","textures/ui/recipe_book_icon")));
        simple.addButton(new ElementButton(language.landSetting,new ElementButtonImageData("path","textures/ui/recipe_book_icon")));
        simple.addButton(new ElementButton(language.setOtherButton,new ElementButtonImageData("path","textures/ui/recipe_book_icon")));
        simple.addButton(getBackButton());
        player.showFormWindow(simple,LAND_ALL_SETTING);
    }

    /**
     * 显示成员列表
     * */
    static void sendMemberList(Player player){
        LandData data =  LandModule.getModule().clickData.get(player);
        if(data != null) {
            FormWindowSimple simple = new FormWindowSimple(LandModule.getModule().getConfig().getTitle(), "");
            for(String member:data.getMember().keySet()){
                simple.addButton(new ElementButton(member,new ElementButtonImageData("path","textures/ui/Friend2")));
            }
            simple.addButton(getBackButton());
            player.showFormWindow(simple,MEMBERS);
        }
    }

    /**
     * 选择踢出玩家
     * */
    static void sendKickMenu(Player player, String name){
        Language language = LandModule.getModule().getLanguage();
        FormWindowModal modal = new FormWindowModal(LandModule.getModule().getConfig().getTitle(),"",language.choseTrue,language.choseFalse);
        modal.setContent(language.kickText.replace("%p%",name));
        player.showFormWindow(modal,KICK_MENU);
    }
    /**
     * 邀请玩家*/
    static void sendInvitePlayerMenu(Player player){
        FormWindowSimple simple = new FormWindowSimple(LandModule.getModule().getConfig().getTitle(), "");
        for(Player player1: Server.getInstance().getOnlinePlayers().values()) {
            if (!player1.getName().equalsIgnoreCase(player.getName())) {
                simple.addButton(new ElementButton(player1.getName(), new ElementButtonImageData("path", "textures/ui/Friend2")));
            }
        }
        simple.addButton(getBackButton());
        player.showFormWindow(simple,INVITE_PLAYER);
    }

    /**
     * 选择转让玩家
     * */
    static void sendGiveLandMenu(Player player, String name){
        Language language = LandModule.getModule().getLanguage();
        LandData data = LandModule.getModule().clickData.get(player);
        if(data != null) {
            FormWindowModal modal = new FormWindowModal(LandModule.getModule().getConfig().getTitle(), "", language.choseTrue, language.choseFalse);
            modal.setContent(language.giveText.replace("%p%", name).replace("%n%", data.getLandName()));
            player.showFormWindow(modal, GIVE_MENU);
        }
    }

    /**
     * 选择退出领地或出售
     * */
    static void sendQuitOrSellMenu(Player player){
        Language language = LandModule.getModule().getLanguage();
        FormWindowModal modal = new FormWindowModal(LandModule.getModule().getConfig().getTitle(),"",language.choseTrue,language.choseFalse);
        LandData data =  LandModule.getModule().clickData.get(player);
        if(data != null) {
            boolean isMaster = data.getMaster().equalsIgnoreCase(player.getName());
            if(data instanceof LandSubData){
                isMaster = ((LandSubData) data).getMasterData().getMaster().equalsIgnoreCase(player.getName());
            }
            if(isMaster){
                double m = DataTool.getGettingMoney(data);
                modal.setContent(language.choseCanSell
                        .replace("%money%",String.format("%.2f",m))
                        .replace("%c%",LandModule.getModule().getConfig().getSellMoney()+"")
                        .replace("%name%",data.getLandName()));
            }else{
                modal.setContent(language.choseQuitLand.replace("%name%",data.getLandName()));
            }
            player.showFormWindow(modal,CHOSE);
        }
    }
    /**
     * 设置加入领地离开领地提示
     * */
    static void onJoinQuitTextMenu(Player player){
        Language language = LandModule.getModule().getLanguage();
        FormWindowCustom custom = new FormWindowCustom(LandModule.getModule().getConfig().getTitle());
        custom.addElement(new ElementInput(language.inputTextJoin,language.playerJoinMessage,language.playerJoinMessage));
        custom.addElement(new ElementInput(language.inputTextQuit,language.playerQuitMessage,language.playerQuitMessage));
        player.showFormWindow(custom,JOIN_QUIT_TEXT);
    }
}
