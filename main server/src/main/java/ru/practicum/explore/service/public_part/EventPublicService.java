package ru.practicum.explore.service.public_part;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.Constants;
import ru.practicum.explore.MainApp;
import ru.practicum.explore.dto.StatInDto;
import ru.practicum.explore.dto.event.EventPublicOutDto;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.sort.SortType;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.utils.AdminStatClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublicService {
    private final EventRepository eventRepository;
    private final AdminStatClient adminStatClient;
//
    public EventPublicOutDto findEventById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED).orElseThrow(
                () -> new NotFoundException("Event not found.")
        );
        eventRepository.incrementViews(eventId);

        Thread sendHit = new Thread(
                () -> {
                    try {
                        adminStatClient.saveHit(new StatInDto(
                                "main service",
                                request.getRequestURI(),
                                request.getRemoteAddr(),
                                LocalDateTime.now()
                        ));
                        log.info(">>Hit send - OK.");
                    } catch (Exception err) {
                        log.info(">>Hit send. Error: " + err.getMessage());
                    }
                });
        sendHit.start();
        return EventMapper.eventToPublicOutDto(event);
    }


// создать класс для параметров и подумать над кодом
    public List<EventPublicOutDto> findAllEvents(String text,
                                                 Long[] categories,
                                                 Boolean paid,
                                                 String rangeStart,
                                                 String rangeEnd,
                                                 Boolean onlyAvailable,
                                                 SortType sortType,
                                                 Integer from,
                                                 Integer size, HttpServletRequest request) {
        Sort sort;
        switch (sortType) {
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

        Pageable pageable = PageRequest.of(from / size, size, sort);

        LocalDateTime startDate = rangeStart == null ? LocalDateTime.now() :
                LocalDateTime.parse(rangeStart, Constants.DATE_TIME_SPACE);

        LocalDateTime endDate = rangeEnd == null ? LocalDateTime.now().plusYears(100) :
                LocalDateTime.parse(rangeEnd, Constants.DATE_TIME_SPACE);

        List<Event> events = eventRepository.findAllByParam(
                text,
                categories,
                paid,
                startDate,
                endDate,
                onlyAvailable,
                pageable);

        Thread sendHit = new Thread(
                () -> {
                    try {
                        adminStatClient.saveHit(new StatInDto(
                                "main service",
                                request.getRequestURI(),
                                request.getRemoteAddr(),
                                LocalDateTime.now()
                        ));
                        log.info(">>Hit search send - OK.");
                    } catch (Exception err) {
                        log.info(">>Hit search send. Error: " + err.getMessage());
                    }
                });
        sendHit.start();

        return EventMapper.eventToPublicListOutDto(events);
    }
}