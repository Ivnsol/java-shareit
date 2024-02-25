package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static ru.practicum.shareit.user.repository.UserRepositoryImpl.USERS;

@Component
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private Long id = 1L;
    public static final List<Item> items = new CopyOnWriteArrayList<>();

    private final ItemMapper itemMapper;


    @Override
    public List<ItemDto> findByUserId(long userId) {
        List<ItemDto> itemsByUserId = new ArrayList<>();
        for (Item item : items) {
            if (item.getOwner().equals(userId)) {
                itemsByUserId.add(itemMapper.itemDtoFromItem(item));
            }
        }
        return itemsByUserId;
    }

    @Override
    public ItemDto save(Long userId, ItemDto itemDto) {
        for (User user : USERS) {
            if (user.getId().equals(userId)) {
                itemDto.setId(id);
                Item item = itemMapper.itemFromItemDto(itemDto);
                item.setOwner(userId);
                items.add(item);
                id++;
                return itemDto;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found");
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        for (Item item : items) {
            if (item.getId().equals(userId) && item.getId().equals(itemId)) {
                items.remove(item);
            }
        }
    }

    @SneakyThrows
    @Override
    public ItemDto upadte(long userId, ItemDto itemDto) {
        for (Item item : items) {
            if (item.getOwner().equals(userId)) {
                if (itemDto.getName() != null) {
                    item.setName(itemDto.getName());
                }
                if (itemDto.getDescription() != null) {
                    item.setDescription(itemDto.getDescription());
                }
                if (itemDto.getAvailable() != null) {
                    item.setAvailable(itemDto.getAvailable());
                }
                return itemMapper.itemDtoFromItem(item);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Incorrect user ID or item is not exist");
    }

    @Override
    public ItemDto getItemDto(long userId, Long itemId) throws IllegalAccessException {
        for (Item item : items) {
            if (item.getId().equals(itemId)) {
                return itemMapper.itemDtoFromItem(item);
            }
        }
        throw new IllegalAccessException("Incorrect id");
    }

    @Override
    public List<ItemDto> getItemByString(long userId, String string) {
        List<ItemDto> itemsAfterSearch = new ArrayList<>();
        if (string.isEmpty()) return itemsAfterSearch;

        for (Item item : items) {
            if (item.getName().toLowerCase().contains(string) &&
                    item.getAvailable() == true) {
                itemsAfterSearch.add(itemMapper.itemDtoFromItem(item));
            } else if (item.getDescription().toLowerCase().contains(string) &&
                    item.getAvailable() == true) {
                itemsAfterSearch.add(itemMapper.itemDtoFromItem(item));
            }
        }
        return itemsAfterSearch;
    }
}
