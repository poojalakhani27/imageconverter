package com.interstellar.imageconverter.controller;

import com.interstellar.imageconverter.model.ImageMetadata;
import com.interstellar.imageconverter.model.NotFoundResponse;
import com.interstellar.imageconverter.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.util.Optional;

@RestController
public class ImagesController {

    private ImageService imageService;

    @Autowired
    public ImagesController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/generate-images")
    public ResponseEntity generateImage(@RequestBody @Valid ImageMetadata imageMetadata) {
        Optional<File> optionalFile = imageService.generateImage(imageMetadata);

        return optionalFile.map(
                file -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG);
                    return new ResponseEntity(new FileSystemResource(file), headers, HttpStatus.OK);
                })
                .orElseGet(
                        () -> {
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            return new ResponseEntity(new NotFoundResponse(imageMetadata), headers, HttpStatus.NOT_FOUND);
                        });
    }

}
