package ru.practicum.explore.service.admin_part;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.compilation.CompilationInDto;
import ru.practicum.explore.dto.compilation.CompilationOutDto;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.CompilationMapper;
import ru.practicum.explore.model.compilation.Compilation;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.repository.CompilationRepository;
import ru.practicum.explore.repository.EventRepository;

@Service
@RequiredArgsConstructor
public class CompilationAdminService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Transactional
    public CompilationOutDto addCompilation(CompilationInDto compilationInDto) {
        Compilation compilation = CompilationMapper.dtoToCompilation(
                compilationInDto, eventRepository.findAllById(compilationInDto.getEvents())
        );

        return CompilationMapper.compilationToOutDto(compilationRepository.saveAndFlush(compilation));
    }

    public void removeCompilation(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Compilation ID not found.");
        }
        compilationRepository.deleteById(compId);
        compilationRepository.flush();
    }

    @Transactional
    public void removeEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation ID not found.")
        );
        compilation.getEvents().removeIf((e) -> e.getId().equals(eventId));
        compilationRepository.flush();
    }

    public CompilationOutDto addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation ID not found.")
        );
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event ID: " + eventId + " not found.")
        );
        compilation.getEvents().add(event);
        compilationRepository.flush();

        return CompilationMapper.compilationToOutDto(compilation);
    }

    public void pinCompilation(Long compId) {
        setPin(compId, true);
    }


    public void unPinCompilation(Long compId) {
        setPin(compId, false);
    }

    private void setPin(Long compId, boolean pinned) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation ID not found.")
        );
        compilation.setPinned(pinned);
        compilationRepository.flush();
    }
}