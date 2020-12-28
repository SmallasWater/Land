package cn.smallaswater.land.commands.sub;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.smallaswater.land.commands.base.BaseSubCommand;
import cn.smallaswater.land.windows.CreateWindow;

/**
 * @author SmallasWater
 */
public class SeekLandSubCommand extends BaseSubCommand {
    public SeekLandSubCommand(String name) {
        super(name);
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }


    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(sender.isPlayer()){
            CreateWindow.sendScreenMenu((Player) sender);
        }
        return false;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
