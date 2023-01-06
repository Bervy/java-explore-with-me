package ru.practicum.explore.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.stats.StatInDto;
import ru.practicum.explore.dto.stats.StatOutDto;
import ru.practicum.explore.service.StatService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatsControllerImpl {
    private final StatService statService;

    @PostMapping("/hit")
    public void saveHit(
            @RequestBody StatInDto statInDto) {
        statService.saveHit(statInDto);
    }

    @GetMapping("/stats")
    public List<StatOutDto> getHits(
            @RequestParam(name = "start") String start,
            @RequestParam(name = "end") String end,
            @RequestParam(name = "uris", defaultValue = "", required = false) List<String> uris,
            @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        return statService.getHits(start, end, uris, unique);
    }
}