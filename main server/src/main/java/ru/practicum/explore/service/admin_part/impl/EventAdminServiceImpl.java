package ru.practicum.explore.service.admin_part.impl;

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
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.service.admin_part.EventAdminService;
import ru.practicum.explore.utils.Constants;
import ru.practicum.explore.utils.Utils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explore.error.ExceptionDescriptions.*;

@Service
@RequiredArgsConstructor
public class EventAdminServiceImpl implements EventAdminService {

    private static final int ADMIN_TIME_HOUR_BEFORE_START = 1;
    private static final int ONE_HUNDRED_YEARS_AFTER_NOW = 100;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    private static EventState stringToEventState(String state) {
        return EventState.valueOf(state);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> findAllEvents(
            Long[] users,
            String[] states,
            Long[] categories,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size) {
        List<EventState> stateList = states == null ? List.of() : getCorrectStates(states);
        LocalDateTime startDate = rangeStart == null ? LocalDateTime.now() :
                LocalDateTime.parse(rangeStart, Constants.DATE_TIME_SPACE);
        LocalDateTime endDate = rangeStart == null ?
                LocalDateTime.now().plusYears(ONE_HUNDRED_YEARS_AFTER_NOW) :
                LocalDateTime.parse(rangeEnd, Constants.DATE_TIME_SPACE);
        Sort sort = Sort.sort(Event.class).by(Event::getEventDate).descending();
        Pageable pageable = PageRequest.of(from / size, size, sort);
        List<Event> eventList = eventRepository.findAllByUsersAndStatesAndCategories(
                users,
                stateList,
                categories,
                startDate,
                endDate,
                pageable);
        return EventMapper.eventToListOutDto(eventList);
    }

    @Override
    @Transactional
    public EventFullDto publishEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(EVENT_NOT_FOUND.getTitle())
        );
        if (event.getState() != EventState.PENDING) {
            throw new ConflictException(EVENT_IS_NOT_PENDING.getTitle());
        }
        Utils.checkTimeBeforeOrThrow(event.getEventDate(), ADMIN_TIME_HOUR_BEFORE_START);
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventState.PUBLISHED);
        return EventMapper.eventToOutDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto rejectEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(EVENT_NOT_FOUND.getTitle())
        );
        if (event.getState() != EventState.PENDING) {
            throw new ConflictException(EVENT_IS_NOT_PENDING.getTitle());
        }
        event.setState(EventState.CANCELED);
        return EventMapper.eventToOutDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, EventDto eventInDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND.getTitle()));
        Utils.setNotNullParamToEntity(eventInDto, event, categoryRepository);
        event.setEventDate(eventInDto.getEventDate());
        return EventMapper.eventToOutDto(eventRepository.save(event));
    }

    private List<EventState> getCorrectStates(String[] states) {
        try {
            return Arrays.stream(states).map(EventAdminServiceImpl::stringToEventState).collect(Collectors.toList());
        } catch (IllegalArgumentException err) {
            throw new IllegalArgumentException(STATE_NOT_FOUND.getTitle());
        }
    }
}