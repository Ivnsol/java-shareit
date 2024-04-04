package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.enums.StatusOfBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.model.commentDto.CommentDto;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingStorage bookingStorage;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public List<ItemDtoWithBooking> getItems(Long userId) {
        User user = userRepository.getById(userId);
        List<Item> items = itemRepository.findAllByOwner(user);

        LocalDateTime localDateTime = LocalDateTime.now();


        List<ItemDtoWithBooking> itemDtoWithBookings = new ArrayList<>();
        for (Item item : items) {
            List<CommentDto> comments = getCommentsByItemId(item).stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
            Booking lastBooking = bookingStorage.findFirstByItemIdAndStartBeforeAndStatusIsNotOrderByEndDesc(
                    item.getId(), localDateTime, StatusOfBooking.REJECTED);
            Booking nextBooking = bookingStorage.findFirstByItemIdAndStartAfterAndStatusIsNotOrderByEndAsc(
                    item.getId(), localDateTime, StatusOfBooking.REJECTED);
            itemDtoWithBookings.add(new ItemDtoWithBooking(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), getBookingForItem(lastBooking), getBookingForItem(nextBooking), comments));
        }

        Collections.sort(itemDtoWithBookings, Comparator.comparing(ItemDtoWithBooking::getId));

        return itemDtoWithBookings;
    }

    @Override
    public ItemDto addNewItem(Long userId, ItemDto itemDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found");

        ItemRequest request = null;
        if (itemDto.getRequestId() != null) {
            request = itemRequestRepository.findById(itemDto.getRequestId()).orElseThrow(() ->
                    new EntityNotFoundException(String.format("Запрос с id = %d не найден!", itemDto.getRequestId())));
        }

        Item item = itemMapper.itemFromItemDto(itemDto);
        item.setOwner(userRepository.getById(userId));
        item.setRequest(request);
        itemRepository.save(item);
        itemDto.setId(item.getId());
        return itemDto;
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) throws IllegalAccessException {
        Item item = itemRepository.getById(itemId);
        if (item.getOwner().getId().equals(userId)) {
            itemRepository.deleteById(itemId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Incorrect user ID or item is not exist");
        }
    }

    @Override
    public ItemDto update(long userId, long itemId, ItemDto itemDto) {
        Item item = itemRepository.getById(itemId);
        if (item.getOwner().getId().equals(userId)) {
            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
            return itemMapper.itemDtoFromItem(itemRepository.save(item));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Incorrect user ID or item is not exist");
        }
    }

    @Override
    public ItemDtoWithBooking getItemDto(Long itemId, long userId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        ItemDtoWithBooking itemDto;

        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();

            List<CommentDto> comments = getCommentsByItemId(item)
                    .stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());

            if (item.getOwner().getId().equals(userId)) {
                LocalDateTime localDateTime = LocalDateTime.now();

                Booking lastBooking = bookingStorage.findFirstByItemIdAndStartBeforeAndStatusIsNotOrderByEndDesc(
                        item.getId(), localDateTime, StatusOfBooking.REJECTED);
                Booking nextBooking = bookingStorage.findFirstByItemIdAndStartAfterAndStatusIsNotOrderByEndAsc(
                        item.getId(), localDateTime, StatusOfBooking.REJECTED);

                itemDto = new ItemDtoWithBooking(itemId,
                        item.getName(), item.getDescription(),
                        item.getAvailable(), getBookingForItem(lastBooking),
                        getBookingForItem(nextBooking), comments);
            } else {
                itemDto = new ItemDtoWithBooking(itemId, item.getName(),
                        item.getDescription(), item.getAvailable(),
                        null, null, comments);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        }

        return itemDto;
    }

    private ItemDtoWithBooking.Booking getBookingForItem(Booking booking) {
        if (booking == null) return null;
        return new ItemDtoWithBooking.Booking(booking.getId(), booking.getBooker());
    }

    @Override
    public List<ItemDto> getItemByString(long userId, String string) {
        if (string.isEmpty()) return new ArrayList<>();
        return itemRepository.findByNameOrDescriptionContainingIgnoreCase(string.toLowerCase()).stream().map(itemMapper::itemDtoFromItem).collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        if (commentDto.getText().isEmpty() || commentDto.getText().isBlank()) {
            throw new ValidationException("This comment is empty or blank");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new ValidationException(String.format("User with id %d is not found", userId)));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ValidationException(String.format("Item with id %d is not found", itemId)));
        Comment comment = CommentMapper.toComment(commentDto, item, user);
        List<Booking> booking = bookingStorage.getByBookerIdStatePast(comment.getUser().getId(), LocalDateTime.now());
        if (booking.isEmpty()) {
            throw new ValidationException("The user has not booked anything");
        }
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    private List<Comment> getCommentsByItemId(Item item) {
        return commentRepository.getByItem_IdOrderByCreatedDesc(item.getId());
    }
}
