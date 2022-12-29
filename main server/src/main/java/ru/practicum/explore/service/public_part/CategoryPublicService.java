package ru.practicum.explore.service.public_part;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.controller.public_part.CategoryPublicController;
import ru.practicum.explore.dto.category.CategoryFullDto;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.CategoryMapper;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.repository.CategoryRepository;

import java.util.List;

import static ru.practicum.explore.error.ExceptionDescriptions.CATEGORY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CategoryPublicService implements CategoryPublicController {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryFullDto> findAllCategories(Integer from, Integer size) {
        Sort sort = Sort.sort(Category.class).by(Category::getName).ascending();
        Pageable pageable = PageRequest.of(from / size, size, sort);
        return CategoryMapper.categoryToListDtoOut(categoryRepository.findAll(pageable).toList());
    }

    @Override
    public CategoryFullDto findCategoryById(Long catId) {
        return CategoryMapper.categoryToDtoOut(categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException(CATEGORY_NOT_FOUND.getTitle())
        ));
    }
}