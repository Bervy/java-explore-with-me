package ru.practicum.explore.controller.private_part;

import ru.practicum.explore.dto.event.EventDto;
import ru.practicum.explore.dto.event.EventFullDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface UserEventPrivateController {

    EventFullDto addEvent(@Positive Long userId, @Valid EventDto eventInDto);

    EventFullDto updateEvent(@Positive Long userId, @Valid EventDto eventInDto);

    List<EventFullDto> findAllEvents(@Positive Long userId, @PositiveOrZero Integer from, @Positive Integer size);

    EventFullDto getEvent(@Positive Long userId, @Positive Long eventId);

    EventFullDto cancelEvent(@Positive Long userId, @Positive Long eventId);

    void addGrade(Long userId, Long eventId, String likeType);

    void removeGrade(Long userId, Long eventId, String likeType);
}