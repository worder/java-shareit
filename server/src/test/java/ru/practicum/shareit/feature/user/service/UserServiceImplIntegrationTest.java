package ru.practicum.shareit.feature.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.common.exception.ConflictEntity;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.feature.user.dto.UserDto;
import ru.practicum.shareit.feature.user.dto.request.CreateUserRequest;
import ru.practicum.shareit.feature.user.dto.request.UpdateUserRequest;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplIntegrationTest {

    @Autowired
    UserServiceImpl userService;

    @Test
    void create_ShouldCreate() {
        CreateUserRequest dto = new CreateUserRequest("John", "john.doe@example.com");

        UserDto createdUser = userService.create(dto);
        assertNotNull(createdUser.getId());
    }

    @Test
    void create_ShouldThrowException_EmailConflict() {
        CreateUserRequest takeEmail = new CreateUserRequest("John", "john.doe@example.com");
        userService.create(takeEmail);

        CreateUserRequest dto = new CreateUserRequest("Doe", "john.doe@example.com");
        assertThrows(ConflictEntity.class, () -> {
            userService.create(dto);
        });
    }

    @Test
    void findById_ShouldFind() {
        CreateUserRequest dto = new CreateUserRequest("John", "john.doe@example.com");
        UserDto newUser = userService.create(dto);

        UserDto result = userService.findById(newUser.getId());
        assertNotNull(result);
        assertEquals(newUser.getEmail(), result.getEmail());
    }

    @Test
    void update_ShouldUpdate() {
        CreateUserRequest dto = new CreateUserRequest("John", "john.doe@example.com");
        UserDto newUser = userService.create(dto);

        userService.update(newUser.getId(), new UpdateUserRequest("John Doe", null));
        UserDto result = userService.update(newUser.getId(),
                new UpdateUserRequest(null, "_john.doe@example.com"));

        assertEquals("John Doe", result.getName());
        assertEquals("_john.doe@example.com", result.getEmail());
    }

    @Test
    void update_ShouldThrowException_EmailConflict() {
        // create user 1
        userService.create(new CreateUserRequest("John Doe", "john.doe@example.com"));

        // create user 2
        UserDto newUser = userService.create(new CreateUserRequest("Robert", "robert.brown@mail.com"));

        // try update user 2 with user 1 email
        UpdateUserRequest update = new UpdateUserRequest(null, "john.doe@example.com");

        assertThrows(ConflictEntity.class, () -> {
            UserDto result = userService.update(newUser.getId(), update);
        });
    }

    @Test
    void delete_ShouldDelete() {
        CreateUserRequest dto = new CreateUserRequest("John", "john.doe@example.com");
        UserDto newUser = userService.create(dto);

        userService.delete(newUser.getId());

        assertThrows(NotFoundException.class, () -> userService.findById(newUser.getId()));
    }
}
