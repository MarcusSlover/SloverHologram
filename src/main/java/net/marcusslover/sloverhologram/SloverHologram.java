package net.marcusslover.sloverhologram;

import net.marcusslover.sloverhologram.api.SloverHologramAPI;
import net.marcusslover.sloverhologram.command.SloverHologramCommand;
import net.marcusslover.sloverhologram.data.SloverConfig;
import net.marcusslover.sloverhologram.data.SloverHologramData;
import net.marcusslover.sloverhologram.event.Events;
import net.marcusslover.sloverhologram.hologram.Hologram;
import net.marcusslover.sloverhologram.hologram.HologramManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    //the api of the plugin
    private SloverHologramAPI hologramAPI;

    //collection of all proper holograms
    public Collection<Hologram> hologramList;

    //config instance
    public SloverConfig sloverConfig;
    //holograms data instance
    public SloverHologramData sloverHologramData;

    //management class
    private HologramManager hologramManager;

    //instance of the main class
    private static SloverHologram sloverHologram;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onEnable() {
        sloverHologram = this;

        hologramList = new ArrayList<>();

        hologramManager =  new HologramManager();
        hologramAPI = new SloverHologramAPI();

        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

        this.loadFiles(s -> {
            //show the holograms
            new BukkitRunnable() {

                @Override
                public void run() {
                    loadHolograms();
                }
            }.runTaskLater(sloverHologram, 5L);
        });

        //register command
        getCommand("sloverhologram").setExecutor(new SloverHologramCommand());

        //events
        Bukkit.getPluginManager().registerEvents(new Events(this), this);
        getLogger().info("The plugin was successfully enabled!");
    }

    @Override
    public void onDisable() {
        this.unloadHolograms();
        getLogger().info("The plugin was successfully disabled!");
    }

    private void loadFiles(Consumer<String> consumer) {
        executorService.execute(() -> {
            this.sloverHologramData = new SloverHologramData(this, "data.yml");
            this.sloverConfig = new SloverConfig(this, "config.yml");
            consumer.accept("");
        });
    }

    @SuppressWarnings("unchecked")
    private void loadHolograms() {
        for (final String hologramName : this.getHologramNames()) {

            Location location = this.sloverHologramData.getLocation("hologram-data."+hologramName+".location");
            if (location == null) {
                continue;
            }

            List<String> lines = this.sloverHologramData.getList("hologram-data."+hologramName+".lines");
            final Hologram hologram = new Hologram(hologramName, lines, location, false);

            if (Bukkit.getOnlinePlayers().size() > 0) {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    hologram.remove(player);
                    hologram.show(player);
                });
            }
        }
    }

    private void unloadHolograms() {
        hologramList.forEach(hologram -> Bukkit.getOnlinePlayers().forEach(hologram::remove));
        hologramAPI.getHologramMap().forEach((uuid, hologram) -> Bukkit.getOnlinePlayers().forEach(hologram::remove));

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
     * A method which returns the {@link HologramManager} class.
     * @return an instance of the {@link HologramManager} class
     */
    public HologramManager getHologramManager() {
        return hologramManager;
    }

    /**
     * A method which returns api class
     * @return api class
     */
    public SloverHologramAPI getAPI() {
        return hologramAPI;
    }

    /**
     * A method which returns instance of the main class
     * @return instance of the main class
     */
    public static SloverHologram getInstance() {
        return sloverHologram;
    }
}
