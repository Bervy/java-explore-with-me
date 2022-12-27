package ru.practicum.explore.controller.private_part;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.event.EventInDto;
import ru.practicum.explore.dto.event.EventOutDto;
import ru.practicum.explore.dto.request.RequestOutDto;
import ru.practicum.explore.service.private_part.UserEventPrivateService;
import ru.practicum.explore.service.private_part.UserEventRequestPrivateService;

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
    public EventOutDto addEvent(@Positive @PathVariable Long userId, @Valid @RequestBody EventInDto eventInDto) {
        return userEventPrivateService.addEvent(userId, eventInDto);
    }

    @PatchMapping
    public EventOutDto updateEvent(@Positive @PathVariable Long userId, @Valid @RequestBody EventInDto eventInDto) {
        return userEventPrivateService.updateEvent(userId, eventInDto);
    }

    @GetMapping
    public List<EventOutDto> findAllEvents(@Positive @PathVariable Long userId,
                                           @PositiveOrZero
                                           @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @Positive
                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return userEventPrivateService.findAllEvents(userId, from, size);
    }

    @GetMapping("{eventId}")
    public EventOutDto getEvent(@Positive @PathVariable Long userId,
                                @Positive @PathVariable Long eventId) {
        return userEventPrivateService.getEvent(userId, eventId);
    }

    @PatchMapping("{eventId}")
    public EventOutDto cancelEvent(@Positive @PathVariable Long userId,
                                   @Positive @PathVariable Long eventId) {
        return userEventPrivateService.cancelEvent(userId, eventId);
    }

    @GetMapping("{eventId}/requests")
    public List<RequestOutDto> findAllEventRequests(@Positive @PathVariable Long userId,
                                                    @Positive @PathVariable Long eventId) {
        return userEventRequestPrivateService.findAllEventRequests(userId, eventId);
    }

    @PatchMapping("{eventId}/requests/{reqId}/confirm")
    public RequestOutDto confirmRequest(@Positive @PathVariable Long userId,
                                   @Positive @PathVariable Long eventId,
                                   @Positive @PathVariable Long reqId) throws AccessDeniedException {
        return userEventRequestPrivateService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("{eventId}/requests/{reqId}/reject")
    public RequestOutDto rejectRequest(@Positive @PathVariable Long userId,
                                      @Positive @PathVariable Long eventId,
                                      @Positive @PathVariable Long reqId) throws AccessDeniedException {
        return userEventRequestPrivateService.rejectRequest(userId, eventId, reqId);
    }
//
//    @PutMapping("/{eventId}/like")
//    public void addLike(
//            @Positive @PathVariable Long userId,
//            @Positive @PathVariable Long eventId,
//            @RequestParam(name = "type") String type
//    ) {
//        LikeType likeType = LikeType.from(type)
//                .orElseThrow(() -> new IllegalArgumentException("Unknown type: " + type));
//        usersEventsService.addLike(userId, eventId, likeType);
//    }
//
//    @DeleteMapping("/{eventId}/like")
//    public void removeLike(
//            @Positive @PathVariable Long userId,
//            @Positive @PathVariable Long eventId,
//            @RequestParam(name = "type") String type
//    ) {
//        LikeType likeType = LikeType.from(type)
//                .orElseThrow(() -> new IllegalArgumentException("Unknown type: " + type));
//        usersEventsService.removeLike(userId, eventId, likeType);
//    }
}