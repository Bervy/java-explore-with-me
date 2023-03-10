package ru.practicum.explore.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    @Query("SELECT e FROM Event e " +
            " WHERE e.initiator.id IN :users " +
            " AND e.state IN :states " +
            " AND e.category.id IN :categories " +
            " AND e.eventDate BETWEEN :startDate AND :endDate "
    )
    List<Event> findAllByUsersAndStatesAndCategories(Long[] users, List<EventState> states, Long[] categories, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    boolean existsByCategoryId(Long catId);

    Optional<Event> findByIdAndState(Long eventId, EventState published);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Event e " +
            " SET e.views = e.views + 1 " +
            " WHERE e.id = :eventId")
    void incrementViews(Long eventId);

    @Query("SELECT e FROM Event e " +
            " WHERE e.state = ru.practicum.explore.model.event.EventState.PUBLISHED " +
            " AND (e.annotation LIKE CONCAT('%',:text,'%') OR e.description LIKE CONCAT('%',:text,'%')) " +
            " AND e.category.id IN :categories " +
            " AND e.paid = :paid " +
            " AND (e.eventDate BETWEEN :rangeStart AND :rangeEnd) " +
            " AND (" +
            " (:onlyAvailable = true AND e.participantLimit = 0) OR " +
            " (:onlyAvailable = true AND e.participantLimit > e.confirmedRequests) OR " +
            " (:onlyAvailable = false)" +
            ") "
    )
    List<Event> findAllByParam(String text,
                               Long[] categories,
                               Boolean paid,
                               LocalDateTime rangeStart,
                               LocalDateTime rangeEnd,
                               Boolean onlyAvailable,
                               Pageable pageable);

    int countByInitiatorId(Long userId);

    @Query("SELECT SUM(e.rate) FROM Event e " +
            " WHERE e.initiator.id = :userId"
    )
    Double sumRateByInitiatorId(Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Event e " +
            " SET e.rate = e.rate + 1 " +
            " WHERE e.id = :eventId")
    void incrementRate(Long eventId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Event e " +
            " SET e.rate = e.rate - 1 " +
            " WHERE e.id = :eventId")
    void decrementRate(Long eventId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Event e " +
            " SET e.userRate = :userRate" +
            " WHERE e.id = :eventId")
    void setUserRate(Long eventId, Double userRate);
}