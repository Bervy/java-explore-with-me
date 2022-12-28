package ru.practicum.explore.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explore.model.sort.SortType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventSearchParameters {
    private Long[] users;
    private String[] states;
    private Long[] categories;
    private String rangeStart;
    private String rangeEnd;
    private Integer from;
    private Integer size;
    private String text;
    private Boolean paid;
    private Boolean onlyAvailable;
    private SortType sortType;

    public EventSearchParameters(Long[] users,
                                 String[] states,
                                 Long[] categories,
                                 String rangeStart,
                                 String rangeEnd,
                                 Integer from,
                                 Integer size) {
        this.users = users;
        this.states = states;
        this.categories = categories;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.from = from;
        this.size = size;
    }

    public EventSearchParameters(String text,
                                 Long[] categories,
                                 Boolean paid,
                                 String rangeStart,
                                 String rangeEnd,
                                 Boolean onlyAvailable,
                                 SortType sortType,
                                 Integer from,
                                 Integer size) {
        this.categories = categories;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.from = from;
        this.size = size;
        this.text = text;
        this.paid = paid;
        this.onlyAvailable = onlyAvailable;
        this.sortType = sortType;
    }
}