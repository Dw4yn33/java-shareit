package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    UserDto create(UserDto user);

    UserDto update(Long id, UserDto user);

    UserDto getUserById(Long id);

    List<UserDto> getUsers();

    UserDto deleteUser(Long id);
}
