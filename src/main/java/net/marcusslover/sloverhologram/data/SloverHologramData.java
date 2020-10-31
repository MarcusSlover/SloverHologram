package net.marcusslover.sloverhologram.data;

import net.marcusslover.sloverhologram.SloverHologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class SloverHologramData extends Config {
    public SloverHologramData(final SloverHologram sloverHologram, final String filename) {
        super(sloverHologram, filename);
    }

    public void set(final String path, final Location value) {
        this.config.set(path+".x", value.getX());
        this.config.set(path+".y", value.getY());
        this.config.set(path+".z", value.getZ());
        this.config.set(path+".pitch", (double) value.getPitch());
        this.config.set(path+".yaw", (double) value.getYaw());
        this.config.set(path+".world", value.getWorld().getName());
        this.save();
    }

    public void set(final String path, final List value) {
        this.config.set(path, value);
        this.save();
    }

    public void set(final String path, final boolean toggle) {
        this.config.set(path, toggle);
        this.save();
    }

    public double getDouble(final String path) {
        if (this.config.contains(path)) {
            return this.config.getDouble(path);
        }
        return 0;
    }

    public boolean getBoolean(final String path) {
        if (this.config.contains(path)) {
            return this.config.getBoolean(path);
        }
        return false;
    }

    public String getString(final String path) {
        if (this.config.contains(path)) {
            return this.config.getString(path);
        }
        return "null";
    }

    public Location getLocation(final String path) {
        if (this.config.contains(path)) {
            final double x = getDouble(path+".x");
            final double y = getDouble(path+".y");
            final double z = getDouble(path+".z");

            final float pitch = (float) getDouble(path+".pitch");
            final float yaw = (float) getDouble(path+".yaw");

            final String world = getString(path+".world");
            final World world1 = Bukkit.getWorld(world);
            if (world1 == null) {
                return null;
            }

            final Location loc = new Location(world1, x, y, z, yaw, pitch);

            loc.setPitch(pitch);
            loc.setYaw(yaw);
            return loc;
        }
        return new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
    }

    public List getList(final String path) {
        if (this.config.contains(path)) {
            return this.config.getList(path);
        }
        return new ArrayList();
    }

}
