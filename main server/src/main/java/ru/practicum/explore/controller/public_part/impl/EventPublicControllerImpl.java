package ru.practicum.explore.controller.public_part.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.controller.public_part.EventPublicController;
import ru.practicum.explore.dto.event.EventPublicFullDto;
import ru.practicum.explore.service.public_part.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class EventPublicControllerImpl implements EventPublicController {

    private final EventPublicService eventPublicService;

    @GetMapping
    @Override
    public List<EventPublicFullDto> findAllEvents(
            @RequestParam(name = "text", defaultValue = "") String text,
            @RequestParam(name = "categories", required = false) Long[] categories,
            @RequestParam(name = "paid", defaultValue = "false", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "onlyAvailable", defaultValue = "false", required = false) Boolean onlyAvailable,
            @RequestParam(name = "sort", defaultValue = "EVENT_DATE", required = false) String sort,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        return eventPublicService.findAllEvents(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size, request);
    }

    @GetMapping("{eventId}")
    @Override
    public EventPublicFullDto findEventById(
            @PathVariable Long eventId,
            HttpServletRequest request) {
        return eventPublicService.findEventById(eventId, request);
    }
}