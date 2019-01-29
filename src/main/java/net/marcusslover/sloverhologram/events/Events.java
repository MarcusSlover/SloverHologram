package net.marcusslover.sloverhologram.events;

import net.marcusslover.sloverhologram.SloverHologram;
import net.marcusslover.sloverhologram.holograms.Hologram;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Events implements Listener {
    private final SloverHologram sloverHologram;

    public Events(SloverHologram sloverHologram) {
        this.sloverHologram = sloverHologram;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                SloverHologram.hologramList.forEach(h -> h.clearMap(player.getUniqueId()));
                SloverHologram.hologramList.forEach(h -> h.show(player));
            }
        }.runTaskLater(sloverHologram, 10L);

    }

    @EventHandler
    public void onDeath(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                SloverHologram.hologramList.forEach(h -> h.clearMap(player.getUniqueId()));
                SloverHologram.hologramList.forEach(h -> h.show(player));
            }
        }.runTaskLater(sloverHologram, 10L);
    }

    @EventHandler
    public void onWorldSwitch(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                SloverHologram.hologramList.forEach(h -> h.clearMap(player.getUniqueId()));
                SloverHologram.hologramList.forEach(h -> h.show(player));
            }
        }.runTaskLater(sloverHologram, 20L);
    }

    @EventHandler
    public void onHologramClick(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof ArmorStand) {
            SloverHologram.hologramList.forEach(h -> {
                for (Map.Entry<UUID, List<EntityArmorStand>> entry : h.entities.entrySet()) {
                    List<EntityArmorStand> list = entry.getValue();
                    list.forEach(a -> {
                        if (a.getUniqueID().equals(event.getRightClicked().getUniqueId())) {
                            SloverHologramInteractEvent sloverHologramInteractEvent = new SloverHologramInteractEvent(h, event.getPlayer());
                            Bukkit.getPluginManager().callEvent(sloverHologramInteractEvent);
                        }
                    });
                }
            });
        }
    }
}
