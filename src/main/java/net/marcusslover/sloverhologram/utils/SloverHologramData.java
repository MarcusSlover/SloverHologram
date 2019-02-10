package net.marcusslover.sloverhologram.utils;

import net.marcusslover.sloverhologram.SloverHologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class SloverHologramData extends Config {
    public SloverHologramData(SloverHologram sloverHologram, String filename) {
        super(sloverHologram, filename);
    }

    public void set(String path, Location value) {
        config.set(path+".x", value.getX());
        config.set(path+".y", value.getY());
        config.set(path+".z", value.getZ());
        config.set(path+".pitch", (double) value.getPitch());
        config.set(path+".yaw", (double) value.getYaw());
        config.set(path+".world", value.getWorld().getName());
        save();
    }

    public void set(String path, List value) {
        config.set(path, value);
        save();
    }

    public double getDouble(String path) {
        if (config.contains(path)) {
            return config.getDouble(path);
        }
        return 0;
    }

    public String getString(String path) {
        if (config.contains(path)) {
            return config.getString(path);
        }
        return "null";
    }

    public Location getLocation(String path) {
        if (config.contains(path)) {
            double x = getDouble(path+".x");
            double y = getDouble(path+".y");
            double z = getDouble(path+".z");
            float pitch = (float) getDouble(path+".pitch");
            float yaw = (float) getDouble(path+".yaw");
            String world = getString(path+".world");
            Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
            loc.setPitch(pitch);
            loc.setYaw(yaw);
            return loc;
        }
        return new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
    }

    public List getList(String path) {
        if (config.contains(path)) {
            return config.getList(path);
        }
        return new ArrayList();
    }

}
