package ru.practicum.explore.mapper;

import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.dto.user.UserPublicDto;
import ru.practicum.explore.model.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto userToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .rate(user.getRate())
                .build();
    }

    public static User dtoToUser(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static List<UserDto> userListToDto(List<User> userList) {
        return userList.stream().map(UserMapper::userToDto).collect(Collectors.toList());
    }

    public static UserPublicDto userToPublicDto(User user) {
        return new UserPublicDto(user.getName());
    }
}