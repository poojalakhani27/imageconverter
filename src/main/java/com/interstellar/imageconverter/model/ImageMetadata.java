package com.interstellar.imageconverter.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ImageMetadata {
    @NotNull
    private Integer utmZone;
    @NotNull
    private Character latitudeBand;
    @NotNull
    private String gridSquare;
    @NotNull
    private String date;
    @NotNull
    private ChannelMap channelMap;

    public ImageMetadata(@NotNull Integer utmZone, @NotNull Character latitudeBand, @NotNull String gridSquare, @NotNull String date, @NotNull ChannelMap channelMap) {
        this.utmZone = utmZone;
        this.latitudeBand = latitudeBand;
        this.gridSquare = gridSquare;
        this.date = date;
        this.channelMap = channelMap;
    }

    public Date getParsedDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Error parsing date format " + date);
        }
    }

    public void setChannelMap(String channelMapStr) {
        channelMap = ChannelMap.valueOf(channelMapStr.toUpperCase());
    }
}
