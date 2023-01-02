package ru.practicum.explore.model.compilation;

import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.explore.model.event.Event;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "COMPILATIONS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Compilation {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Boolean pinned;

    @ManyToMany
    @JoinTable(name = "COMPILATIONS_EVENTS",
            joinColumns = @JoinColumn(name = "COMPILATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "EVENT_ID"))
    private List<Event> events;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Compilation that = (Compilation) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}