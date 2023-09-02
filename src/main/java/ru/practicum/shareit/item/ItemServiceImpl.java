package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public ItemDto create(Long id, ItemDto item) {
        return itemRepository.create(id, item);
    }

    @Override
    public ItemDto update(Long ownerId, Long itemId, ItemDto item) {
        return itemRepository.update(ownerId, itemId, item);
    }

    @Override
    public ItemDto getItemById(Long id) {
        return itemRepository.getItemById(id);
    }

    @Override
    public List<ItemDto> getItemsOfOwner(Long id) {
        return itemRepository.getItemsOfOwner(id);
    }

    @Override
    public List<ItemDto> getItemsFromSearch(String search) {
        return itemRepository.getItemsFromSearch(search);
    }
}
