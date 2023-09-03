package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {

    @NotNull
    private final Long id;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    private User requester;
    @NotNull
    private LocalDateTime created;
}
