package ru.practicum.explore.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.explore.utils.Constants;
import ru.practicum.explore.dto.category.CategoryFullDto;
import ru.practicum.explore.dto.location.LocationDto;
import ru.practicum.explore.dto.user.UserPublicDto;
import ru.practicum.explore.model.event.EventState;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventPublicFullDto {
    private String annotation;
    private CategoryFullDto category;
    private UserPublicDto initiator;
    private LocationDto location;
    private String title;
    private Integer confirmedRequests;
    @JsonFormat(pattern = Constants.DATE_TIME_STRING)
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(pattern = Constants.DATE_TIME_STRING)
    private LocalDateTime eventDate;
    private Long id;
    private Boolean paid;
    private Integer participantLimit;
    @JsonFormat(pattern = Constants.DATE_TIME_STRING)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private Long views;
    private Double userRate;
}