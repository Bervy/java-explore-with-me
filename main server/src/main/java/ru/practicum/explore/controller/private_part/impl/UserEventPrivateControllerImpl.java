package ru.practicum.explore.controller.private_part.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.controller.private_part.UserEventPrivateController;
import ru.practicum.explore.dto.event.EventDto;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.model.event.grade.GradeType;
import ru.practicum.explore.service.private_part.UserEventPrivateService;
import ru.practicum.explore.utils.Constants;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class UserEventPrivateControllerImpl implements UserEventPrivateController {

    private final UserEventPrivateService userEventPrivateService;

    @PostMapping
    @Override
    public EventFullDto addEvent(
            @PathVariable Long userId,
            @RequestBody EventDto eventInDto) {
        return userEventPrivateService.addEvent(userId, eventInDto);
    }

    @PatchMapping
    @Override
    public EventFullDto updateEvent(
            @PathVariable Long userId,
            @RequestBody EventDto eventInDto) {
        return userEventPrivateService.updateEvent(userId, eventInDto);
    }

    @GetMapping
    @Override
    public List<EventFullDto> findAllEvents(
            @PathVariable Long userId,
            @RequestParam(name = "from", defaultValue = Constants.FROM) Integer from,
            @RequestParam(name = "size", defaultValue = Constants.SIZE) Integer size) {
        return userEventPrivateService.findAllEvents(userId, from, size);
    }

    @GetMapping("{eventId}")
    @Override
    public EventFullDto getEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        return userEventPrivateService.getEvent(userId, eventId);
    }

    @PatchMapping("{eventId}")
    @Override
    public EventFullDto cancelEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        return userEventPrivateService.cancelEvent(userId, eventId);
    }

    @PutMapping("/{eventId}/like")
    @Override
    public void addGrade(
             @PathVariable Long userId,
             @PathVariable Long eventId,
             @RequestParam(name = "type") String type
    )  {
        GradeType likeType = GradeType.from(type)
                .orElseThrow(() -> new IllegalArgumentException("Unknown type: " + type));
        userEventPrivateService.addGrade(userId, eventId, likeType);
    }

    @DeleteMapping("/{eventId}/like")
    @Override
    public void removeGrade(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestParam(name = "type") String type
    )  {
        GradeType likeType = GradeType.from(type)
                .orElseThrow(() -> new IllegalArgumentException("Unknown type: " + type));
        userEventPrivateService.removeGrade(userId, eventId, likeType);
    }
}