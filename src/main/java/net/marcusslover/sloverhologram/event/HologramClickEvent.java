package net.marcusslover.sloverhologram.event;

import net.marcusslover.sloverhologram.hologram.Hologram;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class HologramClickEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Hologram hologram;
    private final Action action;

    public HologramClickEvent(Player who, Hologram hologram, Action action) {
        super(who);
        this.hologram = hologram;
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum Action {
        INTERACT(0),
        ATTACK(1),
        INTERACT_AT(2);

        private final int id;

        Action(int id) {
            this.id = id;
        }


        public int getId() {
            return id;
        }

        public static Action byId(int interactionType) {
            for (Action value : Action.values()) {
                if (value.getId() == interactionType) {
                    return value;
                }
            }

            return null;
        }

        public static Action byName(String interactionType) {
            for (Action value : Action.values()) {
                if (value.name().equalsIgnoreCase(interactionType)) {
                    return value;
                }
            }

            return null;
        }
    }
}
