package ru.practicum.explore.controller.admin_part;

import ru.practicum.explore.dto.user.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface UserAdminController {

    UserDto createUser(@Valid UserDto userDto);

    List<UserDto> findAll(
            Long[] ids,
            @PositiveOrZero Integer from,
            @Positive Integer size);

    void deleteUserById(@Valid @Positive Long userId);
}