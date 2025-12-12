package ru.practicum.shareit.feature.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.feature.user.dto.request.CreateUserRequest;
import ru.practicum.shareit.feature.user.dto.request.UpdateUserRequest;
import ru.practicum.shareit.feature.user.dto.UserDto;
import ru.practicum.shareit.feature.user.service.UserService;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
