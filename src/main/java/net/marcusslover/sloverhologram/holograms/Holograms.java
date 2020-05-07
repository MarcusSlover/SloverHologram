package net.marcusslover.sloverhologram.holograms;

import net.marcusslover.sloverhologram.SloverHologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Holograms implements HologramEditor {
    private final SloverHologram sloverHologram;

    public Holograms() {
        sloverHologram = SloverHologram.getSloverHologram();
    }

    @Override
    public boolean exists(final String name) {
        List<String> list = sloverHologram.getHologramNames();
        return list.contains(name);
    }

    @Override
    public void create(final String name, final Location location, final String value) {
        List<String> lines = new ArrayList<>();
        lines.add(value);
        Hologram hologram = new Hologram(name, lines, location);
        List<String> list = sloverHologram.getHologramNames();
        if (list.contains(name)) {
            return;
        }

        list.add(name);
        sloverHologram.sloverHologramData.set("hologram-list", list);
        sloverHologram.sloverHologramData.set("hologram-data."+name+".lines", lines);
        sloverHologram.sloverHologramData.set("hologram-data."+name+".location", location);
        sloverHologram.hologramList.add(hologram);
        for (Player player : Bukkit.getOnlinePlayers()) {
            hologram.show(player);
        }
    }

    @Override
    public void delete(final String name) {
        Hologram holo = null;
        for (Hologram hologram : sloverHologram.hologramList) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                holo = hologram;
                List<String> list = sloverHologram.getHologramNames();
                if (!list.contains(name)) {
                    return;
                }

                list.remove(name);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    hologram.remove(player);
                }
                sloverHologram.allHologramObjects.remove(hologram);
                sloverHologram.sloverHologramData.set("hologram-list", list);
                sloverHologram.sloverHologramData.config.set("hologram-data."+name+".lines", null);
                sloverHologram.sloverHologramData.config.set("hologram-data." + name + ".location", null);
                sloverHologram.sloverHologramData.save();
                break;
            }
        }
        if (holo != null) {
            sloverHologram.hologramList.remove(holo);
        }
    }

    @Override
    public void addLine(final String name, final StringBuilder value) {
        String newValue = value.toString();
        newValue = newValue.substring(0, newValue.length() - 1);
        for (Hologram hologram : sloverHologram.hologramList) {
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

    @Override
    public void setLine(final String name, final int i, final StringBuilder value) {
        String newValue = value.toString();
        newValue = newValue.substring(0, newValue.length() - 1);
        for (Hologram hologram : sloverHologram.hologramList) {
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

    @Override
    public void teleport(final String name, final Location location) {
        for (Hologram hologram : sloverHologram.hologramList) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                hologram.setLocation(location);
                sloverHologram.sloverHologramData.set("hologram-data."+name+".location", location);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    hologram.show(player);
                }
            }
        }
    }

    @Override
    public void teleportPlayer(final String name, final Player player) {
        for (Hologram hologram : sloverHologram.hologramList) {
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
        for (Hologram hologram : sloverHologram.hologramList) {
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

    @Override
    public int size(final String name) {
        for (Hologram hologram : sloverHologram.hologramList) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                return hologram.getLines().size();
            }
        }
        return 0;
    }

    @Override
    public double space() {
        return sloverHologram.sloverConfig.getDouble("hologram-space", 0.4d);
    }

}
