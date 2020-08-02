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
        this.setAliases(new String[]{"圈地","la"});
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
        loadCommandBase();

    }



    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    @Override
    public void sendHelp(CommandSender sender) {
        sender.sendMessage("§e>>§a-------------------§bHelps of Land§a-------------------§e<<");
        sender.sendMessage("§e/领地 §7help §a查看插件帮助");
        sender.sendMessage("§e/领地 §7my§a显示自己的领地列表");
        sender.sendMessage("§e/领地 §7sell§a显示正在出售的领地列表");
        sender.sendMessage("§e/领地 §7all <页数> (当前共有 "+CreateWindow.getPages()+" 页) 显示所有领地列表");
        sender.sendMessage("§e/领地 §7pos <1/2> §a设置领地坐标");
        sender.sendMessage("§e/领地 §7subpos <1/2> §a设置子领地坐标");
        sender.sendMessage("§e/领地 §7create <name> §a创建领地 p.s每个方块: $"+ String.format("%2f",LandModule.getModule().getConfig().getLandMoney()));
        sender.sendMessage("§e/领地 §7subcreate <name> §a创建子领地 p.s每个方块: $"+ String.format("%2f",LandModule.getModule().getConfig().getSubLandMoney()));
        sender.sendMessage("§e/领地 §7accept <player> §a同意该玩家的领地邀请");
        sender.sendMessage("§e/领地 §7deny <player> §a拒绝玩家的领地邀请");
        sender.sendMessage("§e/领地 §7invites §a查看邀请列表");
        sender.sendMessage("§e/领地 §7expand <大小> §a拓展领地");
        sender.sendMessage("§e>>§a-------------------§bHelps of Land§a-------------------§e<<");

    }

    @Override
    public boolean execute( CommandSender sender,  String label,  String[] args) {
        if(args.length == 0){
            if(sender instanceof Player) {
                CreateWindow.sendMenu((Player) sender);
                return true;
            }else{
                sender.sendMessage("请不要在控制台执行此指令");
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
