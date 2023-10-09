package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryJpaTest {

    private final UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void init() {
        user1 = User.builder()
                .id(1L)
                .name("testUser1")
                .email("test1@mail.ru")
                .build();
        user2 = User.builder()
                .id(2L)
                .name("testUser2")
                .email("test2@mail.ru")
                .build();
    }

    @Test
    void testCreateAndFindById() {
        userRepository.save(user1);
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %x not found!", 1L)));
        assertEquals(user1, user);
    }

    @Test
    void testUpdate() {
        userRepository.save(user1);
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %x not found!", 1L)));
        assertEquals(user1, user);
        user1.setName("new");
        user1.setEmail("new@mail.ru");
        userRepository.save(user1);
        user = userRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %x not found!", 1L)));
        assertEquals(user1, user);
    }

    @Test
    void testFindAll() {
        userRepository.save(user1);
        userRepository.save(user2);
        List<User> users = userRepository.findAll();
        List<User> users1 = List.of(user1, user2);
        assertEquals(users.size(), users1.size());
    }

    @Test
    void testDelete() {
        userRepository.save(user1);
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %x not found!", 1L)));
        assertEquals(user1, user);
        userRepository.delete(user1);
        Optional<User> opt = userRepository.findById(1L);
        assertTrue(opt.isEmpty());
    }

}
