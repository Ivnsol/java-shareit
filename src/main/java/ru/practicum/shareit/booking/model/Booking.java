package ru.practicum.shareit.booking.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.enums.StatusOfBooking;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalDateTime start;
    @NotNull
    @Column(name = "end_time")
    private LocalDateTime end;
    @Enumerated(EnumType.STRING)
    private StatusOfBooking status;
    private Long itemId;
    private Long booker;

    public void validateDates() {
        if (start == null || end == null || start.isAfter(end) ||
                end.isBefore(start) || start == end || start.equals(end) || start.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}