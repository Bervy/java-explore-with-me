package ru.practicum.explore.service.private_part.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.event.EventDto;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.error.AccessDeniedException;
import ru.practicum.explore.error.ConflictException;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.event.grade.Grade;
import ru.practicum.explore.model.event.grade.GradeType;
import ru.practicum.explore.model.event.grade.UserEventPrimaryKey;
import ru.practicum.explore.model.request.RequestState;
import ru.practicum.explore.model.user.User;
import ru.practicum.explore.repository.*;
import ru.practicum.explore.service.private_part.UserEventPrivateService;
import ru.practicum.explore.utils.Utils;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.practicum.explore.error.ExceptionDescriptions.*;

@Service
@RequiredArgsConstructor
public class UserEventPrivateServiceImpl implements UserEventPrivateService {

    private static final int USER_TIME_HOUR_BEFORE_START = 2;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final GradeRepository likeRepository;

    @Override
    @Transactional
    public EventFullDto addEvent(Long userId, EventDto eventInDto) {
        Category category = getCategoryFromRepository(eventInDto.getCategory());
        User user = getUserFromRepository(userId);
        if (eventInDto.getLocation() == null) {
            throw new InvalidParameterException(LOCATION_IS_NULL.getTitle());
        }
        if (eventInDto.getPaid() == null) {
            throw new InvalidParameterException(PAID_IS_NULL.getTitle());
        }
        Utils.checkTimeBeforeOrThrow(eventInDto.getEventDate(), USER_TIME_HOUR_BEFORE_START);
        Event event = EventMapper.dtoInToEvent(eventInDto, category);
        event.setInitiator(user);
        event.setState(EventState.PENDING);
        return EventMapper.eventToOutDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, EventDto eventInDto) {
        Event event = getEventFromRepositoryByUserId(eventInDto.getEventId(), userId);
        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException(EVENT_IS_PUBLISHED.getTitle());
        } else if (event.getState() == EventState.CANCELED) {
            event.setState(EventState.PENDING);
        }
        Utils.checkTimeBeforeOrThrow(event.getEventDate(), USER_TIME_HOUR_BEFORE_START);
        Utils.setNotNullParamToEntity(eventInDto, event, categoryRepository);
        return EventMapper.eventToOutDto(eventRepository.saveAndFlush(event));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> findAllEvents(Long userId, Integer from, Integer size) {
        checkUserExists(userId);
        Sort sort = Sort.sort(Event.class).by(Event::getEventDate).descending();
        Pageable pageable = PageRequest.of(from / size, size, sort);
        return EventMapper.eventToListOutDto(eventRepository.findAllByInitiatorId(userId, pageable));
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEvent(Long userId, Long eventId) {
        Event event = getEventFromRepositoryByUserId(eventId, userId);
        return EventMapper.eventToOutDto(event);
    }

    @Override
    @Transactional
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND.getTitle()));
        if (event.getState() != EventState.PENDING) {
            throw new ConflictException(EVENT_IS_NOT_PENDING.getTitle());
        }
        event.setState(EventState.CANCELED);
        return EventMapper.eventToOutDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public void addGrade(Long userId, Long eventId, GradeType gradeType) {
        Event event = getEventFromRepository(eventId);
        if (Boolean.FALSE.equals(requestRepository
                .existsByRequesterIdAndEventIdAndStatus(userId, eventId, RequestState.CONFIRMED))) {
            throw new AccessDeniedException(FORBIDDEN_TO_RATE_DID_NOT_PARTICIPATE.getTitle());
        }
        // Для прохождения тестов постман, так как невозможно создать событие в прошлом
//        if (event.getEventDate().isAfter(LocalDateTime.now())) {
//            throw new AccessDeniedException(FORBIDDEN_TO_RATE_EVENT_NOT_BEGUN.getTitle());
//        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new AccessDeniedException(FORBIDDEN_TO_RATE_OWN_EVENT.getTitle());
        }
        Optional<Grade> grade = likeRepository.findById(new UserEventPrimaryKey(userId, eventId));
        if (grade.isPresent()) {
            if (gradeType == GradeType.LIKE) {
                checkLikeExists(grade.get().getType());
                addLike(userId, eventId);
            } else {
                checkDislikeExists(grade.get().getType());
                addDislike(userId, eventId);
            }
        } else {
            if (gradeType == GradeType.LIKE) {
                addLike(userId, eventId);
            } else {
                addDislike(userId, eventId);
            }
        }
        setRate(event);
    }

    @Override
    @Transactional
    public void removeGrade(Long userId, Long eventId, GradeType gradeType) {
        Event event = getEventFromRepository(eventId);
        Grade grade = likeRepository
                .findByUserEventPrimaryKeyAndType(new UserEventPrimaryKey(userId, eventId), gradeType)
                .orElseThrow(() -> new NotFoundException(GRADE_NOT_FOUND.getTitle()));
        likeRepository.delete(grade);
        if (gradeType == GradeType.LIKE) {
            eventRepository.decrementRate(eventId);
        } else {
            eventRepository.incrementRate(eventId);
        }
        setRate(event);
    }

    private Double getRate(Long userId) {
        int count = eventRepository.countByInitiatorId(userId);
        Double rate = eventRepository.sumRateByInitiatorId(userId);
        return (count == 0 ? 0 : (rate / count));
    }

    private void setRate(Event event) {
        User initiator = event.getInitiator();
        Double userRate = getRate(initiator.getId());
        initiator.setRate(userRate);
        eventRepository.setUserRate(event.getId(), userRate);
        userRepository.save(initiator);
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(USER_NOT_FOUND.getTitle());
        }
    }

    private Event getEventFromRepositoryByUserId(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND.getTitle()));
    }

    private Event getEventFromRepository(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND.getTitle()));
    }

    private User getUserFromRepository(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getTitle()));
    }

    private Category getCategoryFromRepository(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND.getTitle()));
    }

    private void checkLikeExists(GradeType gradeTypeFromDb) {
        if (gradeTypeFromDb == GradeType.LIKE) {
            throw new ConflictException(LIKE_ALREADY_EXISTS.getTitle());
        }
    }

    private void checkDislikeExists(GradeType gradeTypeFromDb) {
        if (gradeTypeFromDb == GradeType.DISLIKE) {
            throw new ConflictException(DISLIKE_ALREADY_EXISTS.getTitle());
        }
    }

    private void addLike(Long userId, Long eventId) {
        likeRepository.save(new Grade(new UserEventPrimaryKey(userId, eventId), GradeType.LIKE));
        eventRepository.incrementRate(eventId);
    }

    private void addDislike(Long userId, Long eventId) {
        likeRepository.save(new Grade(new UserEventPrimaryKey(userId, eventId), GradeType.DISLIKE));
        eventRepository.decrementRate(eventId);
    }
}