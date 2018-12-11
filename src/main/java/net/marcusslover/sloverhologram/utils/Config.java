package net.marcusslover.sloverhologram.utils;

import net.marcusslover.sloverhologram.SloverHologram;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    private SloverHologram sloverHologram;
    private File file;
    public FileConfiguration config;

    public Config(SloverHologram sloverHologram, String filename) {
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
