package ru.practicum.shareit.booking.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.StatusOfBooking;
import ru.practicum.shareit.booking.model.Booking;

public class BookingStorageTest {

    @Test
    void testGetByBookerIdStatePast() {
        Long userId = 1L;
        LocalDateTime currentTime = LocalDateTime.now();
        List<Booking> expectedBookings = Arrays.asList(new Booking(), new Booking());

        BookingStorage bookingStorage = mock(BookingStorage.class);

        when(bookingStorage.getByBookerIdStatePast(anyLong(), any(LocalDateTime.class)))
                .thenReturn(expectedBookings);

        List<Booking> result = bookingStorage.getByBookerIdStatePast(userId, currentTime);

        verify(bookingStorage, times(1)).getByBookerIdStatePast(userId, currentTime);
        assertEquals(expectedBookings, result);
    }

    @Test
    void testFindAllWhereItemIdIsOrderByEndDesc() {
        List<Long> itemsId = Arrays.asList(1L, 2L);
        List<Booking> expectedBookings = Arrays.asList(new Booking(), new Booking());

        BookingStorage bookingStorage = mock(BookingStorage.class);

        when(bookingStorage.findAllByItemsList(anyList()))
                .thenReturn(expectedBookings);

        List<Booking> result = bookingStorage.findAllByItemsList(itemsId);

        verify(bookingStorage, times(1)).findAllByItemsList(itemsId);
        assertEquals(expectedBookings, result);
    }

    @Test
    void testFindFirstByItemIdAndStartBeforeAndStatusIsNotOrderByEndDesc() {
        Long itemId = 1L;
        LocalDateTime end = LocalDateTime.now();
        StatusOfBooking status = StatusOfBooking.APPROVED;
        Booking expectedBooking = new Booking();

        BookingStorage bookingStorage = mock(BookingStorage.class);

        when(bookingStorage.findFirstByItemIdAndStartBeforeAndStatusIsNotOrderByEndDesc(anyLong(), any(LocalDateTime.class), any(StatusOfBooking.class)))
                .thenReturn(expectedBooking);

        Booking result = bookingStorage.findFirstByItemIdAndStartBeforeAndStatusIsNotOrderByEndDesc(itemId, end, status);

        verify(bookingStorage, times(1)).findFirstByItemIdAndStartBeforeAndStatusIsNotOrderByEndDesc(itemId, end, status);
        assertEquals(expectedBooking, result);
    }

    @Test
    void testFindFirstByItemIdAndStartAfterAndStatusIsNotOrderByEndAsc() {
        Long itemId = 1L;
        LocalDateTime start = LocalDateTime.now();
        StatusOfBooking status = StatusOfBooking.APPROVED;
        Booking expectedBooking = new Booking();

        BookingStorage bookingStorage = mock(BookingStorage.class);

        when(bookingStorage.findFirstByItemIdAndStartAfterAndStatusIsNotOrderByEndAsc(anyLong(), any(LocalDateTime.class), any(StatusOfBooking.class)))
                .thenReturn(expectedBooking);

        Booking result = bookingStorage.findFirstByItemIdAndStartAfterAndStatusIsNotOrderByEndAsc(itemId, start, status);

        verify(bookingStorage, times(1)).findFirstByItemIdAndStartAfterAndStatusIsNotOrderByEndAsc(itemId, start, status);
        assertEquals(expectedBooking, result);
    }

}
