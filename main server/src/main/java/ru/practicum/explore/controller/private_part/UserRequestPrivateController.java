package ru.practicum.explore.controller.private_part;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.request.RequestOutDto;
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
    public RequestOutDto addRequest(@Positive @PathVariable Long userId,
                                    @RequestParam(name = "eventId") Long eventId) {
        return requestsService.addRequest(userId, eventId);
    }

    @GetMapping
    public List<RequestOutDto> findAllRequests(@Positive @PathVariable Long userId) {
        return requestsService.findAllRequests(userId);
    }

    @PatchMapping("{requestId}/cancel")
    public RequestOutDto cancelRequest(@Positive @PathVariable Long userId,
                                       @Positive @PathVariable Long requestId) {
        return requestsService.cancelRequest(userId, requestId);
    }
}