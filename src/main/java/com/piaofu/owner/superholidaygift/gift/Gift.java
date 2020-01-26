package com.piaofu.owner.superholidaygift.gift;

import com.piaofu.owner.superholidaygift.Message;
import com.piaofu.owner.superholidaygift.SuperHolidayGift;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
/**
 * 此类为礼物存储处理对象
 */
public class Gift {
    private static List<Gift> gifts;
    private String giftName;
    private String url;
    private int width;
    private int height;
    private double xs;
    private double ys;
    private String display;
    private double opp;
    private String getM;
    private String failM;
    private List<String> commands;
    private String titleShow;



    public Gift(String giftName, String url, int width, int height, double xs, double ys, String display, double opp, String getM, String failM, List<String> commands, String titleShow) {
        this.giftName = giftName;
        this.url = url;
        this.width = width;
        this.height = height;
        this.xs = xs;
        this.ys = ys;
        this.display = display;
        this.opp = opp;
        this.getM = getM;
        this.failM = failM;
        this.commands = commands;
        this.titleShow = titleShow;
    }

    public static Gift getGift(String name) {
        for(Gift gift : gifts) {
            if(gift.giftName.equalsIgnoreCase(name)) {
                return gift;
            }
        }
        return null;
    }

    public static void onLoadGift() {
        gifts = new ArrayList<>();
        ConfigurationSection configurationSection = SuperHolidayGift.plugin.getConfig().getConfigurationSection("giftList");
        if (configurationSection != null) {
            configurationSection.getKeys(false).forEach(giftName -> {
                gifts.add(new Gift(
                        giftName,
                        configurationSection.getString(giftName + ".url"),
                        configurationSection.getInt(giftName + ".width"),
                        configurationSection.getInt(giftName + ".height"),
                        configurationSection.getDouble(giftName + ".xs"),
                        configurationSection.getDouble(giftName + ".ys"),
                        configurationSection.getString(giftName + ".display"),
                        configurationSection.getDouble(giftName + ".oppt"),
                        configurationSection.getString(giftName + ".getM"),
                        configurationSection.getString(giftName + ".failM"),
                        configurationSection.getStringList(giftName + ".commands"),
                        configurationSection.getString(giftName + ".show")
                ));
            });
        }
        Bukkit.getConsoleSender().sendMessage(Message.getMsg(Message.LODE_GIFT_SUCCESS, String.valueOf(gifts.size())));
    }


    public double getOpp() {
        return opp;
    }

    public String getGetM() {
        return getM;
    }

    public String getFailM() {
        return failM;
    }

    public void givePlayer(Player player) {
        this.commands.forEach(command ->{
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("[player]", player.getName()));
        });
    }

    public static List<Gift> getGifts() {
        return gifts;
    }

    public String getGiftName() {
        return giftName;
    }

    public String getUrl() {
        return url;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getXs() {
        return xs;
    }

    public double getYs() {
        return ys;
    }

    public List<String> getCommands() {
        return commands;
    }

    public String getDisplay() {
        return display;
    }

    public String getTitleShow() {
        return titleShow;
    }
}
