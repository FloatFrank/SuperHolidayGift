package com.piaofu.owner.superholidaygift.taggift;

import com.piaofu.owner.superholidaygift.SuperHolidayGift;
import com.piaofu.owner.superholidaygift.gift.Gift;
import com.piaofu.owner.superholidaygift.location.SpawnLocation;
import lk.vexview.api.VexViewAPI;
import lk.vexview.tag.components.VexImageTag;
import lk.vexview.tag.components.VexTag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
/**
 * 此类为Tag最终对象，其中混合了礼物、区域、附加文字Tag等信息，整个Tag的动画逻辑，以及判断逻辑都在此类中
 */
public class TagGift implements ITagGift{

    public static List<TagGift> tagGift = new ArrayList<>();
    private String id;
    private Gift gift;
    private TagSimpleBuilder tagSimpleBuilder;
    private SpawnLocation spawnLocation;
    private TagNowLoc tagNowLoc;
    //文字相关配置
    private static double textHeight;
    private static boolean textEnable;
    private static boolean textBack;
    //文字位置偏移
    private static double xMoveT;
    private static double yMoveT;
    private static double zMoveT;

    //图片位置偏移参数
    private static double xMove;
    private static double yMove;
    private static double zMove;
    //自动回收的时间
    private static int delTime;
    //自动计时销毁
    private static int delTimeNow = 0;


    private static DropWay dropWay;


    private boolean display = true;
    private boolean stop = false;

    private static double down;
    private static int speed;

    //构造函数
    public TagGift(Gift gift, SpawnLocation spawnLocation, TagNowLoc tagNowLoc) {
        this.id = UUID.randomUUID().toString();
        this.gift = gift;
        this.tagSimpleBuilder = new TagSimpleBuilder(gift.getUrl(), gift.getWidth(), gift.getHeight(), (float) gift.getXs(), (float) gift.getYs());
        this.spawnLocation = spawnLocation;
        this.tagNowLoc = tagNowLoc;
        tagGift.add(this);
        start();
    }

    //配置文件读取
    public static void loadConfig() {
        down = SuperHolidayGift.plugin.getConfig().getDouble("down");
        speed = SuperHolidayGift.plugin.getConfig().getInt("speed");
        xMove = SuperHolidayGift.plugin.getConfig().getDouble("imageMove.x");
        yMove = SuperHolidayGift.plugin.getConfig().getDouble("imageMove.y");
        zMove = SuperHolidayGift.plugin.getConfig().getDouble("imageMove.z");
        xMoveT = SuperHolidayGift.plugin.getConfig().getDouble("textMove.x");
        yMoveT = SuperHolidayGift.plugin.getConfig().getDouble("textMove.y");
        zMoveT = SuperHolidayGift.plugin.getConfig().getDouble("textMove.z");
        dropWay = DropWay.valueOf(SuperHolidayGift.plugin.getConfig().getString("dropWay"));
        textHeight = SuperHolidayGift.plugin.getConfig().getDouble("textHeight");
        textEnable = SuperHolidayGift.plugin.getConfig().getBoolean("textEnable");
        textBack = SuperHolidayGift.plugin.getConfig().getBoolean("textBack");
        delTime = SuperHolidayGift.plugin.getConfig().getInt("delTime");
    }

    public static double getXMove() {
        return xMove;
    }

    public static double getYMove() {
        return yMove;
    }

    public static double getZMove() {
        return zMove;
    }

    public static DropWay getDropWay() {
        return dropWay;
    }

    public static double getTextHeight() {
        return textHeight;
    }

    public static boolean isTextEnable() {
        return textEnable;
    }

    public static boolean isTextBack() {
        return textBack;
    }

    public static double getXMoveT() {
        return xMoveT;
    }

    public static double getYMoveT() {
        return yMoveT;
    }

    public static double getZMoveT() {
        return zMoveT;
    }

    //多线程开启检测，下降遇到最低点或者方块时将停止下降，并在每次执行的时候对玩家的位置进行判断
    @Override
    public void start() {
        new BukkitRunnable(){

            @Override
            public void run() {
                delTimeNow += speed;
                if(delTimeNow / 1200 >= delTime) {
                    cancel();
                    delete();
                }
                Bukkit.getOnlinePlayers().forEach(item -> hasPlayerCatch(item));
                if(!stop) {
                    refresh();
                }
                if(!display) {
                    cancel();
                    delete();
                }
            }
        }.runTaskTimerAsynchronously(SuperHolidayGift.plugin, 10, speed);
    }

    //检测玩家是否在附近的具体实现
    private boolean isNear(Player player) {
        if(player.getWorld() != spawnLocation.getWorld()) {
            return false;
        }
        Location location = player.getLocation();
        int pX = location.getBlock().getX();
        int pY = location.getBlock().getY();
        int pZ = location.getBlock().getZ();
        int gX = (int)tagNowLoc.getX();
        int gY = (int)tagNowLoc.getY();
        int gZ = (int)tagNowLoc.getZ();
        return Math.abs(pX - gX) < 3 && Math.abs(pY - gY) < 3 && Math.abs(pZ - gZ) < 3;
    }

    //随机实现
    private boolean randomGet() {
        return gift.getOpp() > Math.random();
    }

    //某个玩家是否已经触碰到了Tag
    private void hasPlayerCatch(Player player) {
        if(!display) {
            return;
        }

        Location location = player.getLocation();
       if(isNear(player)){
            //玩家已经触碰到了这个Tag
            display = false;
            //随机判断
            if(randomGet()) {
                gift.givePlayer(player);
                player.sendMessage(gift.getGetM());
            }else {
                player.sendMessage(gift.getFailM());
            }
            stop();
        }
    }

    //下降方法
    @Override
    public void down() {
        double nY = tagNowLoc.getY() - down;
        tagNowLoc.setY(nY);
    }

    //判断下面一个是否是方块或虚空，根据掉落方式改变返回值
    private boolean underIsBlock() {
        if(tagNowLoc.getY() < 1) {
            return false;
        }
        Block block = spawnLocation.getWorld().getBlockAt((int)tagNowLoc.getX(), (int)tagNowLoc.getY()-1, (int)tagNowLoc.getZ());
        return !block.getType().equals(Material.AIR);
    }

    //判断是否已经达到范围底部
    private boolean isLast() {
        return tagNowLoc.getY() <= spawnLocation.getFrom().getBlockY() + 1;
    }

    //tag停止移动方法，当此处关闭后调度器执行将会停止tag更新
    @Override
    public void stop() {
        stop = true;
    }

    //tag刷新逻辑方法
    @Override
    public void refresh() {
        down();
        switch (dropWay) {
            //范围或者全部随机的话都是检测是否是方块
            case BOTH_BLOCK_FW:
            case BOTH_ALL_RANDOM:
                if(underIsBlock() || isLast()) {
                    stop();
                    return;
                }
                break;
            case BOTH_BLOCK_XK:
                //仅判断是否是虚空或方块
                if(underIsBlock()) {
                    stop();
                    return;
                }
                break;
        }
        VexViewAPI.removeWorldTag(spawnLocation.getWorld(), id);
        VexViewAPI.removeWorldTag(spawnLocation.getWorld(), id + "_t");
        VexTag[] vexImageTag = getNewVexTag();
        VexViewAPI.addWorldTag(spawnLocation.getWorld(), vexImageTag[0]);
        if(TagGift.isTextEnable())
            VexViewAPI.addWorldTag(spawnLocation.getWorld(), vexImageTag[1]);
    }

    //获取此TagGift的新VexTag对象
    @Override
    public VexTag[] getNewVexTag() {
        return tagSimpleBuilder.getTag(id, tagNowLoc, gift);
    }


    //tag内部删除方法
    @Override
    public void delete() {
        stop();
        display = false;
        VexViewAPI.removeWorldTag(spawnLocation.getWorld(), id);
        if(isTextEnable())
             VexViewAPI.removeWorldTag(spawnLocation.getWorld(), id+"_t");
        tagGift.remove(this);
    }

    //tag外部彻清方法
    public void deleteTag() {
        stop();
        display = false;
        VexViewAPI.removeWorldTag(spawnLocation.getWorld(), id);
        if(isTextEnable())
            VexViewAPI.removeWorldTag(spawnLocation.getWorld(), id+"_t");
    }

    //tag外部彻清方法,附加清理List
    public static void clearTagGift() {
        tagGift.clear();
    }

    public SpawnLocation getSpawnLocation() {
        return spawnLocation;
    }

    public TagNowLoc getTagNowLoc() {
        return tagNowLoc;
    }

    public static int getNum(World world) {
        return (int)tagGift.stream().filter(item -> item.getSpawnLocation().getWorld() == world).count();
    }

    public static int getSpe() {
        List<String> s = new ArrayList<>();
        tagGift.forEach(item -> {
            if(!s.contains(item.gift.getGiftName())) {
                s.add(item.gift.getGiftName());
            }
        });
        return s.size();
    }

    public static String near(Player player) {
        String a = "无";
        int min = 99999;
        for (TagGift item : tagGift) {
            if (item.getSpawnLocation().getWorld() != player.getWorld())
                continue;
            int all = (int) (item.tagNowLoc.getX() + item.tagNowLoc.getZ() + item.tagNowLoc.getY());
            if(all < min) {
                min = all;
                a = "[" + item.tagNowLoc.getX() + "," + item.tagNowLoc.getY() + "," + item.tagNowLoc.getZ() + "]";
            }
        }
        return a;
    }
}
