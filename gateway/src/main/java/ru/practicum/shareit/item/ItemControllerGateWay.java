package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.CommentDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemControllerGateWay {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return itemClient.get(userId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                       @RequestBody @Validated ItemDto itemDto) {
        return itemClient.add(userId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                           @PathVariable long itemId) throws IllegalAccessException {
        return itemClient.deleteItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        return itemClient.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemDto(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                         @PathVariable Long itemId) throws IllegalAccessException {
        return itemClient.getItemDto(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemByString(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                         @RequestParam(value = "text") String string) {
        return itemClient.getItemByString(userId, string);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody CommentDto commentDto) {
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
