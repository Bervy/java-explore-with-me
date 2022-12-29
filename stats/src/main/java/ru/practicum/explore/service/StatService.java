package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.explore.Constants;
import ru.practicum.explore.controller.StatsController;
import ru.practicum.explore.dto.stats.StatInDto;
import ru.practicum.explore.dto.stats.StatOutDto;
import ru.practicum.explore.mapper.StatMapper;
import ru.practicum.explore.model.Stat;
import ru.practicum.explore.repository.StatRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatService implements StatsController {
    private final StatRepository statsRepository;

    @Transactional
    @Override
    public void saveHit(@Valid @RequestBody StatInDto statInDto) {
        Stat stats = StatMapper.dtoToStat(statInDto);
        statsRepository.save(stats);
    }

    @Override
    public List<StatOutDto> getHits(String start, String end, List<String> uris, Boolean unique) {
        List<StatOutDto> stats;
        if (uris.isEmpty()) {
            if (Boolean.TRUE.equals(unique)) {
                stats = statsRepository.countByTimestampUniqueIp(
                        LocalDateTime.parse(start, Constants.DATE_TIME_SPACE),
                        LocalDateTime.parse(end, Constants.DATE_TIME_SPACE));
            } else {
                stats = statsRepository.countByTimestamp(
                        LocalDateTime.parse(start, Constants.DATE_TIME_SPACE),
                        LocalDateTime.parse(end, Constants.DATE_TIME_SPACE));
            }
        } else {
            if (Boolean.TRUE.equals(unique)) {
                stats = statsRepository.countByTimestampAndListUniqueIp(
                        LocalDateTime.parse(start, Constants.DATE_TIME_SPACE),
                        LocalDateTime.parse(end, Constants.DATE_TIME_SPACE),
                        uris);
            } else {
                stats = statsRepository.countByTimestampAndList(
                        LocalDateTime.parse(start, Constants.DATE_TIME_SPACE),
                        LocalDateTime.parse(end, Constants.DATE_TIME_SPACE),
                        uris);
            }
        }
        return stats;
    }
}