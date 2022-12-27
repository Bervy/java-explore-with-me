package ru.practicum.explore.mapper;

import ru.practicum.explore.dto.location.LocationDto;
import ru.practicum.explore.model.location.Location;

public class LocationMapper {

    public static LocationDto locationToDto(Location location) {
        return new LocationDto(
                location.getLat(),
                location.getLon()
        );
    }

    public static Location dtoToLocation(LocationDto locationDto) {
        return new Location(
                null,
                locationDto.getLat(),
                locationDto.getLon()
        );
    }
}
