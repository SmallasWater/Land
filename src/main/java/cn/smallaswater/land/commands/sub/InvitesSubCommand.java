package cn.smallaswater.land.commands.sub;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.smallaswater.land.commands.base.BaseSubCommand;
import cn.smallaswater.land.lands.utils.InviteHandle;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.utils.Language;


import java.util.LinkedList;

/**
 * @author 若水
 */
public class InvitesSubCommand extends BaseSubCommand {


    public InvitesSubCommand(String invites) {
        super(invites);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(commandSender instanceof Player){
            Language language = LandModule.getModule().getLanguage();
            LinkedList<InviteHandle> handles = LandModule.getModule().inviteLands.get(commandSender.getName());
            if(handles != null){
                StringBuilder stringBuilder = new StringBuilder();
                for(InviteHandle handle:handles){
                    stringBuilder.append(language.listValue
                            .replace("%p%",handle.getMaster()).replace("%name%",handle.getData().getLandName())).append("\n");
                }
                commandSender.sendMessage(LandModule.getModule().getConfig().getTitle()+language.inviteList.replace("%list%", stringBuilder.toString()));
                return true;
            }else{
                commandSender.sendMessage(LandModule.getModule().getConfig().getTitle()+language.notHaveInvite);
                return true;
            }
        }
        return false;
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "查看邀请列表";
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
