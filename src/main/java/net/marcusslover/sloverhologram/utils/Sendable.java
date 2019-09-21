package net.marcusslover.sloverhologram.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface Sendable {
    /**
     * Sends sendable message to a certain player
     * @param player player the message will be send to
     */
    void send(final Player player);

    /**
     * Sends sendable message to a certain sender
     * @param sender sender the message will be send to
     */
    void send(final CommandSender sender);
}
