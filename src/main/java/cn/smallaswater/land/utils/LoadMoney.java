package cn.smallaswater.land.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import me.onebone.economyapi.EconomyAPI;
import money.Money;
import net.player.api.Point;

/**
 * @author SmallasWater
 */
public class LoadMoney {
    public static final int MONEY = 1;
    public static final int ECONOMY_API = 0;
    public static final int PLAYER_POINT = 2;

    private int money;

    public LoadMoney(){
        if(Server.getInstance().getPluginManager().getPlugin("EconomyAPI") != null){
            money = ECONOMY_API;
        }else if(Server.getInstance().getPluginManager().getPlugin("Money") != null){
            money = MONEY;
        }else if(Server.getInstance().getPluginManager().getPlugin("PlayerPoint") != null){
            money = PLAYER_POINT;
        }else{
            money = -2;
        }

    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getMonetaryUnit(){
        if (this.money == ECONOMY_API) {
            return EconomyAPI.getInstance().getMonetaryUnit();
        }
        return "$";
    }

    public double myMoney(Player player){
        return myMoney(player.getName());
    }

    public double myMoney(String player){
        switch (this.money){
            case MONEY:
                if(Money.getInstance().getPlayers().contains(player)){
                    return Money.getInstance().getMoney(player);
                }
                break;
            case ECONOMY_API:
                return EconomyAPI.getInstance().myMoney(player) ;
            case PLAYER_POINT:
                return Point.myPoint(player);
            default:break;
        }
        return 0;
    }

    public void addMoney(Player player, double money){
        addMoney(player.getName(), money);
    }

    public void addMoney(String player, double money){
        switch (this.money){
            case MONEY:
                if(Money.getInstance().getPlayers().contains(player)){
                    Money.getInstance().addMoney(player, (float) money);
                    return;
                }
                break;
            case ECONOMY_API:
                EconomyAPI.getInstance().addMoney(player, money, true);
                return;
            case PLAYER_POINT:
                Point.addPoint(player, money);
                return;
            default:break;
        }
    }
    public void reduceMoney(Player player, double money){
        reduceMoney(player.getName(), money);
    }

    public void reduceMoney(String player, double money){
        switch (this.money){
            case MONEY:
                if(Money.getInstance().getPlayers().contains(player)){
                    Money.getInstance().reduceMoney(player, (float) money);
                    return;
                }
                break;
            case ECONOMY_API:
                EconomyAPI.getInstance().reduceMoney(player, money, true);
                return;
            case PLAYER_POINT:
                Point.reducePoint(player, money);
                return;
            default:break;
        }
    }

}
