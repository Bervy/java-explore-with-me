package ru.practicum.explore.mapper;

import ru.practicum.explore.dto.event.EventDto;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.EventPublicFullDto;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.event.Event;

import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {
    public static Event dtoInToEvent(EventDto eventInDto, Category category) {
        return Event.builder()
                .annotation(eventInDto.getAnnotation())
                .category(category)
                .description(eventInDto.getDescription())
                .location(LocationMapper.dtoToLocation(eventInDto.getLocation()))
                .title(eventInDto.getTitle())
                .eventDate(eventInDto.getEventDate())
                .paid(eventInDto.getPaid())
                .participantLimit(eventInDto.getParticipantLimit())
                .requestModeration(eventInDto.getRequestModeration())
                .confirmedRequests(0)
                .views(0L)
                .rate(0)
                .userRate(0D)
                .build();
    }

    public static EventFullDto eventToOutDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToDtoOut(event.getCategory()))
                .initiator(UserMapper.userToDto(event.getInitiator()))
                .location(LocationMapper.locationToDto(event.getLocation()))
                .title(event.getTitle())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .views(event.getViews())
                .userRate(event.getUserRate())
                .build();
    }

    public static EventPublicFullDto eventToPublicOutDto(Event event) {
        return EventPublicFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToDtoOut(event.getCategory()))
                .initiator(UserMapper.userToPublicDto(event.getInitiator()))
                .location(LocationMapper.locationToDto(event.getLocation()))
                .title(event.getTitle())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .views(event.getViews())
                .userRate(event.getUserRate())
                .build();
    }

    public static List<EventFullDto> eventToListOutDto(List<Event> listEvents) {
        return listEvents.stream().map(EventMapper::eventToOutDto).collect(Collectors.toList());
    }

    public static List<EventPublicFullDto> eventToPublicListOutDto(List<Event> listEvents) {
        return listEvents.stream().map(EventMapper::eventToPublicOutDto).collect(Collectors.toList());
    }
}