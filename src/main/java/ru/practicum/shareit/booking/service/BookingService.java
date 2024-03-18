package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    BookingDto create(long userId, Booking booking);

    BookingDto book(long userId, Long bookingId, Boolean approved);

    BookingDto get(Long bookingId, long userId);

    List<BookingDto> getAllForUserByState(Long userId, String state);

    List<BookingDto> getAllBookingForOwner(Long userId, String state);
}
