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
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SloverHologram extends JavaPlugin {

    //prefix
    public static String prefix = "&b&lSLOVER HOLOGRAM!";
    public static List<Hologram> allHologramObjects = new ArrayList<>();
    private Server server = this.getServer();
    public ConsoleCommandSender console = this.server.getConsoleSender();
    public SloverHologramData sloverHologramData;
    public List<Hologram> hologramList = new ArrayList<>();
    public SloverConfig sloverConfig;
    private static SloverHologramAPI hologramAPI;

    @Override
    public void onEnable() {
        this.console.sendMessage(new Text(prefix+" &7Plugin was enabled").toString());
        new Holograms(this);
        this.loadCommand();
        this.loadFiles();
        this.loadHolograms();
        this.server.getPluginManager().registerEvents(new Events(this), this);
        hologramAPI = new SloverHologramAPI(this);
    }

    /**
     * A method which returns api class
     * @return api class
     */
    public static SloverHologramAPI getAPI() {
        return hologramAPI;
    }

    /**
     * A method which registers the command
     */
    private void loadCommand() {
        this.getCommand("sloverhologram").setExecutor(new SloverHologramCommand(this));
    }

    /**
     * A method which loads all of the configuration
     * files that are in the data folder
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void loadFiles() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }
        this.sloverHologramData = new SloverHologramData(this, "data.yml");
        this.sloverConfig = new SloverConfig(this, "config.yml");

    }

    /**
     * A method which loads all of the created holograms
     */
    @SuppressWarnings("unchecked")
    private void loadHolograms() {
        for (String hologramName : this.getHologramNames()) {
            Location location = this.sloverHologramData.getLocation("hologram-data."+hologramName+".location");
            List<String> lines = this.sloverHologramData.getList("hologram-data."+hologramName+".lines");
            Hologram hologram = new Hologram(hologramName, lines, location);
            this.hologramList.add(hologram.clone());
            for (Player player : Bukkit.getOnlinePlayers()) {
                hologram.show(player);
            }
        }
    }

    @Override
    public void onDisable() {
        this.console.sendMessage(new Text(prefix+" &7Plugin was disabled").toString());
        this.unloadHolograms();
    }

    /**
     * A method which unloads all of the created holograms
     */
    @SuppressWarnings("unchecked")
    private void unloadHolograms() {
        for (Hologram hologram : allHologramObjects) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                hologram.remove(player);
            }
        }
        for (Hologram hologram : hologramList) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                hologram.remove(player);
            }
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (getAPI().fakeHologram.containsKey(player)) {
                getAPI().destroyHolograms(player);
            }
        }
    }

    /**
     * A method which adds a hologram to global objects
     * @param hologram the hologram
     */
    public static void addHologramObject(Hologram hologram) {
        SloverHologram.allHologramObjects.add(hologram);
    }

    /**
     * A method which returns list of holograms from file
     * @return list of hologram
     */
    @SuppressWarnings("unchecked")
    public List<String> getHologramNames() {
        return (List<String>) this.sloverHologramData.getList("hologram-list");
    }


}
