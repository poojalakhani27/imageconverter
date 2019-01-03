package com.interstellar.imageconverter.model;

import java.util.Optional;

public enum ChannelMap {
    VISIBLE() {
        @Override
        public Optional<String> redSensorBand() {
            return Optional.of("04");
        }

        @Override
        public Optional<String> greenSensorBand() {
            return Optional.of("03");
        }

        @Override
        public Optional<String> blueSensorBand() {
            return Optional.of("02");
        }
    },
    VEGETATION {
        @Override
        public Optional<String> redSensorBand() {
            return Optional.of("05");
        }

        @Override
        public Optional<String> greenSensorBand() {
            return Optional.of("06");
        }

        @Override
        public Optional<String> blueSensorBand() {
            return Optional.of("07");
        }
    },
    WATERVAPOR {
        @Override
        public Optional<String> redSensorBand() {
            return Optional.empty();
        }

        @Override
        public Optional<String> greenSensorBand() {
            return Optional.empty();
        }

        @Override
        public Optional<String> blueSensorBand() {
            return Optional.of("09");
        }
    };

    public abstract Optional<String> redSensorBand();

    public abstract Optional<String> greenSensorBand();

    public abstract Optional<String> blueSensorBand();
}
