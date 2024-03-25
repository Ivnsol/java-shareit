package ru.practicum.shareit.booking.dto;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.StatusOfBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AllBookingsAsListTest {

    @Test
    void testConstructorAndGetters() {
        Long id = 1L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        StatusOfBooking status = StatusOfBooking.APPROVED;
        Item item = new Item();
        User booker = new User();

        AllBookingsAsList booking = new AllBookingsAsList(id, start, end, status, item, booker);

        assertEquals(id, booking.getId());
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(status, booking.getStatus());
        assertEquals(item, booking.getItem());
        assertEquals(booker, booking.getBooker());
    }

    @Test
    void testSetters() {
        AllBookingsAsList booking = new AllBookingsAsList();
        Long id = 1L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        StatusOfBooking status = StatusOfBooking.APPROVED;
        Item item = new Item();
        User booker = new User();

        booking.setId(id);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setStatus(status);
        booking.setItem(item);
        booking.setBooker(booker);

        assertEquals(id, booking.getId());
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(status, booking.getStatus());
        assertEquals(item, booking.getItem());
        assertEquals(booker, booking.getBooker());
    }
}
