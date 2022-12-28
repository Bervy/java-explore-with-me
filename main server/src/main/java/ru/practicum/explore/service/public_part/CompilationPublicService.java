package ru.practicum.explore.service.public_part;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.compilation.CompilationFullDto;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.CompilationMapper;
import ru.practicum.explore.repository.CompilationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationPublicService {
    private final CompilationRepository compilationRepository;

    public List<CompilationFullDto> findAllCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return pinned == null ? CompilationMapper
                .compilationToListOutDto(compilationRepository.findAlLByPinned(pinned, pageable)) :
                CompilationMapper.compilationToListOutDto(compilationRepository.findAll(pageable).toList());
    }

    public CompilationFullDto findCompilationById(Long compId) {
        return CompilationMapper.compilationToOutDto(compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation ID was not found.")
        ));
    }
}