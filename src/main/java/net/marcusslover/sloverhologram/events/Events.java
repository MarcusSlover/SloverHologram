package net.marcusslover.sloverhologram.events;

import net.marcusslover.sloverhologram.SloverHologram;
import net.marcusslover.sloverhologram.holograms.Hologram;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

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
                for (Hologram hologram : sloverHologram.hologramList) {
                    hologram.show(player);
                }
            }
        }.runTaskLater(sloverHologram, 10L);

    }

    @EventHandler
    public void onDeath(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Hologram hologram : sloverHologram.hologramList) {
                    hologram.show(player);
                }
            }
        }.runTaskLater(sloverHologram, 10L);
    }

    @EventHandler
    public void onWorldSwitch(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Hologram hologram : sloverHologram.hologramList) {
                    hologram.show(player);
                }
            }
        }.runTaskLater(sloverHologram, 10L);
    }
}
