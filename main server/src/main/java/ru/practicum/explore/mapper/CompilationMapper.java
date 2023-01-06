package ru.practicum.explore.mapper;

import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.compilation.CompilationFullDto;
import ru.practicum.explore.model.compilation.Compilation;
import ru.practicum.explore.model.event.Event;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation dtoToCompilation(CompilationDto compilationInDto, List<Event> events) {
        return Compilation.builder()
                .title(compilationInDto.getTitle())
                .pinned(compilationInDto.getPinned())
                .events(events)
                .build();
    }

    public static CompilationFullDto compilationToOutDto(Compilation compilation) {
        return CompilationFullDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .events(EventMapper.eventToListOutDto(compilation.getEvents()))
                .pinned(compilation.getPinned())
                .build();
    }

    public static List<CompilationFullDto> compilationToListOutDto(List<Compilation> compilationList) {
        return compilationList.stream().map(CompilationMapper::compilationToOutDto).collect(Collectors.toList());
    }
}