package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    User user = new User(
            1L,
            "userName",
            "user@email.com"
    );

    @MockBean
    UserRepository userRepository;
    UserService userService;
    UserMapper userMapper;

    @BeforeEach
    void beforeEach() {
        userMapper = new UserMapperImpl();
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    void getAll() {
        when(userRepository.findAll())
                .thenReturn(Collections.singletonList(user));
        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertThat(users, equalTo(List.of(user)));
    }

    @Test
    void saveUser() {
        when(userRepository.save(any()))
                .thenReturn(user);
        User user = userService.saveUser(new User(1L, "user Name", "user@email.ru"));

        assertThat(user.getId(), equalTo(user.getId()));
        assertThat(user.getName(), equalTo(user.getName()));
        assertThat(user.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void update() {
        when(userRepository.getById(anyLong())).thenReturn(user);

        when(userRepository.save(user)).thenReturn(user);

        userService.saveUser(user);

        User updatedUser = userService.update(user.getId(), user);

        assertNotNull(updatedUser);

        assertThat(updatedUser.getId(), equalTo(user.getId()));
        assertThat(updatedUser.getName(), equalTo(user.getName()));
        assertThat(updatedUser.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void getById() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        userService.saveUser(user);
        UserDto userDto = userService.get(1L);

        assertThat(userDto.getId(), equalTo(user.getId()));
        assertThat(userDto.getName(), equalTo(user.getName()));
        assertThat(userDto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void getByIdWrongId() {
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(IllegalArgumentException.class, () -> userService.delete(user.getId()));
    }

}