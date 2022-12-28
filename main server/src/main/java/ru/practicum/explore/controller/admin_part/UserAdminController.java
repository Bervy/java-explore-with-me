package ru.practicum.explore.controller.admin_part;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.service.admin_part.UserAdminService;
import ru.practicum.explore.utils.Constants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
public class UserAdminController {
    private final UserAdminService userAdminService;

    @PostMapping()
    public UserDto createUser(
            @Valid @RequestBody UserDto userDto) {
        return userAdminService.createUser(userDto);
    }

    @GetMapping
    public List<UserDto> findAll(
            @RequestParam(name = "ids") Long[] ids,
            @PositiveOrZero
            @RequestParam(name = "from", defaultValue = Constants.FROM, required = false) Integer from,
            @Positive
            @RequestParam(name = "size", defaultValue = Constants.SIZE, required = false) Integer size) {
        return userAdminService.findAll(ids, from, size);
    }

    @DeleteMapping("{userId}")
    public void deleteUser(
            @Valid @Positive @PathVariable Long userId) {
        userAdminService.deleteUserById(userId);
    }
}