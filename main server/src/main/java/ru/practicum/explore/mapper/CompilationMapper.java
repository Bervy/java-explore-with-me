package ru.practicum.explore.mapper;

import ru.practicum.explore.dto.compilation.CompilationInDto;
import ru.practicum.explore.dto.compilation.CompilationOutDto;
import ru.practicum.explore.model.compilation.Compilation;
import ru.practicum.explore.model.event.Event;

import java.util.ArrayList;
import java.util.List;

public class CompilationMapper {
    public static Compilation dtoToCompilation(CompilationInDto compilationInDto, List<Event> events) {
        return new Compilation(
                null,
                compilationInDto.getTitle(),
                compilationInDto.getPinned(),
                events
        );
    }

    public static CompilationOutDto compilationToOutDto(Compilation compilation) {
        return new CompilationOutDto(
                compilation.getId(),
                compilation.getTitle(),
                EventMapper.eventToListOutDto(compilation.getEvents()),
                compilation.getPinned()
        );
    }

    public static List<CompilationOutDto> compilationToListOutDto(List<Compilation> compilationList) {
        List<CompilationOutDto> compilationOutDtoList = new ArrayList<>();
        for (Compilation compilation : compilationList) {
            compilationOutDtoList.add(compilationToOutDto(compilation));
        }
        return compilationOutDtoList;
    }
}