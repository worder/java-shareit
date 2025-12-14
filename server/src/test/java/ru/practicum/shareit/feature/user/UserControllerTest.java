package ru.practicum.shareit.feature.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.feature.user.dto.UserDto;
import ru.practicum.shareit.feature.user.dto.request.CreateUserRequest;
import ru.practicum.shareit.feature.user.dto.request.UpdateUserRequest;
import ru.practicum.shareit.feature.user.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void create_shouldReturnUserDto() throws Exception {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("Ivan")
                .email("ivan@mail.com")
                .build();

        UserDto response = UserDto.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@mail.com")
                .build();

        Mockito.when(userService.create(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.email").value("ivan@mail.com"));
    }

    @Test
    void get_shouldReturnUser() throws Exception {
        UserDto response = UserDto.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@mail.com")
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.email").value("ivan@mail.com"));
    }

    @Test
    void update_shouldReturnUpdatedUser() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .name("Updated")
                .email("updated@mail.com")
                .build();

        UserDto response = UserDto.builder()
                .id(1L)
                .name("Updated")
                .email("updated@mail.com")
                .build();

        Mockito.when(userService.update(Mockito.eq(1L), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(patch("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.email").value("updated@mail.com"));
    }

    @Test
    void delete_shouldReturnOk() throws Exception {
        Mockito.doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());
    }
}
