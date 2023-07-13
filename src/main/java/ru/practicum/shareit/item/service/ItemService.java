package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
public interface ItemService {
    ItemDto getItemById(long userId, long itemId);

    List<ItemDto> getItemsByUserId(long userId);

    List<ItemDto> getItemsByText(String text);

    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    CommentDto addComment(long userId, long itemId, CommentDto commentDto);
}
