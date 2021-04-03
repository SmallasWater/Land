package cn.smallaswater.land.lands.settings;

/**
 * @author SmallasWater
 * Create on 2021/3/31 14:22
 * Package cn.smallaswater.land.lands.settings
 */
public enum OtherLandSetting {
    /**锁箱子 玩家移动 玩家传送 锁熔炉 锁放置 锁破坏,锁 PVP 锁伤害生物,锁丢弃 生物生成 方块更新*/
    BLOCK_UPDATE("方块更新"),
    ENTITY_SPAWN("生物生成"),
    WATER("液体流动"),
    FIRE("火蔓延");

    protected String name;
    OtherLandSetting(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
