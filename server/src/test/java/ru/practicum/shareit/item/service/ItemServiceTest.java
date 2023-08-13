package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDtoForOwner;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.Valid;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    ItemRepository itemRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    BookingService bookingService;
    @Mock
    Valid valid;
    @InjectMocks
    ItemServiceImpl itemService;

    private final User user = new User(null, "John", "john.doe@mail.com");
    private final User owner = new User(2L, "Алексей", "alexey.timoshenko@mail.com");
    private final Item item = new Item(1L, "Мопед", "Железный конь", true, owner,
            new ItemRequest());
    private final Booking booking = new Booking(1L, LocalDateTime.now().minusSeconds(120),
            LocalDateTime.now().minusSeconds(60), item, owner, Status.APPROVED);
    private final Comment comment = new Comment(1L, "text", user, item, LocalDateTime.now());
    private final int from = 0;
    private final int size = 10;
    private final PageRequest pageRequest = PageRequest.of(from, size);
    private final long userId = 1L;
    private final long itemId = 1L;

    @Test
    void getItemById() {
        when(bookingService.getNextBooking(itemId)).thenReturn(new BookingDtoForOwner());
        when(bookingService.getLastBooking(itemId)).thenReturn(new BookingDtoForOwner());
        when(commentRepository.findAllByItemId(itemId)).thenReturn(Collections.emptyList());
        when(valid.checkItem(itemId)).thenReturn(item);

        ItemDto itemDto = itemService.getItemById(owner.getId(), itemId);

        assertNotNull(itemDto);
        assertEquals(itemDto.getId(), item.getId());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        verify(commentRepository, times(1)).findAllByItemId(anyLong());
    }

    @Test
    void getItemsByUserId() {
        List<Item> items = List.of(item);
        when(itemRepository.findAllByOwnerId(userId, pageRequest)).thenReturn(items);
        when(bookingRepository.findAllByItemIn(items)).thenReturn(List.of(booking));
        when(commentRepository.findAllByItemIn(items)).thenReturn(List.of(comment));
        when(valid.checkUser(userId)).thenReturn(user);

        List<ItemDto> itemDto = itemService.getItemsByUserId(userId, from, size);

        assertNotNull(itemDto);
        assertEquals(1, itemDto.size());
        assertEquals(itemDto.get(0).getId(), item.getId());
        assertEquals(itemDto.get(0).getName(), item.getName());
        assertEquals(itemDto.get(0).getDescription(), item.getDescription());
        verify(itemRepository, times(1)).findAllByOwnerId(anyLong(), any());
        verify(bookingRepository, times(1)).findAllByItemIn(anyList());
        verify(commentRepository, times(1)).findAllByItemIn(anyList());
    }

    @Test
    void getItemsByText() {
        String text = "text";
        when(itemRepository.getItemsByText(text.toLowerCase(), pageRequest)).thenReturn(List.of(item));

        List<ItemDto> itemDto = itemService.getItemsByText(text, from, size);

        assertNotNull(itemDto);
        assertEquals(1, itemDto.size());
        assertEquals(itemDto.get(0).getId(), item.getId());
        assertEquals(itemDto.get(0).getName(), item.getName());
        assertEquals(itemDto.get(0).getDescription(), item.getDescription());
        verify(itemRepository, times(1)).getItemsByText(anyString(), any());
    }

    @Test
    void addItem() {
        when(itemRepository.save(any())).thenReturn(item);
        when(valid.checkUser(userId)).thenReturn(user);

        ItemDto itemDto = itemService.addItem(userId, ItemMapper.toItemDto(item));

        assertNotNull(itemDto);
        assertEquals(itemDto.getId(), item.getId());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void addItemWithEmptyName() {
        item.setName("");

        assertThrows(NullPointerException.class,
                () -> itemService.addItem(owner.getId(), ItemMapper.toItemDto(item)));
    }

    @Test
    void addItemWithNullDescription() {
        item.setDescription(null);

        assertThrows(NullPointerException.class,
                () -> itemService.addItem(owner.getId(), ItemMapper.toItemDto(item)));
    }

    @Test
    void addItemWithNullAvailable() {
        item.setAvailable(null);

        assertThrows(NullPointerException.class,
                () -> itemService.addItem(owner.getId(), ItemMapper.toItemDto(item)));
    }

    @Test
    void updateItem() {
        when(itemRepository.save(any())).thenReturn(item);
        when(valid.checkItem(itemId)).thenReturn(item);
        when(valid.checkUser(owner.getId())).thenReturn(owner);

        ItemDto itemDto = itemService.updateItem(owner.getId(), item.getId(), ItemMapper.toItemDto(item));

        assertNotNull(itemDto);
        assertEquals(itemDto.getId(), item.getId());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void updateItemWithEmptyName() {
        item.setName("");
        when(valid.checkItem(itemId)).thenReturn(item);
        when(valid.checkUser(owner.getId())).thenReturn(owner);

        assertThrows(NullPointerException.class,
                () -> itemService.updateItem(owner.getId(), item.getId(), ItemMapper.toItemDto(item)));
    }

    @Test
    void updateItemWithEmptyDescription() {
        item.setDescription("");
        when(valid.checkItem(itemId)).thenReturn(item);
        when(valid.checkUser(owner.getId())).thenReturn(owner);

        assertThrows(NullPointerException.class,
                () -> itemService.updateItem(owner.getId(), item.getId(), ItemMapper.toItemDto(item)));
    }

    @Test
    void updateItemWithNotValidOwner() {
        when(valid.checkItem(itemId)).thenReturn(item);
        when(valid.checkUser(userId)).thenReturn(user);

        assertThrows(NotFoundException.class,
                () -> itemService.updateItem(userId, item.getId(), ItemMapper.toItemDto(item)));
    }

    @Test
    void addComment() {
        when(commentRepository.save(any())).thenReturn(comment);
        when(bookingRepository.findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatus(anyLong(), anyLong(), any(), any()))
                .thenReturn(booking);
        when(valid.checkItem(itemId)).thenReturn(item);
        when(valid.checkUser(userId)).thenReturn(user);

        CommentDto commentDto = itemService.addComment(userId, itemId, CommentMapper.toCommentDto(comment));

        assertNotNull(commentDto);
        assertEquals(commentDto.getId(), comment.getId());
        assertEquals(commentDto.getText(), comment.getText());
        verify(commentRepository, times(1)).save(any());
    }

    @Test
    void addCommentWithNotValidOwner() {
        when(valid.checkItem(itemId)).thenReturn(item);
        when(valid.checkUser(userId)).thenReturn(user);
        when(bookingRepository.findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatus(anyLong(), anyLong(), any(), any()))
                .thenReturn(null);

        assertThrows(ValidationException.class, () -> itemService.addComment(userId, itemId,
                CommentMapper.toCommentDto(comment)));

    }

}
