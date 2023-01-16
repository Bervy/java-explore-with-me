package ru.practicum.explore.service.private_part;

import ru.practicum.explore.dto.event.EventDto;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.model.event.grade.GradeType;

import java.util.List;

public interface UserEventPrivateService {

    EventFullDto addEvent(Long userId, EventDto eventInDto);

    EventFullDto updateEvent(Long userId, EventDto eventInDto);

    List<EventFullDto> findAllEvents(Long userId, Integer from, Integer size);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto cancelEvent(Long userId, Long eventId);

    void addGrade(Long userId, Long eventId, GradeType likeType);

    void removeGrade(Long userId, Long eventId, GradeType likeType);
}