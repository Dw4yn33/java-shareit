package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {


    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader(value = "X-Sharer-User-Id") Long id, @RequestBody ItemDto item) {
        log.info("Получен запрос на добавление предмета");
        return itemService.create(id, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                          @PathVariable("itemId") Long itemId, @RequestBody ItemDto item) {
        log.info("Получен запрос на обновление информации о предмете");
        return itemService.update(ownerId, itemId, item);
    }

    @GetMapping
    public List<ItemDto> getItemsOfOwner(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        log.info("Получен запрос на получение информации о предметах пользователя с идентификатором " + ownerId);
        return itemService.getItemsOfOwner(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable("itemId") Long itemId) {
        log.info("Получен запрос на получение информации о предмете идентификатором " + itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsFromSearch(@RequestParam String text) {
        log.info("Получен запрос на получение информации о предметах, содержащих в себе информацию: " + text);
        return itemService.getItemsFromSearch(text);
    }
}
