package com.interstellar.imageconverter.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

abstract public class SingleChannelImage {
    private int height;
    private int width;
    private BufferedImage bufferedImage;

    public SingleChannelImage(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            bufferedImage = ImageIO.read(fis);
            height = bufferedImage.getHeight();
            width = bufferedImage.getWidth();
        } catch (IOException e) {
            throw new IllegalArgumentException("Exception on reading file into buffered image " + file.getAbsolutePath());
        }
    }

    public abstract Integer getColorForPixel(int x, int y);

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }
}
