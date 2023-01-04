package ru.practicum.explore.service.public_part;

import ru.practicum.explore.dto.event.EventPublicFullDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventPublicService {

    List<EventPublicFullDto> findAllEvents(
            String text,
            Long[] categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Integer from,
            Integer size,
            HttpServletRequest request
    );

    EventPublicFullDto findEventById(Long eventId, HttpServletRequest request);
}