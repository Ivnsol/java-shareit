package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.CommentDto;


import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> add(Long userId, ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> get(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> deleteItem(long userId,
                                             long itemId) {
        return delete("/" + itemId, userId);
    }

    public ResponseEntity<Object> update(long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody ItemDto itemDto) {
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> getItemDto(long userId,
                                             Long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItemByString(long userId,
                                                  String string) {
        Map<String, Object> parameters = Map.of(
                "text", string
        );
        return get("/search?text={text}", userId, parameters);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(Long userId,
                                             Long itemId,
                                             CommentDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }

}