package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.StatusOfBooking;
import ru.practicum.shareit.booking.mapper.BookingCastomMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserMapper userMapper;
    private final BookingStorage bookingStorage;
    private final BookingCastomMapper bookingCastomMapper;
    private final UserService userService;
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto create(long userId, Booking booking) {
        try {
            booking.validateDates();
            UserDto userDto = userService.get(userId);

            ItemDtoWithBooking itemDto = itemService.getItemDto(booking.getItemId(), userId);

            if (itemRepository.findById(booking.getItemId()).get().getOwner().getId().equals(userId))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            if (itemDto.getAvailable().equals(false))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item not available");

            booking.setBooker(userId);
            booking.setStatus(StatusOfBooking.WAITING);
            bookingStorage.save(booking);

            BookingDto bookingDto = bookingCastomMapper.dtoFromBooking(booking);

            return bookingDto;
        } catch (EntityNotFoundException | IllegalAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or item not found", ex);
        }
    }

    @Override
    public BookingDto book(long userId, Long bookingId, Boolean approved) {
        Booking booking = checkExist(bookingId);
        try {
            ItemDtoWithBooking itemDto = itemService.getItemDto(booking.getItemId(), userId);

            Item item = itemRepository.getById(booking.getItemId());

            if (item.getOwner().getId().equals(userId)) {
                if (approved.equals(true) && booking.getStatus().equals(StatusOfBooking.APPROVED)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
                if (approved.equals(true)) {
                    booking.setStatus(StatusOfBooking.APPROVED);
                } else {
                    booking.setStatus(StatusOfBooking.REJECTED);
                }

                BookingDto bookingDto = bookingCastomMapper.dtoFromBooking(booking);

                bookingStorage.save(booking);
                return bookingDto;
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Incorrect ID of owner");

    }

    @Override
    public BookingDto get(Long bookingId, long userId) {
        Booking booking = checkExist(bookingId);
        Item item = itemRepository.getById(booking.getItemId());
        System.out.println(item);
        if (booking.getBooker().equals(userId) ||
                item.getOwner().getId().equals(userId)) {
            return bookingCastomMapper.dtoFromBooking(booking);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Incorrect ID");

    }

    @Override
    public List<BookingDto> getAllForUserByState(Long userId, String state) {
        State stateEnum = stringToEnum(state);
        return getBookings(userId, stateEnum);
    }

    @Override
    public List<BookingDto> getAllBookingForOwner(Long userId, String stringState) {
        User user = userMapper.userFromUserDto(userService.get(userId));
        List<Item> items = itemRepository.findAllByOwner(user);
        List<Long> userItemsId = new ArrayList<>();
        for (Item item : items) {
            if (item.getOwner().getId().equals(userId)) userItemsId.add(item.getId());
        }

        List<Booking> bookings = bookingStorage.findAll();
        State state = stringToEnum(stringState);

        List<Booking> bookingsForOwner = new ArrayList<>();

        if (userItemsId.size() > 0) {
            for (Booking booking : bookings) {
                if (userItemsId.contains(booking.getItemId())) bookingsForOwner.add(booking);
            }
            switch (state) {
                case ALL:
                    bookingsForOwner = bookingsForOwner;
                    break;
                case CURRENT:
                    bookingsForOwner = bookingsForOwner
                            .stream()
                            .filter(o -> o.getStart().isBefore(LocalDateTime.now()))
                            .filter(o -> o.getEnd().isAfter(LocalDateTime.now()))
                            .collect(Collectors.toList());
                    break;
                case PAST:
                    bookingsForOwner = bookingsForOwner
                            .stream()
                            .filter(o -> o.getEnd().isBefore(LocalDateTime.now()))
                            .collect(Collectors.toList());
                    break;
                case FUTURE:
                    bookingsForOwner = bookingsForOwner
                            .stream()
                            .filter(o -> o.getStart().isAfter(LocalDateTime.now()))
                            .collect(Collectors.toList());
                    break;
                case WAITING:
                    bookingsForOwner = bookingsForOwner
                            .stream()
                            .filter(o -> o.getStatus().equals(StatusOfBooking.WAITING))
                            .collect(Collectors.toList());
                    break;
                case REJECTED:
                    bookingsForOwner = bookingsForOwner
                            .stream()
                            .filter(o -> o.getStatus().equals(StatusOfBooking.REJECTED))
                            .collect(Collectors.toList());
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not have items");
        }
        return bookingsForOwner.stream()
                .map(bookingCastomMapper::dtoFromBooking)
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .collect(Collectors.toList());
    }

    private List<BookingDto> getBookings(Long userId, State state) {
        Long userIdValue = userService.get(userId).getId();
        List<Booking> bookings;

        switch (state) {
            case ALL:
                bookings = bookingStorage.findAllByBookerOrderByEndDesc(userIdValue);
                break;
            case CURRENT:
                bookings = bookingStorage.getCurrentBookingsForBooker(userIdValue);
                break;
            case PAST:
                bookings = bookingStorage.getPastBookingsForBooker(userIdValue);
                break;
            case FUTURE:
                bookings = bookingStorage.getFutureBookingsForBooker(userIdValue);
                break;
            case WAITING:
                bookings = bookingStorage.findBookingByBookerAndStatusOrderByEndDesc(userIdValue, StatusOfBooking.WAITING);
                break;
            case REJECTED:
                bookings = bookingStorage.findBookingByBookerAndStatusOrderByEndDesc(userIdValue, StatusOfBooking.REJECTED);
                break;
            default:
                bookings = new ArrayList<>();
        }

        return bookings.stream()
                .map(bookingCastomMapper::dtoFromBooking)
                .collect(Collectors.toList());
    }


    private Booking checkExist(Long bookingId) {
        Optional<Booking> booking = bookingStorage.findById(bookingId);
        if (booking.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Incorrect ID");
        return booking.get();
    }

    private State stringToEnum(String string) {
        State stateEnum;
        if (string.contains("PAST")) {
            stateEnum = State.PAST;
        } else {
            try {
                stateEnum = State.valueOf(string);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
            }
        }
        return stateEnum;
    }
}
