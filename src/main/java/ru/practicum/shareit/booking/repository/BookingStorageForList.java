package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.dto.AllBookingsAsList;
import ru.practicum.shareit.booking.enums.StatusOfBooking;

import java.util.List;

public interface BookingStorageForList extends JpaRepository<AllBookingsAsList, Long> {
    @Query("SELECT b FROM AllBookingsAsList b WHERE b.booker.id = :owner ORDER BY b.start DESC")
    List<AllBookingsAsList> findAllByBookerOrderByEndDesc(@Param("owner") Long owner, Pageable pageRequest);

    @Query("SELECT b FROM AllBookingsAsList b WHERE b.booker.id = :booker AND b.status = :status ORDER BY b.start DESC")
    List<AllBookingsAsList> findBookingByBookerAndStatusOrderByEndDesc(@Param("booker") Long booker,
                                                                       @Param("status") StatusOfBooking status,
                                                                       Pageable pageRequest);

    @Query("SELECT b FROM AllBookingsAsList b WHERE b.booker.id = :userId AND b.end > CURRENT_TIMESTAMP " +
            "AND b.start < CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<AllBookingsAsList> getCurrentBookingsForBooker(@Param("userId") Long userId, Pageable pageRequest);

    @Query("SELECT b FROM AllBookingsAsList b WHERE b.booker.id = :userId AND b.end < CURRENT_TIMESTAMP " +
            "AND b.start < CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<AllBookingsAsList> getPastBookingsForBooker(@Param("userId") Long userId, Pageable pageRequest);

    @Query("SELECT b FROM AllBookingsAsList b WHERE b.booker.id = :userId AND b.start >= CURRENT_TIMESTAMP " +
            "AND b.end > CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<AllBookingsAsList> getFutureBookingsForBooker(@Param("userId") Long userId, Pageable pageRequest);

    @Query("SELECT b FROM AllBookingsAsList b WHERE b.item.id IN :itemsId ORDER BY b.end DESC")
    List<AllBookingsAsList> getAllForOwner(List<Long> itemsId, Pageable pageRequest);

    @Query("SELECT b FROM AllBookingsAsList b WHERE b.item.id IN :itemsId AND b.end > CURRENT_TIMESTAMP " +
            "AND b.start < CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<AllBookingsAsList> getCurrentBookingsForOwner(@Param("itemsId") List<Long> itemsId, Pageable pageRequest);

    @Query("SELECT b FROM AllBookingsAsList b WHERE b.item.id IN :itemsId AND b.end < CURRENT_TIMESTAMP " +
            "AND b.start < CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<AllBookingsAsList> getPastBookingsForOwner(@Param("itemsId") List<Long> itemsId, Pageable pageRequest);

    @Query("SELECT b FROM AllBookingsAsList b WHERE b.item.id IN :itemsId AND b.start >= CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC")
    List<AllBookingsAsList> getFutureBookingsForOwner(@Param("itemsId") List<Long> itemsId, Pageable pageRequest);

    @Query("SELECT b FROM AllBookingsAsList b WHERE b.item.id IN :itemsId AND b.status = :status ORDER BY b.start DESC")
    List<AllBookingsAsList> findBookingByOwnerAndStatusOrderByEndDesc(@Param("itemsId") List<Long> itemsId,
                                                                      @Param("status") StatusOfBooking status,
                                                                      Pageable pageRequest);

}
