package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User get(Long userId) {
        return repository.get(userId);
    }

    @Override
    public User saveUser(User user) {
        return repository.save(user);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public User update(Long id, User user) {
        return repository.update(id, user);
    }
}