package ru.practicum.explore.controller;

import ru.practicum.explore.dto.stats.StatInDto;
import ru.practicum.explore.dto.stats.StatOutDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface StatsController {

    void saveHit(@Valid StatInDto statInDto);

    List<StatOutDto> getHits(
            @NotNull String start,
            @NotNull String end,
            @Valid List<String> uris,
            Boolean unique);
}