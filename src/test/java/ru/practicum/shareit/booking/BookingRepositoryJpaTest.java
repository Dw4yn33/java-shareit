package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@AutoConfigureTestDatabase
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingRepositoryJpaTest {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private User user1;
    private User user2;
    private Item item1;
    private Item item2;
    private Item item3;
    private Item item4;

    @BeforeEach
    void init() {
        user1 = User.builder()
                .id(1L)
                .name("testUser")
                .email("test@mail.ru")
                .build();
        user2 = User.builder()
                .id(2L)
                .name("test2User")
                .email("test2@mail.ru")
                .build();
        item1 = Item.builder()
                .id(1L)
                .name("test1")
                .description("test1")
                .available(true)
                .owner(user1)
                .build();
        item2 = Item.builder()
                .id(2L)
                .name("test2")
                .description("test2")
                .available(true)
                .owner(user1)
                .build();
        item3 = Item.builder()
                .id(3L)
                .name("test3")
                .description("test3")
                .available(true)
                .owner(user1)
                .build();
        item4 = Item.builder()
                .id(4L)
                .name("test4")
                .description("test4")
                .available(true)
                .owner(user1)
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);
    }
}
