package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.AllBookingsAsList;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.StatusOfBooking;
import ru.practicum.shareit.booking.mapper.BookingCastomMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.booking.repository.BookingStorageForList;
import ru.practicum.shareit.item.dto.ItemDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserMapper userMapper;
    private final BookingStorage bookingStorage;
    private final BookingStorageForList bookingStorageForList;
    private final BookingCastomMapper bookingCastomMapper;
    private final UserService userService;
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto create(long userId, Booking booking) {
        try {
            if (!booking.validateDates()) {
                throw new IllegalStateException();
            }
            UserDto userDto = userService.get(userId);

            Item item = itemRepository.getById(booking.getItemId());
            ItemDto itemDto = itemMapper.itemDtoFromItem(item);

            if (itemRepository.findById(booking.getItemId()).get().getOwner().getId().equals(userId))
                throw new EntityNotFoundException();

            if (itemDto.getAvailable().equals(false))
                throw new IllegalStateException();

            booking.setBooker(userDto.getId());
            booking.setStatus(StatusOfBooking.WAITING);
            bookingStorage.save(booking);

            BookingDto bookingDto = bookingCastomMapper.dtoFromBooking(booking,itemDto, userDto);

            return bookingDto;
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public BookingDto book(long userId, Long bookingId, Boolean approved) {
        Booking booking = checkExist(bookingId);
        Item item = itemRepository.getById(booking.getItemId());

        UserDto owner = userMapper.userDtoFromUser(userRepository.getById(userId));
        UserDto booker = userMapper.userDtoFromUser(userRepository.getById(booking.getBooker()));

        if (item.getOwner().getId().equals(userId)) {
            if (approved.equals(true) && booking.getStatus().equals(StatusOfBooking.APPROVED)) {
                throw new IllegalStateException();
            }
            if (approved.equals(true)) {
                booking.setStatus(StatusOfBooking.APPROVED);
            } else {
                booking.setStatus(StatusOfBooking.REJECTED);
            }

            BookingDto bookingDto = bookingCastomMapper.dtoFromBooking(booking, itemMapper.itemDtoFromItem(item), booker);

            bookingStorage.save(booking);
            return bookingDto;
        }

        throw new EntityNotFoundException();

    }

    @Override
    public BookingDto get(Long bookingId, long userId) {
        Booking booking = checkExist(bookingId);
        Item item = itemRepository.getById(booking.getItemId());
        ItemDto itemDto = itemMapper.itemDtoFromItem(itemRepository.getById(booking.getItemId()));
        UserDto userDto = userService.get(booking.getBooker());

        if (booking.getBooker().equals(userId) || item.getOwner().getId().equals(userId)) {
            return bookingCastomMapper.dtoFromBooking(booking, itemDto, userDto);
        }

        throw new EntityNotFoundException();

    }

    @Override
    public List<AllBookingsAsList> getAllForUserByState(Long userId, String state) {
        State stateEnum = stringToEnum(state);
        return getBookings(userId, stateEnum);
    }

    @Override
    public List<AllBookingsAsList> getAllBookingForOwner(Long userId, String stringState) {
        User user = userMapper.userFromUserDto(userService.get(userId));
        List<Long> userItemsId = itemRepository.findAllByOwner(user).stream().map(Item::getId).collect(Collectors.toList());

        State state = stringToEnum(stringState);

        List<AllBookingsAsList> bookingsForOwner;

        if (userItemsId.size() > 0) {
            switch (state) {
                case ALL:
                    bookingsForOwner = bookingStorageForList.getAllForOwner(userItemsId);
                    break;
                case CURRENT:
                    bookingsForOwner = bookingStorageForList.getCurrentBookingsForOwner(userItemsId);
                    break;
                case PAST:
                    bookingsForOwner = bookingStorageForList.getPastBookingsForOwner(userItemsId);
                    break;
                case FUTURE:
                    bookingsForOwner = bookingStorageForList.getFutureBookingsForOwner(userItemsId);
                    break;
                case WAITING:
                    bookingsForOwner = bookingStorageForList.findBookingByOwnerAndStatusOrderByEndDesc(userItemsId, StatusOfBooking.WAITING);
                    break;
                case REJECTED:
                    bookingsForOwner = bookingStorageForList.findBookingByOwnerAndStatusOrderByEndDesc(userItemsId, StatusOfBooking.REJECTED);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalStateException("User does not have items");
        }

        return bookingsForOwner;
    }

    private List<AllBookingsAsList> getBookings(Long userId, State state) {
        Long userIdValue = userService.get(userId).getId();
        List<AllBookingsAsList> bookings;

        switch (state) {
            case ALL:
                bookings = bookingStorageForList.findAllByBookerOrderByEndDesc(userIdValue);
                break;
            case CURRENT:
                bookings = bookingStorageForList.getCurrentBookingsForBooker(userIdValue);
                break;
            case PAST:
                bookings = bookingStorageForList.getPastBookingsForBooker(userIdValue);
                break;
            case FUTURE:
                bookings = bookingStorageForList.getFutureBookingsForBooker(userIdValue);
                break;
            case WAITING:
                bookings = bookingStorageForList.findBookingByBookerAndStatusOrderByEndDesc(userIdValue, StatusOfBooking.WAITING);
                break;
            case REJECTED:
                bookings = bookingStorageForList.findBookingByBookerAndStatusOrderByEndDesc(userIdValue, StatusOfBooking.REJECTED);
                break;
            default:
                bookings = new ArrayList<>();
        }

        return bookings;
    }


    private Booking checkExist(Long bookingId) {
        Optional<Booking> booking = bookingStorage.findById(bookingId);
        if (booking.isEmpty()) throw new EntityNotFoundException("Incorrect ID");
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
