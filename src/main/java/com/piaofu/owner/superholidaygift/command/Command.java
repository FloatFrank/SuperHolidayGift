package com.piaofu.owner.superholidaygift.command;

import com.piaofu.owner.superholidaygift.Message;
import com.piaofu.owner.superholidaygift.SuperHolidayGift;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.stream.IntStream;

public class Command implements CommandExecutor {
    public static String rep = ">>[可用指令]  /{0} {1}&7 -&c {2}";
    public static String color = SuperHolidayGift.plugin.getConfig().getString("commandColor");
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        SenderType type = SenderType.CONSOLE;
        if (sender instanceof Player) {
            if (!sender.hasPermission(SuperHolidayGift.plugin.getName() + ".use")) {
                sender.sendMessage(Message.getMsg(Message.NONE_PERMISSION));
                return true;
            }
            type = SenderType.PLAYER;
        }
        if (args.length == 0) {
            String col = color;
            for (PlayerCommand sub : CommandsHolder.commandMap.keySet()) {
                if (!sub.label().equalsIgnoreCase(label)) {
                    continue;
                }
                if (this.contains(sub.type(), type) && sender.hasPermission(SuperHolidayGift.plugin.getName() + "." + sub.arg()[0])) {
                    col = (col.equals(color) ? "" : color);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageFormat.format(col + rep, label, outCommandListString(sub.arg()) , sub.msg())));
                }
            }
            return true;
        }
        for (PlayerCommand sub2 : CommandsHolder.commandMap.keySet()) {
            if (!sub2.label().equalsIgnoreCase(label)) {
                continue;
            }
            if (args.length < sub2.arg().length) {
                continue;
            }
            if(!caseStringList(args, sub2.arg())) {
                continue;
            }
            if (!this.contains(sub2.type(), type)) {
                sender.sendMessage(Message.getMsg(Message.IS_NOT_THIS_COMMAND));
                return true;
            }
            if (!sender.hasPermission(SuperHolidayGift.plugin.getName() + "." + outCommandListStringPermission(args))) {
                sender.sendMessage(Message.getMsg(Message.NONE_PERMISSION));
                return true;
            }
            try {
                    CommandsHolder.commandMap.get(sub2).invoke(CommandsHolder.class, sender, args);
            }
            catch (IllegalAccessException | InvocationTargetException ex2) {

                ex2.printStackTrace();
            }
            return true;
        }
            sender.sendMessage(Message.getMsg(Message.HAS_NOT_COMMAND));
        return true;
    }
    private boolean contains(SenderType[] type1, SenderType type2) {
        return IntStream.range(0, type1.length).anyMatch(i -> type1[i].equals(SenderType.ALL) || type1[i].equals(type2));
    }
    private boolean caseStringList(String[] longC, String[] shortC) {
        int lengthS = shortC.length;
        int i = 0;
        for(;i < lengthS; i++) {
            if(!longC[i].equalsIgnoreCase(shortC[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 具象化帮助字符串
     */
    public static String outCommandListString(String[] strings) {
        StringBuilder tt = new StringBuilder();
        for(String t : strings) {
            tt.append(t).append(" ");
        }
        return tt.toString();
    }

    /**
     * 具象化权限要求
     */
    public static String outCommandListStringPermission(String[] strings) {
        StringBuilder tt = new StringBuilder();
        int i = 1;
        for(String t : strings) {
            tt.append(t);
            if(strings.length > i)
                tt.append(".");
            i++;
        }
        return tt.toString();
    }
}
