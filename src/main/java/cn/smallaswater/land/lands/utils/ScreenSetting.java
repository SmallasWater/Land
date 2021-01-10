package cn.smallaswater.land.lands.utils;

import java.util.LinkedList;

/**
 * @author SmallasWater
 * @create 2020/9/19 19:35
 */
public class ScreenSetting {

    private final String type;

    private final boolean sort;

    private final boolean showSell;

    private final String text;

    public static LinkedList<String> strings = new LinkedList<String>(){
        {
            add("§a普通查找");
            add("§a查询ID");
            add("§6查询名称");
            add("§c查询用户");
            add("§e查询时间");
        }
    };


    public ScreenSetting(String type,boolean sort,boolean showSell,String text){
        this.text = text;
        this.sort = sort;
        this.showSell = showSell;
        this.type = type;
    }

    public int getType() {
        return strings.indexOf(type);
    }

    public String getText() {
        return text;
    }

    public boolean isShowSell() {
        return showSell;
    }

    public boolean isSort() {
        return sort;
    }
}
