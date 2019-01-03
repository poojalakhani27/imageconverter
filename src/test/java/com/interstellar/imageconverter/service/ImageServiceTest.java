package com.interstellar.imageconverter.service;

import com.interstellar.imageconverter.model.ChannelMap;
import com.interstellar.imageconverter.model.ImageMetadata;
import com.interstellar.imageconverter.repository.StorageEngine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.ResourceUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceTest {
    @Mock
    private StorageEngine storageEngine;
    @Mock
    private ImageGenerator imageGenerator;

    private ImageService underTest;

    @Before
    public void setup() {
        underTest = new ImageService(storageEngine, imageGenerator);
    }

    @Test
    public void shouldGenerateImageOnlyIfNotExist() throws FileNotFoundException {
        ImageMetadata imageMetadata = new ImageMetadata(33, 'U', "UP", "2018-08-04", ChannelMap.VISIBLE);
        BufferedImage mockBufferedImage = mock(BufferedImage.class);
        when(storageEngine.checkGeneratedImageExists(imageMetadata)).thenReturn(Optional.empty());
        when(storageEngine.getSingleChannelImage(imageMetadata.getUtmZone(), imageMetadata.getLatitudeBand(), imageMetadata.getGridSquare(), imageMetadata.getParsedDate(), ChannelMap.VISIBLE.redSensorBand().get())).thenReturn(Optional.of(ResourceUtils.getFile("classpath:T33UUP_20180804T100031_B01.tif")));
        when(storageEngine.getSingleChannelImage(imageMetadata.getUtmZone(), imageMetadata.getLatitudeBand(), imageMetadata.getGridSquare(), imageMetadata.getParsedDate(), ChannelMap.VISIBLE.greenSensorBand().get())).thenReturn(Optional.of(ResourceUtils.getFile("classpath:T33UUP_20180804T100031_B01.tif")));
        when(storageEngine.getSingleChannelImage(imageMetadata.getUtmZone(), imageMetadata.getLatitudeBand(), imageMetadata.getGridSquare(), imageMetadata.getParsedDate(), ChannelMap.VISIBLE.blueSensorBand().get())).thenReturn(Optional.of(ResourceUtils.getFile("classpath:T33UUP_20180804T100031_B01.tif")));
        when(imageGenerator.combine(any(Optional.class), any(Optional.class), any())).thenReturn(mockBufferedImage);
        File expectedGeneratedImage = mock(File.class);
        when(storageEngine.storeGeneratedImage(mockBufferedImage, imageMetadata)).thenReturn(expectedGeneratedImage);

        Optional<File> generatedImage = underTest.generateImage(imageMetadata);

        assertEquals(Optional.of(expectedGeneratedImage), generatedImage);
        verify(imageGenerator).combine(any(Optional.class), any(Optional.class), any());
    }


    @Test
    public void shouldNotGenerateImageIfAlreadyExist() {
        ImageMetadata imageMetadata = new ImageMetadata(33, 'U', "UP", "2018-08-04", ChannelMap.VISIBLE);
        when(storageEngine.checkGeneratedImageExists(imageMetadata)).thenReturn(Optional.of(mock(File.class)));

        underTest.generateImage(imageMetadata);

        verify(storageEngine, never()).storeGeneratedImage(any(BufferedImage.class), eq(imageMetadata));
        verifyZeroInteractions(imageGenerator);
    }

    @Test
    public void shouldReturnEmptyOptionalIfImageDataNotExist() {
        ImageMetadata imageMetadata = new ImageMetadata(33, 'U', "UP", "2018-08-04", ChannelMap.VISIBLE);
        when(storageEngine.checkGeneratedImageExists(imageMetadata)).thenReturn(Optional.empty());
        when(storageEngine.getSingleChannelImage(eq(imageMetadata.getUtmZone()), eq(imageMetadata.getLatitudeBand()), eq(imageMetadata.getGridSquare()), eq(imageMetadata.getParsedDate()), any())).thenReturn(Optional.empty());

        Optional<File> actual = underTest.generateImage(imageMetadata);

        assertEquals(Optional.empty(), actual);
    }

}