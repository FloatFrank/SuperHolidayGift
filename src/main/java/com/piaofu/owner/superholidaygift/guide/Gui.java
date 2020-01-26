package com.piaofu.owner.superholidaygift.guide;

import com.piaofu.owner.superholidaygift.SuperHolidayGift;
import com.piaofu.owner.superholidaygift.taggift.TagGift;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.OpenedVexGui;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.DynamicComponent;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexText;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 藏宝图VV可视化界面
 */
public class Gui extends VexGui{
    private static List<Gui> guiList = new ArrayList<>();
    private UUID player;
    private List<DynamicComponent> dynamicComponents = new ArrayList<>();
    //此GUI标识符及刷新按钮
    private static VexButton refreshButton;
    private static int w;
    private static int h;
    private static int textX;
    private static int textY;
    private static int subTextX;
    private static int subTextY;
    private static String url;
    private static int refreshButtonW;
    private static int refreshButtonH;
    private static int refreshButtonX;
    private static int refreshButtonY;
    private static String refreshButtonUrl;
    private static String refreshButtonUrl_;
    public static List<UUID> blackList = new ArrayList<>();

    private static void initButton() {
        refreshButton = new VexButton("superbutton", "刷新", refreshButtonUrl, refreshButtonUrl_, refreshButtonX, refreshButtonY, refreshButtonW, refreshButtonH, clicker -> {
            if(blackList.contains(clicker.getUniqueId())) {
                clicker.sendMessage("冷却中, 请不要频繁使用该按钮");
                return;
            }
            blackList.add(clicker.getUniqueId());
            new BukkitRunnable() {
                @Override
                public void run() {
                    blackList.remove(clicker.getUniqueId());
                }
            }.runTaskLaterAsynchronously(SuperHolidayGift.plugin, 60);

            refresh(clicker.getUniqueId());
        });
    }

    public Gui(UUID player) {
        super(url, -1, -1, w, h, w, h);
        this.player = player;
        guiList.add(this);
        this.addComponent(refreshButton);
        refresh();
    }

    public static void initConfig() {
        w = SuperHolidayGift.plugin.getConfig().getInt("gui.size.w");
        h = SuperHolidayGift.plugin.getConfig().getInt("gui.size.h");
        textX = SuperHolidayGift.plugin.getConfig().getInt("gui.text.x");
        textY = SuperHolidayGift.plugin.getConfig().getInt("gui.text.y");
        subTextX = SuperHolidayGift.plugin.getConfig().getInt("gui.subText.x");
        subTextY = SuperHolidayGift.plugin.getConfig().getInt("gui.subText.y");
        url = SuperHolidayGift.plugin.getConfig().getString("gui.url");
        refreshButtonW = SuperHolidayGift.plugin.getConfig().getInt("gui.refreshButton.w");
        refreshButtonH = SuperHolidayGift.plugin.getConfig().getInt("gui.refreshButton.h");
        refreshButtonX = SuperHolidayGift.plugin.getConfig().getInt("gui.refreshButton.x");
        refreshButtonY = SuperHolidayGift.plugin.getConfig().getInt("gui.refreshButton.y");
        refreshButtonUrl = SuperHolidayGift.plugin.getConfig().getString("gui.refreshButton.url");
        refreshButtonUrl_ = SuperHolidayGift.plugin.getConfig().getString("gui.refreshButton.url_");
        initButton();
    }

    public static void removeGui(UUID uuid) {
        Gui gui = getGui(uuid);
        guiList.remove(gui);
    }

    public static Gui getGuiEmp(UUID uuid) {
        for (Gui item : guiList) {
            if (item.player.equals(uuid)) {
                return item;
            }
        }
        return null;
    }
    public static Gui getGui(UUID uuid) {
        for (Gui item : guiList) {
            if (item.player.equals(uuid)) {
                return item;
            }
        }
        return new Gui(uuid);
    }

    public static int getSubTextX() {
        return subTextX;
    }

    public static int getSubTextY() {
        return subTextY;
    }

    public void openGuideGui() {
        if(!Bukkit.getOfflinePlayer(player).isOnline()) {
            return;
        }
        Player player = Bukkit.getOfflinePlayer(this.player).getPlayer();
        VexViewAPI.openGui(player, this);
        refresh();
    }

    public void refresh() {
        OfflinePlayer offlinePlayer = getOfflinePlayer();
        if(!offlinePlayer.isOnline()) {
            return;
        }
        String text = Guide.getPlayerString(offlinePlayer.getPlayer());
        List<String> show = Arrays.asList(text.split("<next"));
        VexText vexText = new VexText(textX, textY, show);
        Player player = offlinePlayer.getPlayer();
        List<String> subShow = new ArrayList<>();
        subShow.add("当前世界               " + player.getWorld().getName());
        subShow.add("当前世界总宝藏量       " + TagGift.getNum(player.getWorld()));
        subShow.add("当前世界总宝藏种类      " + TagGift.getSpe());
        subShow.add("距离我最近的宝藏       " + TagGift.near(player));
        VexText vexTextSub = new VexText(subTextX, subTextY, subShow);
        new BukkitRunnable() {

            @Override
            public void run() {
                delAllDynamic();
                addDynamic(vexText);
                addDynamic(vexTextSub);
            }
        }.runTaskLater(SuperHolidayGift.plugin, 5);
    }

    private static void refresh(UUID uuid) {
        Gui gui = getGuiEmp(uuid);
        if(gui != null)
            gui.refresh();
    }


    private void addDynamic(DynamicComponent dynamicComponent) {
        OpenedVexGui openedVexGui = getCur();
        if(openedVexGui == null) {
            return;
        }

        if(openedVexGui.getVexGui().getComponents().contains(refreshButton)) {
//            System.out.println("debug >> ttt");
            openedVexGui.addDynamicComponent(dynamicComponent);
            dynamicComponents.add(dynamicComponent);
        }
    }


    private boolean delAllDynamic() {
        OpenedVexGui openedVexGui = getCur();
        if(openedVexGui == null || !openedVexGui.getVexGui().getComponents().contains(refreshButton))
            return false;
        for (DynamicComponent item : dynamicComponents) {
            openedVexGui.removeDynamicComponent(item);
        }
        dynamicComponents.forEach(openedVexGui::removeDynamicComponent);
        dynamicComponents.clear();
        return true;
    }



    private OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(player);
    }

    private OpenedVexGui getCur() {
        Player player = Bukkit.getOfflinePlayer(this.player).getPlayer();
        return VexViewAPI.getPlayerCurrentGui(player);
    }

    public UUID getPlayer() {
        return player;
    }
}
