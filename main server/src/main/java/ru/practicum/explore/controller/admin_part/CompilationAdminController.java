package ru.practicum.explore.controller.admin_part;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.compilation.CompilationFullDto;
import ru.practicum.explore.service.admin_part.CompilationAdminService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationAdminController {
    private final CompilationAdminService compilationAdminService;

    @PostMapping
    public CompilationFullDto addCompilation(
            @Valid @RequestBody CompilationDto compilationInDto) {
        return compilationAdminService.addCompilation(compilationInDto);
    }

    @DeleteMapping("{compId}")
    public void removeCompilation(
            @Positive @PathVariable Long compId) {
        compilationAdminService.removeCompilation(compId);
    }

    @DeleteMapping("{compId}/events/{eventId}")
    public void removeEventFromCompilation(
            @Positive @PathVariable Long compId,
            @Positive @PathVariable Long eventId) {
        compilationAdminService.removeEventFromCompilation(compId, eventId);
    }

    @PatchMapping("{compId}/events/{eventId}")
    public CompilationFullDto addEventToCompilation(
            @Positive @PathVariable Long compId,
            @Positive @PathVariable Long eventId) {
        return compilationAdminService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("{compId}/pin")
    public void unPinCompilation(
            @Positive @PathVariable Long compId) {
        compilationAdminService.unPinCompilation(compId);
    }

    @PatchMapping("{compId}/pin")
    public void pinCompilation(
            @Positive @PathVariable Long compId) {
        compilationAdminService.pinCompilation(compId);
    }
}