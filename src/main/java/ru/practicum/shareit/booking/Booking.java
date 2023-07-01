package ru.practicum.shareit.booking;

import jdk.jfr.Timestamp;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private Long id;
    private Timestamp start;
    private Timestamp end;
    private Item item;
    private User booker;
    private BookingStatus status;
}
