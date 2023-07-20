package ru.practicum.shareit.validation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static java.lang.String.format;

@Component
@AllArgsConstructor
public class Valid {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public User checkUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(format("Пользователя с id = %s нет в базе", userId)));
    }

    public Item checkItem(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException(format("Вещи с id = %s нет в базе", itemId)));
    }

}
