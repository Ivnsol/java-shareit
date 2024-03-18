package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
                           @RequestParam(value = "approved") Boolean approved) { //только для владельца вещи
        return bookingService.book(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto get(@PathVariable Long bookingId,
                          @RequestHeader(name = "X-Sharer-User-Id") long userId) { //либо автор брони, либо влад. вещи
        return bookingService.get(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingForOwner(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                                  @RequestParam(value = "state", defaultValue = "ALL") String state) {
        return bookingService.getAllBookingForOwner(userId, state);
    }

    @GetMapping
    public List<BookingDto> getAllForUserByState(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                                 @RequestParam(value = "state",
                                                         defaultValue = "ALL",
                                                         required = false) String state) {
        return bookingService.getAllForUserByState(userId, state);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIncorrectState(final IllegalArgumentException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Unknown state: UNSUPPORTED_STATUS");
        return response;
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NoSuchElementException handleResponseStatusException(final NoSuchElementException e) {
        return new NoSuchElementException("No such element");
    }
}
