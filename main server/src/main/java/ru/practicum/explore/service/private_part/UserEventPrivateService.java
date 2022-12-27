package ru.practicum.explore.service.private_part;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.Constants;
import ru.practicum.explore.dto.event.EventInDto;
import ru.practicum.explore.dto.event.EventOutDto;
import ru.practicum.explore.error.ConflictException;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.UserRepository;
import ru.practicum.explore.utils.Utils;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserEventPrivateService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
//    private final LikeRepository likeRepository;
//    private final RequestRepository requestRepository;
//
    @Transactional
    public EventOutDto addEvent(Long userId, EventInDto eventInDto) {
        if (!categoryRepository.existsById(eventInDto.getCategory())) {
            throw new NotFoundException("Category ID not found.");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User ID not found.");
        }
        if (eventInDto.getLocation() == null) {
            throw new InvalidParameterException("Location is null.");
        }
        if (eventInDto.getPaid() == null) {
            throw new InvalidParameterException("Paid is null.");
        }
        Utils.checkTimeBeforeOrThrow(eventInDto.getEventDate(), Constants.USER_TIME_HOUR_BEFORE_START);

        Event event = EventMapper.dtoInToEvent(eventInDto, categoryRepository.getReferenceById(eventInDto.getCategory()));
        event.setInitiator(userRepository.getReferenceById(userId));
        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0);
        event.setViews(0L);
        return EventMapper.eventToOutDto(eventRepository.saveAndFlush(event));
    }

    @Transactional
    public EventOutDto updateEvent(Long userId, EventInDto eventInDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User ID not found.");
        }
        Event event = eventRepository.findById(eventInDto.getEventId()).orElseThrow(
                () -> new NotFoundException("Event ID not found.")
        );

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Event is published.");
        } else if (event.getState() == EventState.CANCELED) {
            event.setState(EventState.PENDING);
        }

        if (eventInDto.getEventDate() != null) {
            event.setEventDate(eventInDto.getEventDate());
        }
        Utils.checkTimeBeforeOrThrow(event.getEventDate(), Constants.USER_TIME_HOUR_BEFORE_START);
        Utils.setNotNullParamToEntity(eventInDto, event, categoryRepository);

        return EventMapper.eventToOutDto(eventRepository.saveAndFlush(event));
    }

    public List<EventOutDto> findAllEvents(Long userId, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User ID not found.");
        }
        Sort sort = Sort.sort(Event.class).by(Event::getEventDate).descending();
        Pageable pageable = PageRequest.of(from / size, size, sort);
        return EventMapper.eventToListOutDto(eventRepository.findAllByInitiatorId(userId, pageable));
    }

    public EventOutDto getEvent(Long userId, Long eventId){
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User ID not found.");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event ID not found.")
        );

        return EventMapper.eventToOutDto(event);
    }

    public EventOutDto cancelEvent(Long userId, Long eventId){
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User ID not found.");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event ID not found.")
        );
        if (event.getState() != EventState.PENDING) {
            throw new ConflictException("Event is not pending.");
        }
        event.setState(EventState.CANCELED);

        return EventMapper.eventToOutDto(eventRepository.saveAndFlush(event));
    }
//
//    @Transactional
//    public void addLike(Long userId, Long eventId, LikeType likeType) throws AccessDeniedException {
//        Event event = getEvent(eventId);
//        checkUser(userId, event);
//        if (!requestRepository.existsByRequesterIdAndEventIdAndStatus(userId, eventId, RequestState.CONFIRMED)) {
//            throw new AccessDeniedException("Запрещено оценивать событие в которых не участвуешь.");
//        }
//
//        Optional<Like> like = likeRepository.findByEventIdAndUserId(userId, eventId);
//        if (like.isPresent()) {
//            if (like.get().getType() != likeType) {
//                LikeType deleteType = LikeType.LIKE;
//                if (likeType == LikeType.LIKE) {
//                    deleteType = LikeType.DISLIKE;
//                }
//                removeLike(userId, eventId, deleteType);
//            } else {
//                throw new ConflictException("Можно поставить только один раз.");
//            }
//        }
//        likeRepository.saveAndFlush(new Like(null, userId, eventId, likeType));
//        if (likeType == LikeType.LIKE) {
//            eventRepository.incrementRate(eventId);
//        } else {
//            eventRepository.decrementRate(eventId);
//        }
//
//        User initiator = event.getInitiator();
//        initiator.setRate(getRate(initiator.getId()));
//        userRepository.save(initiator);
//    }
//
//    @Transactional
//    public void removeLike(Long userId, Long eventId, LikeType likeType) throws AccessDeniedException {
//        Event event = getEvent(eventId);
//        checkUser(userId, event);
//
//        Like like = likeRepository.findByUserIdAndEventIdAndType(userId, eventId, likeType)
//                .orElseThrow(
//                        () -> new NotFoundException(likeType + " not found.")
//                );
//        likeRepository.delete(like);
//
//        if (likeType == LikeType.LIKE) {
//            eventRepository.decrementRate(eventId);
//        } else {
//            eventRepository.incrementRate(eventId);
//        }
//
//        User initiator = event.getInitiator();
//        initiator.setRate(getRate(initiator.getId()));
//        userRepository.save(initiator);
//    }
//
//    private Float getRate(Long userId) {
//        int count = eventRepository.countByInitiatorId(userId);
//        long rate = eventRepository.sumRateByInitiatorId(userId);
//
//        return count == 0 ? 0.0F : (1.0F * rate / count);
//    }
//
//    private void checkUser(Long userId, Event event) throws AccessDeniedException {
//        if (!userRepository.existsById(userId)) {
//            throw new NotFoundException("User ID not found.");
//        }
//        if (userId.equals(event.getInitiator().getId())) {
//            throw new AccessDeniedException("Запрещено оценивать собственное событие.");
//        }
//    }
//
//    private Event getEvent(Long eventId) throws AccessDeniedException {
//        Event event = eventRepository.findById(eventId).orElseThrow(
//                () -> new NotFoundException("Event ID not found.")
//        );
//        if (event.getState() != EventState.PUBLISHED) {
//            throw new AccessDeniedException("Можно оценивать только опубликованные события.");
//        }
//        return event;
//    }
}