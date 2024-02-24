package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User get(Long userId);

    User saveUser(User user);

    void delete(Long id);

    User update(Long id, User user);
}