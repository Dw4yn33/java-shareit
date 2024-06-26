package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemServiceImplTest {

    private final EntityManager em;

    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    private UserDto userDto;
    private ItemDto itemDto;
    private Comment comment;
    private CommentRequestDto commentRequestDto;
    private final Pageable pageRequest = PageRequest.of(0, 10, Sort.by("id"));

    @BeforeEach
    void init() {
        userDto = UserDto.builder()
                .id(1L)
                .name("test")
                .email("test@email.ru")
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("test")
                .description("test")
                .available(true)
                .build();

        commentRequestDto = CommentRequestDto.builder()
                .text("test")
                .build();

        comment = Comment.builder()
                .id(1L)
                .text("test")
                .item(Item.builder()
                        .id(1L)
                        .name("test")
                        .description("test")
                        .available(true)
                        .build())
                .author(User.builder()
                        .id(1L)
                        .name("test")
                        .email("test@email.ru")
                        .build())
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void testFindByUserId() {
        userService.create(userDto);
        itemService.create(userDto.getId(), itemDto);

        List<ItemDto> items = itemService.findByUserId(1L, pageRequest);

        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getName(), "test");
        assertEquals(items.get(0).getDescription(), "test");
    }

    @Test
    void testFindById() {
        userService.create(userDto);
        itemService.create(userDto.getId(), itemDto);

        ItemDto item = itemService.findById(1L, 1L);

        assertEquals(item.getName(), "test");
        assertEquals(item.getDescription(), "test");
    }

    @Test
    void testFindByIdNotFoundError() {
        userService.create(userDto);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                itemService.findById(1L, 1L));

        assertEquals(exception.getMessage(), String.format("Item with id %x not found!", 1L));
    }

    @Test
    void testSearch() {
        userService.create(userDto);
        itemService.create(userDto.getId(), itemDto);

        List<ItemDto> items = itemService.search("test", pageRequest);

        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getDescription(), "test");
    }

    @Test
    void testCreate() {
        userService.create(userDto);
        itemService.create(userDto.getId(), itemDto);

        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.name = :name", Item.class);
        Item item = query.setParameter("name", itemDto.getName()).getSingleResult();

        assertEquals(item.getId(), 1L);
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
    }

    @Test
    void testCreateUserNotFoundError() {
        userService.create(userDto);
        itemService.create(userDto.getId(), itemDto);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                itemService.create(5L, itemDto));

        assertEquals(exception.getMessage(), String.format("User with id %x not found!", 5L));
    }

    @Test
    void testUpdate() {
        userService.create(userDto);
        itemService.create(userDto.getId(), itemDto);

        itemDto.setName("new");
        itemDto.setDescription("new");

        itemService.update(1L, 1L, itemDto);

        assertEquals("new", itemDto.getName());
        assertEquals("new", itemDto.getDescription());
    }

    @Test
    void testUpdateUserNotFoundError() {
        userService.create(userDto);
        itemService.create(userDto.getId(), itemDto);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                itemService.update(5L, 1L, itemDto));

        assertEquals(exception.getMessage(), String.format("User with id %x not found!", 5L));
    }

    @Test
    void testCreateComment() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusNanos(1);

        User user = User.builder()
                .id(1L)
                .name("1")
                .email("1@email.ru")
                .build();
        User user1 = User.builder()
                .id(2L)
                .name("2")
                .email("2@email.ru")
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("test")
                .description("test")
                .available(true)
                .owner(user)
                .requestId(1L)
                .build();
        Booking booking = Booking.builder()
                .id(1L)
                .startDate(startDate)
                .endDate(endDate)
                .status(Status.APPROVED)
                .booker(user1)
                .item(item)
                .build();
        UserDto userDto1 = userService.create(UserMapper.toDto(user));
        UserDto userDto2 = userService.create(UserMapper.toDto(user1));
        ItemDto itemDto1 = itemService.create(userDto.getId(), ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable()).build());
        BookingDto bookingDto = bookingService.create(userDto2.getId(), BookingRequestDto.builder()
                        .id(booking.getId())
                        .start(booking.getStartDate())
                        .end(booking.getEndDate())
                        .itemId(booking.getItem().getId()).build());
        CommentDto commentDto = itemService.createComment(userDto2.getId(), itemDto1.getId(), commentRequestDto);
        assertNotNull(userDto1);
        assertNotNull(userDto2);
        assertNotNull(itemDto1);
        assertNotNull(bookingDto);
        assertNotNull(commentDto);
        assertEquals(userDto1.getId(), itemDto1.getOwner().getId());
        assertEquals(itemDto1.getId(), bookingDto.getItem().getId());
        assertEquals(userDto2.getId(), bookingDto.getBooker().getId());
        assertEquals(userDto2.getName(), commentDto.getAuthorName());
        assertTrue(itemService.findById(userDto2.getId(), itemDto1.getId()).getComments().contains(commentDto));
        assertEquals(commentRequestDto.getText(), commentDto.getText());
    }

}
