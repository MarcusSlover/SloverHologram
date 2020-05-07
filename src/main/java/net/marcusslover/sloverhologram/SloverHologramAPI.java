package net.marcusslover.sloverhologram;

import net.marcusslover.sloverhologram.holograms.Hologram;
import net.marcusslover.sloverhologram.holograms.HologramEditor;
import net.marcusslover.sloverhologram.holograms.Holograms;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class SloverHologramAPI implements HologramEditor {
    private final Holograms holograms;
    private final SloverHologram instance;
    @SuppressWarnings("WeakerAccess")
    public Map<UUID, List<EntityArmorStand>> fakeHologram = new HashMap<>();

    SloverHologramAPI() {
        this.instance = SloverHologram.getSloverHologram();
        this.holograms = instance.getHologramClass();
    }

    /**
     * An api method which updates hologram for a certain player
     * mainly used in languages and personal holograms
     * @param player a certain player
     * @param name name of the hologram
     * @param lines the new lines
     */
    @SuppressWarnings("unused")
    public void updateHologram(Player player, String name, String[] lines) {
        List<String> newLines = new ArrayList<>(Arrays.asList(lines));
        if (this.holograms.exists(name)) {
            for (Hologram hologram : instance.allHologramObjects) {
                if (hologram.getName().equalsIgnoreCase(name)) {
                    if (hologram.entities.containsKey(player.getUniqueId())) {
                        hologram.remove(player);
                        hologram.setLines(newLines);
                        hologram.show(player);
                    }
                }
            }
        }
    }

    /**
     * An api method with sets line of the hologram
     * for everyone one the server
     * @param name name of the hologram
     * @param i line number
     * @param line the new line
     */
    @SuppressWarnings("unused")
    public void setHologramLine(String name, int i, String line) {
        if (this.holograms.exists(name)) {
            this.holograms.setLine(name, i, new StringBuilder(line+" "));
        }
    }

    /**
     * A method which make a fake hologram appear
     * to a certain player
     * this hologram cannot be edited
     * @param player the player
     */
    @SuppressWarnings("unused")
    public void createFakeHologram(Player player, Location location, String[] lines) {
        if (!location.getChunk().isLoaded()) return;
        this.generateFakeHologram(player, location, lines);
    }

    /**
     * A method which generates fake hologram
     * and sends the packets to a certain player
     * @param player the player packet will be send to
     * @param location location where the hologram will be seen
     * @param lines lines of the hologram
     */
    private void generateFakeHologram(Player player, Location location, String[] lines) {
        Location loc = location.clone();
        double space = this.holograms.space();
        if (!fakeHologram.containsKey(player.getUniqueId())) {
            fakeHologram.put(player.getUniqueId(), new ArrayList<>());
        }
        List<EntityArmorStand> list = fakeHologram.get(player.getUniqueId());
        List<String> newLines = new ArrayList<>(Arrays.asList(lines));
        for (String newLine : newLines) {
            EntityArmorStand entityArmorStand = getHologramLine(loc, newLine);
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entityArmorStand);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            loc.add(0, -space, 0);
            list.add(entityArmorStand);
        }
        fakeHologram.put(player.getUniqueId(), list);
    }

    /**
     * A method which make a fake hologram appear
     * to a certain player
     *
     * Returns the entity ids
     *
     * this hologram cannot be edited
     * @param player the player
     */
    @SuppressWarnings("unused")
    public int[] createFakeHologramInt(Player player, Location location, String[] lines) {
        if (!location.getChunk().isLoaded()) return null;
        return this.generateFakeHologramInt(player, location, lines);
    }

    /**
     * A method which generates fake hologram
     * and sends the packets to a certain player
     *
     * Returns the entity ids
     *
     * @param player the player packet will be send to
     * @param location location where the hologram will be seen
     * @param lines lines of the hologram
     */
    private int[] generateFakeHologramInt(Player player, Location location, String[] lines) {
        Location loc = location.clone();
        double space = this.holograms.space();
        if (!fakeHologram.containsKey(player.getUniqueId())) {
            fakeHologram.put(player.getUniqueId(), new ArrayList<>());
        }
        List<EntityArmorStand> list = fakeHologram.get(player.getUniqueId());
        List<String> newLines = new ArrayList<>(Arrays.asList(lines));

        for (String newLine : newLines) {
            EntityArmorStand entityArmorStand = getHologramLine(loc, newLine);
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entityArmorStand);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            loc.add(0, -space, 0);
            list.add(entityArmorStand);
        }
        fakeHologram.put(player.getUniqueId(), list);

        int[] i = new int[list.size()];

        for (int i1 = 0; i1 < list.size(); i1++) {
            EntityArmorStand entityArmorStand = list.get(i1);
            i[i1] = entityArmorStand.getBukkitEntity().getEntityId();
        }

        return i;
    }

    /**
     * A method which destroys all of the hologram
     * and sends the packets to a certain player
     * @param player the player packet will be send to
     */
    @SuppressWarnings("WeakerAccess")
    public void destroyHolograms(Player player) {
        for (Map.Entry<UUID, List<EntityArmorStand>> set : fakeHologram.entrySet()) {
            if (set.getKey() == player.getUniqueId()) {
                for (EntityArmorStand entityArmorStand : set.getValue()) {
                    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityArmorStand.getBukkitEntity().getEntityId());
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }
            }
        }
        fakeHologram.remove(player.getUniqueId());
    }

    /**
     * A method which destroys a certain hologram
     * and sends the packets to a certain player
     * @param player the player packet will be send to
     */
    @SuppressWarnings("unused")
    public void destroyHologram(Player player, int[] ids) {
        List<EntityArmorStand> freshList = new ArrayList<>();

        for (Map.Entry<UUID, List<EntityArmorStand>> set : fakeHologram.entrySet()) {
            if (set.getKey().equals(player.getUniqueId())) {
                freshList.addAll(set.getValue());

                for (EntityArmorStand entityArmorStand : set.getValue()) {
                    int id = entityArmorStand.getBukkitEntity().getEntityId();
                    for (int i : ids) {
                        if (id == i) {
                            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityArmorStand.getBukkitEntity().getEntityId());
                            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                            freshList.remove(entityArmorStand);
                            break;
                        }
                    }
                }
            }
        }
        fakeHologram.put(player.getUniqueId(), freshList);
    }

    /**
     * A method with returns crafted armor stand
     * @return crafted armor stand
     */
    private EntityArmorStand getHologramLine(Location loc, String value) {
        WorldServer worldServer = ((CraftWorld) loc.getWorld()).getHandle();
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

    @Override
    public boolean exists(String name) {
        return instance.getHologramClass().exists(name);
    }

    @Override
    public void create(String name, Location location, String value) {
        instance.getHologramClass().create(name, location, value);
    }

    @Override
    public void delete(String name) {
        instance.getHologramClass().delete(name);
    }

    @Override
    public void removeLine(String name, int value) {
        instance.getHologramClass().removeLine(name, value);
    }

    @Override
    public void addLine(String name, StringBuilder value) {
        instance.getHologramClass().addLine(name, value);
    }

    @Override
    public void setLine(String name, int i, StringBuilder value) {
        instance.getHologramClass().setLine(name, i, value);
    }

    @Override
    public void teleport(String name, Location location) {
        instance.getHologramClass().teleport(name, location);
    }

    @Override
    public void teleportPlayer(String name, Player player) {
        instance.getHologramClass().teleportPlayer(name, player);
    }

    @Override
    public int size(String name) {
        return instance.getHologramClass().size(name);
    }

    @Override
    public double space() {
        return instance.getHologramClass().space();
    }
}
