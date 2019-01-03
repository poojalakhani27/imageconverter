package com.interstellar.imageconverter.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class NotFoundResponse {
    private static String DEFALUT_RESPONSE_MESSAGE = "No single channel image found with the given data";
    private ImageMetadata data;
    private String message;

    public NotFoundResponse(ImageMetadata data) {
        this.data = data;
        this.message = DEFALUT_RESPONSE_MESSAGE;
    }

    public NotFoundResponse(ImageMetadata data, String message) {
        this.data = data;
        this.message = message;
    }
}
