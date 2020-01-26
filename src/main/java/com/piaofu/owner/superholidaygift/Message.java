package com.piaofu.owner.superholidaygift;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * 动态消息添加类，使用到后将在lang.yml中自动生成语言文档，可修改
 */
public enum Message {

    NONE_PERMISSION("您没有权限使用"),
    HAS_NOT_COMMAND("不存在的指令"),
    IS_NOT_THIS_COMMAND("这不是一个当前端能使用的指令"),
    LODE_SUCCESS("§c[SuperHolidayGift] >> 载入成功"),
    LODE_LOC_SUCCESS("§b>> 成功加载区域{0}个"),
    LODE_GIFT_SUCCESS("§b>> 成功加载礼包{0}个")


    ;
    private String text;

    Message(String s) {
        this.text = s;
    }

    public static String format(String msg, String... args) {
        if (args == null) {
            return msg.replace("&", "§");
        }
        for (int i = 0; i < args.length; ++i) {
            msg = msg.replace("{" + i + "}", (args[i] == null) ? "null" : args[i]);
        }
        return msg.replace("&", "§");
    }

    public static String getMsg(Message message, String... args) {
        String t = lang.getString(message.name());
        if(t == null || t.length() < 1) {
            try {
                wirteDefault(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            t = message.text;
        }
        return format(t, args);

    }
    private static void wirteDefault(Message message) throws IOException {
        lang.set(message.name(), message.text);
        lang.save(SuperHolidayGift.plugin.getDataFolder() + File.separator + "lang.yml");
    }
    public static void loadLangData() {
        File file = new File(SuperHolidayGift.plugin.getDataFolder(),"lang.yml");
        if(!file.exists()) {
            SuperHolidayGift.plugin.saveResource("lang.yml", false);
        }
        lang = YamlConfiguration.loadConfiguration(file);
    }
    private static YamlConfiguration lang;
}
