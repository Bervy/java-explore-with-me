package ru.practicum.explore.controller.public_part.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.controller.public_part.CompilationPublicController;
import ru.practicum.explore.dto.compilation.CompilationFullDto;
import ru.practicum.explore.service.public_part.CompilationPublicService;
import ru.practicum.explore.utils.Constants;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationPublicControllerImpl implements CompilationPublicController {

    private final CompilationPublicService compilationPublicService;

    @GetMapping
    @Override
    public List<CompilationFullDto> findAllCompilations(
            @RequestParam(name = "pinned", required = false) Boolean pinned,
            @RequestParam(name = "from", defaultValue = Constants.FROM) Integer from,
            @RequestParam(name = "size", defaultValue = Constants.SIZE) Integer size) {
        return compilationPublicService.findAllCompilations(pinned, from, size);
    }

    @GetMapping("{compId}")
    @Override
    public CompilationFullDto findCompilationById(
            @PathVariable Long compId) {
        return compilationPublicService.findCompilationById(compId);
    }
}