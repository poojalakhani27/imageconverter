package com.interstellar.imageconverter.service;

import com.interstellar.imageconverter.model.BlueChannelImage;
import com.interstellar.imageconverter.model.GreenChannelImage;
import com.interstellar.imageconverter.model.ImageMetadata;
import com.interstellar.imageconverter.model.RedChannelImage;
import com.interstellar.imageconverter.repository.StorageEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

/**
 * Provides the implementation to generate the RGB file.
 * Note that not everytime is the image generated. The implementation looks up a pre-generated image and returns it if found.
 * <code>StorageEngine</code> is used to fetch both generated and single channel images
 */
@Service
public class ImageService {

    private StorageEngine storageEngine;
    private ImageGenerator imageGenerator;

    @Autowired
    public ImageService(StorageEngine storageEngine, ImageGenerator imageGenerator) {
        this.storageEngine = storageEngine;
        this.imageGenerator = imageGenerator;
    }

    public Optional<File> generateImage(ImageMetadata imageMetadata) {
        File file = storageEngine
                .checkGeneratedImageExists(imageMetadata)
                .orElseGet(() -> generate(imageMetadata).orElse(null));
        return Optional.ofNullable(file);
    }

    private Optional<File> generate(ImageMetadata imageMetadata) {
        Optional<File> generatedImage = Optional.empty();

        Optional<RedChannelImage> redChannelImage = imageMetadata.getChannelMap().redSensorBand()
                .flatMap(sensorBand -> storageEngine.getSingleChannelImage(imageMetadata.getUtmZone(), imageMetadata.getLatitudeBand(), imageMetadata.getGridSquare(), imageMetadata.getParsedDate(), sensorBand))
                .map(RedChannelImage::new);

        Optional<GreenChannelImage> greenChannelImage = imageMetadata.getChannelMap().greenSensorBand()
                .flatMap(sensorBand -> storageEngine.getSingleChannelImage(imageMetadata.getUtmZone(), imageMetadata.getLatitudeBand(), imageMetadata.getGridSquare(), imageMetadata.getParsedDate(), sensorBand))
                .map(GreenChannelImage::new);

        Optional<BlueChannelImage> blueChannelImage = imageMetadata.getChannelMap().blueSensorBand()
                .flatMap(sensorBand -> storageEngine.getSingleChannelImage(imageMetadata.getUtmZone(), imageMetadata.getLatitudeBand(), imageMetadata.getGridSquare(), imageMetadata.getParsedDate(), sensorBand))
                .map(BlueChannelImage::new);

        if (redChannelImage.isPresent() || blueChannelImage.isPresent() || greenChannelImage.isPresent()) {
            BufferedImage bufferedImage = imageGenerator.combine(redChannelImage, greenChannelImage, blueChannelImage.get());
            generatedImage = Optional.of(storageEngine.storeGeneratedImage(bufferedImage, imageMetadata));
        }
        return generatedImage;
    }
}
