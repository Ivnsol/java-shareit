package ru.practicum.shareit.item.comment.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;

public class CommentRepositoryTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetByItem_IdOrderByCreatedDesc() {
        Long itemId = 123L;
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        List<Comment> expectedComments = List.of(comment1, comment2);

        when(commentRepository.getByItem_IdOrderByCreatedDesc(itemId)).thenReturn(expectedComments);
        Item item = new Item();
        item.setId(123L);

        List<Comment> actualComments = commentRepository.getByItem_IdOrderByCreatedDesc(item.getId());

        assertNotNull(actualComments);
        assertEquals(expectedComments.size(), actualComments.size());
        assertEquals(expectedComments, actualComments);
    }
}
