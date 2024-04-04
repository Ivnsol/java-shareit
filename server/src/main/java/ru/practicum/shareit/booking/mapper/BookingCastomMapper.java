package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.AllBookingsAsList;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@Component
@RequiredArgsConstructor
public class BookingCastomMapper {
    private  ModelMapper modelMapper;

    public Booking bookingFromDto(BookingDto bookingDto) {
        if (bookingDto == null) {
            return null;
        }

        Booking booking = new Booking();

        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(bookingDto.getStatus());
        booking.setItemId(bookingDto.getItem().getId());
        booking.setBooker(bookingDto.getBooker().getId());

        return booking;
    }

    public BookingDto dtoFromBooking(Booking booking,
                                     ItemDto itemDto,
                                     UserDto userDto) {
        if (booking == null) {
            return null;
        }

        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setItem(itemDto);
        bookingDto.setBooker(userDto);
        return bookingDto;
    }

    public AllBookingsAsList listFromBookings(Booking booking) {
        if (booking == null) {
            return null;
        }

        return modelMapper.map(booking, AllBookingsAsList.class);
    }
}
