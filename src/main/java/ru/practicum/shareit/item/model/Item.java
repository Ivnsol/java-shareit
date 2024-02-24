package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private Long id;
    private Long owner;//== userId
    private String name;
    private String description;
    private Boolean available; //добавить фалс как станд знач
    private ItemRequest itemRequest;

    public Item(Long id, Long owner, String name, String description, Boolean available) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public static Item fromItemDtoToItem(Long userId, ItemDto itemDto) {
        return new Item(itemDto.getId(),
                userId,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable());
    }
}