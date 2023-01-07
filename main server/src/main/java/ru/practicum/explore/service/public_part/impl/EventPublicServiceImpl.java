package ru.practicum.explore.service.public_part.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.event.EventPublicFullDto;
import ru.practicum.explore.dto.stat.StatFullDto;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.sort.SortType;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.service.public_part.EventPublicService;
import ru.practicum.explore.utils.Constants;
import ru.practicum.explore.utils.stats.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explore.error.ExceptionDescriptions.EVENT_NOT_FOUND;
import static ru.practicum.explore.error.ExceptionDescriptions.UNKNOWN_TYPE_OF_SORT;


@Service
@RequiredArgsConstructor
public class EventPublicServiceImpl implements EventPublicService {

    private static final String APP_NAME = "main service";
    private static final int ONE_HUNDRED_YEARS_AFTER_NOW = 100;
    private final EventRepository eventRepository;
    private final StatClient adminStatClient;

    @Override
    @Transactional
    public EventPublicFullDto findEventById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED).orElseThrow(
                () -> new NotFoundException(EVENT_NOT_FOUND.getTitle())
        );
        eventRepository.incrementViews(eventId);
        sendHitStat(request);
        return EventMapper.eventToPublicOutDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventPublicFullDto> findAllEvents(
            String text,
            Long[] categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Integer from,
            Integer size,
            HttpServletRequest request) {
        SortType sortType = SortType.from(sort)
                .orElseThrow(() -> new IllegalArgumentException(UNKNOWN_TYPE_OF_SORT.getTitle()));
        Sort sortBy;
        switch (sortType) {
            case EVENT_DATE:
                sortBy = Sort.sort(Event.class).by(Event::getEventDate).ascending();
                break;
            case VIEWS:
                sortBy = Sort.sort(Event.class).by(Event::getViews).descending();
                break;
            case RATE:
                sortBy = Sort.sort(Event.class).by(Event::getUserRate).descending();
                break;
            default:
                throw new IllegalArgumentException(UNKNOWN_TYPE_OF_SORT.getTitle());
        }

        Pageable pageable = PageRequest.of(from / size, size, sortBy);

        LocalDateTime startDate = rangeStart == null ? LocalDateTime.now() :
                LocalDateTime.parse(rangeStart, Constants.DATE_TIME_SPACE);

        LocalDateTime endDate = rangeEnd == null ?
                LocalDateTime.now().plusYears(ONE_HUNDRED_YEARS_AFTER_NOW) :
                LocalDateTime.parse(rangeEnd, Constants.DATE_TIME_SPACE);

        List<Event> events = eventRepository.findAllByParam(
                text,
                categories,
                paid,
                startDate,
                endDate,
                onlyAvailable,
                pageable);
        sendHitStat(request);
        return EventMapper.eventToPublicListOutDto(events);
    }

    private void sendHitStat(HttpServletRequest request) {
        adminStatClient.saveHit(StatFullDto.builder()
                .app(APP_NAME)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }
}