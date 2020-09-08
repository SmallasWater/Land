package cn.smallaswater.land.commands.base;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;


/**
 * @author SmallasWater
 */
abstract public class BaseSubCommand {
    private final String name;

    protected BaseSubCommand(String name) {
        this.name = name;
    }



    /**
     * @param sender CommandSender
     * @return boolean
     */
    boolean canUse(CommandSender sender){
        return sender.isPlayer();
    }

    public String getDescription(){
        return "";
    }

    /**
     * 获取名称
     * @return string
     */
    public String getName(){
        return name;
    }
    /**
     * 获取别名
     * @return string[]
     */
    public abstract String[] getAliases();

    /**
     * 命令响应
     * @param sender the sender      - CommandSender
     * @param args   The arrugements      - String[]
     * @param label  label..
     * @return true if true
     */
    public abstract boolean execute(CommandSender sender,String label, String[] args);



    /**
     * 指令参数.
     * @return  提示参数
     * */
    abstract public CommandParameter[] getParameters();

}
