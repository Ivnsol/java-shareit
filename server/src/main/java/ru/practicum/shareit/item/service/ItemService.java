package ru.practicum.shareit.item.service;


import ru.practicum.shareit.item.comment.model.commentDto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;

import java.util.List;

public interface ItemService {

    List<ItemDtoWithBooking> getItems(Long userId);

    void deleteByUserIdAndItemId(long userId, long itemId) throws IllegalAccessException;

    ItemDto update(long userId, long itemId, ItemDto itemDto);

    ItemDtoWithBooking getItemDto(Long itemId, long userId) throws IllegalAccessException;

    List<ItemDto> getItemByString(long userId, String string);

    ItemDto addNewItem(Long userId, ItemDto itemDto);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);
}
