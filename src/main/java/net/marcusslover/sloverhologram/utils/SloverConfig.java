package net.marcusslover.sloverhologram.utils;

import net.marcusslover.sloverhologram.SloverHologram;

public class SloverConfig extends Config {
    public SloverConfig(final SloverHologram sloverHologram, final String filename) {
        super(sloverHologram, filename);
    }

    public void set(final String path, final double value) {
        this.config.set(path, value);
        this.save();
    }

    public double getDouble(final String path, final double d) {
        if (this.config.contains(path)) {
            return this.config.getDouble(path);
        } else {
            this.set(path, d);
        }
        return d;
    }
}
