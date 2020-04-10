package net.marcusslover.sloverhologram;

import net.marcusslover.sloverhologram.command.SloverHologramCommand;
import net.marcusslover.sloverhologram.events.Events;
import net.marcusslover.sloverhologram.holograms.Hologram;
import net.marcusslover.sloverhologram.holograms.Holograms;
import net.marcusslover.sloverhologram.utils.SloverConfig;
import net.marcusslover.sloverhologram.utils.SloverHologramData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * A plugin for holograms, based on packets (fake entities)
 * Note: Do not steal or public the code without a permission.
 *
 * @author MarcusSlover
 */
public final class SloverHologram extends JavaPlugin implements Listener {

    //prefix
    public final String prefix = "&b&lSLOVER HOLOGRAM!";

    public final Collection<Hologram> allHologramObjects = new ArrayList<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private static SloverHologramAPI hologramAPI;

    //collection of all manually made holograms (not by code)
    public Collection<Hologram> hologramList;

    //config instance
    public SloverConfig sloverConfig;
    //holograms data instance
    public SloverHologramData sloverHologramData;

    //management class
    private Holograms hologramClass;

    //instance of the main class
    private static SloverHologram sloverHologram;

    @Override
    public void onEnable() {
        sloverHologram = this;

        hologramList = new ArrayList<>();
        hologramClass =  new Holograms();
        hologramAPI = new SloverHologramAPI();

        this.loadFiles(s -> {
            //show the holograms
            new BukkitRunnable() {

                @Override
                public void run() {
                    loadHolograms();
                }
            }.runTaskLater(getSloverHologram(), 5L);
        });

        //register command
        getCommand("sloverhologram").setExecutor(new SloverHologramCommand(this));

        //events
        Bukkit.getPluginManager().registerEvents(new Events(this), this);
        getLogger().info("The plugin was successfully enabled!");
    }

    @Override
    public void onDisable() {
        this.unloadHolograms();
        getLogger().info("The plugin was successfully disabled!");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void loadFiles(Consumer<String> consumer) {
        executorService.execute(() -> {
            if (!this.getDataFolder().exists()) {
                this.getDataFolder().mkdirs();
            }
            this.sloverHologramData = new SloverHologramData(this, "data.yml");
            this.sloverConfig = new SloverConfig(this, "config.yml");
            consumer.accept("");
        });
    }

    @SuppressWarnings("unchecked")
    private void loadHolograms() {
        for (final String hologramName : this.getHologramNames()) {

            Location location = this.sloverHologramData.getLocation("hologram-data."+hologramName+".location");
            List<String> lines = this.sloverHologramData.getList("hologram-data."+hologramName+".lines");

            final Hologram hologram = new Hologram(hologramName, lines, location);
            hologramList.add(hologram);

            if (Bukkit.getOnlinePlayers().size() > 0) {

                Bukkit.getOnlinePlayers().forEach(player -> {
                    hologramList.forEach(h -> h.destroyHologram(player));
                    hologramList.forEach(h -> h.show(player));
                });
            }
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
    public Holograms getHologramClass() {
        return hologramClass;
    }

    /**
     * A method which adds a hologram to global objects
     * @param hologram the hologram
     */
    public void addHologramObject(Hologram hologram) {
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

    /**
     * A method which returns instance of the main class
     * @return instance of the main class
     */
    public static SloverHologram getSloverHologram() {
        return sloverHologram;
    }
}
