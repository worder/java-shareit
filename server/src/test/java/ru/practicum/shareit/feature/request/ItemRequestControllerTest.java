package ru.practicum.shareit.feature.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.feature.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.feature.request.dto.ItemRequestDto;
import ru.practicum.shareit.feature.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.feature.request.service.ItemRequestService;
import ru.practicum.shareit.feature.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    void create_shouldReturnItemRequestDto() throws Exception {
        Long userId = 1L;

        CreateItemRequestDto requestDto =
                new CreateItemRequestDto("Need a drill");

        ItemRequestDto responseDto = ItemRequestDto.builder()
                .id(1L)
                .description("Need a drill")
                .created(LocalDateTime.now())
                .build();

        Mockito.when(itemRequestService.create(Mockito.eq(userId), Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/requests")
                        .header(HEADER_USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Need a drill"));
    }

    @Test
    void getUserRequests_shouldReturnList() throws Exception {
        Long userId = 1L;

        UserDto requestor = UserDto.builder()
                .id(userId)
                .name("User")
                .email("user@mail.com")
                .build();

        ItemRequestDtoWithItems dto = ItemRequestDtoWithItems.builder()
                .id(1L)
                .description("Need a drill")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .items(List.of())
                .build();

        Mockito.when(itemRequestService.findAllByRequesterId(userId))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/requests")
                        .header(HEADER_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].requestor.id").value(userId));
    }

    @Test
    void getAllRequests_shouldReturnList() throws Exception {
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(1L)
                .description("Need a drill")
                .created(LocalDateTime.now())
                .build();

        Mockito.when(itemRequestService.findAll())
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description").value("Need a drill"));
    }

    @Test
    void getRequestById_shouldReturnRequestWithItems() throws Exception {
        Long requestId = 1L;

        UserDto requestor = UserDto.builder()
                .id(1L)
                .name("User")
                .email("user@mail.com")
                .build();

        ItemRequestDtoWithItems.ResponseItemDao item =
                ItemRequestDtoWithItems.ResponseItemDao.builder()
                        .itemId(10L)
                        .ownerId(2L)
                        .name("Drill")
                        .build();

        ItemRequestDtoWithItems dto = ItemRequestDtoWithItems.builder()
                .id(requestId)
                .description("Need a drill")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .items(List.of(item))
                .build();

        Mockito.when(itemRequestService.findById(requestId))
                .thenReturn(dto);

        mockMvc.perform(get("/requests/{requestId}", requestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name").value("Drill"));
    }
}
