package ru.practicum.explore.controller.admin_part;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.event.EventInDto;
import ru.practicum.explore.dto.event.EventOutDto;
import ru.practicum.explore.service.admin_part.EventAdminService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventAdminController {
    private final EventAdminService eventAdminService;
//
    @GetMapping
    public List<EventOutDto> findAllEvents(
            @RequestParam(name = "users", required = false) Long[] users,
            @RequestParam(name = "states", required = false) String[] states,
            @RequestParam(name = "categories", required = false) Long[] categories,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @PositiveOrZero
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return eventAdminService.findAllEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("{eventId}/publish")
    public EventOutDto publishEvent(@Positive @PathVariable Long eventId) {
        return eventAdminService.publishEvent(eventId);
    }

    @PatchMapping("{eventId}/reject")
    public EventOutDto rejectEvent(@Positive @PathVariable Long eventId) {
        return eventAdminService.rejectEvent(eventId);
    }

    @PutMapping("{eventId}")
    public EventOutDto updateEvent(@Positive @PathVariable Long eventId,
                                   @Valid @RequestBody EventInDto eventInDto) {
        return eventAdminService.updateEvent(eventId, eventInDto);
    }
}