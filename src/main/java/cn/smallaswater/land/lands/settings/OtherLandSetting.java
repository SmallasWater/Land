package cn.smallaswater.land.lands.settings;

/**
 * 领地权限配置项
 *
 * @author SmallasWater
 * Create on 2021/3/31 14:22
 * Package cn.smallaswater.land.lands.settings
 */
public enum OtherLandSetting {

    BLOCK_UPDATE("方块更新"),
    ENTITY_SPAWN("生物生成"),
    WATER("液体流动"),
    RED_STONE_OUT("阻止领地外活塞"),
    FIRE("火蔓延");

    private final String name;

    OtherLandSetting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
