package ru.practicum.shareit.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidTest {
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;
    @InjectMocks
    private Valid valid;

    private final User user = new User(1L, "Алексей", "alexey.timoshenko@mail.com");
    private final Item item = new Item(1L, "Мопед", "Железный конь", true, user,
            new ItemRequest());
    private final long userId = 1L;

    @Test
    void checkUser_returnUser() {
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        User actualUser = valid.checkUser(userId);

        assertNotNull(actualUser);
        assertEquals(actualUser.getId(), item.getId());
        assertEquals(actualUser.getName(), user.getName());
        assertEquals(actualUser.getEmail(), user.getEmail());
    }

    @Test
    void checkUser_returnNotFoundException() {
        when(userRepository.findById(any())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
                () -> valid.checkUser(userId));
    }

    @Test
    void checkItem_returnItem() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        Item actualItem = valid.checkItem(item.getId());

        assertNotNull(actualItem);
        assertEquals(actualItem.getId(), item.getId());
        assertEquals(actualItem.getName(), item.getName());
        assertEquals(actualItem.getDescription(), item.getDescription());
    }

    @Test
    void checkItem_returnNotFoundException() {
        when(itemRepository.findById(any())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
                () -> valid.checkItem(1L));
    }

}
