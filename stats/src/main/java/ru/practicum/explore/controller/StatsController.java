package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.stats.StatInDto;
import ru.practicum.explore.dto.stats.StatOutDto;
import ru.practicum.explore.service.StatService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatsController {
    private final StatService statService;

    @PostMapping("/hit")
    public void saveHit(
            @Valid @RequestBody StatInDto statInDto) {
        statService.saveHit(statInDto);
    }

    @GetMapping("/stats")
    public List<StatOutDto> getHits(
            @NotNull @RequestParam(name = "start") String start,
            @NotNull @RequestParam(name = "end") String end,
            @Valid @RequestParam(name = "uris", defaultValue = "", required = false) List<String> uris,
            @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        return statService.getHits(start, end, uris, unique);
    }
}