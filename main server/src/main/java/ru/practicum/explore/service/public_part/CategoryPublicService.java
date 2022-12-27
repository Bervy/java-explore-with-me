package ru.practicum.explore.service.public_part;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.category.CategoryFullDto;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.CategoryMapper;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryPublicService {
    private final CategoryRepository categoryRepository;

    public List<CategoryFullDto> findAllCategories(Integer from, Integer size) {
        Sort sort = Sort.sort(Category.class).by(Category::getName).ascending();
        Pageable pageable = PageRequest.of(from / size, size, sort);

        return CategoryMapper.categoryToListDtoOut(categoryRepository.findAll(pageable).toList());
    }

    public CategoryFullDto findCategoryById(Long catId){
        return CategoryMapper.categoryToDtoOut(categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category ID was not found.")
        ));
    }
}