package net.marcusslover.sloverhologram.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * An interface created for the {@link HologramManager} class.
 * Can be used for multiple classes and storage.
 */
public interface HologramEditor {
    /**
     * A method which checks if certain hologram exists
     * @param name name of the hologram
     * @return true or false
     */
    boolean exists(final String name);

    /**
     * A method which creates a new hologram
     * @param name name of the hologram
     * @param location location where the hologram will appear
     * @param value first line of the hologram
     */
    void create(final String name, final Location location, final String value);

    /**
     * A method which removes a hologram
     * @param name name of the hologram
     */
    void delete(final String name);

    /**
     * A method which remove a line to hologram
     * @param name name of the hologram
     * @param value a line which will be removed
     */
    void removeLine(final String name, final int value);

    /**
     * A method which adds a line to hologram
     * @param name name of the hologram
     * @param value a line which will be added
     */
    void addLine(final String name, final StringBuilder value);

    /**
     * A method which sets a line to hologram
     * @param name name of the hologram
     * @param value a line which will be set
     * @param i the line number
     */
    void setLine(final String name, final int i, final StringBuilder value);

    /**
     * A method which teleports hologram to a
     * certain location
     * @param name name of the hologram
     * @param location new location
     */
    void teleport(final String name, final Location location);

    /**
     * A method which teleports a player to a
     * certain hologram
     * @param name name of the hologram
     * @param player the target
     */
    void teleportPlayer(final String name, final Player player);

    /**
     * A method which returns size of hologram lines
     * @param name name of the hologram
     * @return size of the hologram list
     */
    int size(final String name);

    /**
     * A method which returns a double which is
     * a distance between hologram lines
     * @return distance in double
     */
    double space();

    /**
     * A method which checks if a certain hologram
     * listens to player interactions through a packet adapter.
     * @param name name of the hologram
     * @return true if toggled, false if not
     */
    boolean adapts(final String name);

    /**
     * A method which toggles whether the hologram's interaction
     * adapter should be enabled or not. It's for people
     * who want to create in-game holograms and use the API to be
     * able to handle the hologram interaction through their code.
     * @param name the hologram name
     * @param toggle true / false
     */
    void toggleAdapt(final String name, final boolean toggle);
}
