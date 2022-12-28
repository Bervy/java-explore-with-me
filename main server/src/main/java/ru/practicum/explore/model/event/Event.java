package ru.practicum.explore.model.event;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.explore.model.location.Location;
import ru.practicum.explore.model.user.User;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.compilation.Compilation;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "EVENTS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 2000)
    private String annotation;
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;
    @CreationTimestamp
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "description", length = 7000)
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    private String title;
    private Long views;
    private Integer rate = 0;
    @ManyToMany
    @JoinTable(
            name = "COMPILATIONS_EVENTS",
            joinColumns = @JoinColumn(name = "EVENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "COMPILATION_ID"))
    private List<Compilation> compilations;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Event event = (Event) o;
        return id != null && Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}