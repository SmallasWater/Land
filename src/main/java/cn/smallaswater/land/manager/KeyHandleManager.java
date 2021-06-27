package cn.smallaswater.land.manager;

import cn.nukkit.Player;
import cn.smallaswater.land.handle.KeyHandle;
import cn.smallaswater.land.module.LandModule;


/**
 * @author SmallasWater
 * Create on 2021/6/2 20:52
 * Package com.smallaswater.customvip.manager
 */
public class KeyHandleManager {

    public static boolean isKey(Player player, String key){
        KeyHandle handle = getHandle(player);
        return handle.isKey(key);
    }

    public static void addKey(Player player, String key){
       addKey(player, key,true);
    }

    public static Object getKey(Player player, String key){
        KeyHandle handle = getHandle(player);
        return handle.getKey(key);
    }

    public static void addKey(Player player, String key, Object o){
        KeyHandle handle = getHandle(player);
        handle.addKey(key,o);
    }

    public static void clearPlayerKey(Player player){
        LandModule.getModule().keyHanle.remove(getHandle(player));
    }

    public static void removeKey(Player player, String key){
        KeyHandle handle = getHandle(player);
        handle.removeKey(key);
    }

    private static KeyHandle getHandle(Player player){
        KeyHandle handle = new KeyHandle(player);
        if(!LandModule.getModule().keyHanle.contains(handle)){
            LandModule.getModule().keyHanle.add(handle);
        }
        return LandModule.getModule().keyHanle
                .get(LandModule.getModule().keyHanle.indexOf(handle));
    }
}
