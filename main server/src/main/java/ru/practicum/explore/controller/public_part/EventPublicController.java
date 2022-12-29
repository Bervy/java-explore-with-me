package ru.practicum.explore.controller.public_part;

import ru.practicum.explore.dto.event.EventPublicFullDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface EventPublicController {

    List<EventPublicFullDto> findAllEvents(
            String text,
            Long[] categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            Boolean onlyAvailable,
            String sort,
            @PositiveOrZero Integer from,
            @Positive Integer size,
            HttpServletRequest request
    );

    EventPublicFullDto findEventById(@Positive Long eventId, HttpServletRequest request);
}