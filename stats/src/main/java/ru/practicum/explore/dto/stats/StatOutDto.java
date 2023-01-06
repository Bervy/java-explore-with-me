package ru.practicum.explore.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatOutDto {
    private String app;
    private String uri;
    private Long hits;
}
