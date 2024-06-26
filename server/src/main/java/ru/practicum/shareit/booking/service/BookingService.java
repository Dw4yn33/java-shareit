package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {

    List<BookingDto> getAllBookingsForUser(Long userId, State state, Pageable pageable);

    BookingDto getBooking(Long userId, Long bookingId);

    List<BookingDto> getAllItemsBookingForUser(Long userId, State state, Pageable pageable);

    BookingDto create(Long userId, BookingRequestDto bookingRequestDto);

    BookingDto updateStatus(Long userId, Long bookingId, Boolean approved);

}
