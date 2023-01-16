package ru.practicum.explore.error;

public enum ExceptionDescriptions {
    CATEGORY_ALREADY_IN_USE("Category already in use"),
    CATEGORY_NOT_FOUND("Category not found"),
    CATEGORY_ALREADY_EXISTS("Category already exists"),
    COMPILATION_NOT_FOUND("Compilation not found"),
    EVENT_NOT_FOUND("Event not found"),
    EVENT_IS_NOT_PENDING("Event is not pending"),
    STATE_NOT_FOUND("State not found"),
    USER_ALREADY_EXISTS("User already exists"),
    LIKE_ALREADY_EXISTS("Like already exists"),
    DISLIKE_ALREADY_EXISTS("Dislike already exists"),
    USER_NOT_FOUND("User not found"),
    LOCATION_IS_NULL("Location is null"),
    PAID_IS_NULL("Paid is null"),
    EVENT_IS_PUBLISHED("You can't update event, it was already published"),
    REQUEST_NOT_FOUND("Request not found"),
    REQUEST_IS_NOT_PENDING("Request is not pending"),
    WRONG_EVENT_ID_FOR_REQUEST("Wrong event ID for this request"),
    WRONG_USER_ID_FOR_REQUEST("Wrong user ID for this request, only owner can confirm(reject) request"),
    NO_FREE_SLOT("Event don't have any free slot"),
    USER_CANT_REQUEST_HIMSELF("User can't request himself"),
    EVENT_IS_NOT_PUBLISHED("Event is not published"),
    UNKNOWN_TYPE_OF_SORT("Unknown type of sort"),
    GRADE_NOT_FOUND("Grade not found"),
    FORBIDDEN_TO_RATE_OWN_EVENT("It is forbidden to rate own event"),
    FORBIDDEN_TO_RATE_EVENT_NOT_BEGUN("It is forbidden to evaluate an event that has not yet begun"),
    FORBIDDEN_TO_RATE_DID_NOT_PARTICIPATE("It is forbidden to rate an event in which you did not participate");


    private final String title;

    ExceptionDescriptions(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
