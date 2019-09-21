package net.marcusslover.sloverhologram.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Text implements Sendable {

    private String string;
    public Text(final String string) {
        this.string = string;
    }

    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', this.string);
    }

    public boolean equals(final Text anotherText) {
        return new Text(string).toString().equals(anotherText.toString());
    }

    public boolean equals(final String anotherText) {
        return new Text(string).toString().equals(new Text(anotherText).toString());
    }

    @Override
    public void send(final Player player) {
        player.sendMessage(toColor(string));
    }

    @Override
    public void send(final CommandSender player) {
        player.sendMessage(toColor(string));
    }

    private static String toColor(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
