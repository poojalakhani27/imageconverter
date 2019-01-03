package com.interstellar.imageconverter.model;

import java.awt.*;
import java.io.File;

public class RedChannelImage extends SingleChannelImage {
    public RedChannelImage(File file) {
        super(file);
    }

    @Override
    public Integer getColorForPixel(int x, int y) {
        return new Color(getBufferedImage().getRGB(x, y)).getRed();
    }
}
