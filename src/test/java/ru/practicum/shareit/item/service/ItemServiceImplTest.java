package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.enums.StatusOfBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.item.comment.model.commentDto.CommentDto;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ItemServiceImplTest {

    ItemService itemService;

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    BookingStorage bookingStorage;

    @Mock
    CommentRepository commentRepository;

    @Mock
    ItemRequestRepository itemRequestRepository;

    User user;
    Item item;
    ItemRequest itemRequest;
    ItemMapper itemMapper;

    @BeforeEach
    void beforeEach() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        bookingStorage = mock(BookingStorage.class);
        commentRepository = mock(CommentRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemMapper = new ItemMapperImpl();
        itemService = new ItemServiceImpl(itemMapper, itemRepository, userRepository,
                bookingStorage, commentRepository, itemRequestRepository);
        user = new User(1L, "User", "user@email.ru");
        itemRequest = new ItemRequest(1L, "request description", user,
                LocalDateTime.of(2022, 10, 14, 13, 17, 29));
        item = new Item(1L, "Item", "item description", true, user, null);

    }

    @Test
    void add() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.save(item)).thenReturn(item);

        ItemDto itemDto = itemMapper.itemDtoFromItem(item);
        ItemDto itemDto1 = itemService.addNewItem(1L, itemDto);
        assertNotNull(itemDto1);
        assertEquals(ItemDto.class, itemDto1.getClass());
        assertEquals(item.getId(), itemDto1.getId());
        assertEquals(item.getName(), itemDto1.getName());
        assertEquals(item.getDescription(), itemDto1.getDescription());
        assertEquals(item.getAvailable(), itemDto1.getAvailable());
    }

    @Test
    void update() {
        Item updatedItem = new Item(item.getId(), "updateItemName", "updateItemDescription",
                item.getAvailable(), item.getOwner(), item.getRequest());
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        when(itemRepository.getById(anyLong())).thenReturn(item);
        when(itemRepository.save(updatedItem)).thenReturn(updatedItem);
        ItemDto itemDto = ItemMapper.toItemDtoRequest(updatedItem);
        ItemDto itemDto1 = itemService.update(1, itemDto.getId(), itemDto);
        assertNotNull(itemDto1);
        assertEquals(ItemDto.class, itemDto1.getClass());
        assertEquals(updatedItem.getId(), itemDto1.getId());
        assertEquals(updatedItem.getName(), itemDto1.getName());
        assertEquals(updatedItem.getDescription(), itemDto1.getDescription());
        assertEquals(updatedItem.getAvailable(), itemDto1.getAvailable());

    }

    @Test
    void findById() throws IllegalAccessException {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ItemDtoWithBooking itemDto = new ItemDtoWithBooking(item.getId(), item.getName(),
                item.getDescription(), item.getAvailable(),
                null, null, Collections.emptyList());

        ItemDtoWithBooking itemDto1 = itemService.getItemDto(1L, 1L);
        assertNotNull(itemDto1);
        assertEquals(ItemDtoWithBooking.class, itemDto1.getClass());
        assertEquals(itemDto.getId(), itemDto1.getId());
        assertEquals(itemDto.getName(), itemDto1.getName());
        assertEquals(itemDto.getDescription(), itemDto1.getDescription());
        assertEquals(itemDto.getAvailable(), itemDto1.getAvailable());
    }

    @Test
    void deleteByUserIdAndItemId_SuccessfulDeletion() throws IllegalAccessException {
        long userId = 1L;
        long itemId = 1L;
        User user = new User(userId, "User", "user@email.ru");
        Item item = new Item(itemId, "Item", "item description", true, user, null);
        when(itemRepository.getById(itemId)).thenReturn(item);
        when(userRepository.getById(userId)).thenReturn(user);
        itemService.deleteByUserIdAndItemId(userId, itemId);
        verify(itemRepository, times(1)).deleteById(itemId);
    }

    @Test
    void deleteByUserIdAndItemId_IncorrectUserId() throws IllegalAccessException {
        long userId = 1L;
        long itemId = 1L;
        User user = new User(2L, "AnotherUser", "anotheruser@email.ru");
        Item item = new Item(itemId, "Item", "item description", true, user, null);
        when(itemRepository.getById(itemId)).thenReturn(item);
        when(userRepository.getById(userId)).thenReturn(null);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            itemService.deleteByUserIdAndItemId(userId, itemId);
        });
        assert (exception.getStatus() == HttpStatus.NOT_FOUND);
        verify(itemRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetItems() {
        long userId = 1L;
        User user = new User(userId, "TestUser", "test@example.com");
        List<Item> items = new ArrayList<>();
        items.add(new Item(1L, "Item1", "Description1", true, user, null));
        items.add(new Item(2L, "Item2", "Description2", true, user, null));

        when(userRepository.getById(userId)).thenReturn(user);
        when(itemRepository.findAllByOwner(user)).thenReturn(items);
        when(commentRepository.getByItem_IdOrderByCreatedDesc(anyLong())).thenReturn(new ArrayList<>());
        when(bookingStorage.findFirstByItemIdAndStartBeforeAndStatusIsNotOrderByEndDesc(anyLong(), any(), any())).thenReturn(null);
        when(bookingStorage.findFirstByItemIdAndStartAfterAndStatusIsNotOrderByEndAsc(anyLong(), any(), any())).thenReturn(null);

        List<ItemDtoWithBooking> result = itemService.getItems(userId);

        assertEquals(2, result.size());
    }

    @Test
    void testAddComment_Success() {
        long userId = 1L;
        long itemId = 1L;
        CommentDto commentDto = new CommentDto(1L, "Test comment", "Test author", LocalDateTime.now());

        User user = new User(userId, "TestUser", "test@example.com");
        Item item = new Item(itemId, "TestItem", "Test description", true, user, null);
        Booking booking = new Booking(1L, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1),
                StatusOfBooking.APPROVED, itemId, userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingStorage.getByBookerIdStatePast(eq(userId), any(LocalDateTime.class))).thenReturn(Collections.singletonList(booking));

        CommentDto result = itemService.addComment(userId, itemId, commentDto);

        assertNotNull(result);
        assertEquals(commentDto.getText(), result.getText());
        assertNotNull(result.getCreated());
        verify(commentRepository, times(1)).save(any());
    }

    @Test
    void testAddComment_EmptyText() {
        long userId = 1L;
        long itemId = 1L;
        CommentDto commentDto = new CommentDto(1L, "", "Test author", LocalDateTime.now());

        User user = new User(userId, "TestUser", "test@example.com");
        Item item = new Item(itemId, "TestItem", "Test description", true, user, null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class, () -> itemService.addComment(userId, itemId, commentDto));
        verify(commentRepository, never()).save(any());
    }

    @Test
    void testAddComment_UserNotFound() {
        long userId = 1L;
        long itemId = 1L;
        CommentDto commentDto = new CommentDto(1L, "Test comment", "Test author", LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> itemService.addComment(userId, itemId, commentDto));
        verify(commentRepository, never()).save(any());
    }

    @Test
    void testAddComment_ItemNotFound() {
        long userId = 1L;
        long itemId = 1L;
        CommentDto commentDto = new CommentDto(1L, "Test comment", "Test author", LocalDateTime.now());

        User user = new User(userId, "TestUser", "test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> itemService.addComment(userId, itemId, commentDto));
        verify(commentRepository, never()).save(any());
    }

    @Test
    void testAddComment_UserHasNotBookedAnything() {
        long userId = 1L;
        long itemId = 1L;
        CommentDto commentDto = new CommentDto(1L, "Test comment", "Test author", LocalDateTime.now());

        User user = new User(userId, "TestUser", "test@example.com");
        Item item = new Item(itemId, "TestItem", "Test description", true, user, null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingStorage.getByBookerIdStatePast(eq(userId), any(LocalDateTime.class))).thenReturn(Collections.emptyList());

        assertThrows(ValidationException.class, () -> itemService.addComment(userId, itemId, commentDto));
        verify(commentRepository, never()).save(any());
    }

    @Test
    public void testGetItemByString_WithEmptyString() {
        long userId = 123L;
        String string = "";

        List<ItemDto> result = itemService.getItemByString(userId, string);

        assertEquals(0, result.size());
    }

}