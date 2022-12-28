package ru.practicum.explore.controller.private_part;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.event.EventDto;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.request.RequestFullDto;
import ru.practicum.explore.service.private_part.UserEventPrivateService;
import ru.practicum.explore.service.private_part.UserEventRequestPrivateService;
import ru.practicum.explore.utils.Constants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class EventPrivateController {
    private final UserEventPrivateService userEventPrivateService;
    private final UserEventRequestPrivateService userEventRequestPrivateService;

    @PostMapping
    public EventFullDto addEvent(
            @Positive @PathVariable Long userId,
            @Valid @RequestBody EventDto eventInDto) {
        return userEventPrivateService.addEvent(userId, eventInDto);
    }

    @PatchMapping
    public EventFullDto updateEvent(
            @Positive @PathVariable Long userId,
            @Valid @RequestBody EventDto eventInDto) {
        return userEventPrivateService.updateEvent(userId, eventInDto);
    }

    @GetMapping
    public List<EventFullDto> findAllEvents(
            @Positive @PathVariable Long userId,
            @PositiveOrZero
            @RequestParam(name = "from", defaultValue = Constants.FROM) Integer from,
            @Positive
            @RequestParam(name = "size", defaultValue = Constants.SIZE) Integer size) {
        return userEventPrivateService.findAllEvents(userId, from, size);
    }

    @GetMapping("{eventId}")
    public EventFullDto getEvent(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId) {
        return userEventPrivateService.getEvent(userId, eventId);
    }

    @PatchMapping("{eventId}")
    public EventFullDto cancelEvent(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId) {
        return userEventPrivateService.cancelEvent(userId, eventId);
    }

    @GetMapping("{eventId}/requests")
    public List<RequestFullDto> findAllEventRequests(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId) {
        return userEventRequestPrivateService.findAllEventRequests(userId, eventId);
    }

    @PatchMapping("{eventId}/requests/{reqId}/confirm")
    public RequestFullDto confirmRequest(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId,
            @Positive @PathVariable Long reqId) throws AccessDeniedException {
        return userEventRequestPrivateService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("{eventId}/requests/{reqId}/reject")
    public RequestFullDto rejectRequest(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId,
            @Positive @PathVariable Long reqId) throws AccessDeniedException {
        return userEventRequestPrivateService.rejectRequest(userId, eventId, reqId);
    }
}