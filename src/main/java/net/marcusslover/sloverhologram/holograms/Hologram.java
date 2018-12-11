package net.marcusslover.sloverhologram.holograms;

import net.marcusslover.sloverhologram.SloverHologram;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hologram implements Cloneable {
    private final String name;
    private List<String> lines;
    private Location location;
    public Map<Player, List<EntityArmorStand>> entities = new HashMap<>();

    /**
     * A method which creates a new hologram object
     * @param name name of the hologram
     * @param lines lines of the hologram
     * @param location location of the hologram
     */
    public Hologram(String name, List<String> lines, Location location) {
        this.name = name;
        this.lines = lines;
        this.location = location;
        SloverHologram.addHologramObject(this);
    }

    /**
     * A method which returns the location
     * @return location of the hologram
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * A method which sets location of hologram
     * @param location new location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * A method which returns hologram line
     * @param i number
     * @return actual string line of the hologram
     */
    public String getLine(int i) {
        return lines.get(i);
    }

    /**
     * A method which returns lines of the hologram
     * @return lines of the hologram
     */
    public List<String> getLines() {
        return this.lines;
    }

    /**
     * A method which returns name of the hologram
     * @return name of the hologram
     */
    public String getName() {
        return this.name;
    }

    /**
     * A method which sets lines of the hologram
     * @param stringList a new list of lines
     */
    public void setLines(List<String> stringList) {
        this.lines = stringList;
    }

    /**
     * A method which make the hologram appear
     * to a certain player
     * @param player the player
     */
    public void show(Player player) {
        this.generateHologram(player);
    }

    /**
     * A method which make the hologram disappear
     * to a certain player
     * @param player the player
     */
    public void remove(Player player) {
        this.destroyHologram(player);
    }

    /**
     * A method which generates the hologram
     * and sends the packets to a certain player
     * @param player the player packet will be send to
     */
    private void generateHologram(Player player) {
        for (Map.Entry<Player, List<EntityArmorStand>> set : entities.entrySet()) {
            if (set.getKey().getUniqueId() == player.getUniqueId()) {
                for (EntityArmorStand entityArmorStand : set.getValue()) {
                    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityArmorStand.getBukkitEntity().getEntityId());
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }
            }
        }
        Location loc = this.location.clone();
        double space = Holograms.space();
        if (!entities.containsKey(player)) {
            entities.put(player, new ArrayList<>());
        }
        List<EntityArmorStand> list = entities.get(player);
        for (int i = 0; i < this.lines.size(); i++) {
            EntityArmorStand entityArmorStand = getHologramLine(loc, this.getLine(i));
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entityArmorStand);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            loc.add(0, -space, 0);
            list.add(entityArmorStand);
        }
        entities.put(player, list);
    }

    /**
     * A method which destroys the hologram
     * and sends the packets to a certain player
     * @param player the player packet will be send to
     */
    private void destroyHologram(Player player) {
        for (Map.Entry<Player, List<EntityArmorStand>> set : entities.entrySet()) {
            if (set.getKey().getUniqueId() == player.getUniqueId()) {
                for (EntityArmorStand entityArmorStand : set.getValue()) {
                    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityArmorStand.getBukkitEntity().getEntityId());
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }
            }
        }
    }

    /**
     * A method with returns crafted armor stand
     * @return crafted armor stand
     */
    private EntityArmorStand getHologramLine(Location loc, String value) {
        WorldServer worldServer = ((CraftWorld) this.location.getWorld()).getHandle();
        EntityArmorStand entityArmorStand = new EntityArmorStand(worldServer);
        entityArmorStand.setSilent(true);
        entityArmorStand.setMarker(false);
        entityArmorStand.setNoGravity(true);
        entityArmorStand.setSmall(true);
        entityArmorStand.setInvisible(true);
        entityArmorStand.setCustomNameVisible(true);
        entityArmorStand.setCustomName(getColor(value));
        entityArmorStand.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        return entityArmorStand;
    }

    /**
     * A method which returns colored string
     * @param string a normal string
     * @return colored string
     */
    private String getColor(String string) {
        return string.replaceAll("&", "§");
    }

    /**
     * A method which clones current class object
     * to that point which won't be the same one
     * @return cloned class
     */
    @Override
    public Hologram clone() {
        try {
            SloverHologram.addHologramObject((Hologram) super.clone());
            return (Hologram) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
}
