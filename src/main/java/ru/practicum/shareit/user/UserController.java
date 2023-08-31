package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping
    public UserDto create(@Valid @RequestBody User user) {
        log.info("Получен запрос на добавление пользователя");
        return userService.create(user);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable("userId") Long userId, @Valid @RequestBody User user) {
        log.info("Получен запрос на обновление информации о пользователе");
        return userService.update(userId, user);
    }

    @GetMapping
    public List<UserDto> getUsers() {
        log.info("Получен запрос на получение всех пользователей");
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable("userId") long userId) {
        log.info("Получен запрос на получение пользователеля с идентификатором " + userId);
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public UserDto deleteUserById(@PathVariable("userId") long userId) {
        log.info("Получен запрос на получение пользователеля с идентификатором " + userId);
        return userService.deleteUser(userId);
    }
}
