package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable Long userId) {
        return userService.get(userId);
    }

    @PostMapping
    public User saveNewUser(@RequestBody @Valid User user) {
        return userService.saveUser(user);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) throws IllegalArgumentException {
        userService.delete(userId);
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable Long userId, @RequestBody User user) throws IllegalArgumentException {
        return userService.update(userId, user);
    }
}