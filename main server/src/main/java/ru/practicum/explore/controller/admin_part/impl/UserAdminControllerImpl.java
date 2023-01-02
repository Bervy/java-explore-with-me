package ru.practicum.explore.controller.admin_part.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.controller.admin_part.UserAdminController;
import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.service.admin_part.impl.UserAdminServiceImpl;
import ru.practicum.explore.utils.Constants;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
public class UserAdminControllerImpl implements UserAdminController {

    private final UserAdminServiceImpl userAdminService;

    @PostMapping()
    @Override
    public UserDto createUser(
            @RequestBody UserDto userDto) {
        return userAdminService.createUser(userDto);
    }

    @GetMapping
    @Override
    public List<UserDto> findAll(
            @RequestParam(name = "ids") Long[] ids,
            @RequestParam(name = "from", defaultValue = Constants.FROM, required = false) Integer from,
            @RequestParam(name = "size", defaultValue = Constants.SIZE, required = false) Integer size) {
        return userAdminService.findAll(ids, from, size);
    }

    @DeleteMapping("{userId}")
    @Override
    public void deleteUserById(
            @PathVariable Long userId) {
        userAdminService.deleteUserById(userId);
    }
}