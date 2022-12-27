package ru.practicum.explore.controller.public_part;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.category.CategoryFullDto;
import ru.practicum.explore.service.public_part.CategoryPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CategoryPublicController {

    private final CategoryPublicService categoryPublicService;

    @GetMapping
    public List<CategoryFullDto> findAllCategories(@PositiveOrZero
                                                   @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive
                                                   @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return categoryPublicService.findAllCategories(from, size);
    }

    @GetMapping("{catId}")
    public CategoryFullDto findCategoryById(@Positive @PathVariable Long catId) {
        return categoryPublicService.findCategoryById(catId);
    }
}