package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.model.request.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(Long userId);

    Optional<Request> findByRequesterIdAndEventId(Long userId, Long eventId);

    Optional<Request> findByIdAndRequesterId(Long userId, Long eventId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Request r " +
            " SET r.status = ru.practicum.explore.model.request.RequestState.REJECTED " +
            " WHERE r.event.id = :eventId AND r.status = ru.practicum.explore.model.request.RequestState.PENDING " +
            " ")
    void rejectAllPendingRequest(Long eventId);

    @Query("SELECT r FROM Request r " +
            " JOIN Event e ON r.event.id = e.id " +
            " WHERE e.id = :eventId AND e.initiator.id = :userId "
    )
    List<Request> findAllByInitiatorIdAndEventId(Long userId, Long eventId);
}