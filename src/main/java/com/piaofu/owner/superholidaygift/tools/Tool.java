package com.piaofu.owner.superholidaygift.tools;

import com.piaofu.owner.superholidaygift.gift.Gift;
import com.piaofu.owner.superholidaygift.location.SpawnLocation;
import com.piaofu.owner.superholidaygift.taggift.DropWay;
import com.piaofu.owner.superholidaygift.taggift.TagGift;
import com.piaofu.owner.superholidaygift.taggift.TagNowLoc;
import org.bukkit.Bukkit;

/**
 * 此类为指令以及玩家与功能衔接的工具类
 */
public class Tool {
    public static void startToSpawnGift(SpawnLocation spawnLocation, Gift gift, int num) {
        int fromX = spawnLocation.getFrom().getBlockX();
        int fromY = spawnLocation.getFrom().getBlockY();
        int fromZ = spawnLocation.getFrom().getBlockZ();
        int toX = spawnLocation.getTo().getBlockX();
        int toY = spawnLocation.getTo().getBlockY();
        int toZ = spawnLocation.getTo().getBlockZ();
        //随机在范围内取位置
        for(int i = 0; i<num ;i++) {
            int x = getRandom(fromX, toX);
            int y = toY;
            if(TagGift.getDropWay().equals(DropWay.BOTH_ALL_RANDOM)) {
                y = getRandom(fromY, toY);
            }
            int z = getRandom(fromZ, toZ);
            new TagGift(gift, spawnLocation, new TagNowLoc(x, y, z));
        }
        sendTitle(gift, num);
    }
    public static int getRandom(int start,int end) {
        return (int) (Math.random()*(end-start+1)+start);
    }

    private static void sendTitle(Gift gift, int num) {
        String[] info;
        info = gift.getTitleShow().replace("[INFO]", gift.getDisplay()).replace("[NUM]", String.valueOf(num)).split(">line");
        if(info.length > 1) {
            Bukkit.getOnlinePlayers().forEach(player ->{
                player.sendTitle(info[0], info[1]);
            });
            return;
        }
        Bukkit.getOnlinePlayers().forEach(player ->{
            player.sendTitle(info[0], "");
        });
    }
}
