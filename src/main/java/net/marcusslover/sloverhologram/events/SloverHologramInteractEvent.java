package net.marcusslover.sloverhologram.events;

import net.marcusslover.sloverhologram.holograms.Hologram;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SloverHologramInteractEvent extends Event {
    public Hologram hologram;
    public Player player;

    public SloverHologramInteractEvent(Hologram hologram, Player player) {
        this.hologram = hologram;
        this.player = player;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public Player getPlayer() {
        return player;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
