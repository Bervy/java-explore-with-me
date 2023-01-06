package ru.practicum.explore.controller.admin_part;

import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.category.CategoryFullDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

public interface CategoryAdminController {

    CategoryFullDto addCategory(@Valid CategoryDto categoryInDto);

    CategoryFullDto updateCategory(@Valid CategoryFullDto categoryFullDto);

    void removeCategory(@Positive Long catId);
}