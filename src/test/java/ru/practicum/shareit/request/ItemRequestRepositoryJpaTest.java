package ru.practicum.shareit.request;

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
public class ItemRequestRepositoryJpaTest {

    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private User user1;
    private User user2;
    private ItemRequest itemRequest;
    private ItemRequest itemRequest1;
    private final Pageable pageRequest = PageRequest.of(0, 20, Sort.by("created").descending());

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
    }

    @Test
    void testCreateAndFindById() {
        requestRepository.save(itemRequest);
        ItemRequest request = requestRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id %x not found!", 1L)));
        assertEquals(itemRequest, request);
    }

    @Test
    void testUpdate() {
        requestRepository.save(itemRequest);
        ItemRequest request = requestRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id %x not found!", 1L)));
        assertEquals(itemRequest, request);
        itemRequest.setDescription("new");
        requestRepository.save(itemRequest);
        request = requestRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id %x not found!", 1L)));
        assertEquals(itemRequest, request);
    }

    @Test
    void testFindAll() {
        requestRepository.save(itemRequest);
        requestRepository.save(itemRequest1);
        List<ItemRequest> test = List.of(itemRequest, itemRequest1);
        List<ItemRequest> requests = requestRepository.findAll();
        assertEquals(test.size(), requests.size());
    }

    @Test
    void testDelete() {
        requestRepository.save(itemRequest);
        ItemRequest request = requestRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id %x not found!", 1L)));
        assertEquals(itemRequest, request);
        requestRepository.delete(itemRequest);
        Optional<ItemRequest> opt = requestRepository.findById(1L);
        assertTrue(opt.isEmpty());
    }

    @Test
    void testFindItemRequestByRequestorId() {
        requestRepository.save(itemRequest);
        requestRepository.save(itemRequest1);
        List<ItemRequest> requests = requestRepository.findItemRequestByRequestorId(2L,
                Sort.by("created").descending());
        assertEquals(2, requests.size());
    }

    @Test
    void testFindAllByRequestorIdIsNot() {
        requestRepository.save(itemRequest);
        requestRepository.save(itemRequest1);
        List<ItemRequest> requests = requestRepository.findAllByRequestorIdIsNot(1L, pageRequest);
        List<ItemRequest> requests1 = requestRepository.findAllByRequestorIdIsNot(2L, pageRequest);
        assertEquals(2, requests.size());
        assertEquals(0, requests1.size());
    }
}
