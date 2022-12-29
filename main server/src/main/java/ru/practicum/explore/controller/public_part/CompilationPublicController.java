package ru.practicum.explore.controller.public_part;

import ru.practicum.explore.dto.compilation.CompilationFullDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface CompilationPublicController {

    List<CompilationFullDto> findAllCompilations(Boolean pinned, @PositiveOrZero Integer from, @Positive Integer size);

    CompilationFullDto findCompilationById(@Positive Long compId);
}