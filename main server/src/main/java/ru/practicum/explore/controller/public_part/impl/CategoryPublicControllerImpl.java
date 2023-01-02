package ru.practicum.explore.controller.public_part.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.controller.public_part.CategoryPublicController;
import ru.practicum.explore.dto.category.CategoryFullDto;
import ru.practicum.explore.service.public_part.impl.CategoryPublicServiceImpl;
import ru.practicum.explore.utils.Constants;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoryPublicControllerImpl implements CategoryPublicController {

    private final CategoryPublicServiceImpl categoryPublicService;

    @GetMapping
    @Override
    public List<CategoryFullDto> findAllCategories(
            @RequestParam(name = "from", defaultValue = Constants.FROM) Integer from,
            @RequestParam(name = "size", defaultValue = Constants.SIZE) Integer size) {
        return categoryPublicService.findAllCategories(from, size);
    }

    @GetMapping("{catId}")
    @Override
    public CategoryFullDto findCategoryById(
            @PathVariable Long catId) {
        return categoryPublicService.findCategoryById(catId);
    }
}