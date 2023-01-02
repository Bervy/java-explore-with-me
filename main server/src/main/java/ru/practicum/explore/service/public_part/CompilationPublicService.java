package ru.practicum.explore.service.public_part;

import ru.practicum.explore.dto.compilation.CompilationFullDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface CompilationPublicService {

    List<CompilationFullDto> findAllCompilations(Boolean pinned, @PositiveOrZero Integer from, @Positive Integer size);

    CompilationFullDto findCompilationById(@Positive Long compId);
}