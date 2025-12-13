package ru.practicum.shareit.feature.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.feature.item.dto.*;
import ru.practicum.shareit.feature.item.dto.request.CreateCommentRequest;
import ru.practicum.shareit.feature.item.dto.request.CreateItemRequest;
import ru.practicum.shareit.feature.item.dto.request.UpdateItemRequest;
import ru.practicum.shareit.feature.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @Test
    void getUserItems_shouldReturnList() throws Exception {
        ItemDetailsDto dto = ItemDetailsDto.builder()
                .id(1L)
                .name("Drill")
                .description("Powerful drill")
                .available(true)
                .comments(List.of())
                .build();

        Mockito.when(itemService.getUserItems(1L))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/items")
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Drill"));
    }

    @Test
    void create_shouldReturnItem() throws Exception {
        CreateItemRequest request = CreateItemRequest.builder()
                .name("Drill")
                .description("Powerful drill")
                .available(true)
                .build();

        ItemDto response = ItemDto.builder()
                .id(1L)
                .name("Drill")
                .description("Powerful drill")
                .available(true)
                .build();

        Mockito.when(itemService.create(Mockito.eq(1L), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/items")
                        .header(HEADER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Drill"));
    }

    @Test
    void update_shouldReturnUpdatedItem() throws Exception {
        UpdateItemRequest request = UpdateItemRequest.builder()
                .name("Updated drill")
                .build();

        ItemDto response = ItemDto.builder()
                .id(1L)
                .name("Updated drill")
                .available(true)
                .build();

        Mockito.when(itemService.update(Mockito.eq(1L), Mockito.eq(1L), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header(HEADER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated drill"));
    }

    @Test
    void get_shouldReturnItemDetails() throws Exception {
        ItemDetailsDto dto = ItemDetailsDto.builder()
                .id(1L)
                .name("Drill")
                .description("Powerful drill")
                .available(true)
                .comments(List.of())
                .build();

        Mockito.when(itemService.findById(1L, 1L))
                .thenReturn(dto);

        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Drill"));
    }

    @Test
    void delete_shouldReturnOk() throws Exception {
        Mockito.doNothing().when(itemService).delete(1L, 1L);

        mockMvc.perform(delete("/items/{itemId}", 1L)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void search_shouldReturnItems() throws Exception {
        ItemDto dto = ItemDto.builder()
                .id(1L)
                .name("Drill")
                .build();

        Mockito.when(itemService.findUserItems(1L, "drill"))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/items/search")
                        .header(HEADER_USER_ID, 1L)
                        .param("text", "drill"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Drill"));
    }

    @Test
    void createComment_shouldReturnComment() throws Exception {
        CreateCommentRequest request = new CreateCommentRequest("Great item!");

        CommentDto response = CommentDto.builder()
                .id(1L)
                .text("Great item!")
                .authorName("Ivan")
                .created(LocalDateTime.now())
                .build();

        Mockito.when(itemService.createComment(Mockito.eq(1L), Mockito.eq(1L), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header(HEADER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("Great item!"));
    }
}
