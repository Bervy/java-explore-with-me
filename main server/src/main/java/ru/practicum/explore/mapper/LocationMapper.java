package ru.practicum.explore.mapper;

import ru.practicum.explore.dto.location.LocationDto;
import ru.practicum.explore.model.location.Location;

public class LocationMapper {

    public static LocationDto locationToDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public static Location dtoToLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }
}