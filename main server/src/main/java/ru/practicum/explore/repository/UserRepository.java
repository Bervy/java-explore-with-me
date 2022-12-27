package ru.practicum.explore.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.model.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(
            "SELECT u FROM User as u WHERE u.id IN :ids"
    )
    List<User> findAllByIds(Long[] ids, Pageable pageable);
}