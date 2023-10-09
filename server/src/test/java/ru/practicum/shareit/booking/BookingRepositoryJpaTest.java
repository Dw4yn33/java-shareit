package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingRepositoryJpaTest {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private User user1;
    private Item item1;
    private Item item2;
    private Booking booking1;
    private Booking booking2;
    private Booking booking3;
    private Booking booking4;
    private Booking booking5;
    private Booking booking6;
    private final LocalDateTime start = LocalDateTime.now();
    private final LocalDateTime end = start.plusMinutes(2);
    private final Pageable pageRequest = PageRequest.of(0, 10, Sort.by("startDate").descending());

    @BeforeEach
    void init() {
        user1 = User.builder()
                .id(1L)
                .name("testUser")
                .email("test@mail.ru")
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
        booking1 = Booking.builder()
                .id(1L)
                .startDate(start)
                .endDate(end)
                .item(item1)
                .booker(user1)
                .status(Status.WAITING)
                .build();
        booking2 = Booking.builder()
                .id(2L)
                .startDate(start.plusMinutes(2))
                .endDate(end.plusMinutes(2))
                .item(item1)
                .booker(user1)
                .status(Status.WAITING)
                .build();
        booking3 = Booking.builder()
                .id(3L)
                .startDate(start.plusMinutes(1))
                .endDate(end.plusMinutes(1))
                .item(item1)
                .booker(user1)
                .status(Status.REJECTED)
                .build();
        booking4 = Booking.builder()
                .id(4L)
                .startDate(start.plusMinutes(6))
                .endDate(end.plusMinutes(6))
                .item(item2)
                .booker(user1)
                .status(Status.REJECTED)
                .build();
        booking5 = Booking.builder()
                .id(5L)
                .startDate(start.plusMinutes(1))
                .endDate(end.plusMinutes(1))
                .item(item1)
                .booker(user1)
                .status(Status.APPROVED)
                .build();
        booking6 = Booking.builder()
                .id(6L)
                .startDate(start.plusMinutes(2))
                .endDate(end.plusMinutes(2))
                .item(item1)
                .booker(user1)
                .status(Status.APPROVED)
                .build();
        userRepository.save(user1);
        itemRepository.save(item1);
        itemRepository.save(item2);
    }

    @Test
    void testCreateAndFindById() {
        bookingRepository.save(booking1);
        Booking test = bookingRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with id %x not found!", 1L)));
        assertEquals(booking1, test);
        assertEquals(booking1.hashCode(), test.hashCode());
    }

    @Test
    void testUpdate() {
        bookingRepository.save(booking1);
        Booking test = bookingRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with id %x not found!", 1L)));
        assertEquals(booking1, test);
        booking1.setStatus(Status.APPROVED);
        bookingRepository.save(booking1);
        test = bookingRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with id %x not found!", 1L)));
        assertEquals(booking1, test);
    }

    @Test
    void testFindAll() {
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        List<Booking> test = List.of(booking1, booking2);
        List<Booking> bookings = bookingRepository.findAll();
        assertEquals(test.size(), bookings.size());
    }

    @Test
    void testDelete() {
        bookingRepository.save(booking1);
        Booking test = bookingRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with id %x not found!", 1L)));
        assertEquals(booking1, test);
        bookingRepository.delete(booking1);
        Optional<Booking> opt = bookingRepository.findById(1L);
        assertTrue(opt.isEmpty());
    }

    @Test
    void testFindAllForBooker() {
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        List<Booking> bookings = bookingRepository.findAllForBooker(1L, pageRequest);
        assertEquals(2, bookings.size());
    }

    @Test
    void testFindPastBookingsForBooker() {
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        List<Booking> bookings = bookingRepository.findPastBookingsForBooker(1L,
                LocalDateTime.now().plusMinutes(5), pageRequest);
        assertEquals(2, bookings.size());
    }

    @Test
    void testFindCurrentBookingsForBooker() {
        bookingRepository.save(booking1);
        bookingRepository.save(booking3);
        List<Booking> bookings = bookingRepository.findCurrentBookingsForBooker(1L,
                LocalDateTime.now().plusMinutes(1).plusSeconds(20), pageRequest);
        assertEquals(2, bookings.size());
    }

    @Test
    void testFindFutureBookingsForBooker() {
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        List<Booking> bookings = bookingRepository.findFutureBookingsForBooker(1L,
                LocalDateTime.now(), pageRequest);
        assertEquals(2, bookings.size());
    }

    @Test
    void testFindWaitingBookingsForBooker() {
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
        List<Booking> bookings = bookingRepository.findWaitingBookingsForBooker(1L, pageRequest);
        assertEquals(2, bookings.size());
    }

    @Test
    void testFindRejectedBookingsForBooker() {
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
        List<Booking> bookings = bookingRepository.findRejectedBookingsForBooker(1L, pageRequest);
        assertEquals(2, bookings.size());
    }

    @Test
    void testFindAllForItems() {
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
        List<Booking> bookings1 = bookingRepository.findAllForItems(List.of(1L), pageRequest);
        List<Booking> bookings2 = bookingRepository.findAllForItems(List.of(1L,2L), pageRequest);
        assertEquals(3, bookings1.size());
        assertEquals(4, bookings2.size());
    }

    @Test
    void testFindPastBookingsForItems() {
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        List<Booking> bookings1 = bookingRepository.findPastBookingsForItems(List.of(1L),
                LocalDateTime.now().plusMinutes(10), pageRequest);
        assertEquals(3, bookings1.size());
    }

    @Test
    void testFindCurrentBookingsForItems() {
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        List<Booking> bookings1 = bookingRepository.findCurrentBookingsForItems(List.of(1L),
                LocalDateTime.now().plusMinutes(1).plusSeconds(20), pageRequest);
        assertEquals(2, bookings1.size());
    }

    @Test
    void testFindFutureBookingsForItems() {
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        List<Booking> bookings1 = bookingRepository.findFutureBookingsForItems(List.of(1L),
                LocalDateTime.now(), pageRequest);
        assertEquals(2, bookings1.size());
    }

    @Test
    void testFindWaitingBookingsForItems() {
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
        List<Booking> bookings1 = bookingRepository.findWaitingBookingsForItems(List.of(1L), pageRequest);
        List<Booking> bookings2 = bookingRepository.findWaitingBookingsForItems(List.of(1L,2L), pageRequest);
        assertEquals(2, bookings1.size());
        assertEquals(2, bookings2.size());
    }

    @Test
    void testFindRejectedBookingsForItems() {
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
        List<Booking> bookings1 = bookingRepository.findRejectedBookingsForItems(List.of(1L), pageRequest);
        List<Booking> bookings2 = bookingRepository.findRejectedBookingsForItems(List.of(1L,2L), pageRequest);
        assertEquals(1, bookings1.size());
        assertEquals(2, bookings2.size());
    }

    @Test
    void testFindLastBookingForItem() {
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
        bookingRepository.save(booking5);
        bookingRepository.save(booking6);
        List<Booking> bookings1 = bookingRepository.findLastBookingForItem(1L,
                LocalDateTime.now().plusMinutes(10));
        assertEquals(booking6, bookings1.get(0));
    }

    @Test
    void testFindNextBookingForItem() {
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
        bookingRepository.save(booking5);
        bookingRepository.save(booking6);
        List<Booking> bookings1 = bookingRepository.findNextBookingForItem(1L,
                LocalDateTime.now());
        assertEquals(booking5, bookings1.get(0));
    }

    @Test
    void testFindBookingsForAddComments() {
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        List<Booking> bookings1 = bookingRepository.findBookingsForAddComments(1L, 1L,
                LocalDateTime.now().plusMinutes(5));
        assertEquals(2, bookings1.size());
    }
}
