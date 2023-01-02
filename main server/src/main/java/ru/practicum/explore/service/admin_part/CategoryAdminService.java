package ru.practicum.explore.service.admin_part;

import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.category.CategoryFullDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

public interface CategoryAdminService {

    CategoryFullDto addCategory(@Valid CategoryDto categoryInDto);

    CategoryFullDto updateCategory(@Valid CategoryFullDto categoryFullDto);

    void removeCategory(@Positive Long catId);
}