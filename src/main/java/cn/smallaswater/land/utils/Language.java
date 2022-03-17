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

    private final Config locale;

    public Language(@NotNull File file) {
        this(new Config(file, Config.YAML));
    }

    public Language(@NotNull File file, int type) {
        this(new Config(file, type));
    }

    public Language(@NotNull Config locale){
        this.locale = locale;
        this.loadLocale();
    }

    @Deprecated
    private void loadLocale(){
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
        this.update(newFile, Config.YAML);
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
