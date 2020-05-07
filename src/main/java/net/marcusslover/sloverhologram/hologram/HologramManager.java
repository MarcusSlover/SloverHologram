package net.marcusslover.sloverhologram.hologram;

import net.marcusslover.sloverhologram.SloverHologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HologramManager implements HologramEditor {
    private final SloverHologram plugin = SloverHologram.getInstance();

    @Override
    public boolean exists(final String name) {
        List<String> list = plugin.getHologramNames();
        return list.contains(name);
    }

    @Override
    public void create(final String name, final Location location, final String value) {
        List<String> lines = new ArrayList<>();
        List<String> list = plugin.getHologramNames();
        if (list.contains(name)) {
            return;
        }

        lines.add(value);
        Hologram hologram = new Hologram(name, lines, location, false);

        list.add(name);
        plugin.sloverHologramData.set("hologram-list", list);
        plugin.sloverHologramData.set("hologram-data."+name+".lines", lines);
        plugin.sloverHologramData.set("hologram-data."+name+".location", location);
        plugin.hologramList.add(hologram);
        for (Player player : Bukkit.getOnlinePlayers()) {
            hologram.show(player);
        }
    }

    @Override
    public void delete(final String name) {
        Hologram holo = null;
        for (Hologram hologram : plugin.hologramList) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                holo = hologram;
                List<String> list = plugin.getHologramNames();

                list.remove(name);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    hologram.remove(player);
                }
                plugin.sloverHologramData.set("hologram-list", list);
                plugin.sloverHologramData.config.set("hologram-data."+name+".lines", null);
                plugin.sloverHologramData.config.set("hologram-data." + name + ".location", null);
                plugin.sloverHologramData.save();
                break;
            }
        }
        if (holo != null) {
            plugin.hologramList.remove(holo);
        }
    }

    @Override
    public void addLine(final String name, final StringBuilder value) {
        String newValue = value.toString();
        newValue = newValue.substring(0, newValue.length() - 1);
        for (Hologram hologram : plugin.hologramList) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                List<String> lines = hologram.getLines();
                lines.add(newValue);
                hologram.setLines(lines);
                plugin.sloverHologramData.set("hologram-data."+name+".lines", lines);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    hologram.show(player);
                }
            }
        }
    }

    @Override
    public void setLine(final String name, final int i, final StringBuilder value) {
        String newValue = value.toString();
        newValue = newValue.substring(0, newValue.length() - 1);
        for (Hologram hologram : plugin.hologramList) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                List<String> lines = hologram.getLines();
                lines.set(i - 1, newValue);
                hologram.setLines(lines);
                plugin.sloverHologramData.set("hologram-data."+name+".lines", lines);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    hologram.show(player);
                }
            }
        }
    }

    @Override
    public void teleport(final String name, final Location location) {
        for (Hologram hologram : plugin.hologramList) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                hologram.setLocation(location);
                plugin.sloverHologramData.set("hologram-data."+name+".location", location);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    hologram.show(player);
                }
            }
        }
    }

    @Override
    public void teleportPlayer(final String name, final Player player) {
        for (Hologram hologram : plugin.hologramList) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                if (!hologram.getChunk().isLoaded()) {
                    hologram.getChunk().load();
                }
                player.teleport(hologram.getLocation());
                break;
            }
        }
    }

    @Override
    public void removeLine(final String name, final int value) {
        for (Hologram hologram : plugin.hologramList) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                List<String> lines = hologram.getLines();
                lines.remove(value - 1);
                hologram.setLines(lines);
                plugin.sloverHologramData.set("hologram-data."+name+".lines", lines);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    hologram.show(player);
                }
            }
        }
    }

    @Override
    public int size(final String name) {
        for (Hologram hologram : plugin.hologramList) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                return hologram.getLines().size();
            }
        }
        return 0;
    }

    @Override
    public double space() {
        return plugin.sloverConfig.getDouble("hologram-space", 0.4d);
    }

}
