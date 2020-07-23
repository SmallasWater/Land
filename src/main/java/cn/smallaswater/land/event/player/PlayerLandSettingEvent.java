package cn.smallaswater.land.event.player;

import cn.nukkit.Player;
import cn.smallaswater.land.event.LandPlayerDataEvent;
import cn.smallaswater.land.lands.data.LandData;
import cn.smallaswater.land.players.PlayerSetting;

/**
 * @author SmallasWater
 */
public class PlayerLandSettingEvent extends LandPlayerDataEvent {

    private PlayerSetting playerSetting;

    private String member;

    public PlayerLandSettingEvent(Player player, PlayerSetting setting, LandData data, String member) {
        super(player, data);
        this.member = member;
        this.playerSetting = setting;
    }

    public PlayerSetting getPlayerSetting() {
        return playerSetting;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public void setPlayerSetting(PlayerSetting playerSetting) {
        this.playerSetting = playerSetting;
    }

    public String getMember() {
        return member;
    }


}
