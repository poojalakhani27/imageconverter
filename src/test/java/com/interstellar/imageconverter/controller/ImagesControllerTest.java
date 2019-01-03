package com.interstellar.imageconverter.controller;

import com.interstellar.imageconverter.model.ChannelMap;
import com.interstellar.imageconverter.model.ImageMetadata;
import com.interstellar.imageconverter.model.NotFoundResponse;
import com.interstellar.imageconverter.service.ImageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImagesControllerTest {
    private File file;

    private ImagesController underTest;

    @Mock
    private ImageService imageService;

    @Before
    public void setup() throws IOException {
        underTest = new ImagesController(imageService);
        file = File.createTempFile("dummy", ".jpg");
    }

    @Test
    public void shouldReturnMimeTypeJpgWhenServiceReturnsImage() {
        ImageMetadata imageMetadata = new ImageMetadata(33, 'U', "UP", "2018-08-04", ChannelMap.VISIBLE);
        when(imageService.generateImage(imageMetadata)).thenReturn(Optional.of(file));

        ResponseEntity responseEntity = underTest.generateImage(imageMetadata);

        assertEquals(MediaType.IMAGE_JPEG, responseEntity.getHeaders().getContentType());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void shouldReturnNotFoundResponseWhenServiceDoesNotReturnImage() {
        ImageMetadata imageMetadata = new ImageMetadata(33, 'U', "UP", "2018-08-04", ChannelMap.VISIBLE);
        when(imageService.generateImage(imageMetadata)).thenReturn(Optional.empty());

        ResponseEntity responseEntity = underTest.generateImage(imageMetadata);

        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(new NotFoundResponse(imageMetadata), responseEntity.getBody());
    }

}