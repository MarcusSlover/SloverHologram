package net.marcusslover.sloverhologram;

import net.marcusslover.sloverhologram.holograms.Hologram;
import net.marcusslover.sloverhologram.holograms.Holograms;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class SloverHologramAPI {
    private final SloverHologram sloverHologram;
    public Map<Player, List<EntityArmorStand>> fakeHologram = new HashMap<>();

    public SloverHologramAPI(SloverHologram sloverHologram) {
        this.sloverHologram = sloverHologram;
    }

    /**
     * An api method which updates hologram for a certain player
     * mainly used in languages and personal holograms
     * @param player a certain player
     * @param name name of the hologram
     * @param lines the new lines
     */
    public void updateHologram(Player player, String name, String[] lines) {
        List<String> newLines = new ArrayList<>(Arrays.asList(lines));
        if (Holograms.exists(name)) {
            for (Hologram hologram : SloverHologram.allHologramObjects) {
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
    public void setHologramLine(String name, int i, String line) {
        if (Holograms.exists(name)) {
            Holograms.setLine(name, i, new StringBuilder(line));
        }
    }

    /**
     * A method which make a fake hologram appear
     * to a certain player
     * this hologram cannot be edited
     * @param player the player
     */
    public void createFakeHologram(Player player, Location location, String[] lines) {
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
        double space = Holograms.space();
        if (!fakeHologram.containsKey(player)) {
            fakeHologram.put(player, new ArrayList<>());
        }
        List<EntityArmorStand> list = fakeHologram.get(player);
        List<String> newLines = new ArrayList<>(Arrays.asList(lines));
        for (int i = 0; i < newLines.size(); i++) {
            EntityArmorStand entityArmorStand = getHologramLine(loc, newLines.get(i));
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entityArmorStand);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            loc.add(0, -space, 0);
            list.add(entityArmorStand);
        }
        fakeHologram.put(player, list);
    }

    /**
     * A method which destroys all of the hologram
     * and sends the packets to a certain player
     * @param player the player packet will be send to
     */
    public void destroyHolograms(Player player) {
        for (Map.Entry<Player, List<EntityArmorStand>> set : fakeHologram.entrySet()) {
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
}
