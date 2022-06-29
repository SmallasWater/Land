package cn.smallaswater.land.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.smallaswater.land.commands.base.BaseCommand;
import cn.smallaswater.land.commands.base.BaseSubCommand;
import cn.smallaswater.land.lands.data.LandData;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.utils.DataTool;

/**
 * @author 若水
 */
public class AdminCommand extends BaseCommand {

    public AdminCommand(String command, String description) {
        super(command, description);
        this.setAliases(new String[]{"lad"});
        this.setPermission("Land.admin");
        this.addSubCommand(new BaseSubCommand("del") {

            @Override
            public boolean canUse(CommandSender sender) {
                return sender.isOp();
            }

            @Override
            public CommandParameter[] getParameters() {
                return new CommandParameter[0];
            }

            @Override
            public String getDescription() {
                return "删除领地";
            }

            @Override
            public String[] getAliases() {
                return new String[0];
            }

            @Override
            public boolean execute(CommandSender commandSender, String s, String[] strings) {
                if(commandSender instanceof Player){
                    if(!commandSender.isOp()){
                        return false;
                    }
                    LandData data = DataTool.getPlayerTouchArea(((Player) commandSender).getPosition());
                    if(data != null){

                        data.close();
                        commandSender.sendMessage(LandModule.getModule().getConfig().getTitle()+"§e删除成功!!");
                        return true;
                    }else{
                        commandSender.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().translateString("placeInLandData"));
                    }
                }
                return false;
            }
        });
        this.addSubCommand(new BaseSubCommand("reload") {

            @Override
            public boolean canUse(CommandSender sender) {
                return sender.isOp();
            }

            @Override
            public CommandParameter[] getParameters() {
                return new CommandParameter[0];
            }

            @Override
            public String[] getAliases() {
                return new String[0];
            }

            @Override
            public String getDescription() {
                return "重新读取";
            }

            @Override
            public boolean execute(CommandSender commandSender, String s, String[] strings) {
                if (commandSender.isOp()) {
                    LandModule.getModule().saveList();
                    LandModule.getModule().loadAll();
                    commandSender.sendMessage(LandModule.getModule().getConfig().getTitle() + "§a重新读取完成");
                    return true;
                }
                return false;
            }
        });
        this.addSubCommand(new BaseSubCommand("money") {

            @Override
            public boolean canUse(CommandSender sender) {
                return sender.isOp();
            }

            @Override
            public CommandParameter[] getParameters() {
                return new CommandParameter[]{new CommandParameter("money", CommandParamType.FLOAT,false)};
            }

            @Override
            public String getDescription() {
                return "强制设置领地出售金钱";
            }

            @Override
            public String[] getAliases() {
                return new String[0];
            }

            @Override
            public boolean execute(CommandSender commandSender, String s, String[] strings) {
                if(commandSender.isOp()) {
                    if(commandSender instanceof Player){
                        LandData data = DataTool.getPlayerTouchArea(((Player) commandSender).getPosition());
                        if(data != null){
                            double money;
                            if(strings.length > 1){
                                try {
                                    money = Double.parseDouble(strings[1]);
                                    if(money < 0){
                                        commandSender.sendMessage(LandModule.getModule().getConfig().getTitle() +"§c请输入大于 0 的金钱数");
                                        return true;
                                    }else{
                                        data.setMoney(money);
                                        commandSender.sendMessage(LandModule.getModule().getConfig().getTitle() +"§b成功将脚底领地: §r"+data.getLandName()+"§b设置出售价格为§e"+money);
                                        return true;
                                    }
                                }catch (Exception e){
                                    commandSender.sendMessage(LandModule.getModule().getConfig().getTitle() +"§c请输入大于 0 的金钱数");
                                    return false;
                                }
                            }
                        }else{
                            commandSender.sendMessage(LandModule.getModule().getConfig().getTitle() +"§c请在站在领地内");
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        loadCommandBase();
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(args.length > 0) {
            return super.execute(sender, label, args);
        }
        sendHelp(sender);
        return true;
    }

    @Override
    public void sendHelp(CommandSender sender) {
        if (sender.isOp()) {
            sender.sendMessage(LandModule.getModule().getLanguage().translateString("commandAdminHelp", (sender.isPlayer()?"(请在游戏内执行)":"")));
        }
    }
}
