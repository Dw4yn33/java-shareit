package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    UserDto create(User user);

    UserDto update(Long id, User user);

    UserDto getUserById(Long id);

    List<UserDto> getUsers();

    UserDto deleteUser(Long id);
}
