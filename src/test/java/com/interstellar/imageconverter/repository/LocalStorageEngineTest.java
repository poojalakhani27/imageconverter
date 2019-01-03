package com.interstellar.imageconverter.repository;

import com.interstellar.imageconverter.model.ChannelMap;
import com.interstellar.imageconverter.model.ImageMetadata;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class LocalStorageEngineTest {

    private LocalStorageEngine underTest;

    private File generatedDir;
    private File singleChannelDir;


    @Before
    public void setUp() throws Exception {
        generatedDir = Files.createTempDirectory("generated").toFile();
        singleChannelDir = Files.createTempDirectory("singleChannel").toFile();
        underTest = new LocalStorageEngine(generatedDir.toString() + File.separator, singleChannelDir.toString() + File.separator);
    }

    @Test
    public void shouldCheckIfGeneratedImageExists() throws IOException {
        File file = new File(generatedDir, "33_U_UP_2018-08-04_VISIBLE.jpg");
        file.createNewFile();
        ImageMetadata imageMetadata = new ImageMetadata(33, 'U', "UP", "2018-08-04", ChannelMap.VISIBLE);

        Optional<File> generatedImage = underTest.checkGeneratedImageExists(imageMetadata);

        assertEquals(Optional.of(file), generatedImage);
    }

    @Test
    public void shouldReturnEmpyWhenCheckGeneratedImageExists() throws IOException {
        ImageMetadata imageMetadata = new ImageMetadata(33, 'U', "UP", "2018-08-04", ChannelMap.VISIBLE);

        Optional<File> generatedImage = underTest.checkGeneratedImageExists(imageMetadata);

        assertEquals(Optional.empty(), generatedImage);
    }

    @Test
    public void shouldStoreGeneratedImageWithProperName() throws IOException, ParseException {
        File file = ResourceUtils.getFile("classpath:sample.jpg");
        BufferedImage bufferedImage = ImageIO.read(file);
        ImageMetadata imageMetadata = new ImageMetadata(33, 'U', "UP", "2018-08-04", ChannelMap.VISIBLE);


        File generatedImage = underTest.storeGeneratedImage(bufferedImage, imageMetadata);

        assertEquals("33_U_UP_2018-08-04_VISIBLE.jpg", generatedImage.getName());
    }

}