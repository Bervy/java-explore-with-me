package ru.practicum.explore.service.admin_part;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.Constants;
import ru.practicum.explore.dto.event.EventInDto;
import ru.practicum.explore.dto.event.EventOutDto;
import ru.practicum.explore.error.ConflictException;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.UserRepository;
import ru.practicum.explore.utils.Utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventAdminService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public List<EventOutDto> findAllEvents(Long[] users,
                                           String[] states, Long[] categories, String rangeStart,
                                           String rangeEnd, Integer from,
                                           Integer size){
        checkUsersExitOrThrow(users);
        checkCategoriesExitOrThrow(categories);
        List<EventState> stateList;
        if (states != null) {
            stateList = checkStatesCorrectOrThrow(states);
        } else {
            stateList = List.of();
        }
        LocalDateTime startDate;
        if (rangeStart != null) {
            startDate = LocalDateTime.parse(rangeStart, Constants.DATE_TIME_SPACE);
        } else {
            startDate = LocalDateTime.now();
        }
        LocalDateTime endDate;
        if (rangeStart != null) {
            endDate = LocalDateTime.parse(rangeEnd, Constants.DATE_TIME_SPACE);
        } else {
            endDate = LocalDateTime.now();
        }

        Sort sort = Sort.sort(Event.class).by(Event::getEventDate).descending();
        Pageable pageable = PageRequest.of(from / size, size, sort);
        List<Event> eventList = eventRepository.findAllByUsersAndStatesAndCategories(users, stateList, categories, startDate, endDate, pageable);
        return EventMapper.eventToListOutDto(eventList);
    }

    private List<EventState> checkStatesCorrectOrThrow(String[] states) {
        List<EventState> stateList = new ArrayList<>();
        for (String state : states) {
            try {
                stateList.add(EventState.valueOf(state));
            } catch (IllegalArgumentException err) {
                throw new IllegalArgumentException("Stats: " + state + " not found.");
            }
        }
        return stateList;
    }

    private void checkCategoriesExitOrThrow(Long[] categories) {
        for (Long catId : categories) {
            if (!categoryRepository.existsById(catId)) {
                throw new NotFoundException("Category ID: " + catId + " not found.");
            }
        }
    }

    private void checkUsersExitOrThrow(Long[] users){
        for (Long userId : users) {
            if (!userRepository.existsById(userId)) {
                throw new NotFoundException("User ID: " + userId + " not found.");
            }
        }
    }

    public EventOutDto publishEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event ID not found.")
        );
        if (event.getState() != EventState.PENDING) {
            throw new ConflictException("Event is not pending.");
        }
        Utils.checkTimeBeforeOrThrow(event.getEventDate(), Constants.ADMIN_TIME_HOUR_BEFORE_START);
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventState.PUBLISHED);
        return EventMapper.eventToOutDto(eventRepository.saveAndFlush(event));
    }

    public EventOutDto rejectEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event ID not found.")
        );
        if (event.getState() != EventState.PENDING) {
            throw new ConflictException("Event is not pending.");
        }
        event.setState(EventState.CANCELED);
        return EventMapper.eventToOutDto(eventRepository.saveAndFlush(event));
    }

    @Transactional
    public EventOutDto updateEvent(Long eventId, EventInDto eventInDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event ID not found.")
        );
        if (eventInDto.getEventDate() != null) {
            event.setEventDate(eventInDto.getEventDate());
        }
        Utils.setNotNullParamToEntity(eventInDto, event, categoryRepository);

        return EventMapper.eventToOutDto(eventRepository.saveAndFlush(event));
    }
}