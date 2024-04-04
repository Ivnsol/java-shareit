package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingControllerGateWay {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody @Valid Booking booking) {
        log.info("Creating booking {}, userId={}", booking, userId);
        return bookingClient.create(userId, booking);
    }

//    @GetMapping("/{bookingId}")
//    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
//                                             @PathVariable Long bookingId) {
//        log.info("Get booking {}, userId={}", bookingId, userId);
//        return bookingClient.getBooking(userId, bookingId);
//    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> book(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                       @PathVariable Long bookingId,
                                       @RequestParam(value = "approved") Boolean approved) {
        return bookingClient.book(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> get1(@PathVariable Long bookingId,
                                       @RequestHeader(name = "X-Sharer-User-Id") long userId) throws IllegalAccessException { //либо автор брони, либо влад. вещи
        return bookingClient.get(bookingId, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingForOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                        @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, stateParam, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getAllForUserByState(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                                       @RequestParam(value = "state", defaultValue = "ALL")
                                                       String state,
                                                       @RequestParam(name = "from", defaultValue = "0")
                                                       Integer from,
                                                       @RequestParam(name = "size", defaultValue = "10")
                                                       Integer size) {
        if (from < 0) {
            throw new IllegalStateException("Value of 'from' parameter cannot be less than 0");
        }

        return bookingClient.getAllForUserByState(userId, state, from, size);
    }
}
