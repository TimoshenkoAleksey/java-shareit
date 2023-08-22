package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoFrontend {
    private final Long itemId;
    private final LocalDateTime start;
    private final LocalDateTime end;
}
