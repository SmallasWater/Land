package cn.smallaswater.land.commands.sub;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.smallaswater.land.commands.base.BaseSubCommand;
import cn.smallaswater.land.windows.CreateWindow;


/**
 * @author SmallasWater
 */
public class MySubCommand extends BaseSubCommand {
    public MySubCommand(String subCommandName) {
        super(subCommandName);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(commandSender instanceof Player){
            CreateWindow.sendMenu((Player) commandSender);
            return true;
        }
        return false;
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "显示我的领地列表";
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
