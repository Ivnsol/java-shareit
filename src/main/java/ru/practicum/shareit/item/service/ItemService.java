package ru.practicum.shareit.item.service;


import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItems(Long userId);

    void deleteItem(long userId, long itemId);

    ItemDto update(long userId, ItemDto itemDto);

    ItemDto getItemDto(long userId, Long itemId) throws IllegalAccessException;

    List<ItemDto> getItemByString(long userId, String string);

    ItemDto addNewItem(Long userId, ItemDto itemDto);
}
