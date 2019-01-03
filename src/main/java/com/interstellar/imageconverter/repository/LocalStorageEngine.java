package com.interstellar.imageconverter.repository;

import com.interstellar.imageconverter.model.ImageMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * @inheritDoc
 */
@Component
public class LocalStorageEngine implements StorageEngine {
    private String generatedImagesPath;

    private String singleChannelImagesDirectory;

    @Autowired
    private ResourceLoader resourceLoader;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    public LocalStorageEngine(@Value("${generated.files.directory}") String generatedImagesPath, @Value("${single.channel.file.directory}") String singleChannelImagesDirectory) {
        this.generatedImagesPath = generatedImagesPath;
        this.singleChannelImagesDirectory = singleChannelImagesDirectory;
    }

    @Override
    public File storeGeneratedImage(BufferedImage generatedImage, ImageMetadata imageMetadata) {
        File f = new File(generatedFilePath(imageMetadata.getUtmZone(), imageMetadata.getLatitudeBand(), imageMetadata.getGridSquare(), imageMetadata.getDate(), imageMetadata.getChannelMap().name()));
        try {
            f.getParentFile().mkdirs();
            f.createNewFile();
            ImageIO.write(generatedImage, "JPEG", f);
        } catch (IOException e) {
            throw new RuntimeException("Exception on writing new file " + f.getAbsolutePath());
        }
        return f;
    }

    private String generatedFilePath(Integer utmZone, Character latitudeBand, String gridSquare, String date, String channelMap) {
        SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-YYYY");
        String fileName = String.format("%s_%s_%s_%s_%s.jpg", utmZone, latitudeBand, gridSquare, date, channelMap);
        return generatedImagesPath + fileName;
    }

    @Override
    public Optional<File> checkGeneratedImageExists(ImageMetadata imageMetadata) {
        Optional<File> optionalFile;
        File file = new File(generatedFilePath(imageMetadata.getUtmZone(), imageMetadata.getLatitudeBand(), imageMetadata.getGridSquare(), imageMetadata.getDate(), imageMetadata.getChannelMap().name()));
        if (file.exists())
            optionalFile = Optional.of(file);
        else
            optionalFile = Optional.empty();
        return optionalFile;
    }

    @Override
    public Optional<File> getSingleChannelImage(Integer utmZone, Character latitudeBand, String gridSquare, Date date, String sensorBand) {
        String pattern = String.format("T%2d%s%s_%s*_B%s.tif", utmZone, latitudeBand, gridSquare, simpleDateFormat.format(date), sensorBand);
        File file = null;

        try {
            Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                    .getResources(singleChannelImagesDirectory + pattern);
            if (resources.length > 0)
                file = resources[0].getFile();
        } catch (IOException e) {
            throw new RuntimeException("Exception on reading single channel image of pattern " + pattern);
        }
        return Optional.ofNullable(file);
    }

}
