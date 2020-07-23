package cn.smallaswater.land.commands.sub;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.smallaswater.land.commands.base.BaseSubCommand;
import cn.smallaswater.land.windows.CreateWindow;


/**
 * @author 若水
 */
public class AllSubCommand extends BaseSubCommand {

    public AllSubCommand(String subCommandName) {
        super(subCommandName);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(commandSender instanceof Player){
            int page = 1;
            if(strings.length > 1){
                try {
                    page = Integer.parseInt(strings[1]);
                }catch (Exception ignore){}

            }
            CreateWindow.PAGES.put((Player) commandSender,page);
            CreateWindow.sendLandDataList((Player) commandSender);
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
        return "显示所有领地列表";
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
