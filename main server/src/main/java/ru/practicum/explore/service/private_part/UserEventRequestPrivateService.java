package ru.practicum.explore.service.private_part;

import ru.practicum.explore.dto.request.RequestFullDto;

import javax.validation.constraints.Positive;
import java.util.List;

public interface UserEventRequestPrivateService {

    List<RequestFullDto> findAllEventRequests(@Positive Long userId, @Positive Long eventId);

    RequestFullDto confirmRequest(@Positive Long userId, @Positive Long eventId, @Positive Long reqId);

    RequestFullDto rejectRequest(@Positive Long userId, @Positive Long eventId, @Positive Long reqId);
}