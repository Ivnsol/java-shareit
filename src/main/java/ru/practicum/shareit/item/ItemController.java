package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.model.commentDto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDtoWithBooking> get(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return itemService.getItems(userId);
    }

    @PostMapping
    public ItemDto add(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                       @RequestBody @Validated ItemDto itemDto) {
        return itemService.addNewItem(userId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                           @PathVariable long itemId) throws IllegalAccessException {
        itemService.deleteByUserIdAndItemId(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBooking getItemDto(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                         @PathVariable Long itemId) throws IllegalAccessException {
        return itemService.getItemDto(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByString(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                         @RequestParam(value = "text") String string) {
        return itemService.getItemByString(userId, string);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId, @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }
}
