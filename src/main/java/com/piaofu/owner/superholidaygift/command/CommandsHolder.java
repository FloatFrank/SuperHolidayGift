package com.piaofu.owner.superholidaygift.command;


import com.piaofu.owner.superholidaygift.SuperHolidayGift;
import com.piaofu.owner.superholidaygift.gift.Gift;
import com.piaofu.owner.superholidaygift.guide.Gui;
import com.piaofu.owner.superholidaygift.guide.Guide;
import com.piaofu.owner.superholidaygift.location.SpawnLocation;
import com.piaofu.owner.superholidaygift.taggift.TagGift;
import com.piaofu.owner.superholidaygift.tools.Tool;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * 此类为指令存储类，新增指令只需复制粘贴更改方法名即可实现，权限判断从lable.arg开始判断，例如holiday.spawn
 */
public class CommandsHolder {
    public static HashMap<PlayerCommand, Method> commandMap = new HashMap<>();
    @PlayerCommand(label = "holiday", arg = {"spawn"}, msg = "在这个区域天降该礼包")
    public static void spawn(CommandSender sender, String[] args) {
        switch (args.length) {
            case 1 :
            case 2 :
            case 3 : {
                sender.sendMessage("参数不正确，请使用/holiday spawn [生成区域] [生成礼包] [生成数量] 来生成礼包");
                return;
            }
            case 4 : {
                String locName = args[1];
                String giftName = args[2];
                int num = 0;
                try {
                    num = Integer.parseInt(args[3]);
                }catch (Exception e) {
                    sender.sendMessage("输入的生成数量不正确");
                    return;
                }

                SpawnLocation spawnLocation = SpawnLocation.getSpawnLocation(locName);
                Gift gift = Gift.getGift(giftName);
                if(spawnLocation == null) {
                    sender.sendMessage("该生成区域不存在，请检查指令或配置文件");
                    return;
                }
                if(gift == null) {
                    sender.sendMessage("该礼包不存在，请检查指令或配置文件");
                    return;
                }
                Tool.startToSpawnGift(spawnLocation, gift, num);
            }
        }
    }
    @PlayerCommand(label = "holiday", arg = {"reload"}, msg = "重载配置文件")
    public static void reload(CommandSender sender, String[] args) {
        SuperHolidayGift.plugin.configLoad(sender);
    }
    @PlayerCommand(label = "holiday", arg = {"clear"}, msg = "清理所有的礼包")
    public static void clear(CommandSender sender, String[] args) {
        TagGift.tagGift.forEach(TagGift::deleteTag);
        TagGift.clearTagGift();
        sender.sendMessage("已全部清理完成");
    }
    @PlayerCommand(label = "holiday", arg = {"guide", "get"}, msg = "在聊天框将宝藏列表输出", type = SenderType.PLAYER)
    public static void guideGet(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(Gui.blackList.contains(player.getUniqueId())) {
            player.sendMessage("冷却中, 请不要频繁使用该指令");
            return;
        }
        Gui.blackList.add(player.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                Gui.blackList.remove(player.getUniqueId());
            }
        }.runTaskLaterAsynchronously(SuperHolidayGift.plugin, 60);
        sender.sendMessage("§c=================  §b藏  宝  图 §c================= ");
        Arrays.asList(Guide.getPlayerString((Player)sender).split("<next")).forEach(sender::sendMessage);
        sender.sendMessage("§c=================            §c================= ");
    }
    @PlayerCommand(label = "holiday", arg = {"guide","sendall"}, msg = "为所有玩家在聊天框将宝藏列表输出")
    public static void guildSendAll(CommandSender sender, String[] args) {
        sender.sendMessage("成功发送给全体成员");
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage("§c=================  §b藏  宝  图 §c================= ");
            Arrays.asList(Guide.getPlayerString(player).split("<next")).forEach(item -> player.sendMessage(item));
            player.sendMessage("§c=================            §c================= ");
        });
    }
    @PlayerCommand(label = "holiday", arg = {"guide","send"}, msg = "为单独的一个玩家在聊天框将宝藏列表输出")
    public static void guildSend(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage("使用方法: /holiday guide send [玩家id]");
            return;
        }
        Player player = Bukkit.getPlayer(args[2]);
        if(player == null) {
            sender.sendMessage("该玩家不存在");
            return;
        }
        player.sendMessage("§c=================  §b藏  宝  图 §c================= ");
        Arrays.asList(Guide.getPlayerString((Player)player).split("<next")).forEach(player::sendMessage);
        player.sendMessage("§c=================            §c================= ");
        sender.sendMessage("发送成功");

    }
    @PlayerCommand(label = "holiday", arg = {"guide", "open"}, msg = "打开藏宝图GUI", type = SenderType.PLAYER)
    public static void guideOpen(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Gui.getGui(player.getUniqueId()).openGuideGui();
    }
}
