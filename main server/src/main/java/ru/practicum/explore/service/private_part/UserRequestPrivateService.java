package ru.practicum.explore.service.private_part;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.controller.private_part.UserRequestPrivateController;
import ru.practicum.explore.dto.request.RequestFullDto;
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

import static ru.practicum.explore.error.ExceptionDescriptions.*;

@Service
@RequiredArgsConstructor
public class UserRequestPrivateService implements UserRequestPrivateController {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public RequestFullDto addRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND.getTitle())
        );
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(EVENT_NOT_FOUND.getTitle())
        );
        if (event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException(USER_CANT_REQUEST_HIMSELF.getTitle());
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new IllegalStateException(EVENT_IS_NOT_PUBLISHED.getTitle());
        }
        if (event.getParticipantLimit() != 0 && (event.getParticipantLimit() - event.getConfirmedRequests()) <= 0) {
            throw new IllegalStateException(NO_FREE_SLOT.getTitle());
        }
        RequestState newRequestState = RequestState.PENDING;
        if (Boolean.FALSE.equals(event.getRequestModeration())) {
            newRequestState = RequestState.CONFIRMED;
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        Request request = Request.builder()
                .requester(user)
                .created(LocalDateTime.now())
                .status(newRequestState)
                .event(event)
                .build();
        return RequestMapper.requestToOutDto(requestRepository.save(request));
    }

    @Override
    public List<RequestFullDto> findAllRequests(Long userId) {
        checkUserExists(userId);
        return RequestMapper.requestsToListOutDto(requestRepository.findAllByRequesterId(userId));
    }

    @Override
    public RequestFullDto cancelRequest(Long userId, Long requestId) {
        checkUserExists(userId);
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException(REQUEST_NOT_FOUND.getTitle())
        );
        request.setStatus(RequestState.CANCELED);
        return RequestMapper.requestToOutDto(requestRepository.saveAndFlush(request));
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(USER_NOT_FOUND.getTitle());
        }
    }
}