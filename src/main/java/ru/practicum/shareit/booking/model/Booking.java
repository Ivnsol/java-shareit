package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.enums.StatusOfBooking;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @Column(name = "item_id")
    private Long itemId;
    private Long booker;

    public Boolean validateDates() {
        if (start == null || end == null || start.isAfter(end) ||
                end.isBefore(start) || start.equals(end) || start.isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }
}