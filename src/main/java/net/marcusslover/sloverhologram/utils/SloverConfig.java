package net.marcusslover.sloverhologram.utils;

import net.marcusslover.sloverhologram.SloverHologram;

public class SloverConfig extends Config {
    public SloverConfig(SloverHologram sloverHologram, String filename) {
        super(sloverHologram, filename);
    }

    public void set(String path, double value) {
        config.set(path, value);
        save();
    }

    public double getDouble(String path, double d) {
        if (config.contains(path)) {
            return config.getDouble(path);
        } else {
            set(path, d);
        }
        return d;
    }
}
