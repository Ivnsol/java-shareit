package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    UserDto get(Long userId);

    User saveUser(User user);

    void delete(Long id) throws IllegalArgumentException;

    User update(Long id, User user) throws IllegalArgumentException;
}