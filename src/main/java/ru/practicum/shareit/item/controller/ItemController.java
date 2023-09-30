package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemService itemService;

    @GetMapping()
    public List<ItemDto> findByUserId(@RequestHeader(name = USER_ID_HEADER) Long userId) {
        return itemService.findByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@RequestHeader(name = USER_ID_HEADER) Long userId, @PathVariable Long itemId) {
        return itemService.findById(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text) {
        return itemService.search(text);
    }

    @PostMapping()
    public ItemDto create(@RequestHeader(name = USER_ID_HEADER) Long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(name = USER_ID_HEADER) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                    @PathVariable Long itemId,
                                    @Valid @RequestBody CommentRequestDto commentDto) {
        return itemService.createComment(userId, itemId, commentDto);
    }

}
