package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.AllBookingsAsList;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                             @RequestBody @Valid Booking booking) {
        return bookingService.create(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto book(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                           @PathVariable Long bookingId,
                           @RequestParam(value = "approved") Boolean approved) {
        return bookingService.book(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto get(@PathVariable Long bookingId,
                          @RequestHeader(name = "X-Sharer-User-Id") long userId) throws IllegalAccessException { //либо автор брони, либо влад. вещи
        return bookingService.get(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<AllBookingsAsList> getAllBookingForOwner(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                                         @RequestParam(value = "state", defaultValue = "ALL")
                                                         String state,
                                                         @RequestParam(name = "from", defaultValue = "0")
                                                         Integer from,
                                                         @RequestParam(name = "size", defaultValue = "10")
                                                         Integer size) throws IllegalAccessException {
        if (from < 0) {
            throw new IllegalStateException("Value of 'from' parameter cannot be less than 0");
        }
        int page = from / size;

        return bookingService.getAllBookingForOwner(userId, state, page, size);
    }

    @GetMapping
    public List<AllBookingsAsList> getAllForUserByState(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                                        @RequestParam(value = "state", defaultValue = "ALL")
                                                        String state,
                                                        @RequestParam(name = "from", defaultValue = "0")
                                                         Integer from,
                                                        @RequestParam(name = "size", defaultValue = "10")
                                                        Integer size) throws IllegalAccessException {
        if (from < 0) {
            throw new IllegalStateException("Value of 'from' parameter cannot be less than 0");
        }
        int page = from / size;

        return bookingService.getAllForUserByState(userId, state, page, size);
    }
}
