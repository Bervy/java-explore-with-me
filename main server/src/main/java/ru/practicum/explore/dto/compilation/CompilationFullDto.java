package ru.practicum.explore.dto.compilation;

import lombok.*;
import ru.practicum.explore.dto.event.EventFullDto;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationFullDto {
    private Long id;
    private String title;
    private List<EventFullDto> events;
    private Boolean pinned;
}