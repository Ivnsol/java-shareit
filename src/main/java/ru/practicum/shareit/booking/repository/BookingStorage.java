package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.enums.StatusOfBooking;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingStorage extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerOrderByEndDesc(Long owner);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.itemId = i.id WHERE i.owner = :owner AND b.status = :state ORDER BY b.start DESC")
    List<Booking> findByOwnerAndStatusOrderByStartDesc(Long owner, StatusOfBooking state);

    List<Booking> findBookingByBookerAndStatusOrderByEndDesc(Long booker, StatusOfBooking status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker = :id AND b.end < :currentTime AND upper(b.status) = UPPER('APPROVED')" +
            "ORDER BY b.start DESC")
    List<Booking> getByBookerIdStatePast(@Param("id") Long id, @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b WHERE b.booker = :userId AND b.end >= :currentTime AND :currentTime >= b.start " +
            "ORDER BY b.start DESC")
    List<Booking> getByBookerIdStateCurrent(@Param("userId") Long userId, @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b WHERE b.booker = :userId AND b.end > CURRENT_TIMESTAMP " +
            "AND b.start < CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<Booking> getCurrentBookingsForBooker(@Param("userId") Long userId);

    @Query("SELECT b FROM Booking b WHERE b.booker = :userId AND b.end < CURRENT_TIMESTAMP " +
            "AND b.start < CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<Booking> getPastBookingsForBooker(@Param("userId") Long userId);

    @Query("SELECT b FROM Booking b WHERE b.booker = :userId AND b.start >= CURRENT_TIMESTAMP " +
            "AND b.end > CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<Booking> getFutureBookingsForBooker(@Param("userId") Long userId);

    Booking findFirstByItemIdAndStartBeforeAndStatusIsNotOrderByEndDesc(Long itemId, LocalDateTime end, StatusOfBooking status);

    Booking findFirstByItemIdAndStartAfterAndStatusIsNotOrderByEndAsc(Long itemId, LocalDateTime start, StatusOfBooking status);

    Booking getTopByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime start);

}
