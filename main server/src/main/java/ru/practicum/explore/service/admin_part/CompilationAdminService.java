package ru.practicum.explore.service.admin_part;

import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.compilation.CompilationFullDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

public interface CompilationAdminService {

    CompilationFullDto addCompilation(@Valid CompilationDto compilationInDto);

    void removeCompilation(@Positive Long compId);

    void removeEventFromCompilation(@Positive Long compId, @Positive Long eventId);

    CompilationFullDto addEventToCompilation(@Positive Long compId, @Positive Long eventId);

    void unPinCompilation(@Positive Long compId);

    void pinCompilation(@Positive Long compId);
}