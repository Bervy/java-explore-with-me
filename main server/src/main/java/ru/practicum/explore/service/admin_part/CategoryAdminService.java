package ru.practicum.explore.service.admin_part;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.category.CategoryFullDto;
import ru.practicum.explore.error.ConflictException;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.CategoryMapper;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.EventRepository;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class CategoryAdminService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public CategoryFullDto updateCategory(CategoryFullDto categoryFullDto) {
        checkCategoryExists(categoryFullDto.getId());
        Category category = CategoryMapper.dtoOutToCategory(categoryFullDto);
        Category categoryFromDb = saveCategory(category);
        return CategoryMapper.categoryToDtoOut(categoryFromDb);
    }

    public CategoryFullDto addCategory(CategoryDto categoryInDto) {
        Category category = CategoryMapper.dtoInToCategory(categoryInDto);
        Category categoryFromDb = saveCategory(category);
        return CategoryMapper.categoryToDtoOut(categoryFromDb);
    }

    @Transactional
    public void removeCategory(Long catId) throws AccessDeniedException {
        checkCategoryExists(catId);
        if (Boolean.TRUE.equals(eventRepository.existsByCategoryId(catId))) {
            throw new AccessDeniedException("Category in use.");
        }
        categoryRepository.deleteById(catId);
    }

    private void checkCategoryExists(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Category ID was not found.");
        }
    }

    private Category saveCategory(Category category) {
        Category categoryFromDb;
        try {
            categoryFromDb = categoryRepository.save(category);
        } catch (DataAccessException dataAccessException) {
            throw new ConflictException("123");
        }
        return categoryFromDb;
    }
}