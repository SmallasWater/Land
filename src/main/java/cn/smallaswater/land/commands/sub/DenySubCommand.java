package cn.smallaswater.land.commands.sub;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.smallaswater.land.commands.*;
import cn.smallaswater.land.commands.base.BaseSubCommand;
import cn.smallaswater.land.lands.utils.InviteHandle;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.utils.Language;


import java.util.LinkedList;

/**
 * @author 若水
 */
public class DenySubCommand extends BaseSubCommand {
    public DenySubCommand(String subCommandName) {
        super(subCommandName);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        Language language = LandModule.getModule().getLanguage();
        if(commandSender instanceof Player){
            if(strings.length > 1) {
                String target = strings[1];
                LinkedList<InviteHandle> handles = LandModule.getModule().inviteLands.get(commandSender.getName());
                InviteHandle handle = LandCommand.getInviteHandle((Player) commandSender, target);
                if (handles != null) {
                    if (handle != null) {
                        handle.deny();
                    } else {
                        commandSender.sendMessage(LandModule.getModule().getConfig().getTitle() + language.notHaveTargetInvite.replace("%p%", target));
                    }
                } else {
                    commandSender.sendMessage(LandModule.getModule().getConfig().getTitle() + language.notHaveInvite);
                }
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
        return "拒绝领地邀请";
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET,true)};
    }
}
