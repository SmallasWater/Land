package cn.smallaswater.land.lands.settings;

/**
 * 玩家权限设置项
 *
 * @author 若水
 */
public enum LandSetting {

    LOCK_CHEST("使用箱子"),
    MOVE("进入领地"),
    TRANSFER("传送领地"),
    FRAME("使用熔炉"),
    PLACE("放置方块"),
    BREAK("破坏方块"),
    PVP("PVP"),
    DAMAGE_ENTITY("伤害生物"),
    DROP("丢弃物品"),
    BUCKET("使用桶"),
    IGNITE("使用打火石"),
    SHOW_ITEM("使用展示框"),
    CHANGE_SIGN("编辑告示牌"),
    SUB_ZONE("创建子空间");


    public final String name;

    LandSetting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
