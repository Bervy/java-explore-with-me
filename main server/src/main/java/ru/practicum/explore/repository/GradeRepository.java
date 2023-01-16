package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.event.grade.Grade;
import ru.practicum.explore.model.event.grade.GradeType;
import ru.practicum.explore.model.event.grade.UserEventPrimaryKey;

import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, UserEventPrimaryKey> {

    Optional<Grade> findByUserEventPrimaryKeyAndType(UserEventPrimaryKey userEventPrimaryKey, GradeType gradeType);
}