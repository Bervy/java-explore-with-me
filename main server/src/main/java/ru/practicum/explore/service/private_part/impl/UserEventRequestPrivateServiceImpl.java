package ru.practicum.explore.service.private_part.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.request.RequestFullDto;
import ru.practicum.explore.error.AccessDeniedException;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.RequestMapper;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.request.Request;
import ru.practicum.explore.model.request.RequestState;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.RequestRepository;
import ru.practicum.explore.service.private_part.UserEventRequestPrivateService;

import java.util.List;

import static ru.practicum.explore.error.ExceptionDescriptions.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserEventRequestPrivateServiceImpl implements UserEventRequestPrivateService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Override
    public RequestFullDto confirmRequest(Long userId, Long eventId, Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException(REQUEST_NOT_FOUND.getTitle())
        );
        if (request.getStatus() != RequestState.PENDING) {
            throw new IllegalStateException(REQUEST_IS_NOT_PENDING.getTitle());
        }
        if (!request.getEvent().getId().equals(eventId)) {
            throw new IllegalArgumentException(WRONG_EVENT_ID_FOR_REQUEST.getTitle());
        }
        if (!request.getEvent().getInitiator().getId().equals(userId)) {
            throw new AccessDeniedException(WRONG_USER_ID_FOR_REQUEST.getTitle());
        }
        Event event = request.getEvent();
        if (event.getParticipantLimit() != 0 && (event.getParticipantLimit() - event.getConfirmedRequests()) <= 0) {
            requestRepository.rejectAllPendingRequest(eventId);
            throw new IllegalStateException(NO_FREE_SLOT.getTitle());
        }

        if (request.getStatus() == RequestState.PENDING) {
            request.setStatus(RequestState.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            requestRepository.save(request);
        }
        return RequestMapper.requestToOutDto(request);
    }

    @Override
    public RequestFullDto rejectRequest(Long userId, Long eventId, Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException(REQUEST_NOT_FOUND.getTitle())
        );
        if (!request.getEvent().getId().equals(eventId)) {
            throw new IllegalArgumentException(WRONG_EVENT_ID_FOR_REQUEST.getTitle());
        }
        if (!request.getEvent().getInitiator().getId().equals(userId)) {
            throw new AccessDeniedException(WRONG_USER_ID_FOR_REQUEST.getTitle());
        }
        request.setStatus(RequestState.REJECTED);
        return RequestMapper.requestToOutDto(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestFullDto> findAllEventRequests(Long userId, Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(EVENT_NOT_FOUND.getTitle());
        }
        return RequestMapper.requestsToListOutDto(requestRepository.findAllByInitiatorIdAndEventId(userId, eventId));
    }
}