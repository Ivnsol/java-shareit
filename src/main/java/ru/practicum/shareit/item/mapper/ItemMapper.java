package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item itemFromItemDto(ItemDto itemDto);

    ItemDto itemDtoFromItem(Item item);

}
