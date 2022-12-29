package ru.practicum.explore.service.admin_part;

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
import ru.practicum.explore.utils.Constants;
import ru.practicum.explore.utils.EventSearchParameters;
import ru.practicum.explore.utils.Utils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventAdminService {
    private static final int ADMIN_TIME_HOUR_BEFORE_START = 1;
    private static final int ONE_HUNDRED_YEARS_AFTER_NOW = 100;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    private static EventState stringToEventState(String state) {
        return EventState.valueOf(state);
    }

    public static EventFullDto getEventFullDto(Long eventId, EventRepository eventRepository) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event ID not found.")
        );
        if (event.getState() != EventState.PENDING) {
            throw new ConflictException("Event is not pending.");
        }
        event.setState(EventState.CANCELED);
        return EventMapper.eventToOutDto(eventRepository.save(event));
    }

    public List<EventFullDto> findAllEvents(EventSearchParameters eventSearchParameters) {
        List<EventState> stateList = eventSearchParameters.getStates() == null ? List.of() :
                getCorrectStates(eventSearchParameters.getStates());
        LocalDateTime startDate = eventSearchParameters.getRangeStart() == null ? LocalDateTime.now() :
                LocalDateTime.parse(eventSearchParameters.getRangeStart(), Constants.DATE_TIME_SPACE);
        LocalDateTime endDate = eventSearchParameters.getRangeStart() == null ?
                LocalDateTime.now().plusYears(ONE_HUNDRED_YEARS_AFTER_NOW) :
                LocalDateTime.parse(eventSearchParameters.getRangeEnd(), Constants.DATE_TIME_SPACE);
        Sort sort = Sort.sort(Event.class).by(Event::getEventDate).descending();
        Pageable pageable = PageRequest.of(eventSearchParameters.getFrom() / eventSearchParameters.getSize(),
                eventSearchParameters.getSize(),
                sort);
        List<Event> eventList = eventRepository.findAllByUsersAndStatesAndCategories(eventSearchParameters.getUsers(),
                stateList,
                eventSearchParameters.getCategories(),
                startDate,
                endDate,
                pageable);
        return EventMapper.eventToListOutDto(eventList);
    }

    public EventFullDto publishEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event ID not found.")
        );
        if (event.getState() != EventState.PENDING) {
            throw new ConflictException("Event is not pending.");
        }
        Utils.checkTimeBeforeOrThrow(event.getEventDate(), ADMIN_TIME_HOUR_BEFORE_START);
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventState.PUBLISHED);
        return EventMapper.eventToOutDto(eventRepository.save(event));
    }

    public EventFullDto rejectEvent(Long eventId) {
        return getEventFullDto(eventId, eventRepository);
    }

    @Transactional
    public EventFullDto updateEvent(Long eventId, EventDto eventInDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event ID not found."));
        Utils.setNotNullParamToEntity(eventInDto, event, categoryRepository);
        event.setEventDate(eventInDto.getEventDate());
        return EventMapper.eventToOutDto(eventRepository.save(event));
    }

    private List<EventState> getCorrectStates(String[] states) {
        try {
            return Arrays.stream(states).map(EventAdminService::stringToEventState).collect(Collectors.toList());
        } catch (IllegalArgumentException err) {
            throw new IllegalArgumentException("State not found.");
        }
    }
}