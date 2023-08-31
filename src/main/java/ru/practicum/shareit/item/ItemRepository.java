package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    private static final Map<Long, Item> items = new HashMap<>();
    private Long generatorId;

    private final UserRepository userRepository;

    public ItemRepository() {
        generatorId = 1L;
        userRepository = new UserRepository();
    }

    public ItemDto create(Long ownerId, Item item) {
        if (ownerId != null && ownerId > 0) {
            User user = userRepository.getUserForSettingOwner(ownerId);
            item.setOwner(user);
        } else throw new ValidationException("Предмет без владельца");
        createItemValidation(item);
        if (!items.containsKey(item.getId())) {
            items.put(item.getId(), item);
        } else throw new ValidationException("Предмет с таким идентификатором уже существует");
        return ItemMapper.toItemDto(item);
    }

    public ItemDto update(Long ownerId, Long itemId, Item item) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Предмета с идентификатором " + itemId + "не существует");
        }
        Item ended = items.get(itemId);
        if (!ownerId.equals(ended.getOwner().getId())) {
            throw new NotFoundException("Данный пользователь не является владельцем предмета");
        }
        if (item.getName() != null) ended.setName(item.getName());
        if (item.getDescription() != null) ended.setDescription(item.getDescription());
        if (item.getAvailable() != null) ended.setAvailable(item.getAvailable());
        items.put(ended.getId(), ended);
        return ItemMapper.toItemDto(ended);
    }

    public ItemDto getItemById(Long id) {
        if (!items.containsKey(id)) {
            throw new NotFoundException("Предмета с идентификатором " + id + "не существует");
        } else return ItemMapper.toItemDto(items.get(id));
    }

    public List<ItemDto> getItemsOfOwner(Long ownerId) {
        List<ItemDto> itemList = new ArrayList<>();
        for (Item item : items.values()) {
            if (ownerId.equals(item.getOwner().getId())) itemList.add(ItemMapper.toItemDto(item));
        }
        return itemList;
    }

    public List<ItemDto> getItemsFromSearch(String search) {
        List<ItemDto> itemList = new ArrayList<>();
        if (search == null || search.isEmpty()) return itemList;
        for (Item item : items.values()) {
            if (item.getAvailable() && (item.getName().toLowerCase().contains(search.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(search.toLowerCase()))) {
                itemList.add(ItemMapper.toItemDto(item));
            }
        }
        return itemList;
    }

    private void createItemValidation(Item item) {
        if (item.getName() == null || item.getName().isEmpty() || item.getDescription().isBlank()) {
            throw new ValidationException("Пустое или неправильно введенное название предмета");
        }
        if (item.getDescription() == null || item.getDescription().isEmpty() || item.getDescription().isBlank()) {
            throw new ValidationException("Пустое или неправильно введенное описание предмета");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Пустой статус доступности предмета");
        }
        if (item.getId() == null || item.getId() <= 0) item.setId(generatorId++);
    }
}
