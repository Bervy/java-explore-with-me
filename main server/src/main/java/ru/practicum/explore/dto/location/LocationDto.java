package ru.practicum.explore.dto.location;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {
    private float lat;
    private float lon;
}