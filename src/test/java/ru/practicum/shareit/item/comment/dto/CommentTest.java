package ru.practicum.shareit.item.comment.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentTest {

    @Test
    void testNoArgsConstructor() {
        Comment comment = new Comment();
        assertNull(comment.getId());
        assertNull(comment.getText());
        assertNull(comment.getItem());
        assertNull(comment.getUser());
        assertNull(comment.getCreated());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String text = "Test comment";
        Item item = new Item();
        User user = new User();
        LocalDateTime created = LocalDateTime.now();

        Comment comment = new Comment(id, text, item, user, created);

        assertEquals(id, comment.getId());
        assertEquals(text, comment.getText());
        assertEquals(item, comment.getItem());
        assertEquals(user, comment.getUser());
        assertEquals(created, comment.getCreated());
    }

    @Test
    void testSetterGetterMethods() {
        Comment comment = new Comment();

        Long id = 1L;
        String text = "Test comment";
        Item item = new Item();
        User user = new User();
        LocalDateTime created = LocalDateTime.now();

        comment.setId(id);
        comment.setText(text);
        comment.setItem(item);
        comment.setUser(user);
        comment.setCreated(created);

        assertEquals(id, comment.getId());
        assertEquals(text, comment.getText());
        assertEquals(item, comment.getItem());
        assertEquals(user, comment.getUser());
        assertEquals(created, comment.getCreated());
    }
}
