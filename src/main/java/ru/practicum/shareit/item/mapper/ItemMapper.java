package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item itemFromItemDto(ItemDto itemDto);

    ItemDto itemDtoFromItem(Item item);

    static ItemDto toItemDtoRequest(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    static Item toItemRequest(ItemDto item, User user, @Nullable ItemRequest request) {
        return new Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                user,
                request
        );
    }
}
