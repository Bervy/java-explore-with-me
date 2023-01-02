package ru.practicum.explore.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.explore.utils.Constants;
import ru.practicum.explore.model.user.User;
import ru.practicum.explore.model.event.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "REQUESTS", uniqueConstraints = {@UniqueConstraint(columnNames = {"EVENT_ID", "REQUESTER_ID"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Request {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "REQUESTER_ID")
    private User requester;
    @JsonFormat(pattern = Constants.DATE_TIME_STRING)
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    private RequestState status;
    @ManyToOne()
    @JoinColumn(name = "EVENT_ID")
    private Event event;
}