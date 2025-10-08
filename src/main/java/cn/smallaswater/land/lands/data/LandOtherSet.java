package cn.smallaswater.land.lands.data;

import cn.smallaswater.land.lands.settings.OtherLandSetting;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author SmallasWater
 * Create on 2021/3/31 18:52
 * Package cn.smallaswater.land.lands.settings
 */
public class LandOtherSet {

    private LinkedHashMap<String, Boolean> otherLandSetting = new LinkedHashMap<>();

    public LandOtherSet() {
        for (OtherLandSetting setting : OtherLandSetting.values()) {
            otherLandSetting.put(setting.getName(), true);
        }
    }

    public LandOtherSet(LinkedHashMap<String, Boolean> otherLandSetting) {
        this.otherLandSetting = otherLandSetting;
    }

    public boolean isOpen(OtherLandSetting set) {
        return otherLandSetting.getOrDefault(set.getName(), true);
    }

    public void setOpen(OtherLandSetting otherLandSetting, boolean value) {
        this.otherLandSetting.put(otherLandSetting.getName(), value);
    }

    public LinkedHashMap<String, Boolean> getOtherLandSetting() {
        return otherLandSetting;
    }

    public static LandOtherSet getLandOtherSetByMap(Map map) {
        LinkedHashMap<String, Boolean> otherLandSetting = new LinkedHashMap<>();
        for (OtherLandSetting setting : OtherLandSetting.values()) {
            if (map.containsKey(setting.getName())) {
                otherLandSetting.put(setting.getName(), Boolean.parseBoolean(map.get(setting.getName()).toString()));
            } else {
                otherLandSetting.put(setting.getName(), true);
            }
        }
        return new LandOtherSet(otherLandSetting);
    }
}
