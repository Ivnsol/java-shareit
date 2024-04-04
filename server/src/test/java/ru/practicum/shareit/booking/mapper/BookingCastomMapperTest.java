package ru.practicum.shareit.booking.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import ru.practicum.shareit.booking.dto.AllBookingsAsList;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.StatusOfBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class BookingCastomMapperTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookingCastomMapper bookingCastomMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testBookingFromDto() {
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now(),
                LocalDateTime.now().plusHours(1), StatusOfBooking.APPROVED,
                new ItemDto(), new UserDto());

        Booking booking = bookingCastomMapper.bookingFromDto(bookingDto);

        assertNotNull(booking);
        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getStart(), booking.getStart());
        assertEquals(bookingDto.getEnd(), booking.getEnd());
        assertEquals(bookingDto.getStatus(), booking.getStatus());
        assertEquals(bookingDto.getItem().getId(), booking.getItemId());
        assertEquals(bookingDto.getBooker().getId(), booking.getBooker());
    }

    @Test
    void testBookingFromDtoNull() {
        BookingDto bookingDto = null;

        Booking booking = bookingCastomMapper.bookingFromDto(bookingDto);

        assertNull(booking);
    }

    @Test
    void testDtoFromBooking() {
        User user = new User(1L, "user name", "user@email.ru");
        Item item = new Item(1L, "item1", "description1", true, user, null);
        Booking booking = new Booking(1L, LocalDateTime.now(),
                LocalDateTime.now().plusHours(1), StatusOfBooking.APPROVED,
                item.getId(), user.getId());
        ItemDto itemDto = new ItemDto();
        UserDto userDto = new UserDto();

        BookingDto bookingDto = bookingCastomMapper.dtoFromBooking(booking, itemDto, userDto);

        assertNotNull(bookingDto);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
        assertEquals(itemDto, bookingDto.getItem());
        assertEquals(userDto, bookingDto.getBooker());
    }

    @Test
    void testListFromBookings() {
        User user = new User(1L, "user name", "user@email.ru");
        Item item = new Item(1L, "item1", "description1", true, user, null);
        Booking booking = new Booking(1L, LocalDateTime.now(),
                LocalDateTime.now().plusHours(1), StatusOfBooking.APPROVED,
                item.getId(), user.getId());
        AllBookingsAsList expected = new AllBookingsAsList();

        when(modelMapper.map(booking, AllBookingsAsList.class)).thenReturn(expected);

        AllBookingsAsList result = bookingCastomMapper.listFromBookings(booking);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void testListFromBookingsNull() {
        Booking booking = null;

        AllBookingsAsList result = bookingCastomMapper.listFromBookings(booking);

        assertNull(result);
    }

    @Test
    void testDtoFromBooking2() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusHours(1));
        booking.setStatus(StatusOfBooking.APPROVED);

        ItemDto itemDto = new ItemDto(1L, "Item", "Description", true, null);
        UserDto userDto = new UserDto(1L, "User", "user@example.com");

        BookingDto bookingDto = bookingCastomMapper.dtoFromBooking(booking, itemDto, userDto);

        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
        assertEquals(itemDto, bookingDto.getItem());
        assertEquals(userDto, bookingDto.getBooker());
    }

    @Test
    void testDtoFromBooking_NullInput() {
        Booking booking = null;
        ItemDto itemDto = new ItemDto(1L, "Item", "Description", true, null);
        UserDto userDto = new UserDto(1L, "User", "user@example.com");

        BookingDto bookingDto = bookingCastomMapper.dtoFromBooking(booking, itemDto, userDto);

        assertNull(bookingDto);
    }
}
