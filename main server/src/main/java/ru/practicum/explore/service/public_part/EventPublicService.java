package ru.practicum.explore.service.public_part;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.event.EventPublicFullDto;
import ru.practicum.explore.dto.stat.StatFullDto;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.utils.Constants;
import ru.practicum.explore.utils.EventSearchParameters;
import ru.practicum.explore.utils.stats.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublicService {
    private final EventRepository eventRepository;
    private final StatClient adminStatClient;
    private static final String APP_NAME = "main service";
    private static final int ONE_HUNDRED_YEARS_AFTER_NOW = 100;

    public EventPublicFullDto findEventById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED).orElseThrow(
                () -> new NotFoundException("Event not found.")
        );
        eventRepository.incrementViews(eventId);
        sendHitStat(request);
        return EventMapper.eventToPublicOutDto(event);
    }

    public List<EventPublicFullDto> findAllEvents(EventSearchParameters eventSearchParameters, HttpServletRequest request) {
        Sort sort;
        switch (eventSearchParameters.getSortType()) {
            case EVENT_DATE:
                sort = Sort.sort(Event.class).by(Event::getEventDate).ascending();
                break;
            case VIEWS:
                sort = Sort.sort(Event.class).by(Event::getViews).descending();
                break;
            case RATE:
                sort = Sort.sort(Event.class).by(Event::getRate).descending();
                break;
            default:
                throw new IllegalArgumentException("Указан не существующий тип сортировки.");
        }

        Pageable pageable = PageRequest.of(eventSearchParameters.getFrom() / eventSearchParameters.getSize(),
                eventSearchParameters.getSize(), sort);

        LocalDateTime startDate = eventSearchParameters.getRangeStart() == null ? LocalDateTime.now() :
                LocalDateTime.parse(eventSearchParameters.getRangeStart(), Constants.DATE_TIME_SPACE);

        LocalDateTime endDate = eventSearchParameters.getRangeEnd() == null ?
                LocalDateTime.now().plusYears(ONE_HUNDRED_YEARS_AFTER_NOW) :
                LocalDateTime.parse(eventSearchParameters.getRangeEnd(), Constants.DATE_TIME_SPACE);

        List<Event> events = eventRepository.findAllByParam(
                eventSearchParameters.getText(),
                eventSearchParameters.getCategories(),
                eventSearchParameters.getPaid(),
                startDate,
                endDate,
                eventSearchParameters.getOnlyAvailable(),
                pageable);
        sendHitStat(request);
        return EventMapper.eventToPublicListOutDto(events);
    }

    private void sendHitStat(HttpServletRequest request) {
        Thread sendHit = new Thread(
                () -> adminStatClient.saveHit(StatFullDto.builder()
                        .app(APP_NAME)
                        .uri(request.getRequestURI())
                        .ip(request.getRemoteAddr())
                        .timestamp(LocalDateTime.now())
                        .build()));
        sendHit.start();
    }
}