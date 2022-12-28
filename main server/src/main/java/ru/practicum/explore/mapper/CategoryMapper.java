package ru.practicum.explore.mapper;

import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.category.CategoryFullDto;
import ru.practicum.explore.model.category.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {
    public static Category dtoOutToCategory(CategoryFullDto categoryFullDto) {
        return Category.builder()
                .id(categoryFullDto.getId())
                .name(categoryFullDto.getName())
                .build();
    }

    public static CategoryFullDto categoryToDtoOut(Category category) {
        return CategoryFullDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category dtoInToCategory(CategoryDto categoryInDto) {
        return Category.builder()
                .name(categoryInDto.getName())
                .build();
    }

    public static List<CategoryFullDto> categoryToListDtoOut(List<Category> listCategories) {
        return listCategories.stream().map(CategoryMapper::categoryToDtoOut).collect(Collectors.toList());
    }
}