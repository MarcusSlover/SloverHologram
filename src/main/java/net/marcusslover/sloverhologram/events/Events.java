package net.marcusslover.sloverhologram.events;

import net.marcusslover.sloverhologram.SloverHologram;
import net.marcusslover.sloverhologram.holograms.Hologram;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class Events implements Listener {
    private final SloverHologram sloverHologram;

    public Events(final SloverHologram sloverHologram) {
        this.sloverHologram = sloverHologram;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        updateHolograms(player);
    }

    @EventHandler
    public void onTeleport(final PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        updateHolograms(player);

    }

    @EventHandler
    public void onDeath(final PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        updateHolograms(player);
    }

    @EventHandler
    public void onWorldSwitch(final PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        updateHolograms(player);
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();

        for (final Hologram h : SloverHologram.hologramList) {
            h.destroyHologram(player);
        }
    }

    private void updateHolograms(final Player player) {
        SloverHologram.hologramList.forEach(h -> h.destroyHologram(player));

        new BukkitRunnable() {
            @Override
            public void run() {

                for (final Hologram h : SloverHologram.hologramList) {
                    if (!h.entities.containsKey(player.getUniqueId())) {
                        h.show(player);
                    }
                }
            }
        }.runTaskLater(sloverHologram, 5L);
    }

}
