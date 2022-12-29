package ru.practicum.explore.service.admin_part;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.error.ConflictException;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.mapper.UserMapper;
import ru.practicum.explore.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAdminService {
    private final UserRepository userRepository;

    public UserDto createUser(UserDto userDto) {
        try {
            return UserMapper.userToDto(userRepository.save(UserMapper.dtoToUser(userDto)));
        } catch (DataAccessException dataAccessException) {
            throw new ConflictException("123");
        }
    }

    public List<UserDto> findAll(Long[] ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return UserMapper.userListToDto(userRepository.findAllByIds(ids, pageable));
    }

    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with was not found.");
        }
        userRepository.deleteById(userId);
    }
}