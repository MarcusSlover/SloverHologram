package net.marcusslover.sloverhologram.event;

import net.marcusslover.sloverhologram.SloverHologram;
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

        sloverHologram.hologramList.forEach(h -> h.remove(player));
        sloverHologram.getAPI().getHologramMap().forEach((name, hologram) -> hologram.remove(player));
    }

    private void updateHolograms(final Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                sloverHologram.hologramList.forEach(hologram -> {
                    if (hologram.getEntities().containsKey(player.getUniqueId())) {
                        hologram.remove(player);
                        hologram.show(player);
                    } else {
                        hologram.show(player);
                    }
                });
                sloverHologram.getAPI().getHologramMap().forEach((name, hologram) -> {
                    if (hologram.getEntities().containsKey(player.getUniqueId())) {
                        hologram.remove(player);
                        hologram.show(player);
                    }
                });
            }
        }.runTaskLater(sloverHologram, 5L);
    }

}
