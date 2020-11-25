package net.marcusslover.sloverhologram.hologram;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.marcusslover.sloverhologram.SloverHologram;
import net.marcusslover.sloverhologram.event.HologramClickEvent;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.*;

import static net.marcusslover.sloverhologram.event.HologramClickEvent.Action;

@SuppressWarnings("WeakerAccess")
public class Hologram {
    private final SloverHologram plugin = SloverHologram.getInstance();

    private final String name;
    private final Hologram hologram;
    private List<String> lines;
    private Location location;
    private final Chunk chunk;
    private final boolean custom;

    private final Map<UUID, List<EntityArmorStand>> entities = new HashMap<>();
    private PacketAdapter packetListener = null;
    private final boolean isSmall;

    /**
     * Creates a new hologram object.
     * @param name The hologram name.
     * @param isSmall The hologram size.
     * @param lines The hologram lines.
     * @param location The location for the hologram to appear at.
     * @param custom If true the hologram is consiered to be custom, if not it's just a global hologram
     */
    public Hologram(final String name, final boolean isSmall, final List<String> lines, final Location location, final boolean custom) {
        this.isSmall = isSmall;
        this.hologram = this;

        this.name = name;
        this.lines = lines;
        this.location = location;
        this.chunk = location.getChunk();
        this.custom = custom;

        if (!custom) {
            plugin.hologramList.add(this);
        } else {
            plugin.getAPI().getHologramMap().put(name, this);
        }
    }

    /**
     * Registers a packet adapter which detects
     * whenever players interact with the hologram.
     * To catch the results use {@link net.marcusslover.sloverhologram.event.HologramClickEvent}
     */
    public void registerListener() {
        if (plugin.getProtocolManager() != null) {
            if (packetListener != null) { // Check if already registered.
                return;
            }

            this.packetListener = new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
                @Override
                public void onPacketReceiving(PacketEvent event) {
                    PacketContainer packetContainer = event.getPacket();
                    // Checking for the type just in case.
                    if (packetContainer.getType() == PacketType.Play.Client.USE_ENTITY) {
                        Player player = event.getPlayer();
                        int clickedEntity = packetContainer.getIntegers().read(0);
                        EnumWrappers.EntityUseAction entityUseAction = packetContainer
                                .getEntityUseActions().read(0);

                        // Getting entities.
                        List<EntityArmorStand> armorStands = new ArrayList<>();
                        if (entities.containsKey(player.getUniqueId())) {
                            armorStands = entities.get(player.getUniqueId());
                        }

                        // Check if the entity belongs to this hologram set.
                        if (!armorStands.isEmpty()) {
                            for (EntityArmorStand armorStand : armorStands) {
                                // Check the ids.
                                if (armorStand.getId() == clickedEntity) {
                                    event.setCancelled(true);

                                    // Call the event sync.
                                    Bukkit.getScheduler().runTask(plugin, () -> {
                                        Action action = Action.byName(entityUseAction.name());

                                        // Return if the action is null.
                                        if (action == null) {
                                            return;
                                        }

                                        // Call the event.
                                        HologramClickEvent hologramClickEvent = new HologramClickEvent(player, hologram, action);
                                        Bukkit.getPluginManager().callEvent(hologramClickEvent);
                                    });
                                    break;
                                }
                            }
                        }

                    }
                }
            };
            plugin.getProtocolManager().addPacketListener(packetListener);
        } else {
            Bukkit.getLogger().warning("Tried registering a listener without ProtocolLib plugin installed, action denied!");
        }
    }

    public void unregisterListener() {
        if (plugin.getProtocolManager() != null) {
            if (packetListener != null) { // check if registered
                plugin.getProtocolManager().removePacketListener(packetListener);
                packetListener = null;
            }
        }
    }

    /**
     * Checks if this hologram uses small type of
     * armor stands or not.
     * @return True/false depending on the size option.
     */
    public boolean isSmall() {
        return isSmall;
    }

    /**
     * Checks either the hologram is API-created
     * or created manually in-game using a command.
     * @return True or false.
     */
    public boolean isCustom() {
        return custom;
    }

    /**
     * Returns a chunk where this hologram is located in.
     * @return The chunk.
     */
    public Chunk getChunk() {
        return chunk;
    }

    /**
     * Returns locaiton of this hologram.
     * @return This hologram's location.
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Set location of this hologram.
     * @param location New location to use.
     */
    public void setLocation(final Location location) {
        this.location = location;
    }

    /**
     * Returns a hologram line by the given index.
     * @param i Index from 0 to ...
     * @return The line.
     */
    public String getLine(final int i) {
        return lines.get(i);
    }

    /**
     * Returns a list of this hologram lines.
     * @return This hologram's lines.
     */
    public List<String> getLines() {
        return this.lines;
    }

    /**
     * Returns name of this hologram.
     * @return This hologram's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets lines of this hologram.
     * @param stringList New lines.
     */
    public void setLines(final List<String> stringList) {
        this.lines = stringList;
    }

    /**
     * Makes the hologram visible to a certain player.
     * @param player The target.
     */
    public void show(final Player player) {
        if (this.location.getWorld() == null) {
            return;
        }

        if (this.location.getWorld().equals(player.getWorld())) {
            if (this.getLocation().getChunk().isLoaded()) {
                this.generateHologram(player);
            }
        }
    }

    /**
     * Makes the hologram not visible to a certain player.
     * @param player The target.
     */
    public void remove(final Player player) {
        this.destroyHologram(player);
    }

    /**
     * Generates & spawns the hologram entities.
     * @param player The target.
     */
    private void generateHologram(final Player player) {
        List<EntityArmorStand> toRemove = new ArrayList<>();
        for (Map.Entry<UUID, List<EntityArmorStand>> set : entities.entrySet()) {
            if (set.getKey() == player.getUniqueId()) {
                for (EntityArmorStand entityArmorStand : set.getValue()) {


                    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityArmorStand.getBukkitEntity().getEntityId());
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                    toRemove.add(entityArmorStand);
                }
            }
        }

        Location loc = this.location.clone();
        double space = plugin.getHologramManager().space();
        if (!entities.containsKey(player.getUniqueId())) {
            entities.put(player.getUniqueId(), new ArrayList<>());
        }
        List<EntityArmorStand> list = entities.get(player.getUniqueId());
        toRemove.forEach(list::remove);
        for (int i = 0; i < this.lines.size(); i++) {
            EntityArmorStand entityArmorStand = getHologramLine(loc, this.getLine(i));
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entityArmorStand);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            loc.add(0, -space, 0);
            list.add(entityArmorStand);
        }
        entities.put(player.getUniqueId(), list);
    }

    /**
     * Fully destroys the hologram and makes it not visible
     * to a certain player.
     * @param player The target.
     */
    private void destroyHologram(final Player player) {
        if (!entities.containsKey(player.getUniqueId())) {
            return;
        }

        if (!this.getLocation().getChunk().isLoaded()) {
            return;
        }

        for (Map.Entry<UUID, List<EntityArmorStand>> set : entities.entrySet()) {
            if (set.getKey() == player.getUniqueId()) {
                for (EntityArmorStand entityArmorStand : set.getValue()) {
                    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityArmorStand.getBukkitEntity().getEntityId());
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }
            }
        }
        entities.remove(player.getUniqueId());
    }

    /**
     * Gets rid of all hologram lines and clears them.
     * Packets are being sent to destroy the armor stands.
     */
    public void killEntities() {
        for (Map.Entry<UUID, List<EntityArmorStand>> set : entities.entrySet()) {
            Player player = Bukkit.getPlayer(set.getKey());
            if (player != null) {
                if (player.isOnline()) {
                    for (EntityArmorStand entityArmorStand : set.getValue()) {
                        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityArmorStand.getBukkitEntity().getEntityId());
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                    }
                }
            }
        }
        entities.clear();
    }

    /**
     * Completely destroys the hologram.
     */
    public void destroy() {
        try {
            entities.forEach((key, value) -> {
                Player player = Bukkit.getPlayer(key);
                if (player != null) {
                    destroyHologram(player);
                }
            });
        } catch (Exception ignored) {}

        if (isCustom()) {
            plugin.getAPI().getHologramMap().remove(name);
        } else {
            plugin.hologramList.remove(this);
        }

        if (this.packetListener != null) {
            plugin.getProtocolManager().removePacketListener(packetListener);
        }
    }

    /**
     * Returns a NMS armor stand entity.
     * @return NMS armor stand.
     */
    private EntityArmorStand getHologramLine(final Location loc, final String value) {
        WorldServer worldServer = ((CraftWorld) this.location.getWorld()).getHandle();
        EntityArmorStand entityArmorStand = new EntityArmorStand(worldServer);

        entityArmorStand.setSilent(true);
        entityArmorStand.setMarker(false);
        entityArmorStand.setNoGravity(true);
        entityArmorStand.setSmall(this.isSmall);// is small option
        entityArmorStand.setInvisible(true);
        entityArmorStand.setCustomNameVisible(true);
        entityArmorStand.setCustomName(getColor(value));
        entityArmorStand.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

        return entityArmorStand;
    }

    /**
     * Returns a colored string.
     * @param string String to color.
     * @return A colored string
     */
    protected String getColor(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Returns all entities that represent the lines of this hologram.
     * @return Map with all entities.
     */
    public Map<UUID, List<EntityArmorStand>> getEntities() {
        return entities;
    }
}
