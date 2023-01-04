package ru.practicum.explore.service.admin_part;

import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.compilation.CompilationFullDto;

public interface CompilationAdminService {

    CompilationFullDto addCompilation(CompilationDto compilationInDto);

    void removeCompilation(Long compId);

    void removeEventFromCompilation(Long compId, Long eventId);

    CompilationFullDto addEventToCompilation(Long compId, Long eventId);

    void unPinCompilation(Long compId);

    void pinCompilation(Long compId);
}