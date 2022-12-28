package ru.practicum.explore.service.private_part;

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
import ru.practicum.explore.utils.Utils;

import java.security.InvalidParameterException;
import java.util.List;

import static ru.practicum.explore.service.admin_part.EventAdminService.getEventFullDto;

@Service
@RequiredArgsConstructor
public class UserEventPrivateService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private static final int USER_TIME_HOUR_BEFORE_START = 2;

    @Transactional
    public EventFullDto addEvent(Long userId, EventDto eventInDto) {
        Category category = getCategoryFromRepository(eventInDto.getCategory());
        User user = getUserFromRepository(userId);
        if (eventInDto.getLocation() == null) {
            throw new InvalidParameterException("Location is null.");
        }
        if (eventInDto.getPaid() == null) {
            throw new InvalidParameterException("Paid is null.");
        }
        Utils.checkTimeBeforeOrThrow(eventInDto.getEventDate(), USER_TIME_HOUR_BEFORE_START);
        Event event = EventMapper.dtoInToEvent(eventInDto, category);
        event.setInitiator(user);
        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0);
        event.setViews(0L);
        return EventMapper.eventToOutDto(eventRepository.save(event));
    }

    @Transactional
    public EventFullDto updateEvent(Long userId, EventDto eventInDto) {
        checkUserExists(userId);
        Event event = getEventFromRepository(eventInDto.getEventId());
        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Event is published.");
        } else if (event.getState() == EventState.CANCELED) {
            event.setState(EventState.PENDING);
        }
        Utils.checkTimeBeforeOrThrow(event.getEventDate(), USER_TIME_HOUR_BEFORE_START);
        Utils.setNotNullParamToEntity(eventInDto, event, categoryRepository);
        return EventMapper.eventToOutDto(eventRepository.saveAndFlush(event));
    }

    public List<EventFullDto> findAllEvents(Long userId, Integer from, Integer size) {
        checkUserExists(userId);
        Sort sort = Sort.sort(Event.class).by(Event::getEventDate).descending();
        Pageable pageable = PageRequest.of(from / size, size, sort);
        return EventMapper.eventToListOutDto(eventRepository.findAllByInitiatorId(userId, pageable));
    }

    public EventFullDto getEvent(Long userId, Long eventId) {
        checkUserExists(userId);
        Event event = getEventFromRepository(eventId);
        return EventMapper.eventToOutDto(event);
    }

    public EventFullDto cancelEvent(Long userId, Long eventId) {
        checkUserExists(userId);
        return getEventFullDto(eventId, eventRepository);
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User ID not found.");
        }
    }

    private Event getEventFromRepository(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event ID not found."));
    }

    private User getUserFromRepository(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User ID not found."));
    }

    private Category getCategoryFromRepository(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category ID not found."));
    }
}