package net.marcusslover.sloverhologram;

import net.marcusslover.sloverhologram.command.SloverHologramCommand;
import net.marcusslover.sloverhologram.events.Events;
import net.marcusslover.sloverhologram.holograms.Hologram;
import net.marcusslover.sloverhologram.holograms.Holograms;
import net.marcusslover.sloverhologram.utils.SloverConfig;
import net.marcusslover.sloverhologram.utils.SloverHologramData;
import net.marcusslover.sloverhologram.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * A plugin for holograms, based on packets (fake entities)
 * Note: Do not steal or public the code without a permission.
 *
 * @author MarcusSlover
 */
public final class SloverHologram extends JavaPlugin implements Listener {

    //prefix
    public static String prefix = "&b&lSLOVER HOLOGRAM!";
    public static List<Hologram> allHologramObjects = new ArrayList<>();

    //api class
    private static SloverHologramAPI hologramAPI;

    public SloverHologramData sloverHologramData;
    public static List<Hologram> hologramList;
    public SloverConfig sloverConfig;
    private static Holograms hologramClass;

    @Override
    public void onEnable() {
        hologramClass = new Holograms(this);
        hologramList = new ArrayList<>();
        hologramAPI = new SloverHologramAPI();

        this.loadCommand();
        this.loadFiles();
        new BukkitRunnable() {

            @Override
            public void run() {
                loadHolograms();
            }
        }.runTaskLater(this, 40L);


        Bukkit.getPluginManager().registerEvents(new Events(this), this);
        Bukkit.getConsoleSender().sendMessage(new Text(prefix+" &7The plugin was &asuccessfully&7 enabled!").toString());
    }

    @Override
    public void onDisable() {
        this.unloadHolograms();
        Bukkit.getConsoleSender().sendMessage(new Text(prefix+" &7Plugin was &asuccessfully&7 disabled!").toString());
    }

    private void loadCommand() {
        this.getCommand("sloverhologram").setExecutor(new SloverHologramCommand(this));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void loadFiles() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }
        this.sloverHologramData = new SloverHologramData(this, "data.yml");
        this.sloverConfig = new SloverConfig(this, "config.yml");
    }

    @SuppressWarnings("unchecked")
    private void loadHolograms() {
        for (String hologramName : this.getHologramNames()) {
            Location location = this.sloverHologramData.getLocation("hologram-data."+hologramName+".location");
            List<String> lines = this.sloverHologramData.getList("hologram-data."+hologramName+".lines");
            Hologram hologram = new Hologram(hologramName, lines, location);
            hologramList.add(hologram);

            Bukkit.getOnlinePlayers().forEach(player -> {
                hologramList.forEach(h -> h.destroyHologram(player));
                hologramList.forEach(h -> h.show(player));
            });
        }
    }

    private void unloadHolograms() {
        for (Hologram hologram : allHologramObjects) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                hologram.remove(player);
            }
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (getAPI().fakeHologram.containsKey(player.getUniqueId())) {
                getAPI().destroyHolograms(player);
            }
        }
    }

    /**
     * A method which returns list of holograms from file
     * @return list of hologram
     */
    @SuppressWarnings("unchecked")
    public List<String> getHologramNames() {
        return (List<String>) this.sloverHologramData.getList("hologram-list");
    }

    /**
     * A method which returns the {@link Holograms} class.
     * @return an instance of the {@link Holograms} class
     */
    public static Holograms getHologramClass() {
        return hologramClass;
    }

    /**
     * A method which adds a hologram to global objects
     * @param hologram the hologram
     */
    public static void addHologramObject(Hologram hologram) {
        allHologramObjects.add(hologram);
    }

    /**
     * A method which returns api class
     * @return api class
     */
    @SuppressWarnings("WeakerAccess")
    public static SloverHologramAPI getAPI() {
        return hologramAPI;
    }
}
