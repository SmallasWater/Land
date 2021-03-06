package cn.smallaswater.land.lands.data;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockChest;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.event.entity.ExplosionPrimeEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.smallaswater.land.event.player.*;
import cn.smallaswater.land.lands.data.sub.LandSubData;
import cn.smallaswater.land.utils.DataTool;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.players.LandSetting;
import cn.smallaswater.land.utils.Language;
import cn.smallaswater.land.utils.Vector;


import java.util.LinkedHashMap;
import java.util.LinkedList;


/**
 * @author 若水
 */
public class LandListener implements Listener {
    private LinkedHashMap<Player,LandData> move = new LinkedHashMap<>();
    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        LandData data = DataTool.getPlayerLandData(player);
        if(data != null) {
            if (notHasPermission(player, player.getPosition(), LandSetting.MOVE)) {
                event.setCancelled();
                player.knockBack(player,0,(player.x - player.getLocation().x),(player.z - player.getLocation().z),0.8);
                return;
            }
            if (!move.containsKey(player)) {
                moveJoinEvent(player, data, data);
            }else{
                LandData newData = move.get(player);
                if(!data.equals(newData)){
                    moveJoinEvent(player, data, newData);
                }
            }
        }else{
            if(move.containsKey(player)){
                data = move.get(player);
                if(data != null) {
                    String message = data.getQuitMessage();
                    moveQuitEvent(player, data, message);
                }
                move.remove(player);
            }
        }
    }
    private void moveQuitEvent(Player player, LandData data, String message) {
        PlayerMoveQuitLandEvent event2 = new PlayerMoveQuitLandEvent(player,data,message);
        Server.getInstance().getPluginManager().callEvent(event2);
        if(event2.isCancelled()){
            return;
        }
        message = event2.getMessage();
        if (!"".equalsIgnoreCase(message)) {
            player.sendPopup(LandModule.getModule().getConfig().getTitle() + data.getQuitMessage()
                    .replace("%n%", data.getLandName())
                    .replace("%master%", data.getMaster())
                    .replace("%member%", data.getMember().keySet().toString())+"\n\n\n\n");
        }
    }

    @EventHandler
    public void onInviteTimeOut(PlayerInviteTimeOutEvent event){
        Player master = Server.getInstance().getPlayer(event.getMasterPlayerName());
        if(master != null){
            master.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().inviteTimeOut
                    .replace("%p%",event.getMemberPlayerName())
                    .replace("%name%",event.data.getLandName()));
        }
    }

    private void moveJoinEvent(Player player, LandData data, LandData newData) {
        String message = data.getQuitMessage();
        move.put(player, data);
        PlayerMoveJoinLandEvent event1 = new PlayerMoveJoinLandEvent(player,newData,message);
        Server.getInstance().getPluginManager().callEvent(event1);
        if(event1.isCancelled()){
            return;
        }
        message = event1.getMessage();
        if(!"".equalsIgnoreCase(message)){

            player.sendPopup(LandModule.getModule().getConfig().getTitle()+data.getJoinMessage()
                    .replace("%n%", data.getLandName())
                    .replace("%master%", data.getMaster())
                    .replace("%member%", data.getMember().keySet().toString())+"\n\n\n\n");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        move.remove(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onPlayer(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if(notCancel(player, event.getBlock())) {
            if (notHasPermission(player, event.getBlock(), LandSetting.PLACE)) {
                event.setCancelled();
            }
        }else{
            event.setCancelled();
        }
    }

    @EventHandler
    public void onPlayerGiveLand(PlayerGiveLandEvent event){
        Player player = event.getPlayer();
        LandData data = event.getData();
        if(data.isSell()){
            player.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().giveSellLandError);
            event.setCancelled();
        }
        if(data instanceof LandSubData){
            if(((LandSubData) data).isSellPlayer()){
                player.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().notHavePermission);
                event.setCancelled();
            }
        }else{
            for(LandSubData data1:data.getSubData()){
                if(data1.isSell()){
                    player.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule()
                            .getLanguage().masterGiveHaveLandSell.replace("%name%",data1.getLandName()));
                    event.setCancelled();
                    return;
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event){
        Player player = event.getPlayer();

        if(notCancel(player, event.getBlock())) {
            if (notHasPermission(player, event.getBlock(), LandSetting.BREAK)) {
                event.setCancelled();
            }
        }else{
            event.setCancelled();
        }
    }

    private boolean notCancel(Player player, Position position){
        LandData data = DataTool.getPlayerTouchArea(position);
        if(LandModule.getModule().getConfig().getProtectList().contains(position.level.getFolderName())) {
            if (data == null) {
                if(!player.isOp()){
                    player.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().protectLevel);
                    return false;
                }
            } else {
                return true;
            }
        }
        return true;
    }

    private boolean notHasPermission(Player player, Position position, LandSetting setting){
        LandData data = DataTool.getPlayerLandData(position);
        if(data != null){
            if(!data.hasPermission(player.getName(), setting)){
                player.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().notHavePermission
                        .replace("%title%",LandModule.getModule().getConfig().getTitle()));
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onLandSell(PlayerSellLandEvent event){
        Player player = event.getPlayer();
        LandData data = event.getData();
        if(data.isSell()){
            player.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().sellLandError);
            event.setCancelled();
        }

        if(data instanceof LandSubData){
            if(player.getName().equalsIgnoreCase(((LandSubData) data).getMasterData().getMaster())){
                //出售别人的子领地
                if(!((LandSubData) data).getMasterData().getMaster().equalsIgnoreCase(data.getMaster())){
                    //子领地领主非主领地领主
                    event.setMoney(0);
                    if(((LandSubData) data).isSellPlayer()) {
                        String member = data.getMaster();
                        Player player1 = Server.getInstance().getPlayer(member);
                        ((LandSubData) data).setSellPlayer(false);
                        if (player1 != null) {
                            player1.sendMessage(LandModule.getModule().getConfig().getTitle()
                                    + LandModule.getModule().getLanguage().sellOtherLand.replace("%name%", data.getLandName()));
                        }
                    }
                }
            }
        }else{
            for(LandSubData data1:data.getSubData()){
                if(data1.isSell()){
                    player.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule()
                            .getLanguage().masterSellHaveLandSell.replace("%name%",data1.getLandName()));
                    event.setCancelled();
                    return;
                }
            }
        }
    }



    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onUseChest(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (notCancel(player, player.getPosition())) {
            Item item = event.getItem();
            if(item == null){
                item = Item.get(0);
            }
            if (event.getBlock() instanceof BlockChest) {
                if (notHasPermission(player, event.getBlock(), LandSetting.LOCK_CHEST)) {
                    event.setCancelled();
                }
            }
            if (event.getBlock().getId() == 61 || event.getBlock().getId() == 62) {
                if (notHasPermission(player, event.getBlock(), LandSetting.FRAME)) {
                    event.setCancelled();
                }
            }
            if (item.getId() == 325) {
                if (notHasPermission(player, event.getBlock(), LandSetting.BUCKET)) {
                    event.setCancelled();
                }
            }
            if (item.getId() == 259) {
                if (notHasPermission(player, event.getBlock(), LandSetting.IGNITE)) {
                    event.setCancelled();
                }
            }
            if (event.getBlock().getId() == 389) {
                if (notHasPermission(player, event.getBlock(), LandSetting.SHOW_ITEM)) {
                    event.setCancelled();
                }
            }
        }else{
            event.setCancelled();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        if(notHasPermission(player, player, LandSetting.DROP)){
            event.setCancelled();
        }
    }

    @EventHandler
    public void onTouchLand(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK
                || event.getAction() == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK){
            LinkedList<Position> positions = new LinkedList<>();
            Item playerHand = event.getItem();
            if(playerHand.equals(LandModule.getModule().getConfig().getLandTool(),true)){
                Block block = event.getBlock();
                event.setCancelled();
                Language language = LandModule.getModule().getLanguage();
                if(!LandModule.getModule().pos.containsKey(player.getName()) || LandModule.getModule().pos.get(player.getName()).size() > 1){
                    if(LandModule.getModule().getConfig().getBlackList().contains(block.getLevel().getFolderName()) && !player.isOp()){
                        player.sendMessage(LandModule.getModule().getConfig().getTitle()+language.whiteWorld.replace("%level%",block.level.getFolderName()));
                        return;
                    }
                    Position position = new Position(block.getFloorX(), block.getFloorY()
                            ,block.getFloorZ(),block.getLevel());
                    positions.add(position);
                    player.sendMessage(LandModule.getModule().getConfig().getTitle()+language.playerSetPos1.replace("%pos%",DataTool.getPosToString(position)));
                    LandModule.getModule().pos.put(player.getName(), positions);
                }else{
                    Position position = new Position(block.getFloorX(),block.getFloorY()
                            ,block.getFloorZ(),block.getLevel());
                    positions = LandModule.getModule().pos.get(player.getName());
                    Position position1 = positions.get(0);
                    if(position1.getLevel().getFolderName().equalsIgnoreCase(block.getLevel().getFolderName())){
                        if(positions.size() > 1){
                            positions.remove(1);
                        }
                        positions.add(position);
                        LandModule.getModule().pos.put(player.getName(),positions);
                        //进行一次空值检测
                        try{
                            if(position1.getLevel() == null || position.getLevel() == null){
                                LandModule.getModule().pos.remove(player.getName());
                                player.sendMessage(LandModule.getModule().getConfig().getTitle()+language.playerSetPos2ErrorLevel);
                                return;
                            }
                        }catch (Exception e){
                            LandModule.getModule().pos.remove(player.getName());
                            player.sendMessage(LandModule.getModule().getConfig().getTitle()+language.playerSetPos2ErrorLevel);
                            return;
                        }
                        double money = DataTool.getLandMoney(new Vector(position1,position));
                        player.sendMessage(LandModule.getModule().getConfig().getTitle()+language.playerSetPos2.replace("%pos%",DataTool.getPosToString(position)).replace("%money%",
                                String.format("%.2f", money)));

                    }else{
                        LandModule.getModule().pos.remove(player.getName());
                        player.sendMessage(LandModule.getModule().getConfig().getTitle()+language.playerSetPos2ErrorLevel);
                    }
                }


            }

        }

    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event){
        if(event instanceof EntityDamageByEntityEvent){
            Entity entity1 = event.getEntity();
            Entity entity = ((EntityDamageByEntityEvent) event).getDamager();
            if(entity instanceof Player && entity1 instanceof Player){
                if(notHasPermission((Player) entity, entity.getLocation(), LandSetting.PVP)){
                    event.setCancelled();
                }
            }
            if(entity instanceof Player){
                if(notHasPermission((Player) entity, entity1.getLocation(), LandSetting.DAMAGE_ENTITY)){
                    event.setCancelled();
                }
            }
        }
    }
    @EventHandler
    public void onQuitLandEvent(PlayerQuitLandEvent event){
        LandData data = event.getData();
        Player player = event.getPlayer();
        for(LandSubData subData:data.getSubData()){
            if(subData.getMaster().equalsIgnoreCase(player.getName())){
                subData.close();
            }
        }
    }

    @EventHandler
    public void onCreateLandEvent(PlayerCreateLandEvent event){
        LandData data = event.getData();
        Player player = event.getPlayer();
        if(data instanceof LandSubData){
            if(!((LandSubData) data).getMasterData().getMaster().equalsIgnoreCase(player.getName())){
                if(!((LandSubData) data).getMasterData().hasPermission(player.getName(),LandSetting.SUB_ZONE)){
                    event.setCancelled();
                    player.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().notHavePermission);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onTransfer(PlayerTeleportEvent event){
        if(event.isCancelled()){
            return;
        }
        Player player = event.getPlayer();
        LandData data = DataTool.getPlayerLandData(event.getTo());
        if(data != null) {
            if (!data.hasPermission(player.getName(), LandSetting.TRANSFER)) {
                event.setCancelled();
            } else {
                player.sendMessage(LandModule.getModule().getConfig().getTitle()+LandModule.getModule().getLanguage().playerTransferLand.replace("%name%",data.getLandName()));
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        event.setCancelled(canTnt(event.getEntity()));
    }

    private boolean canTnt(Position pos){
        return DataTool.getPlayerTouchArea(pos) != null;
    }


    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        event.setCancelled(canTnt(event.getEntity()));
    }


}
