package com.interstellar.imageconverter.repository;


import com.interstellar.imageconverter.model.ImageMetadata;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.Optional;

/**
 * Provides behaviours to read single channel image and write the generated image
 */
public interface StorageEngine {

    File storeGeneratedImage(BufferedImage generatedImage, ImageMetadata imageMetadata);

    Optional<File> checkGeneratedImageExists(ImageMetadata imageMetadata);

    Optional<File> getSingleChannelImage(Integer utmZone, Character latitudeBand, String gridSquare, Date date, String sensorBand);
}
