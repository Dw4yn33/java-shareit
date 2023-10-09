package ru.practicum.shareit.exeption;

public class BookingItemByOwnerException extends RuntimeException {

    public BookingItemByOwnerException(String message) {
        super(message);
    }

}
