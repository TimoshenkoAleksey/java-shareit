package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForOwner;
import ru.practicum.shareit.booking.dto.BookingDtoFrontend;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.exception.IllegalStatusException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final Valid valid;
    private final Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
    private final Sort sortByStartAsc = Sort.by(Sort.Direction.ASC, "start");

    @Override
    public BookingDto findById(long bookingId, long userId) {
        valid.checkUser(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(format("Запроса с id = %s нет в базе", bookingId)));
        long ownerId = booking.getItem().getOwner().getId();
        long bookerId = booking.getBooker().getId();
        if (userId != ownerId && userId != bookerId) {
            throw new NotFoundException(format("Пользователь с id = %s не имеет прав на просмотр бронирования вещи",
                    userId));
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> findAllBookingsByUserId(long userId, String state) {
        valid.checkUser(userId);
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByBookerId(userId, sortByStartDesc);
                break;
            case "CURRENT":
                bookings = bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(),
                        LocalDateTime.now(), sortByStartDesc);
                break;
            case "PAST":
                bookings = bookingRepository.findAllByBookerIdAndEndIsBefore(userId, LocalDateTime.now(),
                        sortByStartDesc);
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByBookerIdAndStartIsAfter(userId, LocalDateTime.now(),
                        sortByStartDesc);
                break;
            case "WAITING":
                bookings = bookingRepository.findAllByBookerIdAndStatus(userId, Status.WAITING, sortByStartDesc);
                break;
            case "REJECTED":
                bookings = bookingRepository.findAllByBookerIdAndStatus(userId, Status.REJECTED, sortByStartDesc);
                break;
            default:
                throw new IllegalStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> findAllBookingsByItemOwner(long userId, String state) {
        valid.checkUser(userId);
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByItemOwnerId(userId, sortByStartDesc);
                break;
            case "CURRENT":
                bookings = bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now(), sortByStartDesc);
                break;
            case "PAST":
                bookings = bookingRepository.findAllByItemOwnerIdAndEndIsBefore(userId, LocalDateTime.now(),
                        sortByStartDesc);
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByItemOwnerIdAndStartIsAfter(userId, LocalDateTime.now(),
                        sortByStartDesc);
                break;
            case "WAITING":
                bookings = bookingRepository.findAllByItemOwnerIdAndStatus(userId, Status.WAITING, sortByStartDesc);
                break;
            case "REJECTED":
                bookings = bookingRepository.findAllByItemOwnerIdAndStatus(userId, Status.REJECTED, sortByStartDesc);
                break;
            default:
                throw new IllegalStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public BookingDto add(long userId, BookingDtoFrontend bookingDtoFrontend) {
        Item item = valid.checkItem(bookingDtoFrontend.getItemId());
        if (!item.getAvailable()) {
            throw new ValidationException("Эта вещь не доступна для бронирования");
        }
        User booker = valid.checkUser(userId);
        if (userId == item.getOwner().getId()) {
            throw new NotFoundException(format("Пользователь с id = %s является владельцем вещи, "
                    + "поэтому не может брать вещь взаймы у себя", userId));
        }
        checkDate(bookingDtoFrontend);
        Booking booking = BookingMapper.toBooking(bookingDtoFrontend);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto update(long userId, long bookingId, boolean approved) {
        valid.checkUser(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(format("Запроса с id = %s нет в базе", bookingId)));
        long ownerId = booking.getItem().getOwner().getId();
        if (userId == ownerId) {
            if (approved) {
                if (booking.getStatus().equals(Status.APPROVED)) {
                    throw new ValidationException("Владелец уже одобрил бронирование");
                } else {
                    booking.setStatus(Status.APPROVED);
                }
            } else {
                if (booking.getStatus().equals(Status.REJECTED)) {
                    throw new ValidationException("Владелец уже отклонил бронирование");
                }
                booking.setStatus(Status.REJECTED);
            }
        } else {
            throw new NotFoundException(format("Пользователь с id = %s не является владельцем вещи "
                    + "и не имеет прав согласовывать бронирование", userId));
        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoForOwner getLastBooking(long itemId) {
        return BookingMapper.toBookingDtoForOwner(bookingRepository.findFirstByItemIdAndStartBeforeAndStatus(itemId,
                LocalDateTime.now(), Status.APPROVED, sortByStartDesc));
    }

    @Override
    public BookingDtoForOwner getNextBooking(long itemId) {
        return BookingMapper.toBookingDtoForOwner(bookingRepository.findFirstByItemIdAndStartAfterAndStatus(itemId,
                LocalDateTime.now(), Status.APPROVED, sortByStartAsc));
    }

    private void checkDate(BookingDtoFrontend bookingDtoFrontend) {
        if (bookingDtoFrontend.getEnd().isBefore(bookingDtoFrontend.getStart())) {
            throw new ValidationException("Время начала бронирования вещи не должна быть позже времени " +
                    "окончания бронирования");
        }
        if (bookingDtoFrontend.getEnd().isEqual(bookingDtoFrontend.getStart())) {
            throw new ValidationException("Время начала и окончания бронирования вещи не должны совпадать");
        }
    }
}
