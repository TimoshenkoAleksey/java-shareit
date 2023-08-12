package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForOwner;
import ru.practicum.shareit.booking.dto.BookingDtoFrontend;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

@UtilityClass
public class BookingMapper {
    public static Booking toBooking(BookingDtoFrontend bookingDtoFrontend) {
        Booking booking = new Booking();
        booking.setStart(bookingDtoFrontend.getStart());
        booking.setEnd(bookingDtoFrontend.getEnd());
        Item item = new Item();
        item.setId(bookingDtoFrontend.getItemId());
        booking.setItem(item);
        return booking;
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getId(), UserMapper.toUserDto(booking.getBooker()),
                ItemMapper.toItemDto(booking.getItem()), booking.getStart(), booking.getEnd(),
                booking.getStatus());
    }

    public static BookingDtoForOwner toBookingDtoForOwner(Booking booking) {
        if (booking == null) {
            return null;
        } else {
            return new BookingDtoForOwner(booking.getId(), booking.getBooker().getId(),
                    booking.getStart(), booking.getEnd());
        }
    }
}
