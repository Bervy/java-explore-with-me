package ru.practicum.explore.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explore.utils.Constants;
import ru.practicum.explore.dto.location.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long eventId;
    @NotBlank
    @Size(min = 10, max = 1000)
    private String annotation;
    @Positive
    private Long category;
    @NotBlank
    @Size(min = 10, max = 3000)
    private String description;
    private LocationDto location;
    @NotBlank
    @Size(min = 3, max = 100)
    private String title;
    @NotNull
    @JsonFormat(pattern = Constants.DATE_TIME_STRING)
    private LocalDateTime eventDate;
    private Boolean paid;
    @Positive
    private Integer participantLimit;
    private Boolean requestModeration;
}