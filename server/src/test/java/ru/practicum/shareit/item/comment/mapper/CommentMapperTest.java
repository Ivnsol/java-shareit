package ru.practicum.shareit.item.comment.mapper;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.model.commentDto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapperTest {

    @Test
    void testToCommentDto() {
        User user = new User();
        user.setName("John Doe");
        Comment comment = new Comment(1L, "Test comment", new Item(), user, LocalDateTime.now());

        CommentDto commentDto = CommentMapper.toCommentDto(comment);

        assertEquals(comment.getId(), commentDto.getId());
        assertEquals(comment.getText(), commentDto.getText());
        assertEquals(comment.getUser().getName(), commentDto.getAuthorName());
        assertEquals(comment.getCreated(), commentDto.getCreated());
    }

    @Test
    void testToComment() {
        CommentDto commentDto = new CommentDto(1L, "Test comment", "John Doe", LocalDateTime.now());
        Item item = new Item();
        User user = new User();
        user.setName("John Doe");

        Comment comment = CommentMapper.toComment(commentDto, item, user);

        assertEquals(commentDto.getId(), comment.getId());
        assertEquals(commentDto.getText(), comment.getText());
        assertEquals(item, comment.getItem());
        assertEquals(user, comment.getUser());
        assertEquals(commentDto.getCreated(), comment.getCreated());
    }
}
