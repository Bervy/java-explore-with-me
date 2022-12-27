package ru.practicum.explore.controller.public_part;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.event.EventPublicOutDto;
import ru.practicum.explore.model.sort.SortType;
import ru.practicum.explore.service.public_part.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class EventPublicController {
    private final EventPublicService eventPublicService;

    @GetMapping
    public List<EventPublicOutDto> getEvents(
            @RequestParam(name = "text", defaultValue = "") String text,
            @RequestParam(name = "categories", required = false) Long[] categories,
            @RequestParam(name = "paid", defaultValue = "false", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "onlyAvailable", defaultValue = "false", required = false) Boolean onlyAvailable,
            @RequestParam(name = "sort", defaultValue = "EVENT_DATE", required = false) String sort,
            @PositiveOrZero
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            HttpServletRequest request
                                  ) {
        SortType sortType = SortType.from(sort)
                .orElseThrow(() -> new IllegalArgumentException("Unknown type: " + sort));
        System.out.println("123");
        return eventPublicService.findAllEvents(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sortType,
                from,
                size,
                request);
    }

    @GetMapping("{eventId}")
    public EventPublicOutDto findEventById(@Positive @PathVariable Long eventId,
                                           HttpServletRequest request) {
        return eventPublicService.findEventById(eventId, request);
    }
}