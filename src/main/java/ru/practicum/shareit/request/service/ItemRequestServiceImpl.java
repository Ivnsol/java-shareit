package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ItemRequestOutDto add(long userId, ItemRequestInDto requestInDto) {
        User requester = checkUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(requestInDto, requester);
        itemRequestRepository.save(itemRequest);
        System.out.println(itemRequestRepository.findAll());
        System.out.println(itemRequestRepository.getById(itemRequest.getId()));

        return ItemRequestMapper.toItemRequestOutDto(itemRequest, new ArrayList<>());
    }

    @Override
    public List<ItemRequestOutDto> getByOwner(long userId) {
        checkUserExist(userId);
        List<ItemRequest> requests = itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(userId);
        return toListRequestOutDto(requests);
    }

    @Override
    public List<ItemRequestOutDto> getAll(long userId, Pageable pageRequest) {
        checkUserExist(userId);
        List<ItemRequest> requests = itemRequestRepository.findAllByOtherUsers(userId, pageRequest);
        return toListRequestOutDto(requests);
    }

    @Override
    public ItemRequestOutDto getById(long userId, long requestId) {
        checkUserExist(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Запрос с id = %s не найден!", requestId)));
        return ItemRequestMapper.toItemRequestOutDto(itemRequest,
                itemRepository.findAllByRequestId(itemRequest.getId()));
    }


    private List<ItemRequestOutDto> toListRequestOutDto(List<ItemRequest> requests) {
        List<ItemRequestOutDto> requestsOut;
        List<Item> all = itemRepository.findAll();
        Map<Long, Item> allItemsAsMap = all.stream()
                .collect(Collectors.toMap(Item::getId, item -> item));

        requestsOut = requests.stream()
                .map(request -> ItemRequestMapper.toItemRequestOutDto(request,
                        itemRepository.findAllByRequestId(request.getId())))
                .collect(Collectors.toList());
        for (ItemRequest request : requests) {
            System.out.println(allItemsAsMap.get(request.getId()));

        }
        return requestsOut;
    }

    public User checkUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Пользователь с id = %s не найден!", userId)));
    }

    public void checkUserExist(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(String.format("Пользователь с id = %s не найден!", userId));
        }
    }
}