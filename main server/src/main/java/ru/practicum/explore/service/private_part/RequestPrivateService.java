package ru.practicum.explore.service.private_part;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.request.RequestOutDto;
import ru.practicum.explore.error.BadRequestException;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.RequestMapper;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.request.Request;
import ru.practicum.explore.model.request.RequestState;
import ru.practicum.explore.model.user.User;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.RequestRepository;
import ru.practicum.explore.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestPrivateService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public RequestOutDto addRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User ID not found.")
        );
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event ID not found.")
        );
        if (event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("User can't request himself.");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new IllegalStateException("Event don't PUBLISHED.");
        }
        if (event.getParticipantLimit() != 0 && (event.getParticipantLimit() - event.getConfirmedRequests()) <= 0) {
            throw new IllegalStateException("Event don't have any free slot.");
        }

        RequestState newRequestState = RequestState.PENDING;
        if (!event.getRequestModeration()) {
            newRequestState = RequestState.CONFIRMED;
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }

        Request request = new Request(
                null,
                user,
                LocalDateTime.now(),
                newRequestState,
                event
        );
        return RequestMapper.requestToOutDto(requestRepository.saveAndFlush(request));
    }

    public List<RequestOutDto> findAllRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User ID not found.");
        }
        return RequestMapper.requestsToListOutDto(requestRepository.findAllByRequesterId(userId));
    }

    public RequestOutDto cancelRequest(Long userId, Long requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User ID not found.");
        }
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Request ID not found.")
        );
        request.setStatus(RequestState.CANCELED);
        return RequestMapper.requestToOutDto(requestRepository.saveAndFlush(request));
    }
}