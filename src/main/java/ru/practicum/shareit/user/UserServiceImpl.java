package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserDto create(UserDto user) {
        return userRepository.create(user);
    }

    public UserDto update(Long id, UserDto user) {
        return userRepository.update(id, user);
    }

    public UserDto getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    public List<UserDto> getUsers() {
        return userRepository.getUsers();
    }

    public UserDto deleteUser(Long id) {
        return userRepository.deleteUserById(id);
    }
}
