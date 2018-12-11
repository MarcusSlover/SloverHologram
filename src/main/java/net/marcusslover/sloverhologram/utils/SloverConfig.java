package net.marcusslover.sloverhologram.utils;

import net.marcusslover.sloverhologram.SloverHologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SloverConfig extends Config {
    public SloverConfig(SloverHologram sloverHologram, String filename) {
        super(sloverHologram, filename);
    }

    public void set(String path, int value) {
        config.set(path, value);
        save();
    }

    public void set(String path, double value) {
        config.set(path, value);
        save();
    }

    public void set(String path, float value) {
        config.set(path, value);
        save();
    }

    public void set(String path, String value) {
        config.set(path, value);
        save();
    }

    public void set(String path, ItemStack value) {
        config.set(path, value);
        save();
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

    public int getInt(String path) {
        if (config.contains(path)) {
            return config.getInt(path);
        }
        return 0;
    }

    public float getFloat(String path) {
        if (config.contains(path)) {
            return Float.parseFloat(config.getString(path));
        }
        return 0.0f;
    }

    public int getIntDelivery(String path) {
        if (config.contains(path)) {
            return config.getInt(path);
        }
        return -1;
    }

    public double getDouble(String path) {
        if (config.contains(path)) {
            return config.getDouble(path);
        }
        return 0;
    }
    public double getDouble(String path, double d) {
        if (config.contains(path)) {
            return config.getDouble(path);
        } else {
            set(path, d);
        }
        return d;
    }

    public ItemStack getItemStack(String path) {
        if (config.contains(path)) {
            return config.getItemStack(path);
        }
        return null;
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
        ArrayList empty = new ArrayList();
        return empty;
    }
}
