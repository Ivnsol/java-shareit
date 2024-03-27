package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @Mock
    private UserDto userDto;

    @Mock
    private User user;

    @Mock
    private UserMapper userMapper;

    @Test
    void testUserFromUserDto() {
        when(userMapper.userFromUserDto(userDto)).thenReturn(user);
        User resultUser = userMapper.userFromUserDto(userDto);
        assertEquals(user, resultUser);
        verify(userMapper, times(1)).userFromUserDto(userDto);
    }

    @Test
    void testUserDtoFromUser() {
        when(userMapper.userDtoFromUser(user)).thenReturn(userDto);
        UserDto resultUserDto = userMapper.userDtoFromUser(user);
        assertEquals(userDto, resultUserDto);
        verify(userMapper, times(1)).userDtoFromUser(user);
    }
}
