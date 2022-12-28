package ru.practicum.explore.controller.admin_part;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.category.CategoryFullDto;
import ru.practicum.explore.service.admin_part.CategoryAdminService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Validated
public class CategoryAdminController {
    private final CategoryAdminService categoryAdminService;

    @PatchMapping
    public CategoryFullDto updateCategory(
            @Valid @RequestBody CategoryFullDto categoryFullDto) {
        return categoryAdminService.updateCategory(categoryFullDto);
    }

    @PostMapping
    public CategoryFullDto addCategory(
            @Valid @RequestBody CategoryDto categoryInDto) {
        return categoryAdminService.addCategory(categoryInDto);
    }

    @DeleteMapping("{catId}")
    void removeCategory(
            @Positive @PathVariable Long catId) throws AccessDeniedException {
        categoryAdminService.removeCategory(catId);
    }
}