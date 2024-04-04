package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemRequestMapperTest {

    @Test
    void testToItemRequestOutDto() {
        ItemRequest itemRequest = new ItemRequest(1L, "Test request", new User(), LocalDateTime.now());
        List<Item> items = new ArrayList<>();
        items.add(new Item(1L, "item1", "description1", true, new User(), null));
        items.add(new Item(2L, "item2", "description2", true, new User(), null));

        ItemRequestOutDto itemRequestOutDto = ItemRequestMapper.toItemRequestOutDto(itemRequest, items);

        assertEquals(itemRequest.getId(), itemRequestOutDto.getId());
        assertEquals(itemRequest.getDescription(), itemRequestOutDto.getDescription());
        assertEquals(itemRequest.getCreated(), itemRequestOutDto.getCreated());
        assertEquals(items.size(), itemRequestOutDto.getItems().size());
    }

    @Test
    void testToItemRequest() {
        ItemRequestInDto request = new ItemRequestInDto();
        request.setDescription("Test description");
        User requester = new User();

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(request, requester);

        assertEquals(request.getDescription(), itemRequest.getDescription());
        assertEquals(requester, itemRequest.getRequester());
        assertNotNull(itemRequest.getCreated());
    }
}
