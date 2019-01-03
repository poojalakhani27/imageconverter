package com.interstellar.imageconverter.service;

import com.interstellar.imageconverter.model.BlueChannelImage;
import com.interstellar.imageconverter.model.GreenChannelImage;
import com.interstellar.imageconverter.model.RedChannelImage;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.stream.IntStream;

@Component
public class ImageGenerator {
    public BufferedImage combine(Optional<RedChannelImage> redChannelImage, Optional<GreenChannelImage> greenChannelImage, BlueChannelImage blueChannelImage) {
        int width = blueChannelImage.getWidth();
        int height = blueChannelImage.getHeight();
        BufferedImage jpgImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        IntStream.range(0, width).forEach(x -> {
            IntStream.range(0, height).forEach(y -> {
                Integer red = redChannelImage.isPresent() ? redChannelImage.get().getColorForPixel(x, y) : 0;
                Integer green = greenChannelImage.isPresent() ? greenChannelImage.get().getColorForPixel(x, y) : 0;
                Integer blue = blueChannelImage.getColorForPixel(x, y);
                jpgImage.setRGB(x, y, (red << 16) | (green << 8) | blue);
            });
        });

        return jpgImage;
    }
}
