package ru.practicum.explore.service.admin_part;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.controller.admin_part.CompilationAdminController;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.compilation.CompilationFullDto;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.CompilationMapper;
import ru.practicum.explore.model.compilation.Compilation;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.repository.CompilationRepository;
import ru.practicum.explore.repository.EventRepository;

import static ru.practicum.explore.error.ExceptionDescriptions.*;

@Service
@RequiredArgsConstructor
public class CompilationAdminService implements CompilationAdminController {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Transactional
    @Override
    public CompilationFullDto addCompilation(CompilationDto compilationInDto) {
        Compilation compilation = CompilationMapper.dtoToCompilation(
                compilationInDto, eventRepository.findAllById(compilationInDto.getEvents())
        );
        return CompilationMapper.compilationToOutDto(compilationRepository.save(compilation));
    }

    @Override
    public void removeCompilation(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException(COMPILATION_NOT_FOUND.getTitle());
        }
        compilationRepository.deleteById(compId);
    }

    @Transactional
    @Override
    public void removeEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = getCompilationFromRepository(compId);
        Event eventToRemove = compilation.getEvents().stream().filter(e -> e.getId().equals(eventId)).findFirst()
                        .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND.getTitle()));
        compilation.getEvents().remove(eventToRemove);
        compilationRepository.save(compilation);
    }

    @Override
    public CompilationFullDto addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = getCompilationFromRepository(compId);
        Event event = getEventFromRepository(eventId);
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
        return CompilationMapper.compilationToOutDto(compilation);
    }

    @Override
    public void pinCompilation(Long compId) {
        setPin(compId, true);
    }

    @Override
    public void unPinCompilation(Long compId) {
        setPin(compId, false);
    }

    private void setPin(Long compId, boolean pinned) {
        Compilation compilation = compilationRepository.findById(compId)
                        .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND.getTitle()));
        compilation.setPinned(pinned);
        compilationRepository.save(compilation);
    }

    private Compilation getCompilationFromRepository(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(COMPILATION_NOT_FOUND.getTitle()));
    }

    private Event getEventFromRepository(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(EVENT_NOT_FOUND.getTitle())
        );
    }
}