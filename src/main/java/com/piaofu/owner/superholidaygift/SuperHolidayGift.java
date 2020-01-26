package com.piaofu.owner.superholidaygift;

import com.piaofu.owner.superholidaygift.command.Command;
import com.piaofu.owner.superholidaygift.command.CommandsHolder;
import com.piaofu.owner.superholidaygift.gift.Gift;
import com.piaofu.owner.superholidaygift.guide.Gui;
import com.piaofu.owner.superholidaygift.guide.Guide;
import com.piaofu.owner.superholidaygift.location.SpawnLocation;
import com.piaofu.owner.superholidaygift.taggift.TagGift;
import com.piaofu.owner.superholidaygift.taggift.TagSimpleBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class SuperHolidayGift extends JavaPlugin {
    public static SuperHolidayGift plugin;
    public static CommandExecutor commandExecutor;

    public void configLoad(CommandSender sender) {
        saveDefaultConfig();
        reloadConfig();
        Message.loadLangData();
        SpawnLocation.loadSpawnLocation();
        Gift.onLoadGift();
        TagGift.loadConfig();
        TagSimpleBuilder.readTagDirection();
        Guide.initConfig();
        Gui.initConfig();
        sender.sendMessage(Message.getMsg(Message.LODE_SUCCESS));
    }
    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§b=========   §3SuperHolidayGift   §b=========");
        Bukkit.getConsoleSender().sendMessage("§b=========   §3假   日   礼   物§b   =======");
        Bukkit.getConsoleSender().sendMessage("§b==========     §3BUILDING...     §b=========");
        plugin = this;
        configLoad(Bukkit.getConsoleSender());
        // Plugin startup logic
        commandExecutor = new Command();
        PFAPI.registeCommands(this, CommandsHolder.class);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
