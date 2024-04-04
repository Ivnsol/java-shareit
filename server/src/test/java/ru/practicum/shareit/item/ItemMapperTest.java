package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.*;

public class ItemMapperTest {

    @Test
    void testItemFromItemDto() {
        ItemMapper itemMapper = new ItemMapperImpl();
        ItemDto itemDto = new ItemDto(1L, "TestItem", "TestDescription", true, 100L);
        Item item = itemMapper.itemFromItemDto(itemDto);
        assertNotNull(item);
        assertEquals(itemDto.getId(), item.getId());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getAvailable(), item.getAvailable());
    }

    @Test
    void testItemDtoFromItem() {
        ItemMapper itemMapper = new ItemMapperImpl();
        Item item = new Item(1L, "TestItem", "TestDescription", true, null, null);
        ItemDto itemDto = itemMapper.itemDtoFromItem(item);
        assertNotNull(itemDto);
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
    }

    @Test
    void testToItemDtoRequest() {
        Item item = new Item(1L, "TestItem", "TestDescription", true, null, null);
        ItemDto itemDto = ItemMapper.toItemDtoRequest(item);
        assertNotNull(itemDto);
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
    }
}
