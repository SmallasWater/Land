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

    private final Config locale;

    public Language(@NotNull File file) {
        this(new Config(file, Config.PROPERTIES));
    }

    public Language(@NotNull File file, int type) {
        this(new Config(file, type));
    }

    public Language(@NotNull Config locale){
        this.locale = locale;
    }

    public String translateString(@NotNull String key) {
        return this.translateString(key, new Object[]{});
    }

    public String translateString(@NotNull String key, Object... params) {
        String string = TextFormat.colorize('&', this.locale.getString(key, "&c Unknown key:" + key));
        if (params != null && params.length > 0) {
            for (int i = 1; i < params.length + 1; i++) {
                string = string.replace("%" + i + "%", Objects.toString(params[i-1]));
            }
        }
        return string;
    }

    public void update(@NotNull File newFile) {
        this.update(newFile, Config.PROPERTIES);
    }

    public void update(@NotNull File newFile, int type) {
        this.update(new Config(newFile, type));
    }

    public void update(@NotNull Config newConfig) {
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
