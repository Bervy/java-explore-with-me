package ru.practicum.explore.service.public_part;

import ru.practicum.explore.dto.compilation.CompilationFullDto;

import java.util.List;

public interface CompilationPublicService {

    List<CompilationFullDto> findAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationFullDto findCompilationById(Long compId);
}