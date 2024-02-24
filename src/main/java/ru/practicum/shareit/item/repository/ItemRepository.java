package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository {

    List<ItemDto> findByUserId(long userId);

    void deleteByUserIdAndItemId(long userId, long itemId);

    ItemDto upadte(long userId, ItemDto itemDto);

    ItemDto getItemDto(long userId, Long itemId);

    List<ItemDto> getItemByString(long userId, String string);

    ItemDto save(Long userId, ItemDto itemDto);
}