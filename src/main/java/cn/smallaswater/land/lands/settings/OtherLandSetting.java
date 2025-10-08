package cn.smallaswater.land.lands.settings;

import lombok.Getter;

/**
 * 领地权限配置项
 *
 * @author SmallasWater
 * Create on 2021/3/31 14:22
 * Package cn.smallaswater.land.lands.settings
 */
@Getter
public enum OtherLandSetting {

    BLOCK_UPDATE("方块更新", "是否允许领地内方块更新"),
    ENTITY_SPAWN("生物生成", "是否允许领地内生物生成"),
    WATER("液体流动", "是否允许领地内液体流动"),
    WATER_OUT("领地外液体流入", "是否允许领地外液体流入领地内"),
    RED_STONE_OUT("阻止领地外活塞", "是否阻止领地外活塞退入领地内"),
    FIRE("火蔓延", "是否允许领地内火蔓延");

    private final String name;
    private final String tip;

    OtherLandSetting(String name, String tip) {
        this.name = name;
        this.tip = tip;
    }

}
