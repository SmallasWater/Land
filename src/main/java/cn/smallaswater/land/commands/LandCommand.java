package cn.smallaswater.land.commands;


import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.smallaswater.land.commands.base.BaseCommand;
import cn.smallaswater.land.commands.sub.*;
import cn.smallaswater.land.lands.utils.InviteHandle;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.windows.CreateWindow;


import java.util.LinkedList;

/**
 * @author 若水
 */
public class LandCommand extends BaseCommand {
    public LandCommand(String command, String description) {
        super(command, description);
        this.setPermission("Land.land");
        this.setAliases(new String[]{"land","la"});
        this.addSubCommand(new AllSubCommand("all"));
        this.addSubCommand(new CreateSubCommand("create"));
        this.addSubCommand(new MySubCommand("my"));
        this.addSubCommand(new PosSubCommand("pos"));
        this.addSubCommand(new AcceptSubCommand("accept"));
        this.addSubCommand(new DenySubCommand("deny"));
        this.addSubCommand(new ExpandSubCommand("expand"));
        this.addSubCommand(new InvitesSubCommand("invites"));
        this.addSubCommand(new PosSubLandSubCommand("subpos"));
        this.addSubCommand(new CreateSubLandSubCommand("subcreate"));
        this.addSubCommand(new SellSubCommand("sell"));
        this.addSubCommand(new SeekLandSubCommand("screen"));
        loadCommandBase();

    }



    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    @Override
    public void sendHelp(CommandSender sender) {
        //TODO 多语言支持
        sender.sendMessage("§e>>§a-------------------§bHelps of Land§a-------------------§e<<");
        sender.sendMessage("§e/land §7help §a查看插件帮助/Check the plugin to help");
        sender.sendMessage("§e/land §7my§a显示自己的领地列表/List according to their own territory");
        sender.sendMessage("§e/land §7sell§a显示正在出售的领地列表/Display the list of domain is for sale");
        sender.sendMessage("§e/land §7all <page> (当前共有 " + CreateWindow.getPages() + " 页) 显示所有领地列表)/(The current total of "+CreateWindow.getPages()+" page) The list shows all territory");
        sender.sendMessage("§e/land §7pos <1/2> §a设置领地坐标/Set the territory coordinates");
        sender.sendMessage("§e/land §7subpos <1/2> §a设置子领地坐标/Set the child domain coordinates");
        sender.sendMessage("§e/land §7create <name> §a创建领地 p.s每个方块: $"+ String.format("%2f",LandModule.getModule().getConfig().getLandMoney() + "/Create land p.sOne block: $"+ String.format("%2f",LandModule.getModule().getConfig().getLandMoney())));
        sender.sendMessage("§e/land §7subcreate <name> §a创建子领地 p.s每个方块: $"+ String.format("%2f",LandModule.getModule().getConfig().getSubLandMoney() + "/Create SubLand p.sOne block: $"+ String.format("%2f",LandModule.getModule().getConfig().getSubLandMoney())));
        sender.sendMessage("§e/land §7accept <player> §a同意该玩家的领地邀请/Agree to the player's territory invitation");
        sender.sendMessage("§e/land §7deny <player> §a拒绝玩家的领地邀请/Refuse to the domain of the players invited");
        sender.sendMessage("§e/land §7invites §a查看邀请列表/Check the invitation list");
        sender.sendMessage("§e/land §7expand <size> §a拓展领地/Expand the territory");
        sender.sendMessage("§e/land §7screen  §a查找领地/Find the domain");
        sender.sendMessage("§e>>§a-------------------§bHelps of Land§a-------------------§e<<");

    }

    @Override
    public boolean execute( CommandSender sender,  String label,  String[] args) {
        if(args.length == 0){
            if(sender instanceof Player) {
                CreateWindow.sendMenu((Player) sender);
                return true;
            }else{
                sender.sendMessage("Please don't in the console to perform this command");
            }
        }
        return super.execute(sender,label,args);
    }

    public static InviteHandle getInviteHandle(Player player,String name){
        Player target = Server.getInstance().getPlayer(name);
        if(target != null){
            name = target.getName();
        }
        LinkedList<InviteHandle> handles = LandModule.getModule().inviteLands.get(player.getName());
        InviteHandle handle = null;
        if (handles != null) {
            for (InviteHandle handle1 : handles) {
                if (handle1.getMaster().equalsIgnoreCase(name)) {
                    handle = handle1;
                }
            }
        }
        return handle;
    }
}
