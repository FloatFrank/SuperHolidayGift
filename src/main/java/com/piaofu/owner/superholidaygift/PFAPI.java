package com.piaofu.owner.superholidaygift;

import com.piaofu.owner.superholidaygift.command.CommandsHolder;
import com.piaofu.owner.superholidaygift.command.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 此类为自制的指令注册的工具类
 */
public class PFAPI {
    public static String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
    public static void registeCommands(Plugin plugin, Class c){
        for (Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PlayerCommand.class)) {
                PlayerCommand playerCommand = method.getAnnotation(PlayerCommand.class);
                String lable = playerCommand.label();
                if (Bukkit.getPluginCommand(lable) == null) {
                    try {
                        Constructor constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
                        constructor.setAccessible(true);
                        PluginCommand command = (PluginCommand)constructor.newInstance(lable, SuperHolidayGift.plugin);
                        String packages = "org.bukkit.craftbukkit."+ version +".CraftServer";
                        Class craftServer = Class.forName(packages);

                        Object craftServerObject = craftServer.cast(Bukkit.getServer());
                        Object simpleCommandMap = craftServer.getDeclaredMethod("getCommandMap").invoke(craftServerObject);
                        simpleCommandMap.getClass().getDeclaredMethod("register", String.class, Command.class).invoke(simpleCommandMap, plugin.getName(), command);

                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                CommandsHolder.commandMap.put(playerCommand, method);
                Bukkit.getPluginCommand(lable).setExecutor(SuperHolidayGift.commandExecutor);
            }
        }
    }
}
