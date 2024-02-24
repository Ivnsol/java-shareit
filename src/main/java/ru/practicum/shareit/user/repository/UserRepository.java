package ru.practicum.shareit.user.repository;

import lombok.SneakyThrows;
import org.springframework.web.client.HttpClientErrorException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User get(Long userId);

    User save(User user) throws HttpClientErrorException;

    @SneakyThrows
    void delete(Long id);

    @SneakyThrows
    User update(Long id, User updateUser);
}