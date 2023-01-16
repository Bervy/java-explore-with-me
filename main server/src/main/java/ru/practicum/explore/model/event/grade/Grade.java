package ru.practicum.explore.model.event.grade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "GRADES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Grade {
    @EmbeddedId
    private UserEventPrimaryKey userEventPrimaryKey;
    @Enumerated(EnumType.STRING)
    private GradeType type;
}
