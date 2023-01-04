package ru.practicum.explore.service.private_part.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.event.EventDto;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.error.ConflictException;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.user.User;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.UserRepository;
import ru.practicum.explore.service.private_part.UserEventPrivateService;
import ru.practicum.explore.utils.Utils;

import java.security.InvalidParameterException;
import java.util.List;

import static ru.practicum.explore.error.ExceptionDescriptions.*;

@Service
@RequiredArgsConstructor
public class UserEventPrivateServiceImpl implements UserEventPrivateService {

    private static final int USER_TIME_HOUR_BEFORE_START = 2;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public EventFullDto addEvent(Long userId, EventDto eventInDto) {
        Category category = getCategoryFromRepository(eventInDto.getCategory());
        User user = getUserFromRepository(userId);
        if (eventInDto.getLocation() == null) {
            throw new InvalidParameterException(LOCATION_IS_NULL.getTitle());
        }
        if (eventInDto.getPaid() == null) {
            throw new InvalidParameterException(PAID_IS_NULL.getTitle());
        }
        Utils.checkTimeBeforeOrThrow(eventInDto.getEventDate(), USER_TIME_HOUR_BEFORE_START);
        Event event = EventMapper.dtoInToEvent(eventInDto, category);
        event.setInitiator(user);
        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0);
        event.setViews(0L);
        return EventMapper.eventToOutDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, EventDto eventInDto) {
        Event event = getEventFromRepository(eventInDto.getEventId(), userId);
        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException(EVENT_IS_PUBLISHED.getTitle());
        } else if (event.getState() == EventState.CANCELED) {
            event.setState(EventState.PENDING);
        }
        Utils.checkTimeBeforeOrThrow(event.getEventDate(), USER_TIME_HOUR_BEFORE_START);
        Utils.setNotNullParamToEntity(eventInDto, event, categoryRepository);
        return EventMapper.eventToOutDto(eventRepository.saveAndFlush(event));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> findAllEvents(Long userId, Integer from, Integer size) {
        checkUserExists(userId);
        Sort sort = Sort.sort(Event.class).by(Event::getEventDate).descending();
        Pageable pageable = PageRequest.of(from / size, size, sort);
        return EventMapper.eventToListOutDto(eventRepository.findAllByInitiatorId(userId, pageable));
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEvent(Long userId, Long eventId) {
        Event event = getEventFromRepository(eventId, userId);
        return EventMapper.eventToOutDto(event);
    }

    @Override
    @Transactional
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND.getTitle()));
        if (event.getState() != EventState.PENDING) {
            throw new ConflictException(EVENT_IS_NOT_PENDING.getTitle());
        }
        event.setState(EventState.CANCELED);
        return EventMapper.eventToOutDto(eventRepository.save(event));
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(USER_NOT_FOUND.getTitle());
        }
    }

    private Event getEventFromRepository(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND.getTitle()));
    }

    private User getUserFromRepository(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getTitle()));
    }

    private Category getCategoryFromRepository(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND.getTitle()));
    }
}