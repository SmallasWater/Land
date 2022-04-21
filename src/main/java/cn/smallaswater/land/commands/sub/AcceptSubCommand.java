package cn.smallaswater.land.commands.sub;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.smallaswater.land.commands.base.BaseSubCommand;
import cn.smallaswater.land.lands.utils.InviteHandle;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.utils.Language;
import cn.smallaswater.land.commands.*;

import java.util.LinkedList;

/**
 * @author 若水
 */
public class AcceptSubCommand extends BaseSubCommand {

    public AcceptSubCommand(String subCommandName) {
        super(subCommandName);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        Language language = LandModule.getModule().getLanguage();
        if(commandSender instanceof Player){
            if(strings.length > 1) {
                String target = strings[1];
                LinkedList<InviteHandle> handles = LandModule.getModule().inviteLands.get(commandSender.getName());
                InviteHandle handle = LandCommand.getInviteHandle((Player) commandSender,target);
                if (handles != null) {
                    if(handle != null){
                        handle.accept();
                        return true;
                    }else{
                        commandSender.sendMessage(LandModule.getModule().getConfig().getTitle()+language.translateString("notHaveTargetInvite").replace("%p%",target));
                        return true;
                    }
                }else{
                    commandSender.sendMessage(LandModule.getModule().getConfig().getTitle()+language.translateString("notHaveInvite"));
                    return true;
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
        return "接受邀请领地";
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET,true)};
    }
}
