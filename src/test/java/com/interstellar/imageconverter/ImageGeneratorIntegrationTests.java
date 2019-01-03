package com.interstellar.imageconverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interstellar.imageconverter.model.ChannelMap;
import com.interstellar.imageconverter.model.ImageMetadata;
import com.interstellar.imageconverter.service.ImageGenerator;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class ImageGeneratorIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private ImageGenerator imageGenerator;

    @Value("${generated.files.directory}")
    private String generatedImagesPath;


    @Before
    public void setup() throws IOException {
        deleteGeneratedImages();
    }

    private void deleteGeneratedImages() throws IOException {
        File file = new File(generatedImagesPath);
        FileUtils.deleteDirectory(file);
    }

    @Test
    public void shouldGenerateImageOnFirstRequest() throws Exception {
        ImageMetadata imageMetadata = new ImageMetadata(33, 'U', "UP", "2018-08-04", ChannelMap.VISIBLE);
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(imageMetadata);

        mockMvc.perform(post("/generate-images")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));

        verify(imageGenerator, times(1)).combine(any(Optional.class), any(Optional.class), any());

    }

    @Test
    public void shouldReturnPreGeneratedImageUponSubsequentRequests() throws Exception {
        ImageMetadata imageMetadata = new ImageMetadata(33, 'U', "UP", "2018-08-04", ChannelMap.VISIBLE);
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(imageMetadata);

        //first request
        mockMvc.perform(post("/generate-images")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));
        //verify image generated
        verify(imageGenerator, times(1)).combine(any(Optional.class), any(Optional.class), any());


        //second request
        mockMvc.perform(post("/generate-images")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));

        //verify image not generated
        verifyNoMoreInteractions(imageGenerator);
    }

    @Test
    public void shouldReturnNotFoundWhenImageDataNotFound() throws Exception {
        String dateWhenNoImageData = "2018-01-01";
        ImageMetadata imageMetadata = new ImageMetadata(33, 'U', "UP", dateWhenNoImageData, ChannelMap.VISIBLE);
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(imageMetadata);

        mockMvc.perform(post("/generate-images")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isNotFound());

    }

    @Test
    public void shouldReturnBadRequestWhenMissingRequestData() throws Exception {
        ChannelMap invalidChannelMap = null;
        ImageMetadata imageMetadata = new ImageMetadata(33, 'U', "UP", "2018-08-04", invalidChannelMap);
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(imageMetadata);

        mockMvc.perform(post("/generate-images")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest());
    }


}

