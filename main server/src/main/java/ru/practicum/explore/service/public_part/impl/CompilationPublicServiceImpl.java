package ru.practicum.explore.service.public_part.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.compilation.CompilationFullDto;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.CompilationMapper;
import ru.practicum.explore.repository.CompilationRepository;
import ru.practicum.explore.service.public_part.CompilationPublicService;

import java.util.List;

import static ru.practicum.explore.error.ExceptionDescriptions.COMPILATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CompilationPublicServiceImpl implements CompilationPublicService {

    private final CompilationRepository compilationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CompilationFullDto> findAllCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return pinned == null ? CompilationMapper
                .compilationToListOutDto(compilationRepository.findAllByPinned(pinned, pageable)) :
                CompilationMapper.compilationToListOutDto(compilationRepository.findAll(pageable).toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationFullDto findCompilationById(Long compId) {
        return CompilationMapper.compilationToOutDto(compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException(COMPILATION_NOT_FOUND.getTitle())
        ));
    }
}