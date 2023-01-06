package ru.practicum.explore.mapper;

import ru.practicum.explore.dto.request.RequestFullDto;
import ru.practicum.explore.model.request.Request;

import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {

    public static RequestFullDto requestToOutDto(Request request) {
        return RequestFullDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public static List<RequestFullDto> requestsToListOutDto(List<Request> requests) {
        return requests.stream().map(RequestMapper::requestToOutDto).collect(Collectors.toList());
    }
}