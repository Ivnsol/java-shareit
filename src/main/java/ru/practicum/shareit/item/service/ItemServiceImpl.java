package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public List<ItemDto> getItems(Long userId) {
        return itemRepository.findByUserId(userId);
    }

    @Override
    public ItemDto addNewItem(Long userId, ItemDto itemDto) {
        return itemRepository.save(userId, itemDto);
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteByUserIdAndItemId(userId, itemId);
    }

    @Override
    public ItemDto update(long userId, ItemDto itemDto) {
        return itemRepository.upadte(userId, itemDto);
    }

    @Override
    public ItemDto getItemDto(long userId, Long itemId) {
        return itemRepository.getItemDto(userId, itemId);
    }

    @Override
    public List<ItemDto> getItemByString(long userId, String string) {
        return itemRepository.getItemByString(userId, string.toLowerCase());
    }
}
