package ru.practicum.explore.service.admin_part;

import ru.practicum.explore.dto.event.EventDto;
import ru.practicum.explore.dto.event.EventFullDto;

import java.util.List;

public interface EventAdminService {

    List<EventFullDto> findAllEvents(
            Long[] users,
            String[] states,
            Long[] categories,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size);

    EventFullDto publishEvent(Long eventId);

    EventFullDto rejectEvent(Long eventId);

    EventFullDto updateEvent(Long eventId, EventDto eventInDto);
}