package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.model.commentDto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @MockBean
    ItemService itemService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    ItemDto itemDto;

    @BeforeEach
    void beforeEach() {
        itemDto = new ItemDto(1L, "item1", "description1", true, null);
    }

    @Test
    void add() throws Exception {
        when(itemService.addNewItem(anyLong(), any()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));

        verify(itemService, times(1)).addNewItem(anyLong(), any());
    }

    @Test
    void update() throws Exception {
        ItemDto updateItem = new ItemDto(itemDto.getId(),
                itemDto.getName(),
                "new description",
                true, null);
        when(itemService.update(anyLong(), anyLong(), any(ItemDto.class)))
                .thenReturn(updateItem);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(updateItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updateItem.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(updateItem.getName())))
                .andExpect(jsonPath("$.description", is(updateItem.getDescription())))
                .andExpect(jsonPath("$.available", is(updateItem.getAvailable())));

        verify(itemService, times(1)).update(anyLong(), anyLong(), any(ItemDto.class));
    }

    @Test
    void findById() throws Exception {
        ItemDtoWithBooking itemResponseDto = new ItemDtoWithBooking(1L, "item1", "description1",
                true, null, null, null);
        when(itemService.getItemDto(anyLong(), anyLong()))
                .thenReturn(itemResponseDto);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemResponseDto.getName())))
                .andExpect(jsonPath("$.description", is(itemResponseDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemResponseDto.getAvailable())));

        verify(itemService, times(1)).getItemDto(anyLong(), anyLong());
    }

    @Test
    void searchItemsByText() throws Exception {
        when(itemService.getItemByString(anyLong(), anyString()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "descr"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())));

        verify(itemService, times(1)).getItemByString(anyLong(), anyString());
    }

    @Test
    void searchItemsByTextIsBlank() throws Exception {
        when(itemService.getItemByString(anyLong(), anyString()))
                .thenReturn(Collections.emptyList());

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(itemService, times(1)).getItemByString(anyLong(), anyString());
    }

    @Test
    void getAll() throws Exception {
        ItemDtoWithBooking responseDto1 = new ItemDtoWithBooking(1L, "item1", "description1",
                true, null, null, null);
        ItemDtoWithBooking responseDto2 = new ItemDtoWithBooking(2L, "item2", "description2",
                true, null, null, null);
        List<ItemDtoWithBooking> items = List.of(responseDto1, responseDto2);
        when(itemService.getItems(anyLong()))
                .thenReturn(items);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(responseDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(responseDto1.getName())))
                .andExpect(jsonPath("$[0].description", is(responseDto1.getDescription())))
                .andExpect(jsonPath("$[0].available", is(responseDto1.getAvailable())));

        verify(itemService, times(1)).getItems(anyLong());
    }

    @Test
    void addComment() throws Exception {
        CommentDto commentDto = new CommentDto(1L, "text comment", "author1",
                LocalDateTime.now());
        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));

        verify(itemService, times(1)).addComment(anyLong(), anyLong(), any());
    }
}