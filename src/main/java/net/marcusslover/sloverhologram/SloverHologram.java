package net.marcusslover.sloverhologram;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
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
 * A plugin for simple holograms.
 * @author MarcusSlover
 */
public final class SloverHologram extends JavaPlugin implements Listener {

    public final String prefix = "&b&lSLOVER HOLOGRAM!"; // Prefix
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private SloverHologramAPI hologramAPI; // API class

    public Collection<Hologram> hologramList; // Collection of all 'global' holograms
    public SloverConfig sloverConfig; // Config instance
    public SloverHologramData sloverHologramData; // Hologram Data instance

    private HologramManager hologramManager; // Hologram Manager instance
    private ProtocolManager protocolManager = null; // Protocol Manager instance

    private static SloverHologram sloverHologram; // Instance of the main class

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

        // Protocol support
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            this.protocolManager = ProtocolLibrary.getProtocolManager();
            getLogger().info("Successfully hooked into ProtocolLib!");
        }

        // Load holograms from the config.
        this.loadFiles(s -> {
            // Spawn the holograms.
            new BukkitRunnable() {

                @Override
                public void run() {
                    loadHolograms();
                }
            }.runTaskLater(sloverHologram, 5L);
        });

        // Register the command
        getCommand("sloverhologram").setExecutor(new SloverHologramCommand());

        // Register the events
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
            boolean packetAdapter = this.sloverHologramData.getBoolean("hologram-data."+hologramName+".packetAdapter");

            final Hologram hologram = new Hologram(hologramName, true, lines, location, false);
            if (packetAdapter) {
                hologram.registerListener();
            }

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

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    /**
     * Returns a list of holograms from the file.
     * @return List of holograms.
     */
    @SuppressWarnings("unchecked")
    public List<String> getHologramNames() {
        return (List<String>) this.sloverHologramData.getList("hologram-list");
    }

    /**
     * Returns the HologramManager class.
     * @return An instance of the HologramManager class.
     */
    public HologramManager getHologramManager() {
        return hologramManager;
    }

    /**
     * Returns the SloverHologramAPI class.
     * @return An instance of the SloverHologramAPI class.
     */
    public SloverHologramAPI getAPI() {
        return hologramAPI;
    }

    /**
     * Returns the SloverHologram class.
     * @return  An instance of the SloverHologram class.
     */
    public static SloverHologram getInstance() {
        return sloverHologram;
    }
}
