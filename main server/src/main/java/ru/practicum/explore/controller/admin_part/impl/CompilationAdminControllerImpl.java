package ru.practicum.explore.controller.admin_part.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.controller.admin_part.CompilationAdminController;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.compilation.CompilationFullDto;
import ru.practicum.explore.service.admin_part.CompilationAdminService;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationAdminControllerImpl implements CompilationAdminController {
    private final CompilationAdminService compilationAdminService;

    @PostMapping
    @Override
    public CompilationFullDto addCompilation(
            @RequestBody CompilationDto compilationInDto) {
        return compilationAdminService.addCompilation(compilationInDto);
    }

    @DeleteMapping("{compId}")
    @Override
    public void removeCompilation(
            @PathVariable Long compId) {
        compilationAdminService.removeCompilation(compId);
    }

    @DeleteMapping("{compId}/events/{eventId}")
    @Override
    public void removeEventFromCompilation(
            @PathVariable Long compId,
            @PathVariable Long eventId) {
        compilationAdminService.removeEventFromCompilation(compId, eventId);
    }

    @PatchMapping("{compId}/events/{eventId}")
    @Override
    public CompilationFullDto addEventToCompilation(
            @PathVariable Long compId,
            @PathVariable Long eventId) {
        return compilationAdminService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("{compId}/pin")
    @Override
    public void unPinCompilation(
            @PathVariable Long compId) {
        compilationAdminService.unPinCompilation(compId);
    }

    @PatchMapping("{compId}/pin")
    @Override
    public void pinCompilation(
            @PathVariable Long compId) {
        compilationAdminService.pinCompilation(compId);
    }
}