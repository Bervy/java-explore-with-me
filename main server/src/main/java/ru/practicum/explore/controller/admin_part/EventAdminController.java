package ru.practicum.explore.controller.admin_part;

import ru.practicum.explore.dto.event.EventDto;
import ru.practicum.explore.dto.event.EventFullDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface EventAdminController {

    List<EventFullDto> findAllEvents(
            Long[] users,
            String[] states,
            Long[] categories,
            String rangeStart,
            String rangeEnd,
            @PositiveOrZero Integer from,
            @Positive Integer size);

    EventFullDto publishEvent(@Positive Long eventId);

    EventFullDto rejectEvent(@Positive Long eventId);

    EventFullDto updateEvent(@Positive Long eventId, @Valid EventDto eventInDto);
}