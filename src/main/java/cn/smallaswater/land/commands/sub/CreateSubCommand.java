package cn.smallaswater.land.commands.sub;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.smallaswater.land.commands.base.BaseSubCommand;
import cn.smallaswater.land.event.land.LandCreateEvent;
import cn.smallaswater.land.event.player.PlayerCreateLandEvent;
import cn.smallaswater.land.lands.data.LandData;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.players.MemberSetting;
import cn.smallaswater.land.players.PlayerSetting;
import cn.smallaswater.land.utils.DataTool;
import cn.smallaswater.land.utils.Language;
import cn.smallaswater.land.utils.Vector;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author 若水
 */
public class CreateSubCommand extends BaseSubCommand {
    public CreateSubCommand(String subCommandName) {
        super(subCommandName);
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        Language language = LandModule.getModule().getLanguage();
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (strings.length > 1) {
                String name = strings[1].trim();
                if(!isValidFileName(name)){
                    player.sendMessage(LandModule.getModule().getConfig().getTitle() + TextFormat.RED+"领地名称含有\\/:*?\"<>| 非法字符 请更换名称");
                    return true;
                }
                if(getPos().containsKey(player.getName())) {
                    LinkedList<Position> positions = getPos().get(player.getName());
                    if(positions.size() > 1) {
                        if (isInLand(player)) {
                            String landName = getLandName(positions);
                            if (landName != null) {
                                player.sendMessage(LandModule.getModule().getConfig().getTitle() + language.translateString("playerBuyLandErrorLandInArray").replace("%name%", landName));
                                getPos().remove(player.getName());
                                return true;
                            } else {
                                if (!canExistsLand(name)) {
                                    player.sendMessage(LandModule.getModule().getConfig().getTitle() + language.translateString("playerBuyLandErrorLandExists").replace("%name%", name));
                                    return true;
                                } else {
                                    double money = getMoney(new Vector(positions.get(0), positions.get(1)));
                                    if (LandModule.getModule().getMoney().myMoney(player.getName()) >= money) {
                                        if (DataTool.getLands(player.getName()).size() >= getMaxLand()) {
                                            getPos().remove(player.getName());
                                            player.sendMessage(LandModule.getModule().getConfig().getTitle() + language.translateString("playerLandMax").replace("%count%", getMaxLand() + ""));
                                            return true;
                                        } else {
                                            Vector vector = new Vector(positions.get(0), positions.get(1));
                                            if(!createLandData(name, player, vector, language)){
                                                return true;
                                            }
                                            LandModule.getModule().getMoney().reduceMoney(player.getName(), money);
                                            player.sendMessage(LandModule.getModule().getConfig().getTitle() + language.translateString("playerBuyLandSuccess").replace("%name%", name).replace("%money%", String.format("%.2f", money)));
                                            getPos().remove(player.getName());

                                            return true;
                                        }
                                    } else {
                                        player.sendMessage(LandModule.getModule().getConfig().getTitle() + language.translateString("playerBuyLandError").replace("%money%", String.format("%.2f", money)));
                                        getPos().remove(player.getName());
                                        return true;
                                    }
                                }
                            }
                        }else{
                            getPos().remove(player.getName());
                        }
                    }else{
                        player.sendMessage(LandModule.getModule().getConfig().getTitle()+language.translateString("createNotHavePos2"));
                    }
                }else{
                    player.sendMessage(LandModule.getModule().getConfig().getTitle()+language.translateString("createNotHavePos"));
                }

            }
        }
        return false;
    }

    protected double getMoney(Vector vector){
        return DataTool.getLandMoney(vector);
    }

    protected boolean isInLand(Player player){
        return true;
    }

    protected String getLandName(LinkedList<Position> positions){
        LandData data = DataTool.checkOverlap(new Vector(positions.get(0),positions.get(1)));
        if(data != null){
            return data.getLandName();
        }
        return null;
    }

    private static boolean isValidFileName(String fileName) {
        if (fileName == null || fileName.length() > 255) {
            return false;
        } else {
            return fileName.matches("[^\\s\\\\/:*?\"<>|]*[^\\s\\\\/:*?\"<>|.]$");
        }
    }

    protected boolean canExistsLand(String name) {
        return LandModule.getModule().getList().getLandDataByName(name) == null;
    }

    protected int getMaxLand(){
        return LandModule.getModule().getConfig().getMaxLand();
    }
    protected boolean createLandData(String name,Player sender,Vector vector,Language language){
        LandData data = new LandData(
                LandModule.getModule().getList().size(),
                name,
                sender.getName(),
                vector,
                new MemberSetting(new LinkedHashMap<>()),
                PlayerSetting.getDefaultSetting(),
                language.translateString("playerJoinMessage"),
                language.translateString("playerQuitMessage"),
                DataTool.getDefaultPosition(vector)
        );
        PlayerCreateLandEvent event = new PlayerCreateLandEvent(sender, data);
        Server.getInstance().getPluginManager().callEvent(event);
        if(event.isCancelled()){
            return false;
        }
        LandCreateEvent event1 = new LandCreateEvent(data);
        Server.getInstance().getPluginManager().callEvent(event1);
        if(event1.isCancelled()){
            return false;
        }
        LandModule.getModule().getList().add(data);
        Config config = new Config(LandModule.getModule().getModuleInfo().getDataFolder()+"/lands/"+data.getLandName()+".yml",2);
        config.setAll(data.getAll());
        config.save();
        data.setConfig(config);
        LandModule.getModule().saveList();
        return true;
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
        return "创建领地";
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[]{
                new CommandParameter("name", CommandParamType.TEXT,true)
        };
    }
}
