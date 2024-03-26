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

    @Test
    void testValidateDates_NullStart_ReturnsFalse() {
        AllBookingsAsList booking = new AllBookingsAsList();
        booking.setStart(null);
        booking.setEnd(LocalDateTime.now());
        assertFalse(booking.validateDates());
    }

    @Test
    void testValidateDates_NullEnd_ReturnsFalse() {
        AllBookingsAsList booking = new AllBookingsAsList();
        booking.setStart(LocalDateTime.now());
        booking.setEnd(null);
        assertFalse(booking.validateDates());
    }

    @Test
    void testValidateDates_EndBeforeStart_ReturnsFalse() {
        AllBookingsAsList booking = new AllBookingsAsList();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.minusHours(1);
        booking.setStart(start);
        booking.setEnd(end);
        assertFalse(booking.validateDates());
    }

    @Test
    void testValidateDates_StartEqualsEnd_ReturnsFalse() {
        AllBookingsAsList booking = new AllBookingsAsList();
        LocalDateTime now = LocalDateTime.now();
        booking.setStart(now);
        booking.setEnd(now);
        assertFalse(booking.validateDates());
    }

    @Test
    void testValidateDates_StartBeforeNow_ReturnsFalse() {
        AllBookingsAsList booking = new AllBookingsAsList();
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        booking.setStart(start);
        booking.setEnd(start.plusHours(1));
        assertFalse(booking.validateDates());
    }

    @Test
    void testValidateDates_True() {
        AllBookingsAsList booking = new AllBookingsAsList();
        booking.setStart(LocalDateTime.of(2024, 6, 1, 0, 0, 0));
        booking.setEnd(LocalDateTime.of(2024, 7, 2, 0, 0, 0));
        boolean result = booking.validateDates();

        assertTrue(result);
    }
}
