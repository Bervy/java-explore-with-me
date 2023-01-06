package ru.practicum.explore.service.admin_part.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.category.CategoryFullDto;
import ru.practicum.explore.error.AccessDeniedException;
import ru.practicum.explore.error.ConflictException;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.CategoryMapper;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.service.admin_part.CategoryAdminService;

import java.util.Optional;

import static ru.practicum.explore.error.ExceptionDescriptions.*;

@Service
@RequiredArgsConstructor
public class CategoryAdminServiceImpl implements CategoryAdminService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryFullDto addCategory(CategoryDto categoryInDto) {
        try {
            return CategoryMapper.categoryToDtoOut(categoryRepository
                    .save(CategoryMapper.dtoInToCategory(categoryInDto)));
        } catch (DataAccessException dataAccessException) {
            throw new ConflictException(CATEGORY_ALREADY_EXISTS.getTitle());
        }
    }

    @Override
    @Transactional
    public CategoryFullDto updateCategory(CategoryFullDto categoryFullDto) {
        checkCategoryExists(categoryFullDto.getId());
        Optional<Category> categoryByName = categoryRepository.findByName(categoryFullDto.getName());
        if (categoryByName.isPresent()) {
            throw new ConflictException(CATEGORY_ALREADY_EXISTS.getTitle());
        }
        return CategoryMapper.categoryToDtoOut(categoryRepository
                .save(CategoryMapper.dtoOutToCategory(categoryFullDto)));
    }

    @Override
    @Transactional
    public void removeCategory(Long catId) {
        checkCategoryExists(catId);
        if (Boolean.TRUE.equals(eventRepository.existsByCategoryId(catId))) {
            throw new AccessDeniedException(CATEGORY_ALREADY_IN_USE.getTitle());
        }
        categoryRepository.deleteById(catId);
    }

    private void checkCategoryExists(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException(CATEGORY_NOT_FOUND.getTitle());
        }
    }
}