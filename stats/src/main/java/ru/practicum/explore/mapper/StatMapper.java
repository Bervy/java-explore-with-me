package ru.practicum.explore.mapper;

import ru.practicum.explore.dto.stats.StatInDto;
import ru.practicum.explore.model.Stat;

public class StatMapper {
    public static Stat dtoToStat(StatInDto statInDto) {
        return Stat.builder()
                .app(statInDto.getApp())
                .uri(statInDto.getUri())
                .ip(statInDto.getIp())
                .timestamp(statInDto.getTimestamp())
                .build();
    }
}