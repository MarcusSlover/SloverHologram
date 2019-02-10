package net.marcusslover.sloverhologram.holograms;

import net.marcusslover.sloverhologram.SloverHologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Holograms {
    private static SloverHologram sloverHologram;

    public Holograms(SloverHologram sloverHologram) {
        Holograms.sloverHologram = sloverHologram;
    }

    /**
     * A method which checks if certain hologram exists
     * @param name name of the hologram
     * @return true or false
     */
    public static boolean exists(String name) {
        List<String> list = sloverHologram.getHologramNames();
        return list.contains(name);
    }

    /**
     * A method which creates a new hologram
     * @param name name of the hologram
     * @param location location where the hologram will appear
     * @param value first line of the hologram
     */
    public static void create(String name, Location location, String value) {
        List<String> lines = new ArrayList<>();
        lines.add(value);
        Hologram hologram = new Hologram(name, lines, location);
        List<String> list = sloverHologram.getHologramNames();
        list.add(name);
        sloverHologram.sloverHologramData.set("hologram-list", list);
        sloverHologram.sloverHologramData.set("hologram-data."+name+".lines", lines);
        sloverHologram.sloverHologramData.set("hologram-data."+name+".location", location);
        SloverHologram.hologramList.add(hologram);
        for (Player player : Bukkit.getOnlinePlayers()) {
            hologram.show(player);
        }
    }

    /**
     * A method which removes a hologram
     * @param name name of the hologram
     */
    public static void delete(String name) {
        Hologram holo = null;
        for (Hologram hologram : SloverHologram.hologramList) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                holo = hologram;
                List<String> list = sloverHologram.getHologramNames();
                list.remove(name);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    hologram.remove(player);
                }
                SloverHologram.allHologramObjects.remove(hologram);
                sloverHologram.sloverHologramData.set("hologram-list", list);
                sloverHologram.sloverHologramData.config.set("hologram-data."+name+".lines", null);
                sloverHologram.sloverHologramData.config.set("hologram-data." + name + ".location", null);
                sloverHologram.sloverHologramData.save();
            }
        }
        if (holo != null) {
            SloverHologram.hologramList.remove(holo);
        }
    }

    /**
     * A method which adds a line to hologram
     * @param name name of the hologram
     * @param value a line which will be added
     */
    public static void addLine(String name, StringBuilder value) {
        String newValue = value.toString();
        newValue = newValue.substring(0, newValue.length() - 1);
        for (Hologram hologram : SloverHologram.hologramList) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                List<String> lines = hologram.getLines();
                lines.add(newValue);
                hologram.setLines(lines);
                sloverHologram.sloverHologramData.set("hologram-data."+name+".lines", lines);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    hologram.show(player);
                }
            }
        }
    }

    /**
     * A method which sets a line to hologram
     * @param name name of the hologram
     * @param value a line which will be set
     * @param i the line number
     */
    public static void setLine(String name, int i, StringBuilder value) {
        String newValue = value.toString();
        newValue = newValue.substring(0, newValue.length() - 1);
        for (Hologram hologram : SloverHologram.hologramList) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                List<String> lines = hologram.getLines();
                lines.set(i - 1, newValue);
                hologram.setLines(lines);
                sloverHologram.sloverHologramData.set("hologram-data."+name+".lines", lines);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    hologram.show(player);
                }
            }
        }
    }

    /**
     * A method which teleports hologram to a
     * certain location
     * @param name name of the hologram
     * @param location new location
     */
    public static void teleport(String name, Location location) {
        for (Hologram hologram : SloverHologram.hologramList) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                hologram.setLocation(location);
                sloverHologram.sloverHologramData.set("hologram-data."+name+".location", location);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    hologram.show(player);
                }
            }
        }
    }

    /**
     * A method which remove a line to hologram
     * @param name name of the hologram
     * @param value a line which will be removed
     */
    public static void removeLine(String name, int value) {
        for (Hologram hologram : SloverHologram.hologramList) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                List<String> lines = hologram.getLines();
                lines.remove(value - 1);
                hologram.setLines(lines);
                sloverHologram.sloverHologramData.set("hologram-data."+name+".lines", lines);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    hologram.show(player);
                }
            }
        }
    }

    /**
     * A method which returns size of hologram lines
     * @param name name of the hologram
     * @return size of the hologram list
     */
    public static int size(String name) {
        for (Hologram hologram : SloverHologram.hologramList) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                return hologram.getLines().size();
            }
        }
        return 0;
    }

    /**
     * A method with returns a double which is
     * a distance between hologram lines
     * @return distance in double
     */
    public static double space() {
        return sloverHologram.sloverConfig.getDouble("hologram-space", 0.4d);
    }
}
