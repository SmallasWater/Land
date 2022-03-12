package cn.smallaswater.land.commands.sub;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Position;
import cn.smallaswater.land.commands.base.BaseSubCommand;
import cn.smallaswater.land.lands.data.LandData;
import cn.smallaswater.land.lands.data.sub.LandSubData;
import cn.smallaswater.land.utils.DataTool;
import cn.smallaswater.land.utils.Language;
import cn.smallaswater.land.utils.Vector;
import cn.smallaswater.land.module.LandModule;


/**
 * 扩大领地
 * @author 若水
 *
 */
public class ExpandSubCommand extends BaseSubCommand {
    public ExpandSubCommand(String subCommandName) {
        super(subCommandName);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        Language language = LandModule.getModule().getLanguage();
        String title = LandModule.getModule().getConfig().getTitle();
        if(commandSender.isPlayer()){
            if(strings.length > 1){
                int i;
                try{
                    i = Integer.parseInt(strings[1]);
                }catch (Exception ignore){
                    commandSender.sendMessage(title+language.integerError);
                    return false;
                }
                if(i <= 0){
                    commandSender.sendMessage(title+language.integerError);
                    return true;
                }
                LandData data = DataTool.getPlayerLandData(((Player) commandSender).getPosition());
                if(data != null) {
                    Vector vector;
                    commandSender.sendMessage(title+language.mathLandMoney);
                    vector = newLandDataVector((Player) commandSender,i,data.getVector().clone());
                    LandData name = DataTool.checkOverlap(vector);
                    if(data instanceof LandSubData) {
                        LandData in = DataTool.inLandAll(vector);
                        if(in != null && in.getLandName().equalsIgnoreCase(((LandSubData) data).getMasterData().getLandName())){
                            if(!data.getMaster().equalsIgnoreCase(((LandSubData) data).getMasterData().getMaster())){
                                commandSender.sendMessage(title + language.notHavePermission);
                                return true;
                            }
                        }else{
                            commandSender.sendMessage(language.subInMaster.replace("%name%",((LandSubData) data).getMasterData().getLandName()));
                            return true;
                        }
                    }else{
                        if (name != null && !name.equals(data)) {
                            commandSender.sendMessage(title + language.translateString("playerBuyLandErrorLandInArray").replace("%name%", name.getLandName()));
                            return true;
                        }
                    }
                    double money = DataTool.getLandMoney(vector) - DataTool.getLandMoney(data.getVector());
                    if(LandModule.getModule().getMoney().myMoney(commandSender.getName()) >= money){
                        LandModule.getModule().getMoney().reduceMoney(commandSender.getName(),money);
                        data.setVector(vector);
                        commandSender.sendMessage(title+language.expandNeedSuccess
                                .replace("%count%",i+"").replace("%name%",data.getLandName()).replace("%money%",money+""));
                        return true;
                    }else{
                        commandSender.sendMessage(title+language.expandNeedNotHaveMoney.replace("%money%",money+""));
                        return true;
                    }
                }else{
                    commandSender.sendMessage(title+language.placeInLandData);
                    return true;
                }


            }else{
                return false;
            }

        }
        return true;
    }

    private static final int PLAYER_SEE_UP = 50;

    private static final int PLAYER_SEE_DOWN = -50;

    /**
     * 根据朝向与数值计算新的坐标区域
     * @param commandSender 玩家
     * @param i 增加数值
     * @param vector 区域  {@link Vector}
     * @return {@link Vector}
     * */
    private Vector newLandDataVector(Player commandSender,int i,Vector vector){
        vector.sort();
        double x,y,z;
        if(commandSender.pitch <= PLAYER_SEE_DOWN || commandSender.pitch >= PLAYER_SEE_UP){
            if(commandSender.pitch <= PLAYER_SEE_DOWN){
                y = i;
            }else{
                y = -i;
            }
            if (y != 0) {
                if(y > 0){
                    if(vector.getStartY() < vector.getEndY()){
                        vector.addEndY((int) y);
                    }else{
                        vector.addStartY((int) y);
                    }
                }else{
                    if(vector.getStartY() < vector.getEndY()){
                        vector.addStartY((int) y);
                    }else{
                        vector.addEndY((int) y);
                    }
                }
            }
        }else{
            Position position1 = commandSender.getPosition();
            Position position2 = position1.getSide(commandSender.getDirection(), i);
            x = Math.abs(position2.x - position1.x);
            z = Math.abs(position2.z - position1.z);
            if(x > z){
                if(position2.x < position1.x){
                    vector.addStartX(-i);
                }else{
                    vector.addEndX(i);
                }
            }else{
                if(position2.z < position1.z){
                    vector.addStartZ(-i);
                }else{
                    vector.addEndZ(i);
                }
            }

        }
        return vector.deSort();
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "根据朝向拓展领地";
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[]{
                new CommandParameter("int", CommandParamType.INT,false)};
    }
}
