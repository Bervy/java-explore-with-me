package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}