package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.StatusOfBooking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingDtoTest {

    @Test
    void testConstructorAndGetters() {
        Long id = 1L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        StatusOfBooking status = StatusOfBooking.APPROVED;
        ItemDto item = new ItemDto();
        UserDto booker = new UserDto();

        BookingDto bookingDto = new BookingDto(id, start, end, status, item, booker);

        assertEquals(id, bookingDto.getId());
        assertEquals(start, bookingDto.getStart());
        assertEquals(end, bookingDto.getEnd());
        assertEquals(status, bookingDto.getStatus());
        assertEquals(item, bookingDto.getItem());
        assertEquals(booker, bookingDto.getBooker());
    }

    @Test
    void testSetters() {
        BookingDto bookingDto = new BookingDto();
        Long id = 1L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        StatusOfBooking status = StatusOfBooking.APPROVED;
        ItemDto item = new ItemDto();
        UserDto booker = new UserDto();

        bookingDto.setId(id);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);
        bookingDto.setStatus(status);
        bookingDto.setItem(item);
        bookingDto.setBooker(booker);

        assertEquals(id, bookingDto.getId());
        assertEquals(start, bookingDto.getStart());
        assertEquals(end, bookingDto.getEnd());
        assertEquals(status, bookingDto.getStatus());
        assertEquals(item, bookingDto.getItem());
        assertEquals(booker, bookingDto.getBooker());
    }
}
