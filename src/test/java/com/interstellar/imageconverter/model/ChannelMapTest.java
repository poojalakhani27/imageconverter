package com.interstellar.imageconverter.model;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class ChannelMapTest {
    @Test
    public void shouldMapVisibleChannelToCorrespondingSensorBands() {
        ChannelMap visible = ChannelMap.VISIBLE;
        assertEquals(Optional.of("04"), visible.redSensorBand());
        assertEquals(Optional.of("03"), visible.greenSensorBand());
        assertEquals(Optional.of("02"), visible.blueSensorBand());
    }

    @Test
    public void shouldMapVegetationChannelToCorrespondingSensorBands() {
        ChannelMap vegetation = ChannelMap.VEGETATION;
        assertEquals(Optional.of("05"), vegetation.redSensorBand());
        assertEquals(Optional.of("06"), vegetation.greenSensorBand());
        assertEquals(Optional.of("07"), vegetation.blueSensorBand());
    }

    @Test
    public void shouldMapWaterVaporChannelToCorrespondingSensorBands() {
        ChannelMap visible = ChannelMap.WATERVAPOR;
        assertEquals(Optional.empty(), visible.redSensorBand());
        assertEquals(Optional.empty(), visible.greenSensorBand());
        assertEquals(Optional.of("09"), visible.blueSensorBand());
    }

}