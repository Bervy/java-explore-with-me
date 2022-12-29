package ru.practicum.explore.controller.private_part.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.controller.private_part.UserEventRequestPrivateController;
import ru.practicum.explore.dto.request.RequestFullDto;
import ru.practicum.explore.service.private_part.UserEventRequestPrivateService;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class UserEventRequestPrivateControllerImpl implements UserEventRequestPrivateController {
    private final UserEventRequestPrivateService userEventRequestPrivateService;

    @GetMapping("{eventId}/requests")
    @Override
    public List<RequestFullDto> findAllEventRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        return userEventRequestPrivateService.findAllEventRequests(userId, eventId);
    }

    @PatchMapping("{eventId}/requests/{reqId}/confirm")
    @Override
    public RequestFullDto confirmRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @PathVariable Long reqId) throws AccessDeniedException {
        return userEventRequestPrivateService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("{eventId}/requests/{reqId}/reject")
    @Override
    public RequestFullDto rejectRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @PathVariable Long reqId) throws AccessDeniedException {
        return userEventRequestPrivateService.rejectRequest(userId, eventId, reqId);
    }
}