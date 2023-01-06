package ru.practicum.explore.service.admin_part;

import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.category.CategoryFullDto;

public interface CategoryAdminService {

    CategoryFullDto addCategory(CategoryDto categoryInDto);

    CategoryFullDto updateCategory(CategoryFullDto categoryFullDto);

    void removeCategory(Long catId);
}