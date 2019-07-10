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

    public Events(SloverHologram sloverHologram) {
        this.sloverHologram = sloverHologram;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        updateHolograms(player);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        updateHolograms(player);

    }

    @EventHandler
    public void onDeath(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        updateHolograms(player);
    }

    @EventHandler
    public void onWorldSwitch(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        updateHolograms(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        for (Hologram h : SloverHologram.hologramList) {
            h.destroyHologram(player);
        }
    }

    private void updateHolograms(Player player) {
        SloverHologram.hologramList.forEach(h -> h.destroyHologram(player));

        new BukkitRunnable() {
            @Override
            public void run() {

                for (Hologram h : SloverHologram.hologramList) {
                    if (!h.entities.containsKey(player.getUniqueId())) {
                        h.show(player);
                    }
                }
            }
        }.runTaskLater(sloverHologram, 15L);
    }

}
