package ru.practicum.explore.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explore.dto.event.EventOutDto;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationOutDto {
    private Long id;
    private String title;
    private List<EventOutDto> events;
    private Boolean pinned;
}