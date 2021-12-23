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
        sender.sendMessage("§e>>§a-------------------§bHelps of Land§a-------------------§e<<");
        sender.sendMessage("§e/land §7help §aCheck the plugin to help");
        sender.sendMessage("§e/land §7my§aList according to their own territory");
        sender.sendMessage("§e/land §7sell§aDisplay the list of domain is for sale");
        sender.sendMessage("§e/land §7all <page> (The current total of "+CreateWindow.getPages()+" page) The list shows all territory");
        sender.sendMessage("§e/land §7pos <1/2> §aSet the territory coordinates");
        sender.sendMessage("§e/land §7subpos <1/2> §aSet the child domain coordinates");
        sender.sendMessage("§e/land §7create <name> §aCreate land p.sOne block: $"+ String.format("%2f",LandModule.getModule().getConfig().getLandMoney()));
        sender.sendMessage("§e/land §7subcreate <name> §aCreate SubLand p.sOne block: $"+ String.format("%2f",LandModule.getModule().getConfig().getSubLandMoney()));
        sender.sendMessage("§e/land §7accept <player> §aAgree to the player's territory invitation");
        sender.sendMessage("§e/land §7deny <player> §aRefuse to the domain of the players invited");
        sender.sendMessage("§e/land §7invites §aCheck the invitation list");
        sender.sendMessage("§e/land §7expand <size> §aExpand the territory");
        sender.sendMessage("§e/land §7screen  §aFind the domain");
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
