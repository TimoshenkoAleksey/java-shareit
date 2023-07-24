package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForOwner;
import ru.practicum.shareit.booking.dto.BookingDtoFrontend;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.exception.IllegalStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    BookingRepository bookingRepository;
    @Mock
    Valid valid;
    @InjectMocks
    BookingServiceImpl bookingService;

    private final User user = new User(null, "John", "john.doe@mail.com");
    private final User owner = new User(2L, "Алексей", "alexey.timoshenko@mail.com");
    private final Item item = new Item(1L, "Мопед", "Железный конь", true, owner,
            new ItemRequest());
    private final Booking booking = new Booking(1L, LocalDateTime.now().minusSeconds(120),
            LocalDateTime.now().minusSeconds(60), item, owner, Status.WAITING);
    private final BookingDtoFrontend bookingDtoFrontend = new BookingDtoFrontend(1L,
            LocalDateTime.now().minusSeconds(120), LocalDateTime.now().minusSeconds(60));
    private final int from = 0;
    private final int size = 10;
    private final long itemId = 1L;
    private final long bookingId = 1L;
    long ownerId = owner.getId();

    @Test
    void findById() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(valid.checkUser(ownerId)).thenReturn(user);

        BookingDto bookingDto = bookingService.findById(bookingId, ownerId);

        assertNotNull(bookingDto);
        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingDto.getItem().getId(), booking.getItem().getId());
        verify(bookingRepository, times(1)).findById(anyLong());
    }

    @Test
    void findAllBookingsByUserId_StateAll() {
        String state = "ALL";
        when(bookingRepository.findAllByBookerId(anyLong(), any())).thenReturn(List.of(booking));
        when(valid.checkUser(ownerId)).thenReturn(user);

        List<BookingDto> bookingDto = bookingService.findAllBookingsByUserId(ownerId, state, from, size);

        assertNotNull(bookingDto);
        assertEquals(1, bookingDto.size());
        assertEquals(bookingDto.get(0).getId(), booking.getId());
        assertEquals(bookingDto.get(0).getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingDto.get(0).getItem().getId(), booking.getItem().getId());
        verify(bookingRepository, times(1)).findAllByBookerId(anyLong(), any());
    }

    @Test
    void findAllBookingsByUserId_StateCurrent() {
        String state = "CURRENT";
        when(bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(anyLong(), any(), any(), any()))
                .thenReturn(List.of(booking));
        when(valid.checkUser(ownerId)).thenReturn(user);

        List<BookingDto> bookingDto = bookingService.findAllBookingsByUserId(ownerId, state, from, size);

        assertNotNull(bookingDto);
        assertEquals(1, bookingDto.size());
        assertEquals(bookingDto.get(0).getId(), booking.getId());
        assertEquals(bookingDto.get(0).getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingDto.get(0).getItem().getId(), booking.getItem().getId());
        verify(bookingRepository, times(1)).findAllByBookerIdAndStartIsBeforeAndEndIsAfter(
                anyLong(), any(), any(), any());
    }

    @Test
    void findAllBookingsByUserId_StatePast() {
        String state = "PAST";
        when(bookingRepository.findAllByBookerIdAndEndIsBefore(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(valid.checkUser(ownerId)).thenReturn(user);

        List<BookingDto> bookingDto = bookingService.findAllBookingsByUserId(ownerId, state, from, size);

        assertNotNull(bookingDto);
        assertEquals(1, bookingDto.size());
        assertEquals(bookingDto.get(0).getId(), booking.getId());
        assertEquals(bookingDto.get(0).getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingDto.get(0).getItem().getId(), booking.getItem().getId());
        verify(bookingRepository, times(1)).findAllByBookerIdAndEndIsBefore(anyLong(), any(),
                any());
    }

    @Test
    void findAllBookingsByUserId_StateFuture() {
        String state = "FUTURE";
        when(bookingRepository.findAllByBookerIdAndStartIsAfter(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(valid.checkUser(ownerId)).thenReturn(user);

        List<BookingDto> bookingDto = bookingService.findAllBookingsByUserId(ownerId, state, from, size);

        assertNotNull(bookingDto);
        assertEquals(1, bookingDto.size());
        assertEquals(bookingDto.get(0).getId(), booking.getId());
        assertEquals(bookingDto.get(0).getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingDto.get(0).getItem().getId(), booking.getItem().getId());
        verify(bookingRepository, times(1)).findAllByBookerIdAndStartIsAfter(anyLong(), any(),
                any());
    }

    @Test
    void findAllBookingsByUserId_StateWaitting() {
        String state = "WAITING";
        when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(valid.checkUser(ownerId)).thenReturn(user);

        List<BookingDto> bookingDto = bookingService.findAllBookingsByUserId(ownerId, state, from, size);

        assertNotNull(bookingDto);
        assertEquals(1, bookingDto.size());
        assertEquals(bookingDto.get(0).getId(), booking.getId());
        assertEquals(bookingDto.get(0).getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingDto.get(0).getItem().getId(), booking.getItem().getId());
        verify(bookingRepository, times(1)).findAllByBookerIdAndStatus(anyLong(), any(), any());
    }

    @Test
    void findAllBookingsByUserId_StateRejected() {
        String state = "REJECTED";
        when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(valid.checkUser(ownerId)).thenReturn(user);

        List<BookingDto> bookingDto = bookingService.findAllBookingsByUserId(ownerId, state, from, size);

        assertNotNull(bookingDto);
        assertEquals(1, bookingDto.size());
        assertEquals(bookingDto.get(0).getId(), booking.getId());
        assertEquals(bookingDto.get(0).getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingDto.get(0).getItem().getId(), booking.getItem().getId());
        verify(bookingRepository, times(1)).findAllByBookerIdAndStatus(anyLong(), any(), any());
    }

    @Test
    void findAllBookingsByUserId_OtherState() {
        String state = "OTHER";
        when(valid.checkUser(ownerId)).thenReturn(user);

        assertThrows(IllegalStatusException.class,
                () -> bookingService.findAllBookingsByUserId(ownerId, state, from, size));
    }

    @Test
    void findAllBookingsByItemOwnerId_StateAll() {
        String state = "ALL";
        when(bookingRepository.findAllByItemOwnerId(anyLong(), any())).thenReturn(List.of(booking));
        when(valid.checkUser(ownerId)).thenReturn(user);

        List<BookingDto> bookingDto = bookingService.findAllBookingsByItemOwnerId(ownerId, state, from, size);

        assertNotNull(bookingDto);
        assertEquals(1, bookingDto.size());
        assertEquals(bookingDto.get(0).getId(), booking.getId());
        assertEquals(bookingDto.get(0).getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingDto.get(0).getItem().getId(), booking.getItem().getId());
        verify(bookingRepository, times(1)).findAllByItemOwnerId(anyLong(), any());
    }

    @Test
    void findAllBookingsByItemOwnerId_StateCurrent() {
        String state = "CURRENT";
        when(bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(anyLong(), any(), any(), any()))
                .thenReturn(List.of(booking));
        when(valid.checkUser(ownerId)).thenReturn(user);

        List<BookingDto> bookingDto = bookingService.findAllBookingsByItemOwnerId(ownerId, state, from, size);

        assertNotNull(bookingDto);
        assertEquals(1, bookingDto.size());
        assertEquals(bookingDto.get(0).getId(), booking.getId());
        assertEquals(bookingDto.get(0).getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingDto.get(0).getItem().getId(), booking.getItem().getId());
        verify(bookingRepository, times(1)).findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(
                anyLong(), any(), any(), any());
    }

    @Test
    void findAllBookingsByItemOwnerId_StatePast() {
        String state = "PAST";
        when(bookingRepository.findAllByItemOwnerIdAndEndIsBefore(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(valid.checkUser(ownerId)).thenReturn(user);

        List<BookingDto> bookingDto = bookingService.findAllBookingsByItemOwnerId(ownerId, state, from, size);

        assertNotNull(bookingDto);
        assertEquals(1, bookingDto.size());
        assertEquals(bookingDto.get(0).getId(), booking.getId());
        assertEquals(bookingDto.get(0).getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingDto.get(0).getItem().getId(), booking.getItem().getId());
        verify(bookingRepository, times(1)).findAllByItemOwnerIdAndEndIsBefore(anyLong(), any(),
                any());
    }

    @Test
    void findAllBookingsByItemOwnerId_StateFuture() {
        String state = "FUTURE";
        when(bookingRepository.findAllByItemOwnerIdAndStartIsAfter(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(valid.checkUser(ownerId)).thenReturn(user);

        List<BookingDto> bookingDto = bookingService.findAllBookingsByItemOwnerId(ownerId, state, from, size);

        assertNotNull(bookingDto);
        assertEquals(1, bookingDto.size());
        assertEquals(bookingDto.get(0).getId(), booking.getId());
        assertEquals(bookingDto.get(0).getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingDto.get(0).getItem().getId(), booking.getItem().getId());
        verify(bookingRepository, times(1)).findAllByItemOwnerIdAndStartIsAfter(anyLong(), any(),
                any());
    }

    @Test
    void findAllBookingsByItemOwnerId_StateWaitting() {
        String state = "WAITING";
        when(bookingRepository.findAllByItemOwnerIdAndStatus(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(valid.checkUser(ownerId)).thenReturn(user);

        List<BookingDto> bookingDto = bookingService.findAllBookingsByItemOwnerId(ownerId, state, from, size);

        assertNotNull(bookingDto);
        assertEquals(1, bookingDto.size());
        assertEquals(bookingDto.get(0).getId(), booking.getId());
        assertEquals(bookingDto.get(0).getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingDto.get(0).getItem().getId(), booking.getItem().getId());
        verify(bookingRepository, times(1)).findAllByItemOwnerIdAndStatus(anyLong(), any(), any());
    }

    @Test
    void findAllBookingsByItemOwnerId_StateRejected() {
        String state = "REJECTED";
        when(bookingRepository.findAllByItemOwnerIdAndStatus(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(valid.checkUser(ownerId)).thenReturn(user);

        List<BookingDto> bookingDto = bookingService.findAllBookingsByItemOwnerId(ownerId, state, from, size);

        assertNotNull(bookingDto);
        assertEquals(1, bookingDto.size());
        assertEquals(bookingDto.get(0).getId(), booking.getId());
        assertEquals(bookingDto.get(0).getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingDto.get(0).getItem().getId(), booking.getItem().getId());
        verify(bookingRepository, times(1)).findAllByItemOwnerIdAndStatus(anyLong(), any(), any());
    }

    @Test
    void findAllBookingsByItemOwnerId_OtherState() {
        String state = "OTHER";
        when(valid.checkUser(ownerId)).thenReturn(user);

        assertThrows(IllegalStatusException.class,
                () -> bookingService.findAllBookingsByItemOwnerId(ownerId, state, from, size));
    }

    @Test
    void add() {
        long userId = 1L;
        when(bookingRepository.save(any())).thenReturn(booking);
        when(valid.checkUser(userId)).thenReturn(user);
        when(valid.checkItem(itemId)).thenReturn(item);

        BookingDto bookingDto = bookingService.add(userId, bookingDtoFrontend);

        assertNotNull(bookingDto);
        assertEquals(bookingDto.getBooker().getId(), user.getId());
        assertEquals(bookingDto.getItem().getId(), item.getId());
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void update() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(valid.checkUser(ownerId)).thenReturn(owner);

        BookingDto bookingDto = bookingService.update(ownerId, bookingId, true);

        assertNotNull(bookingDto);
        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingDto.getItem().getId(), booking.getItem().getId());
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void getLastBooking() {
        when(bookingRepository.findFirstByItemIdAndStartBeforeAndStatus(anyLong(), any(), any(), any()))
                .thenReturn(booking);

        BookingDtoForOwner bookingDto = bookingService.getLastBooking(itemId);

        assertNotNull(bookingDto);
        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getBookerId(), booking.getBooker().getId());
        verify(bookingRepository, times(1)).findFirstByItemIdAndStartBeforeAndStatus(anyLong(),
                any(), any(), any());
    }

    @Test
    void getNextBooking() {
        when(bookingRepository.findFirstByItemIdAndStartAfterAndStatus(anyLong(), any(), any(), any()))
                .thenReturn(booking);

        BookingDtoForOwner bookingDto = bookingService.getNextBooking(itemId);

        assertNotNull(bookingDto);
        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getBookerId(), booking.getBooker().getId());
        verify(bookingRepository, times(1)).findFirstByItemIdAndStartAfterAndStatus(anyLong(),
                any(), any(), any());
    }

}
