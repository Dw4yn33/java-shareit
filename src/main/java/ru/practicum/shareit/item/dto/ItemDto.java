package ru.practicum.shareit.item.dto;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {

    private Long id;
    //@NotBlank
    private String name;
    //@NotBlank
    private String description;
    //@NotNull
    private Boolean available;
    //private long requestId;

    public ItemDto(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
