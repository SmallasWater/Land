package cn.smallaswater.land.utils.form;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.smallaswater.land.utils.form.windows.AdvancedFormWindowCustom;
import cn.smallaswater.land.utils.form.windows.AdvancedFormWindowModal;
import cn.smallaswater.land.utils.form.windows.AdvancedFormWindowSimple;

/**
 * 窗口操作监听器
 * 实现AdvancedFormWindow 操作处理
 *
 * @author LT_Name
 */
@SuppressWarnings("unused")
public class WindowListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerFormResponded(PlayerFormRespondedEvent event) {
        if (AdvancedFormWindowSimple.onEvent(event.getWindow(), event.getPlayer())) {
            return;
        }
        if (AdvancedFormWindowModal.onEvent(event.getWindow(), event.getPlayer())) {
            return;
        }
        AdvancedFormWindowCustom.onEvent(event.getWindow(), event.getPlayer());
    }

}
