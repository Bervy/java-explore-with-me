package ru.practicum.explore.controller.private_part.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.controller.private_part.UserRequestPrivateController;
import ru.practicum.explore.dto.request.RequestFullDto;
import ru.practicum.explore.service.private_part.impl.UserRequestPrivateServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class UserRequestPrivateControllerImpl implements UserRequestPrivateController {

    private final UserRequestPrivateServiceImpl requestsService;

    @PostMapping
    @Override
    public RequestFullDto addRequest(
            @PathVariable Long userId,
            @RequestParam(name = "eventId") Long eventId) {
        return requestsService.addRequest(userId, eventId);
    }

    @GetMapping
    @Override
    public List<RequestFullDto> findAllRequests(
            @PathVariable Long userId) {
        return requestsService.findAllRequests(userId);
    }

    @PatchMapping("{requestId}/cancel")
    @Override
    public RequestFullDto cancelRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId) {
        return requestsService.cancelRequest(userId, requestId);
    }
}