package ru.practicum.explore.service.private_part;

import ru.practicum.explore.dto.request.RequestFullDto;

import java.util.List;

public interface UserEventRequestPrivateService {

    List<RequestFullDto> findAllEventRequests(Long userId, Long eventId);

    RequestFullDto confirmRequest(Long userId, Long eventId, Long reqId);

    RequestFullDto rejectRequest(Long userId, Long eventId, Long reqId);
}