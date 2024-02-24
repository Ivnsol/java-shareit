package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> get(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return itemService.getItems(userId);
    }

    @PostMapping
    public ItemDto add(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                       @RequestBody @Validated ItemDto itemDto) {
        return itemService.addNewItem(userId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                           @PathVariable long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                          @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemDto(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                              @PathVariable Long itemId) {
        return itemService.getItemDto(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByString(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                         @RequestParam(value = "text") String string) {
        return itemService.getItemByString(userId, string);
    }
}
