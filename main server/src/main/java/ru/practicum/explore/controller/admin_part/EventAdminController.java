package ru.practicum.explore.controller.admin_part;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.event.EventDto;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.service.admin_part.EventAdminService;
import ru.practicum.explore.utils.Constants;
import ru.practicum.explore.utils.EventSearchParameters;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class EventAdminController {
    private final EventAdminService eventAdminService;

    @GetMapping
    public List<EventFullDto> findAllEvents(
            @RequestParam(name = "users", required = false) Long[] users,
            @RequestParam(name = "states", required = false) String[] states,
            @RequestParam(name = "categories", required = false) Long[] categories,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @PositiveOrZero
            @RequestParam(name = "from", defaultValue = Constants.FROM) Integer from,
            @Positive
            @RequestParam(name = "size", defaultValue = Constants.SIZE) Integer size) {
        return eventAdminService.findAllEvents(
                new EventSearchParameters(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("{eventId}/publish")
    public EventFullDto publishEvent(
            @Positive @PathVariable Long eventId) {
        return eventAdminService.publishEvent(eventId);
    }

    @PatchMapping("{eventId}/reject")
    public EventFullDto rejectEvent(
            @Positive @PathVariable Long eventId) {
        return eventAdminService.rejectEvent(eventId);
    }

    @PutMapping("{eventId}")
    public EventFullDto updateEvent(
            @Positive @PathVariable Long eventId,
            @Valid @RequestBody EventDto eventInDto) {
        return eventAdminService.updateEvent(eventId, eventInDto);
    }
}