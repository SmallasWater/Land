package cn.smallaswater.land.commands.sub;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Position;
import cn.smallaswater.land.LandMainClass;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.utils.DataTool;
import cn.smallaswater.land.utils.Language;
import cn.smallaswater.land.utils.Vector;
import cn.smallaswater.land.commands.base.*;


import java.util.LinkedHashMap;
import java.util.LinkedList;


/**
 * @author 若水
 */
public class PosSubCommand extends BaseSubCommand {


    public PosSubCommand(String subCommandName) {
        super(subCommandName);
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        if(sender instanceof Player) {
            if (strings.length > 1) {
                Language language = LandModule.getModule().getLanguage();
                LinkedList<Position> positions = new LinkedList<>();
                if ("1".equalsIgnoreCase(strings[1])) {
                    if(LandModule.getModule().getConfig().getBlackList().contains(((Player) sender).getLevel().getFolderName()) && !sender.isOp()){
                        if(LandMainClass.MAIN_CLASS.getModule().getConfig().isEchoBlackListMessage()){
                            sender.sendMessage(LandModule.getModule().getConfig().getTitle()+language.whiteWorld.replace("%level%",((Player) sender).level.getFolderName()));
                            return true;
                        }
                        return true;
                    }
                    Position position = new Position(((Player) sender).getFloorX(), ((Player) sender).getFloorY()
                            ,((Player) sender).getFloorZ(),((Player) sender).getLevel());
                    positions.add(position);
                    sender.sendMessage(LandModule.getModule().getConfig().getTitle()+language.translateString("playerSetPos1").replace("%pos%",DataTool.getPosToString(position)));
                    getPos().put(sender.getName(), positions);
                    return true;
                }else if("2".equalsIgnoreCase(strings[1])){
                    if(!getPos().containsKey(sender.getName())){
                        sender.sendMessage(LandModule.getModule().getConfig().getTitle()+language.translateString("playerSetPos2Error"));
                        return false;
                    }
                    Position position = new Position(((Player) sender).getFloorX(), ((Player) sender).getFloorY()
                            ,((Player) sender).getFloorZ(),((Player) sender).getLevel());
                    positions = getPos().get(sender.getName());
                    Position position1 = positions.get(0);
                    if(position1.getLevel().getFolderName().equalsIgnoreCase(((Player) sender).getLevel().getFolderName())){
                        if(positions.size() > 1){
                            positions.remove(1);
                        }
                        positions.add(position);
                        getPos().put(sender.getName(),positions);
                        //进行一次空值检测
                        try{
                            if(position1.getLevel() == null || position.getLevel() == null){
                                getPos().remove(sender.getName());
                                sender.sendMessage(LandModule.getModule().getConfig().getTitle()+language.translateString("playerSetPos2ErrorLevel"));
                                return true;
                            }
                        }catch (Exception e){
                            getPos().remove(sender.getName());
                            sender.sendMessage(LandModule.getModule().getConfig().getTitle()+language.translateString("playerSetPos2ErrorLevel"));
                            return true;
                        }
                        double money = getLandMoney(new Vector(position1,position));
                        sender.sendMessage(LandModule.getModule().getConfig().getTitle()+getPos2String().replace("%pos%",DataTool.getPosToString(position)).replace("%money%",
                                String.format("%.2f", money)));
                        return true;
                    }else{
                        getPos().remove(sender.getName());
                        sender.sendMessage(LandModule.getModule().getConfig().getTitle()+language.translateString("playerSetPos2ErrorLevel"));
                    }
                }
            }
        }
        return false;
    }

    protected double getLandMoney(Vector vector){
        return DataTool.getLandMoney(vector);
    }
    protected String getPos2String(){
        return LandModule.getModule().getLanguage().translateString("playerSetPos2");
    }

    protected LinkedHashMap<String, LinkedList<Position>> getPos(){
        return LandModule.getModule().pos;
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "设置领地范围";
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[]{
                new CommandParameter("value",new String[]{"1","2"})
        };
    }


}
