package net.marcusslover.sloverhologram.command;

import net.marcusslover.sloverhologram.SloverHologram;
import net.marcusslover.sloverhologram.holograms.Hologram;
import net.marcusslover.sloverhologram.holograms.Holograms;
import net.marcusslover.sloverhologram.utils.Text;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SloverHologramCommand implements CommandExecutor {
    private final SloverHologram sloverHologram;
    private final Holograms holograms;

    public SloverHologramCommand(SloverHologram sloverHologram) {
        this.sloverHologram = sloverHologram;
        this.holograms = SloverHologram.getHologramClass();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("sloverhologram")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("sloverhologram.use")) {
                    if (args.length == 0) {
                        new Text(SloverHologram.prefix+" &7Command arguments").send(player);
                        new Text("&f/sh create (name) (text) &7creates hologram").send(player);
                        new Text("&f/sh delete (name) &7deletes hologram").send(player);
                        new Text("&f/sh addline (name) (text) &7adds a line").send(player);
                        new Text("&f/sh removeline (name) (int) &7removes a line").send(player);
                        new Text("&f/sh setline (name) (int) (text) &7sets a line").send(player);
                        new Text("&f/sh tp (name) &7teleports to hologram").send(player);
                        new Text("&f/sh move (name) &7moves hologram").send(player);
                        new Text("&f/sh near (int) &7displays hologram in certain radius").send(player);
                        new Text("&f/sh setdistance (double) &7sets the distance between holograms").send(player);
                        new Text("&f/sh list &7displays list of holograms").send(player);
                        new Text("&ePlugin created by MarcusSlover").send(player);
                    } else {
                        if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("c")) {
                            if (args.length >= 2 && args[1] != null) {
                                if (args.length >= 3 && args[2] != null) {
                                    if (!this.holograms.exists(args[1])) {
                                        StringBuilder value = new StringBuilder();
                                        for (int i = 2; i < args.length; i++) {
                                            value.append(args[i]).append(" ");
                                        }
                                        String newValue = value.toString();
                                        newValue = newValue.substring(0, newValue.length() - 1);
                                        this.holograms.create(args[1], player.getLocation(), newValue);
                                        new Text(SloverHologram.prefix+" &7Hologram created!").send(player);
                                    } else {
                                        new Text(SloverHologram.prefix+" &7This name is already used by other hologram!").send(player);
                                    }
                                } else {
                                    if (!this.holograms.exists(args[1])) {
                                        this.holograms.create(args[1], player.getLocation(), "/sh setline "+args[1]);
                                        new Text(SloverHologram.prefix+" &7Hologram created!").send(player);
                                    } else {
                                        new Text(SloverHologram.prefix+" &7This name is already used by other hologram!").send(player);
                                    }
                                }
                            } else {
                                new Text(SloverHologram.prefix+" &7Usage: &f/sh create (name) (text)&7!").send(player);
                            }
                        }
                        if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")
                                || args[0].equalsIgnoreCase("del")) {
                            if (args.length >= 2 && args[1] != null) {
                                if (this.holograms.exists(args[1])) {
                                    new Text(SloverHologram.prefix+" &7Hologram deleted!").send(player);
                                    this.holograms.delete(args[1]);
                                } else {
                                    new Text(SloverHologram.prefix+" &7This hologram doesn't exist!").send(player);
                                }
                            } else {
                                new Text(SloverHologram.prefix+" &7Usage: &f/sh delete (name)&7!").send(player);
                            }
                        }
                        if (args[0].equalsIgnoreCase("addline") || args[0].equalsIgnoreCase("al")) {
                            if (args.length >= 2 && args[1] != null) {
                                if (args.length >= 3 && args[2] != null) {
                                    if (this.holograms.exists(args[1])) {
                                        StringBuilder value = new StringBuilder();
                                        for (int i = 2; i < args.length; i++) {
                                            value.append(args[i]).append(" ");
                                        }
                                        this.holograms.addLine(args[1], value);
                                        new Text(SloverHologram.prefix + " &7Line added!").send(player);
                                    } else {
                                        new Text(SloverHologram.prefix+" &7This hologram doesn't exist!").send(player);
                                    }
                                } else {
                                    new Text(SloverHologram.prefix+" &7Usage: &f/sh addline (name) (text)&7!").send(player);
                                }
                            } else {
                                new Text(SloverHologram.prefix+" &7Usage: &f/sh addline (name) (text)&7!").send(player);
                            }
                        }
                        if (args[0].equalsIgnoreCase("removeline") || args[0].equalsIgnoreCase("rl")) {
                            if (args.length >= 2 && args[1] != null) {
                                if (args.length >= 3 && args[2] != null) {
                                    try {
                                        int line = Integer.parseInt(args[2]);
                                        if (line >= 1) {
                                            if (this.holograms.size(args[1]) > line - 1) {
                                                this.holograms.removeLine(args[1], line);
                                                new Text(SloverHologram.prefix + " &7Line removed!").send(player);
                                            } else {
                                                new Text(SloverHologram.prefix + " &7This hologram doesn't have that many lines!").send(player);
                                            }
                                        } else {
                                            new Text(SloverHologram.prefix + " &7Use a number bigger than zero!").send(player);
                                        }
                                    } catch (NumberFormatException e) {
                                        new Text(SloverHologram.prefix + " &7This is not a number!").send(player);
                                    }
                                } else {
                                    new Text(SloverHologram.prefix+" &7Usage: &f/sh removeline (name) (number)&7!").send(player);
                                }
                            } else {
                                new Text(SloverHologram.prefix+" &7Usage: &f/sh removeline (name) (number)&7!").send(player);
                            }
                        }
                        if (args[0].equalsIgnoreCase("setline") || args[0].equalsIgnoreCase("sl")) {
                            if (args.length >= 2 && args[1] != null) {
                                if (args.length >= 3 && args[2] != null) {
                                    if (args.length >= 4 && args[3] != null) {
                                        if (this.holograms.exists(args[1])) {
                                            try {
                                                StringBuilder value = new StringBuilder();
                                                for (int i = 3; i < args.length; i++) {
                                                    value.append(args[i]).append(" ");
                                                }
                                                int line = Integer.parseInt(args[2]);
                                                if (line >= 1) {
                                                    if (this.holograms.size(args[1]) >= line) {
                                                        this.holograms.setLine(args[1], line, value);
                                                        new Text(SloverHologram.prefix + " &7Line updated!").send(player);
                                                    } else {
                                                        new Text(SloverHologram.prefix + " &7This hologram doesn't have that many lines!").send(player);
                                                    }
                                                } else {
                                                    new Text(SloverHologram.prefix + " &7Use a number bigger than zero!").send(player);
                                                }
                                            } catch (NumberFormatException e) {
                                                new Text(SloverHologram.prefix + " &7This is not a number!").send(player);
                                            }
                                        } else {
                                            new Text(SloverHologram.prefix + " &7This hologram doesn't exist!").send(player);
                                        }
                                    } else {
                                        new Text(SloverHologram.prefix+" &7Usage: &f/sh setline (name) (number) (text)&7!").send(player);
                                    }
                                } else {
                                    new Text(SloverHologram.prefix+" &7Usage: &f/sh setline (name) (number) (text)&7!").send(player);
                                }
                            } else {
                                new Text(SloverHologram.prefix+" &7Usage: &f/sh setline (name) (number) (text)&7!").send(player);
                            }
                        }
                        if (args[0].equalsIgnoreCase("tphere") || args[0].equalsIgnoreCase("move")) {
                            if (args.length >= 2 && args[1] != null) {
                                if (this.holograms.exists(args[1])) {
                                    new Text(SloverHologram.prefix+" &7Hologram teleported!").send(player);
                                    this.holograms.teleport(args[1], player.getLocation());
                                } else {
                                    new Text(SloverHologram.prefix+" &7This hologram doesn't exist!").send(player);
                                }
                            } else {
                                new Text(SloverHologram.prefix+" &7Usage: &f/sh move (name)&7!").send(player);
                            }
                        }
                        if (args[0].equalsIgnoreCase("tp")) {
                            if (args.length >= 2 && args[1] != null) {
                                if (this.holograms.exists(args[1])) {
                                    new Text(SloverHologram.prefix+" &7You were teleported!").send(player);
                                    this.holograms.teleportPlayer(args[1], player);
                                } else {
                                    new Text(SloverHologram.prefix+" &7This hologram doesn't exist!").send(player);
                                }
                            } else {
                                new Text(SloverHologram.prefix+" &7Usage: &f/sh tp (name)&7!").send(player);
                            }
                        }
                        if (args[0].equalsIgnoreCase("near")) {
                            if (args.length >= 2 && args[1] != null) {
                                try {
                                    int radius = Integer.parseInt(args[1]);
                                    List<Hologram> found = new ArrayList<>();
                                    for (Hologram hologram : SloverHologram.hologramList) {
                                        if (hologram.getLocation().getWorld().equals(player.getWorld())) {
                                            if (hologram.getLocation().distance(player.getLocation()) <= radius) {
                                                found.add(hologram);
                                            }
                                        }
                                    }
                                    if (found.isEmpty()) {
                                        new Text(SloverHologram.prefix + " &7Could not find any holograms!").send(player);
                                    } else {
                                        new Text(SloverHologram.prefix + " &7Found &a"+toFancyCost(found.size())
                                                +"&7 hologram"+(found.size() > 1 ? "s" : "")+"!").send(player);
                                        for (Hologram hologram : found) {
                                            Location loc = hologram.getLocation();
                                            DecimalFormat f = new DecimalFormat("##.000");
                                            String x = f.format(loc.getX());
                                            String y = f.format(loc.getY());
                                            String z = f.format(loc.getZ());
                                            new Text("&f"+hologram.getName()
                                                    +" &3- &6"+loc.getWorld().getName()
                                                    +"&7, x: &e"+x+"&7, y: &e"+y+"&7, z: &e"+z)
                                                    .send(player);
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    new Text(SloverHologram.prefix + " &7This is not a number!").send(player);
                                }
                            } else {
                                new Text(SloverHologram.prefix+" &7Usage: &f/sh near (int)&7!").send(player);
                            }
                        }
                        if (args[0].equalsIgnoreCase("setdistance")) {
                            if (args.length >= 2 && args[1] != null) {
                                try {
                                    double distance = Double.parseDouble(args[1]);
                                    if (distance > 0) {
                                        sloverHologram.sloverConfig.set("hologram-space", distance);
                                        new Text(SloverHologram.prefix + " &7Distance changed!").send(player);
                                    } else {
                                        new Text(SloverHologram.prefix + " &7Use a number bigger than zero!").send(player);
                                    }
                                } catch (NumberFormatException e) {
                                    new Text(SloverHologram.prefix + " &7This is not a double!").send(player);
                                }
                            } else {
                                new Text(SloverHologram.prefix+" &7Usage: &f/sh setdistance (double)&7!").send(player);
                            }
                        }
                        if (args[0].equalsIgnoreCase("list")) {
                            if (SloverHologram.hologramList.isEmpty()) {
                                new Text(SloverHologram.prefix + " &7This list is empty!").send(player);
                            } else {
                                new Text(SloverHologram.prefix + " &7There is &a"+toFancyCost(SloverHologram.hologramList.size())
                                        +" &7hologram"+(SloverHologram.hologramList.size() > 1 ? "s" : "")+"!").send(player);
                                for (Hologram hologram : SloverHologram.hologramList) {
                                    Location loc = hologram.getLocation();
                                    DecimalFormat f = new DecimalFormat("##.000");
                                    String x = f.format(loc.getX());
                                    String y = f.format(loc.getY());
                                    String z = f.format(loc.getZ());
                                    new Text("&f"+hologram.getName()
                                            +" &3- &6"+loc.getWorld().getName()
                                            +"&7, x: &e"+x+"&7, y: &e"+y+"&7, z: &e"+z)
                                            .send(player);
                                }
                            }
                        }
                    }
                } else {
                    new Text(SloverHologram.prefix+" &7No permission!").send(player);
                }
            } else {
                if (sender instanceof ConsoleCommandSender) {
                    if (args.length == 0) {
                        new Text(SloverHologram.prefix+" &7Command arguments").send(sender);
                        new Text("&f/sh create (name) (text) &7creates hologram").send(sender);
                        new Text("&f/sh delete (name) &7deletes hologram").send(sender);
                        new Text("&f/sh addline (name) (text) &7adds a line").send(sender);
                        new Text("&f/sh removeline (name) (int) &7removes a line").send(sender);
                        new Text("&f/sh setline (name) (int) (text) &7sets a line").send(sender);
                        new Text("&f/sh tp (name) &7moves hologram").send(sender);
                        new Text("&f/sh near (int) &7displays hologram in certain radius").send(sender);
                        new Text("&f/sh setdistance (double) &7sets the distance between holograms").send(sender);
                        new Text("&f/sh list &7displays list of holograms").send(sender);
                        new Text("&ePlugin created by MarcusSlover").send(sender);
                    } else {
                        if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")
                                || args[0].equalsIgnoreCase("del")) {
                            if (args.length >= 2 && args[1] != null) {
                                if (this.holograms.exists(args[1])) {
                                    new Text(SloverHologram.prefix+" &7Hologram deleted!").send(sender);
                                    this.holograms.delete(args[1]);
                                } else {
                                    new Text(SloverHologram.prefix+" &7This hologram doesn't exist!").send(sender);
                                }
                            } else {
                                new Text(SloverHologram.prefix+" &7Usage: &f/sh delete (name)&7!").send(sender);
                            }
                        }
                        if (args[0].equalsIgnoreCase("addline") || args[0].equalsIgnoreCase("al")) {
                            if (args.length >= 2 && args[1] != null) {
                                if (args.length >= 3 && args[2] != null) {
                                    if (this.holograms.exists(args[1])) {
                                        StringBuilder value = new StringBuilder();
                                        for (int i = 2; i < args.length; i++) {
                                            value.append(args[i]).append(" ");
                                        }
                                        this.holograms.addLine(args[1], value);
                                        new Text(SloverHologram.prefix + " &7Line added!").send(sender);
                                    } else {
                                        new Text(SloverHologram.prefix+" &7This hologram doesn't exist!").send(sender);
                                    }
                                } else {
                                    new Text(SloverHologram.prefix+" &7Usage: &f/sh addline (name) (text)&7!").send(sender);
                                }
                            } else {
                                new Text(SloverHologram.prefix+" &7Usage: &f/sh addline (name) (text)&7!").send(sender);
                            }
                        }
                        if (args[0].equalsIgnoreCase("removeline") || args[0].equalsIgnoreCase("rl")) {
                            if (args.length >= 2 && args[1] != null) {
                                if (args.length >= 3 && args[2] != null) {
                                    try {
                                        int line = Integer.parseInt(args[2]);
                                        if (line >= 1) {
                                            if (this.holograms.size(args[1]) > line - 1) {
                                                this.holograms.removeLine(args[1], line);
                                                new Text(SloverHologram.prefix + " &7Line removed!").send(sender);
                                            } else {
                                                new Text(SloverHologram.prefix + " &7This hologram doesn't have that many lines!").send(sender);
                                            }
                                        } else {
                                            new Text(SloverHologram.prefix + " &7Use a number bigger than zero!").send(sender);
                                        }
                                    } catch (NumberFormatException e) {
                                        new Text(SloverHologram.prefix + " &7This is not a number!").send(sender);
                                    }
                                } else {
                                    new Text(SloverHologram.prefix+" &7Usage: &f/sh removeline (name) (number)&7!").send(sender);
                                }
                            } else {
                                new Text(SloverHologram.prefix+" &7Usage: &f/sh removeline (name) (number)&7!").send(sender);
                            }
                        }
                        if (args[0].equalsIgnoreCase("setline") || args[0].equalsIgnoreCase("sl")) {
                            if (args.length >= 2 && args[1] != null) {
                                if (args.length >= 3 && args[2] != null) {
                                    if (args.length >= 4 && args[3] != null) {
                                        if (this.holograms.exists(args[1])) {
                                            try {
                                                StringBuilder value = new StringBuilder();
                                                for (int i = 3; i < args.length; i++) {
                                                    value.append(args[i]).append(" ");
                                                }
                                                int line = Integer.parseInt(args[2]);
                                                if (line >= 1) {
                                                    if (this.holograms.size(args[1]) >= line) {
                                                        this.holograms.setLine(args[1], line, value);
                                                        //new Text(SloverHologram.prefix + " &7Line updated!").send(sender);
                                                    } else {
                                                        new Text(SloverHologram.prefix + " &7This hologram doesn't have that many lines!").send(sender);
                                                    }
                                                } else {
                                                    new Text(SloverHologram.prefix + " &7Use a number bigger than zero!").send(sender);
                                                }
                                            } catch (NumberFormatException e) {
                                                new Text(SloverHologram.prefix + " &7This is not a number!").send(sender);
                                            }
                                        } else {
                                            new Text(SloverHologram.prefix + " &7This hologram doesn't exist! &c(Details: &6"+ Arrays.toString(args)).send(sender);
                                        }
                                    } else {
                                        new Text(SloverHologram.prefix+" &7Usage: &f/sh setline (name) (number) (text)&7!").send(sender);
                                    }
                                } else {
                                    new Text(SloverHologram.prefix+" &7Usage: &f/sh setline (name) (number) (text)&7!").send(sender);
                                }
                            } else {
                                new Text(SloverHologram.prefix+" &7Usage: &f/sh setline (name) (number) (text)&7!").send(sender);
                            }
                        }
                        if (args[0].equalsIgnoreCase("setdistance")) {
                            if (args.length >= 2 && args[1] != null) {
                                try {
                                    double distance = Double.parseDouble(args[1]);
                                    if (distance > 0) {
                                        sloverHologram.sloverConfig.set("hologram-space", distance);
                                        new Text(SloverHologram.prefix + " &7Distance changed!").send(sender);
                                    } else {
                                        new Text(SloverHologram.prefix + " &7Use a number bigger than zero!").send(sender);
                                    }
                                } catch (NumberFormatException e) {
                                    new Text(SloverHologram.prefix + " &7This is not a double!").send(sender);
                                }
                            } else {
                                new Text(SloverHologram.prefix+" &7Usage: &f/sh setdistance (double)&7!").send(sender);
                            }
                        }
                        if (args[0].equalsIgnoreCase("list")) {
                            if (SloverHologram.hologramList.isEmpty()) {
                                new Text(SloverHologram.prefix + " &7This list is empty!").send(sender);
                            } else {
                                new Text(SloverHologram.prefix + " &7There is &a"+toFancyCost(SloverHologram.hologramList.size())
                                        +" &7hologram"+(SloverHologram.hologramList.size() > 1 ? "s" : "")+"!").send(sender);
                                for (Hologram hologram : SloverHologram.hologramList) {
                                    Location loc = hologram.getLocation();
                                    DecimalFormat f = new DecimalFormat("##.000");
                                    String x = f.format(loc.getX());
                                    String y = f.format(loc.getY());
                                    String z = f.format(loc.getZ());
                                    new Text("&f"+hologram.getName()
                                            +" &3- &6"+loc.getWorld().getName()
                                            +"&7, x: &e"+x+"&7, y: &e"+y+"&7, z: &e"+z)
                                            .send(sender);
                                }
                            }
                        }
                    }

                }
            }
        }
        return true;
    }

    private String toFancyCost(int num) {
        return NumberFormat.getInstance().format((Integer) num);
    }
}
