package cn.smallaswater.land.commands.sub;

import cn.nukkit.level.Position;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.utils.DataTool;
import cn.smallaswater.land.utils.Vector;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author 若水
 */
public class PosSubLandSubCommand extends PosSubCommand{
    public PosSubLandSubCommand(String subCommandName) {
        super(subCommandName);
    }

    @Override
    protected double getLandMoney(Vector vector) {
        return DataTool.getLandMoney(vector,true);
    }

    @Override
    protected LinkedHashMap<String, LinkedList<Position>> getPos() {
        return LandModule.getModule().subPos;
    }

    @Override
    protected String getPos2String() {
        return LandModule.getModule().getLanguage().playerSetSubPos2;
    }

    @Override
    public String getDescription() {
        return "设置子领地范围";
    }
}
