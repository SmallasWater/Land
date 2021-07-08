package cn.smallaswater.land.players;

/**
 * @author 若水
 */
@Deprecated
public enum LandSetting {
    /**锁箱子 玩家移动 玩家传送 锁熔炉 锁放置 锁破坏,锁 PVP 锁伤害生物,锁丢弃 生物生成 方块更新*/
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
    SUB_ZONE("创建子空间"),
    ENTITY_SPAWN("生物生成"),
    BLOCK_UPDATE("方块更新"),
    SHOW_ITEM("使用展示框");


    public String name;
    LandSetting(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }



}
