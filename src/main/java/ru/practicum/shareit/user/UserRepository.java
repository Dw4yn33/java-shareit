package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.EmailDuplicateException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    private Long generatorId;
    private static final Map<Long, User> userMap = new HashMap<>();

    public UserRepository() {
        generatorId = 1L;
    }

    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        userCreateValidation(user);
        if (!userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
        } else throw new ValidationException("Пользователь с таким идентификатором уже существует");
        return UserMapper.toUserDto(user);
    }

    public UserDto update(Long id, UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (userMap.containsKey(id)) {
            User ended = userMap.get(id);
            ended.setId(id);
            if (user.getName() != null) ended.setName(user.getName());
            if (user.getEmail() != null) {
                for (User test : userMap.values()) {
                    if (!id.equals(test.getId()) && user.getEmail().equals(test.getEmail())) {
                        throw new EmailDuplicateException("Два одинаковых email");
                    }
                }
                ended.setEmail(user.getEmail());
            }
            userMap.put(ended.getId(), ended);
            return UserMapper.toUserDto(ended);
        } else throw new NotFoundException("Пользователя с идентификатором" + user.getId() + "не существует" +
                ", нечего обновлять");
    }

    public UserDto getUserById(Long id) {
        if (!userMap.containsKey(id)) {
            throw new NotFoundException("Пользователя с идентификатором" + id + "не существует");
        } else return UserMapper.toUserDto(userMap.get(id));
    }

    public List<UserDto> getUsers() {
        List<UserDto> users = new ArrayList<>();
        for (User user : userMap.values()) {
            users.add(UserMapper.toUserDto(user));
        }
        return users;
    }

    public UserDto deleteUserById(Long id) {
        if (!userMap.containsKey(id)) {
            throw new NotFoundException("Пользователя с идентификатором" + id + "не существует");
        } else return UserMapper.toUserDto(userMap.remove(id));
    }

    private void userCreateValidation(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getEmail() == null) {
            throw new ValidationException("Ошибка валидации");
        }
        for (User test : userMap.values()) {
            if (user.getEmail().equals(test.getEmail())) throw new EmailDuplicateException("Два одинаковых email");
        }
        if (user.getId() == null) user.setId(generatorId++);
    }

    public static User getUserForSettingOwner(Long id) {
        if (userMap.containsKey(id)) {
            return userMap.get(id);
        } else throw new NotFoundException("Такого пользователя не существует");
    }
}
