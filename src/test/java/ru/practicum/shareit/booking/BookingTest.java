package ru.practicum.shareit.booking;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.StatusOfBooking;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    @Test
    void testValidateDates() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime future = now.plusHours(1);
        LocalDateTime past = now.minusHours(1);

        Booking validBooking = new Booking(1L, future, future.plusHours(1), StatusOfBooking.WAITING, 1L, 1L);
        Booking bookingWithNullStart = new Booking(1L, null, future, StatusOfBooking.WAITING, 1L, 1L);
        Booking bookingWithNullEnd = new Booking(1L, future, null, StatusOfBooking.WAITING, 1L, 1L);
        Booking bookingWithEndBeforeStart = new Booking(1L, future, past, StatusOfBooking.WAITING, 1L, 1L);
        Booking bookingWithStartEqualsEnd = new Booking(1L, future, future, StatusOfBooking.WAITING, 1L, 1L);
        Booking bookingWithStartBeforeNow = new Booking(1L, past, future, StatusOfBooking.WAITING, 1L, 1L);

        assertTrue(validBooking.validateDates());
        assertFalse(bookingWithNullStart.validateDates());
        assertFalse(bookingWithNullEnd.validateDates());
        assertFalse(bookingWithEndBeforeStart.validateDates());
        assertFalse(bookingWithStartEqualsEnd.validateDates());
        assertFalse(bookingWithStartBeforeNow.validateDates());
    }
}
