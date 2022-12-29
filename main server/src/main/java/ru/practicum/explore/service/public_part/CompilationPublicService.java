package ru.practicum.explore.service.public_part;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.controller.public_part.CompilationPublicController;
import ru.practicum.explore.dto.compilation.CompilationFullDto;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.CompilationMapper;
import ru.practicum.explore.repository.CompilationRepository;

import java.util.List;

import static ru.practicum.explore.error.ExceptionDescriptions.COMPILATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CompilationPublicService implements CompilationPublicController {
    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationFullDto> findAllCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return pinned == null ? CompilationMapper
                .compilationToListOutDto(compilationRepository.findAlLByPinned(pinned, pageable)) :
                CompilationMapper.compilationToListOutDto(compilationRepository.findAll(pageable).toList());
    }

    @Override
    public CompilationFullDto findCompilationById(Long compId) {
        return CompilationMapper.compilationToOutDto(compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException(COMPILATION_NOT_FOUND.getTitle())
        ));
    }
}