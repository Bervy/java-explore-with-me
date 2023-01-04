package ru.practicum.explore.service.private_part;

import ru.practicum.explore.dto.request.RequestFullDto;

import java.util.List;

public interface UserRequestPrivateService {

    RequestFullDto addRequest(Long userId, Long eventId);

    List<RequestFullDto> findAllRequests(Long userId);

    RequestFullDto cancelRequest(Long userId, Long requestId);
}