package ru.practicum.explore.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.explore.utils.Constants;
import ru.practicum.explore.model.request.RequestState;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestFullDto {
    private Long id;
    @JsonFormat(pattern = Constants.DATE_TIME_STRING)
    private LocalDateTime created;
    @Positive
    private Long event;
    @Positive
    private Long requester;
    private RequestState status;
}