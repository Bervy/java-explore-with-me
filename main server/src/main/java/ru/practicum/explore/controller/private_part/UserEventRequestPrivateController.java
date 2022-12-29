package ru.practicum.explore.controller.private_part;

import ru.practicum.explore.dto.request.RequestFullDto;

import javax.validation.constraints.Positive;
import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserEventRequestPrivateController {

    List<RequestFullDto> findAllEventRequests(@Positive Long userId, @Positive Long eventId);

    RequestFullDto confirmRequest(@Positive Long userId, @Positive Long eventId, @Positive Long reqId)
            throws AccessDeniedException;

    RequestFullDto rejectRequest(@Positive Long userId, @Positive Long eventId, @Positive Long reqId)
            throws AccessDeniedException;
}