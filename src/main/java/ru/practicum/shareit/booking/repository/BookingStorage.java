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

    List<Booking> findBookingByBookerAndStatusOrderByEndDesc(Long booker, StatusOfBooking status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker = :id AND b.end < :currentTime AND upper(b.status) = UPPER('APPROVED')" +
            "ORDER BY b.start DESC")
    List<Booking> getByBookerIdStatePast(@Param("id") Long id, @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b WHERE b.booker = :userId AND b.end > CURRENT_TIMESTAMP " +
            "AND b.start < CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<Booking> getCurrentBookingsForBooker(@Param("userId") Long userId);

    @Query("SELECT b FROM Booking b WHERE b.booker = :userId AND b.end < CURRENT_TIMESTAMP " +
            "AND b.start < CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<Booking> getPastBookingsForBooker(@Param("userId") Long userId);

    @Query("SELECT b FROM Booking b WHERE b.booker = :userId AND b.start >= CURRENT_TIMESTAMP " +
            "AND b.end > CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<Booking> getFutureBookingsForBooker(@Param("userId") Long userId);

    @Query("SELECT b FROM Booking b WHERE b.itemId IN :itemsId ORDER BY b.end DESC")
    List<Booking> getAllForOwner(List<Long> itemsId);

    @Query("SELECT b FROM Booking b WHERE b.itemId IN :itemsId AND b.end > CURRENT_TIMESTAMP " +
            "AND b.start < CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<Booking> getCurrentBookingsForOwner(@Param("itemsId") List<Long> itemsId);

    @Query("SELECT b FROM Booking b WHERE b.itemId IN :itemsId AND b.end < CURRENT_TIMESTAMP " +
            "AND b.start < CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<Booking> getPastBookingsForOwner(@Param("itemsId") List<Long> itemsId);

    @Query("SELECT b FROM Booking b WHERE b.itemId IN :itemsId AND b.start >= CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC")
    List<Booking> getFutureBookingsForOwner(@Param("itemsId") List<Long> itemsId);

    @Query("SELECT b FROM Booking b WHERE b.itemId IN :itemsId AND b.status = :status ORDER BY b.start DESC")
    List<Booking> findBookingByOwnerAndStatusOrderByEndDesc(@Param("itemsId") List<Long> itemsId, @Param("status") StatusOfBooking status);


    @Query("SELECT b FROM Booking b WHERE b.itemId IN :itemsId ORDER BY b.end DESC")
    List<Booking> findAllWhereItemIdIsOrderByEndDesc(List<Long> itemsId);


    Booking findFirstByItemIdAndStartBeforeAndStatusIsNotOrderByEndDesc(Long itemId, LocalDateTime end, StatusOfBooking status);

    Booking findFirstByItemIdAndStartAfterAndStatusIsNotOrderByEndAsc(Long itemId, LocalDateTime start, StatusOfBooking status);

}
