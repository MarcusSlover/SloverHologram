package net.marcusslover.sloverhologram.api;

import net.marcusslover.sloverhologram.SloverHologram;
import net.marcusslover.sloverhologram.hologram.Hologram;
import net.marcusslover.sloverhologram.hologram.HologramManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * This class allows you to create and delete custom holograms.
 * This type of hologram is a bit different than the actual one.
 * It's not stored in any files and each hologram created here is
 * displayed only to a certain player.
 */
@SuppressWarnings("unused")
public class SloverHologramAPI {
    private final SloverHologram plugin = SloverHologram.getInstance();
    private final HologramManager hologramManager = plugin.getHologramManager();
    private final Map<String, Hologram> hologramMap = new HashMap<>();

    public static final Collection<UUID> disabledUpdating = new ArrayList<>();

    /**
     * A method which disables hologram updates for a certain player.
     * @param player the target
     */
    public void disableUpdating(Player player) {
        if (disabledUpdating.contains(player.getUniqueId())) {
            return;
        }

        disabledUpdating.add(player.getUniqueId());
    }

    /**
     * A method which enabled the hologram updates again.
     * @param player the target player
     */
    public void enableUpdating(Player player) {
        disabledUpdating.remove(player.getUniqueId());
    }

    public Hologram createHologram(String name, Player player, Location location, String... lines) {
        return this.createHologram(name, false, player, location, lines);
    }

    /**
     * A method that creates a new hologram for a certain player.
     * In order to show this hologram to other players you must
     * use the following method {@link #showExisting(Player, String)}.
     *
     * @param name name of the hologram
     * @param isSmall small option for the size
     * @param player the target
     * @param location specific location
     * @param lines lines of the hologram
     */
    public Hologram createHologram(String name, boolean isSmall, Player player, Location location, String... lines) {
       if (exists(name)) {
           return null;
       }
       final Hologram hologram = new Hologram(name, isSmall, Arrays.asList(lines), location, true);
       hologram.show(player);

       return hologram;
    }

    /**
     * A method that deletes a certain hologram by specifying a valid name.
     * All players won't see this hologram after this action.
     * @param name the name
     */
    public void deleteHologram(String name) {
        if (!exists(name)) {
            return;
        }
        Hologram hologram = hologramMap.get(name);
        hologram.destroy();
    }

    /**
     * A method that hides an existing hologram from a specific player.
     * You have to prove a valid name of the hologram.
     *
     * @param player the target
     * @param name name of a hologram
     */
    public void hideExisting(Player player, String name) {
        if (!exists(name)) {
            return;
        }

        final Hologram hologram = hologramMap.get(name);
        hologram.remove(player);
    }

    /**
     * A method that shows an existing hologram to a specific player.
     * You have to prove a valid name of the hologram.
     *
     * @param player the target
     * @param name name of a hologram
     */
    public void showExisting(Player player, String name) {
        if (!exists(name)) {
            return;
        }

        final Hologram hologram = hologramMap.get(name);
        hologram.remove(player); //just in case
        hologram.show(player);
    }

    /**
     * A method that checks if there is an existing hologram
     * with specified name.
     * @param name the name
     * @return true if exists, false if not
     */
    public boolean exists(String name) {
        return hologramMap.containsKey(name);
    }

    /**
     * A method that returns the main hologram manager that
     * allows you edit and create actual holograms that save in the files.
     * @return the main hologram manager
     */
    public HologramManager getHologramManager() {
        return hologramManager;
    }

    /**
     * A method that returns the hologram map with all
     * custom holograms.
     * @return the map with all custom holograms
     */
    public Map<String, Hologram> getHologramMap() {
        return hologramMap;
    }
}
