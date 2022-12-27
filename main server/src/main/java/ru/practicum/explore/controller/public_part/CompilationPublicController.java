package ru.practicum.explore.controller.public_part;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.compilation.CompilationOutDto;
import ru.practicum.explore.service.public_part.CompilationPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CompilationPublicController {

    private final CompilationPublicService compilationPublicService;

    @GetMapping
    public List<CompilationOutDto> findAllCompilations(
            @RequestParam(name = "pinned", required = false) Boolean pinned,
            @PositiveOrZero
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return compilationPublicService.findAllCompilations(pinned, from, size);
    }

    @GetMapping("{compId}")
    public CompilationOutDto findCompilationById(@Positive @PathVariable Long compId) {
        return compilationPublicService.findCompilationById(compId);
    }
}