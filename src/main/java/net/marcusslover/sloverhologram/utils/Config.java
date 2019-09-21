package net.marcusslover.sloverhologram.utils;

import net.marcusslover.sloverhologram.SloverHologram;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final SloverHologram sloverHologram;
    private final File file;
    public final FileConfiguration config;

    @SuppressWarnings({"ResultOfMethodCallIgnored", "WeakerAccess"})
    public Config(final SloverHologram sloverHologram, final String filename) {
        this.sloverHologram = sloverHologram;
        this.file = new File(sloverHologram.getDataFolder(), filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
