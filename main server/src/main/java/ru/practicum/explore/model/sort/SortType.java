package ru.practicum.explore.model.sort;

import java.util.Arrays;
import java.util.Optional;

public enum SortType {
    EVENT_DATE,
    VIEWS,
    RATE;

    public static Optional<SortType> from(String stringType) {
        return Arrays.stream(values()).filter(s1 -> s1.name().equalsIgnoreCase(stringType)).findFirst();
    }
}