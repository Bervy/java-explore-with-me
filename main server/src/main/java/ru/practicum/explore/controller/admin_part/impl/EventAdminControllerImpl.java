package ru.practicum.explore.controller.admin_part.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.controller.admin_part.EventAdminController;
import ru.practicum.explore.dto.event.EventDto;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.service.admin_part.EventAdminService;
import ru.practicum.explore.utils.Constants;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class EventAdminControllerImpl implements EventAdminController {
    private final EventAdminService eventAdminService;

    @GetMapping
    @Override
    public List<EventFullDto> findAllEvents(
            @RequestParam(name = "users", required = false) Long[] users,
            @RequestParam(name = "states", required = false) String[] states,
            @RequestParam(name = "categories", required = false) Long[] categories,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "from", defaultValue = Constants.FROM) Integer from,
            @RequestParam(name = "size", defaultValue = Constants.SIZE) Integer size) {
        return eventAdminService.findAllEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("{eventId}/publish")
    @Override
    public EventFullDto publishEvent(
            @PathVariable Long eventId) {
        return eventAdminService.publishEvent(eventId);
    }

    @PatchMapping("{eventId}/reject")
    @Override
    public EventFullDto rejectEvent(
            @PathVariable Long eventId) {
        return eventAdminService.rejectEvent(eventId);
    }

    @PutMapping("{eventId}")
    @Override
    public EventFullDto updateEvent(
            @PathVariable Long eventId,
            @RequestBody EventDto eventInDto) {
        return eventAdminService.updateEvent(eventId, eventInDto);
    }
}