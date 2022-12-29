package ru.practicum.explore.controller.public_part;

import ru.practicum.explore.dto.category.CategoryFullDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface CategoryPublicController {

    List<CategoryFullDto> findAllCategories(@PositiveOrZero Integer from, @Positive Integer size);

    CategoryFullDto findCategoryById(@Positive Long catId);
}