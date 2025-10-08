package cn.smallaswater.land.lands.data;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.*;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.item.EntityFishingHook;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.*;
import cn.nukkit.event.entity.*;
import cn.nukkit.event.player.*;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBucket;
import cn.nukkit.item.ItemFlintSteel;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.smallaswater.land.LandMainClass;
import cn.smallaswater.land.event.player.*;
import cn.smallaswater.land.lands.data.sub.LandSubData;
import cn.smallaswater.land.lands.settings.LandSetting;
import cn.smallaswater.land.lands.settings.OtherLandSetting;
import cn.smallaswater.land.manager.KeyHandleManager;
import cn.smallaswater.land.module.LandModule;
import cn.smallaswater.land.utils.DataTool;
import cn.smallaswater.land.utils.Language;
import cn.smallaswater.land.utils.Vector;

import java.util.*;


/**
 * @author 若水
 */
@SuppressWarnings("unused")
public class LandListener implements Listener {

    private final LinkedHashMap<Player, LandData> move = new LinkedHashMap<>();

    private boolean hasBlockBarrelClass = false;

    public LandListener() {
        try {
            Class.forName("cn.nukkit.block.BlockBarrel");
            this.hasBlockBarrelClass = true;
        } catch (Exception ignored) {

        }
    }

    @EventHandler
    public void onTransferMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (KeyHandleManager.isKey(player, "transfer")) {
            Location f = event.getFrom();
            Location t = event.getTo();
            if (f.getFloorX() != t.getFloorX() || f.getFloorY() != t.getFloorY() || f.getFloorZ() != t.getFloorZ()) {
                KeyHandleManager.addKey(player, "transferClose");
                player.sendMessage(LandModule.getModule().getConfig().getTitle() + LandModule.getModule().getLanguage().translateString("transfer_error"));
            }
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        LandData fromData = DataTool.getPlayerLandData(event.getFrom());
        if (fromData != null) {
            if (!fromData.getLandOtherSet().isOpen(OtherLandSetting.BLOCK_UPDATE)) {
                event.setCancelled();
                return;
            }
        }
        LandData toData = DataTool.getPlayerLandData(event.getTo());
        if (toData != null) {
            LandOtherSet landOtherSet = toData.getLandOtherSet();
            if (!landOtherSet.isOpen(OtherLandSetting.BLOCK_UPDATE)) {
                event.setCancelled();
            }
            if (!landOtherSet.isOpen(OtherLandSetting.WATER_OUT)) {
                if (event.getTo() instanceof BlockLiquid) {
                    event.setCancelled();
                }
            }
        }
    }

    @EventHandler
    public void onLiquidFlow(LiquidFlowEvent event) {
        LandData data = DataTool.getPlayerLandData(event.getSource());
        LandData toData = DataTool.getPlayerLandData(event.getTo());
        if (toData != null && (data == null || data != toData)) {
            if (!toData.getLandOtherSet().isOpen(OtherLandSetting.WATER_OUT)) {
                event.setCancelled();
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        LandData data = DataTool.getPlayerLandData(player);

        if (data != null) {

            if (notHasPermission(player, player.getPosition(), LandSetting.MOVE)) {
                event.setCancelled();
                player.knockBack(player, 0, (player.x - player.getLocation().x), (player.z - player.getLocation().z), 0.8);
                return;
            }
            if (!move.containsKey(player)) {
                moveJoinEvent(player, data, data);
            } else {
                LandData newData = move.get(player);
                if (!data.equals(newData)) {
                    moveJoinEvent(player, data, newData);
                }
            }
        } else {
            if (move.containsKey(player)) {
                data = move.get(player);
                if (data != null) {
                    String message = data.getQuitMessage();
                    moveQuitEvent(player, data, message);
                }
                move.remove(player);
            }
        }
    }

    private void moveQuitEvent(Player player, LandData data, String message) {
        PlayerMoveQuitLandEvent event2 = new PlayerMoveQuitLandEvent(player, data, message);
        Server.getInstance().getPluginManager().callEvent(event2);
        if (event2.isCancelled()) {
            return;
        }
        message = event2.getMessage();
        if (!"".equalsIgnoreCase(message)) {
            player.sendPopup(LandModule.getModule().getConfig().getTitle() + data.getQuitMessage()
                    .replace("%n%", data.getLandName())
                    .replace("%master%", data.getMaster())
                    .replace("%member%", data.getMember().keySet().toString()) + "\n\n\n\n");
        }
    }

    @EventHandler
    public void onInviteTimeOut(PlayerInviteTimeOutEvent event) {
        Player master = Server.getInstance().getPlayer(event.getMasterPlayerName());
        if (master != null) {
            master.sendMessage(LandModule.getModule().getConfig().getTitle() + LandModule.getModule().getLanguage().translateString("inviteTimeOut")
                    .replace("%p%", event.getMemberPlayerName())
                    .replace("%name%", event.data.getLandName()));
        }
    }

    private void moveJoinEvent(Player player, LandData data, LandData newData) {
        String message = data.getQuitMessage();
        move.put(player, data);
        PlayerMoveJoinLandEvent event1 = new PlayerMoveJoinLandEvent(player, newData, message);
        Server.getInstance().getPluginManager().callEvent(event1);
        if (event1.isCancelled()) {
            return;
        }
        message = event1.getMessage();
        if (!"".equalsIgnoreCase(message)) {

            player.sendPopup(LandModule.getModule().getConfig().getTitle() + data.getJoinMessage()
                    .replace("%n%", data.getLandName())
                    .replace("%master%", data.getMaster())
                    .replace("%member%", data.getMember().keySet().toString()) + "\n\n\n\n");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        KeyHandleManager.clearPlayerKey(e.getPlayer());
        move.remove(e.getPlayer());
        playerTouchLandCooling.remove(e.getPlayer());
    }

    @EventHandler
    public void onPlayer(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (notCancel(player, event.getBlock())) {
            if (notHasPermission(player, event.getBlock(), LandSetting.PLACE)) {
                event.setCancelled();
            }
        } else {
            event.setCancelled();
        }
    }

    @EventHandler
    public void onPlayerGiveLand(PlayerGiveLandEvent event) {
        Player player = event.getPlayer();
        LandData data = event.getData();
        if (data.isSell()) {
            player.sendMessage(LandModule.getModule().getConfig().getTitle() + LandModule.getModule().getLanguage().translateString("giveSellLandError"));
            event.setCancelled();
        }
        if (data instanceof LandSubData) {
            if (((LandSubData) data).isSellPlayer()) {
                player.sendMessage(LandModule.getModule().getConfig().getTitle() + LandModule.getModule().getLanguage().translateString("notHavePermission"));
                event.setCancelled();
            }
        } else {
            for (LandSubData data1 : data.getSubData()) {
                if (data1.isSell()) {
                    player.sendMessage(LandModule.getModule().getConfig().getTitle() + LandModule.getModule()
                            .getLanguage().translateString("masterGiveHaveLandSell").replace("%name%", data1.getLandName()));
                    event.setCancelled();
                    return;
                }
            }
        }
    }


    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (notCancel(player, event.getBlock())) {
            if (notHasPermission(player, event.getBlock(), LandSetting.BREAK)) {
                event.setCancelled();
            }
        } else {
            event.setCancelled();
        }
    }

    private boolean notCancel(Player player, Position position) {
        //点击空气一般是食用物品，不会操作地图，可以直接忽略
        if (position instanceof Block) {
            Block block = (Block) position;
            if (block.getId() == BlockID.AIR) {
                return true;
            }
        }
        LandData data = DataTool.getPlayerTouchArea(position);
        if (LandModule.getModule().getConfig().getProtectList().contains(position.level.getFolderName())) {
            if (data == null) {
                if (!player.isOp()) {
                    if (LandMainClass.MAIN_CLASS.getModule().getConfig().isEchoProtectListMessage()) {
                        player.sendMessage(LandModule.getModule().getConfig().getTitle() + LandModule.getModule().getLanguage().translateString("protectLevel"));
                    }
                    return false;
                }
            } else {
                return true;
            }
        }
        return true;
    }

    private boolean notHasPermission(Player player, Position position, LandSetting setting) {
        LandData data = DataTool.getPlayerLandData(position);
        if (data != null) {
            if (!data.hasPermission(player.getName(), setting)) {
                player.sendMessage(LandModule.getModule().getConfig().getTitle() + LandModule.getModule().getLanguage().translateString("notHavePermission")
                        .replace("%title%", LandModule.getModule().getConfig().getTitle())
                        .replace("%setting%", setting.getName()));
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onLandSell(PlayerSellLandEvent event) {
        Player player = event.getPlayer();
        LandData data = event.getData();
        if (data.isSell()) {
            player.sendMessage(LandModule.getModule().getConfig().getTitle() + LandModule.getModule().getLanguage().translateString("sellLandError"));
            event.setCancelled();
        }

        if (data instanceof LandSubData) {
            if (player.getName().equalsIgnoreCase(((LandSubData) data).getMasterData().getMaster())) {
                //出售别人的子领地
                if (!((LandSubData) data).getMasterData().getMaster().equalsIgnoreCase(data.getMaster())) {
                    //子领地领主非主领地领主
                    event.setMoney(0);
                    if (((LandSubData) data).isSellPlayer()) {
                        String member = data.getMaster();
                        Player player1 = Server.getInstance().getPlayer(member);
                        ((LandSubData) data).setSellPlayer(false);
                        if (player1 != null) {
                            player1.sendMessage(LandModule.getModule().getConfig().getTitle()
                                    + LandModule.getModule().getLanguage().translateString("sellOtherLand").replace("%name%", data.getLandName()));
                        }
                    }
                }
            }
        } else {
            for (LandSubData data1 : data.getSubData()) {
                if (data1.isSell()) {
                    player.sendMessage(LandModule.getModule().getConfig().getTitle() + LandModule.getModule()
                            .getLanguage().translateString("masterSellHaveLandSell").replace("%name%", data1.getLandName()));
                    event.setCancelled();
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onUseChest(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (notCancel(player, event.getBlock())) {
            Item item = event.getItem();
            if (item == null) {
                item = Item.get(0);
            }
            if (this.hasBlockBarrelClass) {
                if (event.getBlock() instanceof BlockBarrel) {
                    if (notHasPermission(player, event.getBlock(), LandSetting.LOCK_CHEST)) {
                        event.setCancelled();
                    }
                }
            }

            if (event.getBlock() instanceof BlockChest || event.getBlock() instanceof BlockShulkerBox || event.getBlock() instanceof BlockUndyedShulkerBox) {
                if (notHasPermission(player, event.getBlock(), LandSetting.LOCK_CHEST)) {
                    event.setCancelled();
                }
            }
            if (event.getBlock() instanceof BlockFurnaceBurning) {
                if (notHasPermission(player, event.getBlock(), LandSetting.FRAME)) {
                    event.setCancelled();
                }
            }
            if (item instanceof ItemBucket) {
                if (notHasPermission(player, event.getBlock(), LandSetting.BUCKET)) {
                    event.setCancelled();
                }

            }
            if (item instanceof ItemFlintSteel) {
                if (notHasPermission(player, event.getBlock(), LandSetting.IGNITE)) {
                    event.setCancelled();
                }

            }
            if (event.getBlock() instanceof BlockItemFrame) {
                if (notHasPermission(player, event.getBlock(), LandSetting.SHOW_ITEM)) {
                    event.setCancelled();
                }

            }

        } else {
            event.setCancelled();
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (notHasPermission(player, player, LandSetting.DROP)) {
            event.setCancelled();
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        //不拦截human实体和掉落物品实体
        if (entity instanceof EntityHuman || entity instanceof EntityItem) {
            return;
        }

        Position position = event.getPosition();
        LandData data = DataTool.getPlayerLandData(position);
        if (data != null) {
            if (!data.getLandOtherSet().isOpen(OtherLandSetting.ENTITY_SPAWN)) {
                entity.close();
            }
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        Position position = event.getBlock();
        LandData data = DataTool.getPlayerLandData(position);
        if (data != null) {
            if (!data.getLandOtherSet().isOpen(OtherLandSetting.FIRE)) {
                event.setCancelled();
            }
        }
    }

    @EventHandler
    public void onBlockUpData(BlockUpdateEvent event) {
        Position position = event.getBlock();
        LandData data = DataTool.getPlayerLandData(position);
        if (data != null) {
            if (!data.getLandOtherSet().isOpen(OtherLandSetting.BLOCK_UPDATE)) {
                event.setCancelled();
            }
            if (!data.getLandOtherSet().isOpen(OtherLandSetting.WATER)) {
                if (event.getBlock() instanceof BlockLiquid) {
                    event.setCancelled();
                }
            }
        }
    }

    private final HashSet<Player> playerTouchLandCooling = new HashSet<>();

    @EventHandler
    public void onTouchLand(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK
                || event.getAction() == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            LinkedList<Position> positions = new LinkedList<>();
            Item playerHand = event.getItem();
            if (playerHand.equals(LandModule.getModule().getConfig().getLandTool(), true)) {
                event.setCancelled();
                if (this.playerTouchLandCooling.contains(player)) {
                    return;
                }
                this.playerTouchLandCooling.add(player);
                Server.getInstance().getScheduler().scheduleDelayedTask(LandMainClass.MAIN_CLASS, () -> this.playerTouchLandCooling.remove(player), 10);
                Block block = event.getBlock();
                Language language = LandModule.getModule().getLanguage();
                if (!LandModule.getModule().pos.containsKey(player.getName()) || LandModule.getModule().pos.get(player.getName()).size() > 1) {
                    if (LandModule.getModule().getConfig().getBlackList().contains(block.getLevel().getFolderName()) && !player.isOp()) {
                        if (LandMainClass.MAIN_CLASS.getModule().getConfig().isEchoBlackListMessage()) {
                            player.sendMessage(LandModule.getModule().getConfig().getTitle() + language.translateString("whiteWorld").replace("%level%", block.level.getFolderName()));
                            return;
                        }
                        return;
                    }
                    Position position = new Position(block.getFloorX(), block.getFloorY()
                            , block.getFloorZ(), block.getLevel());
                    positions.add(position);
                    player.sendMessage(LandModule.getModule().getConfig().getTitle() + language.translateString("playerSetPos1").replace("%pos%", DataTool.getPosToString(position)));
                    LandModule.getModule().pos.put(player.getName(), positions);
                } else {
                    Position position = new Position(block.getFloorX(), block.getFloorY()
                            , block.getFloorZ(), block.getLevel());
                    positions = LandModule.getModule().pos.get(player.getName());
                    Position position1 = positions.get(0);
                    if (position1.getLevel().getFolderName().equalsIgnoreCase(block.getLevel().getFolderName())) {
                        if (positions.size() > 1) {
                            positions.remove(1);
                        }
                        positions.add(position);
                        LandModule.getModule().pos.put(player.getName(), positions);
                        //进行一次空值检测
                        try {
                            if (position1.getLevel() == null || position.getLevel() == null) {
                                LandModule.getModule().pos.remove(player.getName());
                                player.sendMessage(LandModule.getModule().getConfig().getTitle() + language.translateString("playerSetPos2ErrorLevel"));
                                return;
                            }
                        } catch (Exception e) {
                            LandModule.getModule().pos.remove(player.getName());
                            player.sendMessage(LandModule.getModule().getConfig().getTitle() + language.translateString("playerSetPos2ErrorLevel"));
                            return;
                        }
                        double money = DataTool.getLandMoney(new Vector(position1, position));
                        player.sendMessage(LandModule.getModule().getConfig().getTitle() + language.translateString("playerSetPos2").replace("%pos%", DataTool.getPosToString(position)).replace("%money%",
                                String.format("%.2f", money)));

                    } else {
                        LandModule.getModule().pos.remove(player.getName());
                        player.sendMessage(LandModule.getModule().getConfig().getTitle() + language.translateString("playerSetPos2ErrorLevel"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            Entity entity = event.getEntity();
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (damager instanceof Player) {
                if (entity instanceof Player) {
                    if (notHasPermission((Player) damager, damager.getLocation(), LandSetting.PVP)) {
                        if (event instanceof EntityDamageByChildEntityEvent) {
                            if (((EntityDamageByChildEntityEvent) event).getDamager() instanceof EntityFishingHook) {
                                ((Player) damager).stopFishing(false);
                            }
                        }
                        event.setCancelled();
                    }
                } else {
                    if (notHasPermission((Player) damager, entity.getLocation(), LandSetting.DAMAGE_ENTITY)) {
                        event.setCancelled();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuitLandEvent(PlayerQuitLandEvent event) {
        LandData data = event.getData();
        Player player = event.getPlayer();
        for (LandSubData subData : data.getSubData()) {
            if (subData.getMaster().equalsIgnoreCase(player.getName())) {
                subData.close();
            }
        }
    }

    @EventHandler
    public void onCreateLandEvent(PlayerCreateLandEvent event) {
        LandData data = event.getData();
        Player player = event.getPlayer();
        if (data instanceof LandSubData) {
            if (!((LandSubData) data).getMasterData().getMaster().equalsIgnoreCase(player.getName())) {
                if (!((LandSubData) data).getMasterData().hasPermission(player.getName(), LandSetting.SUB_ZONE)) {
                    event.setCancelled();
                    player.sendMessage(LandModule.getModule().getConfig().getTitle() + LandModule.getModule().getLanguage().translateString("notHavePermission"));
                }
            }
        }
    }

    @EventHandler
    public void onTransfer(PlayerTeleportEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        LandData data = DataTool.getPlayerLandData(event.getTo());
        if (data != null) {
            if (!data.hasPermission(player.getName(), LandSetting.TRANSFER)) {
                event.setCancelled();
            } else {
                player.sendMessage(LandModule.getModule().getConfig().getTitle() + LandModule.getModule().getLanguage().translateString("playerTransferLand").replace("%name%", data.getLandName()));
            }
        }
    }

    /**
     * 指定坐标是否存在禁止破坏限制
     *
     * @param pos 坐标
     * @return 是否存在禁止破坏限制
     */
    private boolean canNotTnt(Position pos) {
        LandData landData = DataTool.getPlayerTouchArea(pos);
        return landData != null && !landData.getDefaultSetting().getSetting(LandSetting.BREAK.getName());
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        if (this.canNotTnt(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (this.canNotTnt(event.getEntity())) {
            event.setCancelled(true);
        } else {
            List<Block> blockList = new ArrayList<>(event.getBlockList());
            for (Block block : event.getBlockList()) {
                if (this.canNotTnt(block)) {
                    blockList.remove(block);
                }
            }
            event.setBlockList(blockList);
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (player == null || block == null) {
            return;
        }
        if (this.notCancel(player, block)) {
            if (this.notHasPermission(player, block, LandSetting.CHANGE_SIGN)) {
                event.setCancelled();
                player.sendMessage(LandModule.getModule().getConfig().getTitle() + LandModule.getModule().getLanguage().translateString("notHavePermission"));
            }
        }
    }

}
