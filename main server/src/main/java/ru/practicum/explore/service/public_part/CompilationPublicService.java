package ru.practicum.explore.service.public_part;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.compilation.CompilationOutDto;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.CompilationMapper;
import ru.practicum.explore.model.compilation.Compilation;
import ru.practicum.explore.repository.CompilationRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompilationPublicService {
    private final CompilationRepository compilationRepository;

    public List<CompilationOutDto> findAllCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (pinned != null) {
            return CompilationMapper.compilationToListOutDto(compilationRepository.findAlLByPinned(pinned, pageable));
        }
        return CompilationMapper.compilationToListOutDto(compilationRepository.findAll(pageable).toList());
    }

    public CompilationOutDto findCompilationById(Long compId) {
        return CompilationMapper.compilationToOutDto(compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation ID was not found.")
        ));
    }
}