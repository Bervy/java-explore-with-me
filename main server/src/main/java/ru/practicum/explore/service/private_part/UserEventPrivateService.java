package ru.practicum.explore.service.private_part;

import ru.practicum.explore.dto.event.EventDto;
import ru.practicum.explore.dto.event.EventFullDto;

import java.util.List;

public interface UserEventPrivateService {

    EventFullDto addEvent(Long userId, EventDto eventInDto);

    EventFullDto updateEvent(Long userId, EventDto eventInDto);

    List<EventFullDto> findAllEvents(Long userId, Integer from, Integer size);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto cancelEvent(Long userId, Long eventId);
}