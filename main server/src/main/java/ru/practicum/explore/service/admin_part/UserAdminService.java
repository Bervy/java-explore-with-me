package ru.practicum.explore.service.admin_part;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.controller.admin_part.UserAdminController;
import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.error.ConflictException;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.UserMapper;
import ru.practicum.explore.repository.UserRepository;

import java.util.List;
import static ru.practicum.explore.error.ExceptionDescriptions.*;

@Service
@RequiredArgsConstructor
public class UserAdminService implements UserAdminController {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        try {
            return UserMapper.userToDto(userRepository.save(UserMapper.dtoToUser(userDto)));
        } catch (DataAccessException dataAccessException) {
            throw new ConflictException(USER_ALREADY_EXISTS.getTitle());
        }
    }

    @Override
    public List<UserDto> findAll(Long[] ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return UserMapper.userListToDto(userRepository.findAllByIds(ids, pageable));
    }

    @Override
    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(USER_NOT_FOUND.getTitle());
        }
        userRepository.deleteById(userId);
    }
}