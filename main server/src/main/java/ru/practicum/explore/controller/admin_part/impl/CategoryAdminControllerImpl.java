package ru.practicum.explore.controller.admin_part.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.controller.admin_part.CategoryAdminController;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.category.CategoryFullDto;
import ru.practicum.explore.service.admin_part.impl.CategoryAdminServiceImpl;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Validated
public class CategoryAdminControllerImpl implements CategoryAdminController {

    private final CategoryAdminServiceImpl categoryAdminService;

    @PatchMapping
    @Override
    public CategoryFullDto updateCategory(
            @RequestBody CategoryFullDto categoryFullDto) {
        return categoryAdminService.updateCategory(categoryFullDto);
    }

    @PostMapping
    @Override
    public CategoryFullDto addCategory(
            @RequestBody CategoryDto categoryInDto) {
        return categoryAdminService.addCategory(categoryInDto);
    }

    @DeleteMapping("{catId}")
    @Override
    public void removeCategory(
            @PathVariable Long catId) {
        categoryAdminService.removeCategory(catId);
    }
}