package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.AllBookingsAsList;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.StatusOfBooking;
import ru.practicum.shareit.booking.mapper.BookingCastomMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.booking.repository.BookingStorageForList;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class BookingServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private BookingStorage bookingStorage;

    @Mock
    private BookingStorageForList bookingStorageForList;

    @Mock
    private BookingCastomMapper bookingCastomMapper;

    @Mock
    private UserService userService;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private UserDto userDto;
    private ItemDto itemDto;
    private User user;
    private Item item;
    private AllBookingsAsList allBookingsAsList;
    private BookingDto bookingDto;
    private Booking booking;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);

        BookingCastomMapper bcm = new BookingCastomMapper();
        userDto = new UserDto(1L, "userName", "user@email.ru");
        itemDto = new ItemDto(1L, "item1", "description1", true, null);

        user = new User(1L, "user name", "user@email.ru");
        User user2 = new User(1L, "user name", "user@email.ru");
        item = new Item(1L, "item1", "description1", true, user, null);
        allBookingsAsList = new AllBookingsAsList(1L,
                LocalDateTime.of(2024, 6, 1, 0, 0, 0),
                LocalDateTime.of(2024, 7, 2, 0, 0, 0),
                StatusOfBooking.WAITING, item, user);

        bookingDto = new BookingDto(1L,
                LocalDateTime.of(2024, 6, 1, 0, 0, 0),
                LocalDateTime.of(2024, 7, 2, 0, 0, 0),
                StatusOfBooking.WAITING, itemDto, userDto);
        booking = new Booking(1L,
                LocalDateTime.of(2024, 6, 1, 0, 0, 0),
                LocalDateTime.of(2024, 7, 2, 0, 0, 0),
                StatusOfBooking.WAITING, itemDto.getId(), userDto.getId());
    }

    @Test
    void testCreateBooking() {
        when(userService.get(anyLong())).thenReturn(userDto);
        when(itemRepository.getById(anyLong())).thenReturn(item);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemMapper.itemDtoFromItem(any())).thenReturn(itemDto);
        when(bookingCastomMapper.dtoFromBooking(any(), any(), any())).thenReturn(bookingDto);
        when(bookingStorage.save(any())).thenReturn(booking);

        BookingDto createdBookingDto = bookingService.create(2L, booking);

        assertNotNull(createdBookingDto);
        assertEquals(bookingDto, createdBookingDto);

        verify(userService, times(1)).get(anyLong());
        verify(itemRepository, times(1)).getById(anyLong());
        verify(bookingStorage, times(1)).save(any());
    }

    @Test
    void testCreateBookingInvalidDates() {
        booking.setStart(LocalDateTime.of(2022, 10, 17, 21, 22, 22));
        booking.setEnd(LocalDateTime.of(2022, 10, 16, 21, 22, 22));

        assertThrows(IllegalStateException.class, () -> bookingService.create(1L, booking));
    }

    @Test
    void testBook() {
        when(bookingStorage.findById(any())).thenReturn(Optional.of(booking));
        when(itemRepository.getById(anyLong())).thenReturn(item);
        when(itemMapper.itemDtoFromItem(any())).thenReturn(itemDto);
        when(userRepository.getById(any())).thenReturn(user);
        when(userMapper.userDtoFromUser(any())).thenReturn(userDto);
        when(bookingCastomMapper.dtoFromBooking(any(), any(), any())).thenReturn(bookingDto);

        BookingDto saveBooking = bookingService.book(1L, 1L, true);

        verify(bookingStorage, times(1)).save(any());
    }

    @Test
    void testGetUserCallsUserServiceGet() {

        when(bookingStorage.findById(any())).thenReturn(Optional.of(booking));
        when(itemRepository.getById(anyLong())).thenReturn(item);
        when(itemMapper.itemDtoFromItem(any())).thenReturn(itemDto);
        when(userService.get(any())).thenReturn(userDto);

        bookingService.get(1L, 1L);

        verify(bookingCastomMapper, times(1)).dtoFromBooking(any(), any(), any());
    }

    @Test
    void testGetAllBookingForOwnert() {
        Long userId = 1L;
        String stringState = "ALL";
        Pageable pageRequest = PageRequest.of(0, 10);
        List<Item> items = new ArrayList<>();
        items.add(item);

        when(userMapper.userDtoFromUser(any())).thenReturn(userDto);
        when(itemRepository.findAllByOwner(any())).thenReturn(items);

        List<AllBookingsAsList> result = bookingService.getAllBookingForOwner(userId, stringState, pageRequest);

        verify(bookingStorageForList, times(1)).getAllForOwner(any(), any());
    }

    @Test
    void testGetAllBookingsForUser() {
        Long userId = 1L;
        String stringState = "ALL";
        Pageable pageRequest = PageRequest.of(0, 10);
        List<Item> items = new ArrayList<>();
        items.add(item);

        when(userMapper.userDtoFromUser(any())).thenReturn(userDto);
        when(itemRepository.findAllByOwner(any())).thenReturn(items);
        when(userService.get(any())).thenReturn(userDto);

        bookingService.getAllForUserByState(userId, stringState, pageRequest);
        verify(bookingStorageForList, times(1)).findAllByBookerOrderByEndDesc(any(), any());
    }
}
