package com.piaofu.owner.superholidaygift.guide;

import com.piaofu.owner.superholidaygift.SuperHolidayGift;
import com.piaofu.owner.superholidaygift.taggift.TagGift;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Arrays;

//藏宝图
public class Guide {
    //半径
    private static short r;
    private static int line;

    //配置量
    private static String guildP;
    private static String guildE;
    private static String guildH;


    public static void initConfig() {
        r = (short)SuperHolidayGift.plugin.getConfig().getInt("guideR");
        line = r * 2 + 1;
        guildP = SuperHolidayGift.plugin.getConfig().getString("guideP");
        guildE = SuperHolidayGift.plugin.getConfig().getString("guideE");
        guildH = SuperHolidayGift.plugin.getConfig().getString("guideH");
    }

    public static String getPlayerString(Player player) {
        return stringArrayFormat(getPlayerNear(player));
    }

    private static String[][] getPlayerNear(Player player) {
        String[][] near = new String[line][line];
        int pBX = player.getLocation().getBlockX();
        int pBZ = player.getLocation().getBlockZ();
        World world = player.getWorld();
        for(int x = 0; x < line; x += 1) {
            for(int y = 0; y < line; y+=1) {
                near[x][y] = getPointChar(r - x, y - r, pBX, pBZ, world);
            }
        }
        return near;
    }

    //玩家中心点看做（0,0），第一个点的差值为（-r，-r）
    //第一个点看做（0,0），玩家中心点的坐标为（r，r）
    //但是因为MC的机制，上述做法会反向
    //故采用如下判定方式
    /**
     *  (2r,0)-------------------(0,0)
     *
     *              (r,r)
     *
     *  (2r,2r)------------------(0,2r)
     */
    private static String getPointChar(int a, int b, int pBX, int pBZ, World world) {
        if(a == 0 && b == 0)
            return guildP;
        if(hasGift(pBX + a, pBZ + b, world)) {
            return guildH;
        }
        return guildE;
    }

    private static boolean hasGift(int pBX, int pBZ, World world) {
        for (TagGift gift : TagGift.tagGift) {
            if (gift.getSpawnLocation().getWorld() == world) {
                if(gift.getTagNowLoc().getX() == pBX && gift.getTagNowLoc().getZ() == pBZ)
                    return true;
            }
        }
        return false;
    }

    private static String stringArrayFormat(String[][] strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for(String[] s : strings) {
            for(String ss : s) {
                stringBuilder.append(ss);
            }
            stringBuilder.append("<next");
        }
        return stringBuilder.toString();
    }
}
