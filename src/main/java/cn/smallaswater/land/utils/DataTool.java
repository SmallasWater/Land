package cn.smallaswater.land.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.smallaswater.land.lands.data.LandData;
import cn.smallaswater.land.lands.data.sub.LandSubData;
import cn.smallaswater.land.module.LandModule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 若水
 */
public class DataTool {

    private static boolean checkIt(Vector vector,LandData overlap){
        if(vector != null && overlap != null) {
            if (overlap.getVector().getLevel().getFolderName().equals(vector.getLevel().getFolderName())) {
                return vector.getStartX() <= overlap.getVector().getEndX()
                        && vector.getEndX() >= overlap.getVector().getStartX() &&
                        vector.getStartY() <= overlap.getVector().getEndY()
                        && vector.getEndY() >= overlap.getVector().getStartY() &&
                        vector.getStartZ() <= overlap.getVector().getEndZ()
                        && vector.getEndZ() >= overlap.getVector().getStartZ();
            }
        }
        return false;
    }

    public static boolean hasMasterBack(Player player,LandData data){
        return data.getMaster().equalsIgnoreCase(player.getName());
    }

    public static LinkedList<LandData> getPlayerAllLand(Player player){
        LinkedList<LandData> data = new LinkedList<>();
        for(LandData data1:LandModule.getModule().getList().getData()){
            if(data1.getMaster().equalsIgnoreCase(player.getName())){
                data.add(data1);
            }else{
                if(data1.getMember().containsKey(player.getName())){
                    data.add(data1);
                }
                for(LandSubData data2:data1.getSubData()){
                    if(data2.getMaster().equalsIgnoreCase(player.getName())){
                        data.add(data2);
                        continue;
                    }
                    if(data2.getMember().containsKey(player.getName())){
                        data.add(data2);
                    }
                }
            }
        }
        return data;
    }

    public static LandData checkOverlap(Vector vector,LandData sub){
        vector = vector.clone();
        vector.sort();
        if(sub == null) {
            for (LandData overlap : LandModule.getModule().getList().getData()) {
                if(checkIt(vector,overlap)) {
                    return overlap;
                }
            }
        }else{
            for(LandSubData data:sub.getSubData()){
                if(checkIt(vector,data)){
                    return data;
                }
            }
        }
        return null;
    }
    /**
     * 判断是否存在区域重复
     * */
    public static LandData checkOverlap(Vector vector){
        return checkOverlap(vector,null);
    }

    public static double getLandMoney(Vector vector,boolean sub){
        LandData data = getLand(vector, sub);
        if(data != null){
            if(data.getMoney() > 0){
                return data.getMoney();
            }
        }
        return vector.size() * (sub?LandModule.getModule().getConfig().getSubLandMoney():LandModule.getModule().getConfig().getLandMoney());
    }


    public static LandData getLand(Vector vector,boolean sub){
       for(LandData data:getServerAllLands()){
           if(sub){
               if(data instanceof LandSubData){
                   if(data.getVector().equals(vector)){
                       return data;
                   }
               }
           }else{
               if(!(data instanceof LandSubData)){
                   if(data.getVector().equals(vector)){
                       return data;
                   }
               }
           }
       }
       return null;
    }

    /**
     * 获取服务器全部领地
     * */
    public static LinkedList<LandData> getServerAllLands(){
        LinkedList<LandData> data = new LinkedList<>();
        for(LandData data1:LandModule.getModule().getList().getData()){
            data.add(data1);
            if(data1.getSubData().size() > 0){
                data.addAll(data1.getSubData());
            }
        }
        return data;
    }
    /**
     * 获取领地价值
     * */
    public static double getLandMoney(Vector vector){
       return getLandMoney(vector,false);
    }

    public static double getGettingMoney(LandData data){
        double m = LandModule.getModule().getConfig().getSellMoney();
        if(m != 0){
            m = DataTool.getLandMoney(data.getVector(),(data instanceof LandSubData)) - (DataTool.getLandMoney(data.getVector(),(data instanceof LandSubData)) *(((double) LandModule.getModule().getConfig().getSellMoney()) / 100));
        }else{
            m = DataTool.getLandMoney(data.getVector(),(data instanceof LandSubData));
        }

        return m;
    }

    public static Position getPositionByMap(Map transfer) {
        String levelName = (String) transfer.get("level");
        Level level = Server.getInstance().getLevelByName(levelName);
        return Position.fromObject(new Vector3((double)transfer.get("x"),(double) transfer.get("y"), (double)transfer.get("z")),level);
    }

    public static LinkedHashMap<String,Object> getMapByTransfer(Position position){
        LinkedHashMap<String,Object> obj = new LinkedHashMap<>();
        obj.put("x",position.x);
        obj.put("y",position.y);
        obj.put("z",position.z);
        obj.put("level",position.level.getFolderName());
        return obj;
    }

    public static LandData getPlayerLandData(Position position){
        return getPlayerTouchArea(position);
//        return LandModule.getModule().getList().getLandDataByName(name);
    }
    /**
     * 获取点击位置是否在领地
     * @param position 坐标
     * @param isMaster 是否只获取主领地
     * @return {@link LandData}
     * */
    public static LandData getPlayerTouchArea(Position position,boolean isMaster){
        LandData landName = null;
        if(LandModule.getModule().getList().getData().size() > 0) {
            Vector vector;
            for (LandData areaClass : LandModule.getModule().getList().getData()) {
                if(!isMaster){
                    for(LandSubData subData:areaClass.getSubData()){
                        vector = subData.getVector().clone();
                        vector.sort();
                        if(isEquals(position,vector)){
                            return subData;
                        }
                    }
                }
                vector = areaClass.getVector().clone();
                vector.sort();
                if (isEquals(position,vector)) {
                    landName = areaClass;
                    break;
                }
            }
        }
        return landName;
    }

    /** 获取点击位置是否在领地*/
    public static LandData getPlayerTouchArea(Position position){
        return getPlayerTouchArea(position,false);
    }

    private static boolean isEquals(Position position,Vector vector) {
        try {
            if (vector != null && position != null) {
                return position.level.getFolderName().equals(vector.getLevel().getFolderName())
                        && vector.getStartX() <= position.getFloorX() && vector.getEndX() >= position.getFloorX()
                        && vector.getStartY() <= position.getFloorY() && vector.getEndY() >= position.getFloorY()
                        && vector.getStartZ() <= position.getFloorZ() && vector.getEndZ() >= position.getFloorZ();
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }
    /**
     * 判断是否全部在领地范围内
     * */
    public static LandData inLandAll(Vector vector){
        LandData name1 = getPlayerTouchArea(vector.getPos1(),true);
        LandData name2 = getPlayerTouchArea(vector.getPos2(),true);
        if(name1 != null && name2 != null){
            if(name1.equals(name2)){
                return name1;
            }
        }
        return null;
    }

    /**
     * 获取玩家的领地
     * */
    public static LinkedList<LandData> getLands(String playerName){
        LinkedList<LandData> dataList = new LinkedList<>();
        for(LandData data:LandModule.getModule().getList().getData()){
            if(data.getMaster().equals(playerName) || data.getMember().containsKey(playerName)){
                dataList.add(data);
            }
        }
        return dataList;
    }

    /**
     * 获取服务器所有出售领地
     * */
    public static LinkedList<LandData> getSellLandAll(){
        LinkedList<LandData> list = new LinkedList<>();
        for(LandData data:LandModule.getModule().getList().getData()){
            if(data.isSell()){
                list.add(data);
            }else{
                for(LandSubData data1:data.getSubData()){
                    if(data1.isSell()){
                        list.add(data1);
                    }
                }
            }
        }
        return list;

    }







    /**
     * 获取传送点
     * */
    public static Position getDefaultPosition(Position pos1, Position pos2) {
        double y;
        if(pos1.getY() >= pos2.getY()){
            y = pos1.getY();
        }else{
            y = pos2.getY();
        }
        double x = 0;
        if((pos1.getX() + pos2.getX()) != 0){
            x = (pos1.getX() + pos2.getX()) / 2;
        }
        return pos1.setComponents(x,y,pos1.getZ());

    }
    public static Position getDefaultPosition(Vector vector){
        return getDefaultPosition(vector.getPos1(),vector.getPos2());
    }

    public static String getDateToString(Date date){
        SimpleDateFormat lsdStrFormat = new SimpleDateFormat("yyyy-MM-dd");
        return lsdStrFormat.format(date);
    }


    //转换String为Date
    private static Date getDate(String format){
        SimpleDateFormat lsdStrFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return lsdStrFormat.parse(format);
        }catch (ParseException e){
            return null;
        }
    }



    /** 获取剩余天数*/
    public static int getOutDay(LandData data)
    {
        String str =  data.getSellDay();
        if(str == null || "".equals(str)) {
            return 0;
        }
        Date date = getDate(str);
        if(date == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long time1 = cal.getTimeInMillis();
        cal.setTime(new Date());
        long time2 = cal.getTimeInMillis();
        long betweenDays = (time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(betweenDays)) + 1;
    }


}
