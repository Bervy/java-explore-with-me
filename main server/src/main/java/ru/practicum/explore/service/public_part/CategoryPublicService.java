package ru.practicum.explore.service.public_part;

import ru.practicum.explore.dto.category.CategoryFullDto;

import java.util.List;

public interface CategoryPublicService {

    List<CategoryFullDto> findAllCategories(Integer from, Integer size);

    CategoryFullDto findCategoryById(Long catId);
}