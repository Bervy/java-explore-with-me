package ru.practicum.explore.controller.private_part;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.request.RequestFullDto;
import ru.practicum.explore.service.private_part.RequestPrivateService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class UserRequestPrivateController {
    private final RequestPrivateService requestsService;

    @PostMapping
    public RequestFullDto addRequest(
            @Positive @PathVariable Long userId,
            @RequestParam(name = "eventId") Long eventId) {
        return requestsService.addRequest(userId, eventId);
    }

    @GetMapping
    public List<RequestFullDto> findAllRequests(
            @Positive @PathVariable Long userId) {
        return requestsService.findAllRequests(userId);
    }

    @PatchMapping("{requestId}/cancel")
    public RequestFullDto cancelRequest(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long requestId) {
        return requestsService.cancelRequest(userId, requestId);
    }
}