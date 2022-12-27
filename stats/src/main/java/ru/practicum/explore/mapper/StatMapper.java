package ru.practicum.explore.mapper;

import ru.practicum.explore.dto.stats.StatInDto;
import ru.practicum.explore.model.Stat;

public class StatMapper {
    public static Stat dtoToStat(StatInDto statInDto) {
        Stat stat = new Stat();

        stat.setApp(statInDto.getApp());
        stat.setUri(statInDto.getUri());
        stat.setIp(statInDto.getIp());
        stat.setTimestamp(statInDto.getTimestamp());

        return stat;
    }
}