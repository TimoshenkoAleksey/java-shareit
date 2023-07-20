package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForOwner;
import ru.practicum.shareit.booking.dto.BookingDtoFrontend;

import java.util.List;

public interface BookingService {

    BookingDto findById(long bookingId, long userId);

    List<BookingDto> findAllBookingsByUserId(long userId, String state);

    List<BookingDto> findAllBookingsByItemOwner(long userId, String state);

    BookingDto add(long userId, BookingDtoFrontend bookingDtoFrontend);

    BookingDto update(long userId, long bookingId, boolean approved);

    BookingDtoForOwner getLastBooking(long itemId);

    BookingDtoForOwner getNextBooking(long itemId);
}
