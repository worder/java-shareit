package ru.practicum.shareit.feature.request.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.feature.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.feature.request.dto.ItemRequestDto;
import ru.practicum.shareit.feature.user.dto.UserDto;
import ru.practicum.shareit.feature.user.dto.request.CreateUserRequest;
import ru.practicum.shareit.feature.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImplTest {

    @Autowired
    ItemRequestServiceImpl itemRequestService;

    @Autowired
    UserService userService;

    CreateUserRequest userRequest = new CreateUserRequest("John", "john@example.com");
    CreateItemRequestDto requestDto = new CreateItemRequestDto("Item request desc");


    @Test
    void create_shouldCreateItemRequest() {
        UserDto user = userService.create(userRequest);
        ItemRequestDto result = itemRequestService.create(user.getId(), requestDto);

        assertNotNull(result.getId());
    }

    @Test
    void create_ThrowsException_WrongUser() {
        assertThrows(NotFoundException.class, () -> itemRequestService.create(999L, requestDto));
    }

    @Test
    void findById() {
        UserDto user = userService.create(userRequest);
        ItemRequestDto result = itemRequestService.create(user.getId(), requestDto);

        assertNotNull(itemRequestService.findById(result.getId()));
    }

    @Test
    void findAll() {
        UserDto user = userService.create(userRequest);
        itemRequestService.create(user.getId(), requestDto);

        assertEquals(1, itemRequestService.findAll().size());
    }

    @Test
    void findAllByRequesterId() {
        UserDto user = userService.create(userRequest);
        itemRequestService.create(user.getId(), requestDto);

        assertEquals(1, itemRequestService.findAllByRequesterId(user.getId()).size());
        assertEquals(0, itemRequestService.findAllByRequesterId(999L).size());
    }
}
