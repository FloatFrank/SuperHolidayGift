package com.piaofu.owner.superholidaygift.location;

import com.piaofu.owner.superholidaygift.Message;
import com.piaofu.owner.superholidaygift.SuperHolidayGift;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类为生成区域存储处理对象
 */
public class SpawnLocation {
    private static List<SpawnLocation> spawnLocations;
    private World world;
    private Location from;
    private Location to;
    private String locName;

    public SpawnLocation(String locName, World world, Location from, Location to) {
        this.locName = locName;
        this.world = world;
        this.from = from;
        this.to = to;
        if(from.getBlockY() > to.getBlockY()) {
            this.from = to;
            this.to =from;
        }
    }

    public static SpawnLocation getSpawnLocation(String name) {
        for(SpawnLocation spawnLocation : spawnLocations) {
            if(spawnLocation.locName.equalsIgnoreCase(name)) {
                return spawnLocation;
            }
        }
        return null;
    }

    public static void loadSpawnLocation() {
        spawnLocations = new ArrayList<>();
        ConfigurationSection configurationSection = SuperHolidayGift.plugin.getConfig().getConfigurationSection("locationList");
        if (configurationSection != null) {
            configurationSection.getKeys(false).forEach(locName -> {
                try {
                    World world = Bukkit.getWorld(configurationSection.getString(locName + ".world"));
//                    System.out.println("DEBUG>>>" + world.getName());
                    spawnLocations.add(new SpawnLocation(locName, world,
                            new Location(world,
                                    configurationSection.getDouble(locName + ".from.x"),
                                    configurationSection.getDouble(locName + ".from.y"),
                                    configurationSection.getDouble(locName + ".from.z")),
                            new Location(world,
                                    configurationSection.getDouble(locName + ".to.x"),
                                    configurationSection.getDouble(locName + ".to.y"),
                                    configurationSection.getDouble(locName + ".to.z")
                            )));
                }catch (Exception exception) {
                    Bukkit.getConsoleSender().sendMessage("§4 >>> [Lode_Error: 在LocationList下的]" + locName + "配置出错，请仔细检查， 目前" + locName + "未加载");
                }

            });
        }
        Bukkit.getConsoleSender().sendMessage(Message.getMsg(Message.LODE_LOC_SUCCESS, String.valueOf(spawnLocations.size())));
    }

    public World getWorld() {
        return world;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public String getLocName() {
        return locName;
    }
}
