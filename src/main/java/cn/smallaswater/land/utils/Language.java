package cn.smallaswater.land.utils;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author 若水
 * 自定义语言文件
 */
public class Language {

    public String playerBuyLandSuccess = "成功购买领地 %name% 花费 %money%";
    public String playerBuyLandError = "购买领地失败，，你没有足够的金钱";
    public String landLists = "当前服务器拥有 %count% 个领地 第 %i% 页/共 %max% 页";
    public String quitLandButton = "离开领地";
    public String sellLandButton = "出售领地 ( - %c% ％)";
    public String giveLandButton = "转让领地";
    public String setLandButton = "领地设置";
    public String setTextButton = "领地进入 / 退出提示";
    public String choseCanSell = "你确定要出售这个领地吗 当前出售价格 %money% ( - %c% ％)";
    public String choseQuitLand = "你确定要退出这个领地吗?";
    public String labelText = "权限设置对象: %p%";
    public String inviteButton = "邀请玩家";
    public String kickButton = "踢出玩家";
    public String kickLandMessage = "你被 %p% 踢出了 %name% 领地";
    public String sellLandMessage = "你成功出售 %name% 领地 获得 %money%";
    public String setPlayerButton = "玩家领地权限设置";
    public String setOtherButton = "访客领地权限设置";
    public String kickText = "你确定要将玩家 %p% 移除领地吗?";
    public String giveText = "你确定要将领地 %n% 转让给玩家 %p%吗?";
    public String acceptMessageMaster = "玩家 %p% 接受了你的 %name% 领地邀请..";
    public String acceptErrorMaxMaster = "玩家 %p% 的领地已经到达上限 无法加入 %name%领地";
    public String acceptErrorMaxTarget = "你的的领地已经到达上限啦..无法加入%n%的 %name%领地";
    public String acceptMessageMember = "你加入了 %p% 的%name%领地...";
    public String inviteTimeOut = "邀请 %p% 加入 %name% 领地的请求已超时";
    public String denyMessageMaster = "玩家 %p% 拒绝了你的 %name% 领地邀请..";
    public String denyMessageMember = "你拒绝了 %p% 的领地邀请...";
    public String notHaveInvite = "你没有任何领地邀请";
    public String notHaveTargetInvite = "你没有 %p% 的领地邀请..";
    public String inviteList = ">>========邀请列表=======<<\n%list%";
    public String listValue = " %p% 邀请你加入 %name% 领地 (输/领地 accept <%p%> 同意邀请)";
    public String notHavePermission = "你没有 %setting% 权限...";
    public String choseTrue = "确定";
    public String choseFalse = "取消";
    public String saveSetting = "%p% 权限设置已保存..";
    public String saveTransfer = "%name% 传送点已保存 ";
    public String saveText = "%name% 已保存";
    public String saveTransferError = "请在 %name% 领地内设置传送点";
    public String invitePlayerMaster = "成功向 %p% 发送了加入 %name% 领地邀请 (有效时间 %time% 秒)";
    public String invitePlayerTarget = "%p% 向你发送了 %name% 领地邀请\n/领地 accept <%p%> 同意领地邀请\n/领地 deny <%p%> 拒绝邀请";
    public String invitePlayerExists = "你已经邀请了 %p% 啦...";
    public String invitePlayerInArray = "%p% 已经是 %name% 的成员啦.";
    public String dataNotExists = "%name% 领地不存在啦...";
    public String playerOffOnline = "%p% 不在线~~";
    public String givePlayerLandTarget = "%p% 成为了 %name% 的新领主~";
    public String givePlayerLandMaster = "你失去了 %name% 领地的领主身份，转让给了 %p%~";
    public String whiteWorld = "%level% 世界禁止圈地";
    public String inputTextJoin = "请设置玩家进入领地的提示 %n% 代表领地名称 %master% 是领主 %member%是领地成员";
    public String inputTextQuit = "请设置玩家离开领地的提示 %n% 代表领地名称 %master% 是领主 %member%是领地成员";
    public String integerError = "请输入正确的数值";
    public String mathLandMoney = "正在计算拓展领地需要花费的金钱...";
    public String placeInLandData = "请站在领地内...";
    public String expandNeedNotHaveMoney = "你的钱不够哦~~ 无法拓展领地 需要花费 %money%";
    public String expandNeedSuccess = "成功拓展 %count% 格 %name% 领地 花费 %money%";
    public String cancelChose = "你取消了操作";
    public String subInMaster = "请保证子领地全部在主领地 %name% 内";
    public String inLandData = "请站在你自己的领地内";
    public String subLandSetting = "子领地管理";
    public String showParticle = "领地范围显示";
    public String subLandButton = "子领地: %name% 身份: %p%\n点击查看详情";
    public String backButton = "返回上一步";
    public String protectLevel = "此世界被圈地之前禁止交互";
    public String sellOtherLand = "主领地领主出售了 %name% 子领地 你失去了 %name% 的权限";
    public String showSellLandButton = "(%type%) %name% 售卖中 领主: %player% (%day%天后下架)\n点我查看详情";
    public String showSellLandMenu = "领地名称: %name%&r\n\n领地大小: %size% 方块\n\n领地位置: %pos%\n\n领地地图: %level%\n\n公示时间: %day%天后下架\n\n领地价格: %money%\n\n领地介绍: %message%";
    public String masterLand = "主领地";
    public String subLand = "子领地";
    public String buyLandButton = "是我的了，购买！";
    public String setSellText = "你将%name%领地挂在了寄售行 公示期 %day%天， 公示期结束后下架领地";
    public String setSellFalseText = "你将 %name% 领地从寄售行下架了";
    public String buyLandFalse = "%name% 领地不在寄售行了";
    public String buyLandMe = "你不能购买自己的领地";
    public String buyLandTrue = "你成功购买了 %name% 领地 花费 %money%";
    public String buyLandMaster = "%player% 购买了你的 %name% 获得 %money%";
    public String buyLandNotHaveMoney = "你没有足够的金钱购买 %name% 需要花费 %money%";
    public String inSellButton = "寄售领地";
    public String inSellFalseButton = "取消寄售领地";
    public String isSellInputMoney = "请设置领地出售金钱..";
    public String isSellInputMessage = "请设置宣传标语..";
    public String setSellMoneyMessage = "成功设置 %name% 领地出售价格为 %money%";

    public String masterSellHaveLandSell = "此领地中 %name% 子领地处于寄售状态..无法出售";
    public String masterGiveHaveLandSell = "此领地中 %name% 子领地处于寄售状态..无法给予玩家";

    public String giveSellLandError = "此领地正在寄售..无法给予任何玩家";



    private final Config locale;

    public Language(@NotNull File file) {
        this(new Config(file, Config.PROPERTIES));
    }

    public Language(@NotNull File file, int type) {
        this(new Config(file, type));
    }

    public Language(@NotNull Config locale){
        this.locale = locale;
        this.loadLocale();
    }

    @Deprecated
    private void loadLocaleA(){
        this.expandNeedSuccess = TextFormat.colorize('&', this.locale.getString("expandNeedSuccess", expandNeedSuccess));
        this.expandNeedNotHaveMoney = TextFormat.colorize('&', this.locale.getString("expandNeedNotHaveMoney", expandNeedNotHaveMoney));
        this.placeInLandData = TextFormat.colorize('&', this.locale.getString("placeInLandData", placeInLandData));
        this.mathLandMoney = TextFormat.colorize('&', this.locale.getString("mathLandMoney", mathLandMoney));
        this.integerError = TextFormat.colorize('&', this.locale.getString("integerError", integerError));
        this.inputTextQuit = TextFormat.colorize('&', this.locale.getString("inputTextQuit", inputTextQuit));
        this.inputTextJoin = TextFormat.colorize('&', this.locale.getString("inputTextJoin", inputTextJoin));
        this.whiteWorld = TextFormat.colorize('&', this.locale.getString("whiteWorld", whiteWorld));
        this.givePlayerLandMaster = TextFormat.colorize('&', this.locale.getString("givePlayerLandMaster", givePlayerLandMaster));
        this.givePlayerLandTarget = TextFormat.colorize('&', this.locale.getString("givePlayerLandTarget", givePlayerLandTarget));
        this.playerOffOnline = TextFormat.colorize('&', this.locale.getString("playerOffOnline", playerOffOnline));
        this.dataNotExists = TextFormat.colorize('&', this.locale.getString("dataNotExists", dataNotExists));
        this.invitePlayerInArray = TextFormat.colorize('&', this.locale.getString("invitePlayerInArray", invitePlayerInArray));
        this.invitePlayerExists = TextFormat.colorize('&', this.locale.getString("invitePlayerExists", invitePlayerExists));
        this.invitePlayerTarget = TextFormat.colorize('&', this.locale.getString("invitePlayerTarget", invitePlayerTarget));
        this.invitePlayerMaster = TextFormat.colorize('&', this.locale.getString("invitePlayerMaster", invitePlayerMaster));
        this.saveTransferError = TextFormat.colorize('&', this.locale.getString("saveTransferError", saveTransferError));
        this.saveText = TextFormat.colorize('&', this.locale.getString("saveText", saveText));
        this.saveTransfer = TextFormat.colorize('&', this.locale.getString("saveTransfer", saveTransfer));
        this.saveSetting = TextFormat.colorize('&', this.locale.getString("saveSetting", saveSetting));
        this.choseFalse = TextFormat.colorize('&', this.locale.getString("choseFalse", choseFalse));
        this.choseTrue = TextFormat.colorize('&', this.locale.getString("choseTrue", choseTrue));
        this.notHavePermission = TextFormat.colorize('&', this.locale.getString("notHavePermission", notHavePermission));
        this.listValue = TextFormat.colorize('&', this.locale.getString("listValue", listValue));
        this.inviteList = TextFormat.colorize('&', this.locale.getString("inviteList", inviteList));
        this.notHaveTargetInvite = TextFormat.colorize('&', this.locale.getString("notHaveTargetInvite", notHaveTargetInvite));
        this.notHaveInvite = TextFormat.colorize('&', this.locale.getString("notHaveInvite", notHaveInvite));
        this.denyMessageMember = TextFormat.colorize('&', this.locale.getString("denyMessageMember", denyMessageMember));
        this.denyMessageMaster = TextFormat.colorize('&', this.locale.getString("denyMessageMaster", denyMessageMaster));
        this.acceptMessageMember = TextFormat.colorize('&', this.locale.getString("acceptMessageMember", acceptMessageMember));
        this.acceptErrorMaxTarget = TextFormat.colorize('&', this.locale.getString("acceptErrorMaxTarget", acceptErrorMaxTarget));
        this.acceptErrorMaxMaster = TextFormat.colorize('&', this.locale.getString("acceptErrorMaxMaster", acceptErrorMaxMaster));
        this.acceptMessageMaster = TextFormat.colorize('&', this.locale.getString("acceptMessageMaster", acceptMessageMaster));
        this.giveText = TextFormat.colorize('&', this.locale.getString("giveText", giveText));
        this.kickText = TextFormat.colorize('&', this.locale.getString("kickText", kickText));
        this.setOtherButton = TextFormat.colorize('&', this.locale.getString("setOtherButton", setOtherButton));
        this.setPlayerButton = TextFormat.colorize('&', this.locale.getString("setPlayerButton", setPlayerButton));
        this.sellLandMessage = TextFormat.colorize('&', this.locale.getString("sellLandMessage", sellLandMessage));
        this.kickLandMessage = TextFormat.colorize('&', this.locale.getString("kickLandMessage", kickLandMessage));
        this.kickButton = TextFormat.colorize('&', this.locale.getString("kickButton", kickButton));
        this.inviteButton = TextFormat.colorize('&', this.locale.getString("inviteButton", inviteButton));
        this.labelText = TextFormat.colorize('&', this.locale.getString("labelText", labelText));
        this.choseQuitLand = TextFormat.colorize('&', this.locale.getString("choseQuitLand", choseQuitLand));
        this.choseCanSell = TextFormat.colorize('&', this.locale.getString("choseCanSell", choseCanSell));
        this.setTextButton = TextFormat.colorize('&', this.locale.getString("setTextButton", setTextButton));
        this.setLandButton = TextFormat.colorize('&', this.locale.getString("setLandButton", setLandButton));
        this.giveLandButton = TextFormat.colorize('&', this.locale.getString("giveLandButton", giveLandButton));
        this.sellLandButton = TextFormat.colorize('&', this.locale.getString("sellLandButton", sellLandButton));
        this.quitLandButton = TextFormat.colorize('&', this.locale.getString("quitLandButton", quitLandButton));
    }

    @Deprecated
    private void loadLocalB(){
        this.playerBuyLandError = TextFormat.colorize('&', this.locale.getString("playerBuyLandError", playerBuyLandError));
        this.playerBuyLandSuccess = TextFormat.colorize('&', this.locale.getString("playerBuyLandSuccess", playerBuyLandSuccess));
        this.cancelChose = TextFormat.colorize('&', this.locale.getString("cancelChose", cancelChose));
        this.subInMaster = TextFormat.colorize('&', this.locale.getString("subInMaster", subInMaster));
        this.inLandData = TextFormat.colorize('&', this.locale.getString("inLandData", inLandData));
        this.subLandSetting = TextFormat.colorize('&', this.locale.getString("subLandSetting", subLandSetting));
        this.subLandButton = TextFormat.colorize('&', this.locale.getString("subLandButton", subLandButton));
        this.showParticle = TextFormat.colorize('&', this.locale.getString("showParticle", showParticle));
        this.backButton = TextFormat.colorize('&', this.locale.getString("backButton", backButton));
        this.protectLevel = TextFormat.colorize('&',this.locale.getString("protectLevel",protectLevel));
        this.showParticle = TextFormat.colorize('&', this.locale.getString("showParticle", showParticle));
        this.backButton = TextFormat.colorize('&', this.locale.getString("backButton", backButton));
        this.protectLevel = TextFormat.colorize('&',this.locale.getString("protectLevel",protectLevel));
    }

    @Deprecated
    private void loadLocale(){
        loadLocaleA();
        loadLocalB();
        //-----//
        this.sellOtherLand = TextFormat.colorize('&', this.locale.getString("sellOtherLand", sellOtherLand));
        this.showSellLandButton = TextFormat.colorize('&', this.locale.getString("showSellLandButton", showSellLandButton));
        this.showSellLandMenu = TextFormat.colorize('&',this.locale.getString("showSellLandMenu",showSellLandMenu));
        this.masterLand = TextFormat.colorize('&', this.locale.getString("masterLand", masterLand));
        this.subLand = TextFormat.colorize('&', this.locale.getString("subLand", subLand));
        this.buyLandButton = TextFormat.colorize('&',this.locale.getString("buyLandButton",buyLandButton));
        this.setSellText = TextFormat.colorize('&', this.locale.getString("setSellText", setSellText));
        this.setSellFalseText = TextFormat.colorize('&', this.locale.getString("setSellFalseText", setSellFalseText));
        this.buyLandFalse = TextFormat.colorize('&',this.locale.getString("buyLandFalse",buyLandFalse));
        this.buyLandMe = TextFormat.colorize('&', this.locale.getString("buyLandMe", buyLandMe));
        this.buyLandTrue = TextFormat.colorize('&', this.locale.getString("buyLandTrue", buyLandTrue));
        this.buyLandMaster = TextFormat.colorize('&',this.locale.getString("buyLandMaster",buyLandMaster));
        this.inviteTimeOut = TextFormat.colorize('&',this.locale.getString("inviteTimeOut",inviteTimeOut));


        this.masterSellHaveLandSell = TextFormat.colorize('&', this.locale.getString("masterSellHaveLandSell", masterSellHaveLandSell));
        this.masterGiveHaveLandSell = TextFormat.colorize('&',this.locale.getString("masterGiveHaveLandSell",masterGiveHaveLandSell));

        this.buyLandNotHaveMoney = TextFormat.colorize('&', this.locale.getString("buyLandNotHaveMoney", buyLandNotHaveMoney));
        this.inSellButton = TextFormat.colorize('&', this.locale.getString("inSellButton", inSellButton));
        this.inSellFalseButton = TextFormat.colorize('&',this.locale.getString("inSellFalseButton",inSellFalseButton));
        this.giveSellLandError = TextFormat.colorize('&', this.locale.getString("giveSellLandError", giveSellLandError));
        this.isSellInputMessage = TextFormat.colorize('&',this.locale.getString("isSellInputMessage",isSellInputMessage));
        this.isSellInputMoney = TextFormat.colorize('&',this.locale.getString("isSellInputMoney",isSellInputMoney));
        this.setSellMoneyMessage = TextFormat.colorize('&',this.locale.getString("setSellMoneyMessage",setSellMoneyMessage));
        this.landLists = TextFormat.colorize('&',this.locale.getString("landLists",landLists));
    }

    public String translateString(String key) {
        return this.translateString(key, new Object[]{});
    }

    public String translateString(String key, Object... params) {
        String string = TextFormat.colorize('&', this.locale.getString(key, "&c Unknown key:" + key));
        if (params != null && params.length > 0) {
            for (int i = 1; i < params.length + 1; i++) {
                string = string.replace("%" + i + "%", Objects.toString(params[i-1]));
            }
        }
        return string;
    }

    public void update(File newFile) {
        this.update(newFile, Config.PROPERTIES);
    }

    public void update(File newFile, int type) {
        this.update(new Config(newFile, type));
    }

    public void update(Config newConfig) {
        boolean needSave = false;
        HashMap<String, String> cache = new HashMap<>();
        for (String key : this.locale.getKeys()) {
            if (newConfig.getKeys().contains(key)) {
                cache.put(key, this.locale.getString(key, "§c Unknown key:" + key));
            }else {
                this.locale.remove(key);
                needSave = true;
            }
        }
        for (String key : newConfig.getKeys()) {
            if (!cache.containsKey(key)) {
                String string = newConfig.getString(key, "§c Unknown key:" + key);
                this.locale.set(key, string);
                needSave = true;
            }
        }
        if (needSave) {
            this.locale.save();
        }
    }

}
