package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto create(Long id, Item item);

    ItemDto update(Long ownerId, Long itemId, Item item);

    ItemDto getItemById(Long id);

    List<ItemDto> getItemsOfOwner(Long id);

    List<ItemDto> getItemsFromSearch(String search);
}