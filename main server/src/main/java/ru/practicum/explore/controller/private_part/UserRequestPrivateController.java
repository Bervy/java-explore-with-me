package ru.practicum.explore.controller.private_part;

import ru.practicum.explore.dto.request.RequestFullDto;

import javax.validation.constraints.Positive;
import java.util.List;

public interface UserRequestPrivateController {

    RequestFullDto addRequest(@Positive Long userId, Long eventId);

    List<RequestFullDto> findAllRequests(@Positive Long userId);

    RequestFullDto cancelRequest(@Positive Long userId, @Positive Long requestId);
}