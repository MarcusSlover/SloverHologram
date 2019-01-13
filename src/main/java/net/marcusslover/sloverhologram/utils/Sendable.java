package net.marcusslover.sloverhologram.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface Sendable {
    /**
     * Sends sendable message to a certain player
     * @param player player the message will be send ti
     */
    void send(Player player);
    void send(CommandSender sender);
}
