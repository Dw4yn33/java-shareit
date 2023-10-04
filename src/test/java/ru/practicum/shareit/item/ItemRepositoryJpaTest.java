package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRepositoryJpaTest {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;

    private User user1;
    private User user2;
    private Item item1;
    private Item item2;
    private Item item3;
    private Item item4;
    private ItemRequest itemRequest;
    private ItemRequest itemRequest1;
    private final Pageable pageRequest = PageRequest.of(0, 10, Sort.by("id"));

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
                .requestId(1L)
                .build();
        item4 = Item.builder()
                .id(4L)
                .name("test4")
                .description("test4")
                .available(true)
                .owner(user1)
                .requestId(2L)
                .build();
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("test3")
                .requestor(user2)
                .build();
        itemRequest1 = ItemRequest.builder()
                .id(2L)
                .description("test4")
                .requestor(user2)
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        itemRequestRepository.save(itemRequest);
        itemRequestRepository.save(itemRequest1);
    }

    @Test
    void testCreateAndFindById() {
        itemRepository.save(item1);
        Item test = itemRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %x not found!", 1L)));
        assertEquals(item1, test);
    }

    @Test
    void testUpdate() {
        itemRepository.save(item1);
        Item test = itemRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %x not found!", 1L)));
        assertEquals(item1, test);
        item1.setName("new");
        item1.setDescription("new");
        itemRepository.save(item1);
        test = itemRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %x not found!", 1L)));
        assertEquals(item1, test);
    }

    @Test
    void testFindAll() {
        itemRepository.save(item1);
        itemRepository.save(item2);
        List<Item> test = List.of(item1, item2);
        List<Item> items = itemRepository.findAll();
        assertEquals(test.size(), items.size());
    }

    @Test
    void testDelete() {
        itemRepository.save(item1);
        Item test = itemRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %x not found!", 1L)));
        assertEquals(item1, test);
        itemRepository.delete(item1);
        Optional<Item> opt = itemRepository.findById(1L);
        assertTrue(opt.isEmpty());
    }

    @Test
    void testFindByOwner() {
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        List<Item> test = itemRepository.findByOwnerId(1L, pageRequest);
        assertEquals(3, test.size());
    }

    @Test
    void testSearch() {
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);
        List<Item> test = itemRepository.search("test", pageRequest);
        assertEquals(4, test.size());
    }
    @Test
    void testFindByRequestIdIn() {
        itemRepository.save(item3);
        itemRepository.save(item4);
        List<Item> test = itemRepository.findByRequestIdIn(List.of(itemRequest.getId(), itemRequest1.getId()));
        assertEquals(test.size(), 2);
    }

    @Test
    void testFindByRequestId() {
        itemRepository.save(item3);
        List<Item> test = itemRepository.findByRequestId(itemRequest.getId());
        assertEquals(test.size(), 1);
    }
}
