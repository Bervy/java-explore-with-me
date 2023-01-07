package ru.practicum.explore.model.event.grade;

import java.util.Optional;

public enum GradeType {
    LIKE,
    DISLIKE;

    public static Optional<GradeType> from(String stringType) {
        for (GradeType type : values()) {
            if (type.name().equalsIgnoreCase(stringType)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}