package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDto get(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.map(userMapper::userDtoFromUser)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) throws IllegalArgumentException {
        Optional<User> user = Optional.ofNullable(userRepository.getById(id));
        if (!user.isEmpty()) {
            userRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public User update(Long id, User updateUser) throws IllegalArgumentException {
        Optional<User> user = Optional.ofNullable(userRepository.getById(id));
        if (!user.isEmpty()) {
            if (updateUser.getEmail() != null) user.get().setEmail(updateUser.getEmail());
            if (updateUser.getName() != null) user.get().setName(updateUser.getName());
            return userRepository.save(user.get());
        } else {
            throw new IllegalArgumentException();
        }
    }
}