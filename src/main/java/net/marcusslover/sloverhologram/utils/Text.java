package net.marcusslover.sloverhologram.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Text implements Sendable {

    private String string;
    public Text(String string) {
        this.string = string;
    }

    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', this.string);
    }

    public boolean equals(Text anotherText) {
        return new Text(string).toString().equals(anotherText.toString());
    }

    public boolean equals(String anotherText) {
        return new Text(string).toString().equals(new Text(anotherText).toString());
    }

    @Override
    public void send(Player player) {
        player.sendMessage(toColor(string));
    }

    @Override
    public void send(CommandSender player) {
        player.sendMessage(toColor(string));
    }

    public static String toColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
