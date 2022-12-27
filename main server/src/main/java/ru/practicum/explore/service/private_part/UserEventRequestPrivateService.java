package ru.practicum.explore.service.private_part;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.request.RequestOutDto;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.RequestMapper;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.request.Request;
import ru.practicum.explore.model.request.RequestState;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.RequestRepository;
import ru.practicum.explore.repository.UserRepository;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserEventRequestPrivateService {
    private final RequestRepository requestRepository;
    private final UserRepository usersRepository;
    private final EventRepository eventsRepository;

    @Transactional
    public RequestOutDto confirmRequest(Long userId, Long eventId, Long requestId) throws AccessDeniedException {
        if (!usersRepository.existsById(userId)) {
            throw new NotFoundException("User ID not found.");
        }
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Request ID not found.")
        );
        if (request.getStatus() != RequestState.PENDING) {
            throw new IllegalStateException("Request status can be PENDING.");
        }
        if (!request.getEvent().getId().equals(eventId)) {
            throw new IllegalArgumentException("Wrong Event ID for this Request.");
        }
        if (!request.getEvent().getInitiator().getId().equals(userId)) {
            throw new AccessDeniedException("Only owner of Event can Reject Request.");
        }
        Event event = request.getEvent();
        if (event.getParticipantLimit() != 0 && (event.getParticipantLimit() - event.getConfirmedRequests()) <= 0) {
            requestRepository.rejectAllPendingRequest(eventId);
            throw new IllegalStateException("Event don't have any free slot.");
        }

        if (request.getStatus() == RequestState.PENDING) {
            request.setStatus(RequestState.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            requestRepository.saveAndFlush(request);
        }

        return RequestMapper.requestToOutDto(request);
    }

    public RequestOutDto rejectRequest(Long userId, Long eventId, Long requestId) throws AccessDeniedException {
        if (!usersRepository.existsById(userId)) {
            throw new NotFoundException("User ID not found.");
        }
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Request ID not found.")
        );
        if (!request.getEvent().getId().equals(eventId)) {
            throw new IllegalArgumentException("Wrong Event ID for this Request.");
        }
        if (!request.getEvent().getInitiator().getId().equals(userId)) {
            throw new AccessDeniedException("Only owner of Event can Reject Request.");
        }
        request.setStatus(RequestState.REJECTED);
        return RequestMapper.requestToOutDto(requestRepository.saveAndFlush(request));
    }

    public List<RequestOutDto> findAllEventRequests(Long userId, Long eventId) {
        if (!usersRepository.existsById(userId)) {
            throw new NotFoundException("User ID not found.");
        }
        if (!eventsRepository.existsById(eventId)) {
            throw new NotFoundException("Event ID not found.");
        }
        return RequestMapper.requestsToListOutDto(requestRepository.findAllByInitiatorIdAndEventId(userId, eventId));
    }
}