package net.marcusslover.sloverhologram.events;

import net.marcusslover.sloverhologram.holograms.Hologram;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SloverHologramInteractEvent extends Event {
    private EntityArmorStand a = null;
    private Hologram hologram = null;
    private Player player;


    @SuppressWarnings("WeakerAccess")
    public SloverHologramInteractEvent(Hologram hologram, Player player) {
        this.hologram = hologram;
        this.player = player;
    }

    @SuppressWarnings("WeakerAccess")
    public SloverHologramInteractEvent(EntityArmorStand a, Player player) {
        this.a = a;
        this.player = player;
    }

    @SuppressWarnings("unused")
    public EntityArmorStand getA() {
        return a;
    }

    @SuppressWarnings("unused")
    public Hologram getHologram() {
        return hologram;
    }

    @SuppressWarnings("unused")
    public Player getPlayer() {
        return player;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
