package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.like.Like;
import ru.practicum.explore.model.like.LikeType;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
//
//    Optional<Like> findByEventIdAndUserId(Long userId, Long eventId);
//
//    Optional<Like> findByUserIdAndEventIdAndType(Long userId, Long eventId, LikeType likeType);
}
