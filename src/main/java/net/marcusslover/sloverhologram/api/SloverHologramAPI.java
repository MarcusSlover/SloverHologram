package net.marcusslover.sloverhologram.api;

import net.marcusslover.sloverhologram.SloverHologram;
import net.marcusslover.sloverhologram.hologram.Hologram;
import net.marcusslover.sloverhologram.hologram.HologramManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * This class allows you to create and delete custom holograms.
 * This type of hologram is a bit different than the normal one.
 * It's not stored in any files and each hologram that was created here is
 * displayed only to certain players.
 */
@SuppressWarnings("unused")
public class SloverHologramAPI {
    private final SloverHologram plugin = SloverHologram.getInstance();
    private final HologramManager hologramManager = plugin.getHologramManager();
    private final Map<String, Hologram> hologramMap = new HashMap<>();

    public static final Collection<UUID> disabledUpdating = new ArrayList<>();

    /**
     * Disables hologram updates.
     * @param player The target.
     */
    public void disableUpdating(Player player) {
        if (disabledUpdating.contains(player.getUniqueId())) {
            return;
        }

        disabledUpdating.add(player.getUniqueId());
    }

    /**
     * Disables hologram updates.
     * @param player The target.
     */
    public void enableUpdating(Player player) {
        disabledUpdating.remove(player.getUniqueId());
    }

    public Hologram createHologram(String name, Player player, Location location, String... lines) {
        return this.createHologram(name, false, player, location, lines);
    }

    /**
     * Creates a new hologram and shows it to a certain player.
     * However, to display this hologram to other players you have to
     * use the following method: {@link #showExisting(Player, String)}.
     *
     * @param name Name of the hologram.
     * @param isSmall Size of the hologram armor stands.
     * @param player The target.
     * @param location The location.
     * @param lines Lines of the hologram.
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
     * Deletes a certain hologram.
     * The given hologram name must be valid.
     * After this action, the hologram will dissapear to
     * all players on the server.
     * @param name The hologram name.
     */
    public void deleteHologram(String name) {
        if (!exists(name)) {
            return;
        }
        Hologram hologram = hologramMap.get(name);
        hologram.destroy();
    }

    /**
     * Hides an existing hologram from a specific player.
     * You have to prove a valid hologram name.
     *
     * @param player The target.
     * @param name The hologram name.
     */
    public void hideExisting(Player player, String name) {
        if (!exists(name)) {
            return;
        }

        final Hologram hologram = hologramMap.get(name);
        hologram.remove(player);
    }

    /**
     * Shows an existing hologram to a specific player.
     * You have to prove a valid hologram name.
     *
     * @param player The target.
     * @param name The hologram name.
     */
    public void showExisting(Player player, String name) {
        if (!exists(name)) {
            return;
        }

        final Hologram hologram = hologramMap.get(name);
        hologram.remove(player); // Remove just in case.
        hologram.show(player);
    }

    /**
     * Checks if hologram with given name exists.
     * @param name The hologram name.
     * @return True if exists, false if doesn't.
     */
    public boolean exists(String name) {
        return hologramMap.containsKey(name);
    }

    /**
     * Returns the main hologram manager that
     * allows you edit holograms that are saved in the files.
     * @return HologramManager class.
     */
    public HologramManager getHologramManager() {
        return hologramManager;
    }

    /**
     * Returns a map with all custom holograms.
     * @return The map.
     */
    public Map<String, Hologram> getHologramMap() {
        return hologramMap;
    }
}
