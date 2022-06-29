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
        sender.sendMessage(LandModule.getModule().getLanguage()
                .translateString("commandUserHelp",
                        CreateWindow.getPages(),
                        String.format("%.2f",LandModule.getModule().getConfig().getLandMoney()),
                        String.format("%.2f",LandModule.getModule().getConfig().getSubLandMoney())
                ));
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
